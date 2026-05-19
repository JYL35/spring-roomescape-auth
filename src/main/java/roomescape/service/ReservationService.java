package roomescape.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.request.ReservationUpdateRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationDao reservationDao;
    private final MemberDao memberDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    @Autowired
    public ReservationService(ReservationDao reservationDao, MemberDao memberDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> getReservations() {
        List<Reservation> reservations = reservationDao.findAllReservations();
        return buildReservationResponses(reservations);
    }

    @Transactional
    public ReservationResponse createReservation(Long memberId, ReservationCreateRequest request) {
        Reservation reservation = Reservation.from(memberId, request.date(), request.timeId(), request.themeId());
        ReservationTime time = reservationTimeDao.findById(request.timeId());
        reservation.validateNotPast(LocalDateTime.of(request.date(), time.getStartAt()));

        Long id = reservationDao.insertReservation(reservation);
        Reservation newReservation = reservationDao.findReservationById(id);
        Member member = memberDao.findById(newReservation.getMemberId());
        Theme theme = themeDao.findById(newReservation.getThemeId());

        return ReservationResponse.from(newReservation, member, time, theme);
    }

    @Transactional
    public void deleteReservation(Long id) {
        int deleteCount = reservationDao.delete(id);
        Reservation.validateDeletion(deleteCount);
    }

    public List<AvailableTimeResponse> getAvailableTimes(LocalDate date, Long id) {
        List<Long> reservedTimeIds = reservationDao.findReservationTimeIds(date, id);
        List<ReservationTime> allTimes = reservationTimeDao.findAllReservationTimes();

        Map<ReservationTime, Boolean> reservationTimeMap = allTimes.stream()
                .collect(Collectors.toMap(
                        time -> time,
                        time -> !reservedTimeIds.contains(time.getId()),
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
        return AvailableTimeResponse.from(reservationTimeMap);
    }

    public List<ReservationResponse> getUserReservations(Long memberId) {
        List<Reservation> reservations = reservationDao.findUserReservations(memberId);
        return buildReservationResponses(reservations);
    }

    @Transactional
    public void deleteUserReservation(Long id, Long memberId) {
        Reservation reservation = reservationDao.findReservationById(id);
        ReservationTime time = reservationTimeDao.findById(reservation.getTimeId());
        reservation.validateNotPast(LocalDateTime.of(reservation.getDate(), time.getStartAt()));
        int deleteCount = reservationDao.deleteUserReservation(id, memberId);
        Reservation.validateDeletion(deleteCount);
    }

    @Transactional
    public void updateUserReservation(Long id, Long memberId, ReservationUpdateRequest request) {
        ReservationTime time = reservationTimeDao.findById(request.timeId());
        Reservation reservation = Reservation.from(id, memberId, request.date(), request.timeId(), request.themeId());
        reservation.validateNotPast(LocalDateTime.of(request.date(), time.getStartAt()));
        int updateCount = reservationDao.update(id, reservation);
        Reservation.validateDeletion(updateCount);
    }

    private List<ReservationResponse> buildReservationResponses(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            return List.of();
        }
        Map<Long, Member> memberMap = getMemberMap(reservations);
        Map<Long, ReservationTime> timeMap = getReservationTimeMap(reservations);
        Map<Long, Theme> themeMap = getThemeMap(reservations);
        return reservations.stream()
                .map(reservation -> ReservationResponse.from(
                        reservation,
                        memberMap.get(reservation.getMemberId()),
                        timeMap.get(reservation.getTimeId()),
                        themeMap.get(reservation.getThemeId())
                )).toList();
    }

    private Map<Long, Member> getMemberMap(List<Reservation> reservations) {
        List<Long> memberIds = reservations.stream().map(Reservation::getMemberId).toList();
        return memberDao.findAllByIds(memberIds)
                .stream().collect(Collectors.toMap(Member::getId, Function.identity()));
    }

    private Map<Long, ReservationTime> getReservationTimeMap(List<Reservation> reservations) {
        List<Long> timeIds = reservations.stream().map(Reservation::getTimeId).toList();
        return reservationTimeDao.findAllByIds(timeIds)
                .stream().collect(Collectors.toMap(ReservationTime::getId, Function.identity()));
    }

    private Map<Long, Theme> getThemeMap(List<Reservation> reservations) {
        List<Long> themeIds = reservations.stream().map(Reservation::getThemeId).toList();
        return themeDao.findAllByIds(themeIds)
                .stream().collect(Collectors.toMap(Theme::getId, Function.identity()));
    }
}

package roomescape.dto.response;

import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationResponse(
        Long id,
        MemberResponse member,
        String date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(Reservation reservation, Member member, ReservationTime reservationTime, Theme theme) {
        return new ReservationResponse(
                reservation.getId(),
                MemberResponse.from(member),
                reservation.getDate().toString(),
                ReservationTimeResponse.from(reservationTime),
                ThemeResponse.from(theme)
        );
    }
}

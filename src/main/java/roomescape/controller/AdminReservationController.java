package roomescape.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.LoginMember;
import roomescape.domain.Member;
import roomescape.dto.request.ReservationUpdateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;
import jakarta.validation.Valid;

import java.util.List;

@RequestMapping("/api/v1/admin/reservations")
@RestController
@Validated
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations(@LoginMember Member member) {
        List<ReservationResponse> reservationResponses = reservationService.getManageableReservations(member);
        return ResponseEntity.ok().body(reservationResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @LoginMember Member member,
            @PathVariable @NotNull(message = "예약 ID는 필수로 입력해야 합니다.") Long id) {
        reservationService.deleteManageableReservation(member, id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReservation(
            @LoginMember Member member,
            @PathVariable @NotNull(message = "예약 ID는 필수로 입력해야 합니다.") Long id,
            @Valid @RequestBody ReservationUpdateRequest request) {
        reservationService.updateManageableReservation(member, id, request);
        return ResponseEntity.noContent().build();
    }
}

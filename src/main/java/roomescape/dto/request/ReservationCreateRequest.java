package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationCreateRequest(

        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "날짜는 필수로 입력해야 합니다.")
        LocalDate date,

        @NotNull(message = "시간은 필수로 입력해야 합니다.")
        Long timeId,

        @NotNull(message = "테마는 필수로 입력해야 합니다.")
        Long themeId
) {
}

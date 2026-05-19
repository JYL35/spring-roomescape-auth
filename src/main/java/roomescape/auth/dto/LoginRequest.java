package roomescape.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "로그인 ID는 필수입니다.")
        String loginId,

        @NotBlank(message = "패스워드는 필수입니다.")
        String password
) {
}

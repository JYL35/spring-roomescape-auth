package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ThemeCreateRequest(
        @NotBlank(message = "테마 이름은 필수로 입력해야 합니다.")
        String name,

        @NotBlank(message = "테마 설명은 필수로 입력해야 합니다.")
        String description,

        @NotBlank(message = "테마 이미지 URL는 필수로 입력해야 합니다.")
        String imgUrl,

        @NotNull(message = "매장 ID는 필수로 입력해야 합니다.")
        Long storeId
) {
}

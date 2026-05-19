package roomescape.auth.dto;

import roomescape.domain.Member;

public record LoginResponse(
        Long id,
        String loginId,
        String name
) {

    public static LoginResponse from(Member member) {
        return new LoginResponse(
                member.getId(),
                member.getLoginId(),
                member.getName()
        );
    }
}

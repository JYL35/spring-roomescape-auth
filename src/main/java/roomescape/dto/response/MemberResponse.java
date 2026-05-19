package roomescape.dto.response;

import roomescape.domain.Member;

public record MemberResponse(
        Long id,
        String loginId,
        String password,
        String name,
        String role
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getLoginId(),
                member.getPassword(),
                member.getName(),
                member.getRole()
        );
    }
}

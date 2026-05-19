package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.exception.AuthenticationException;

@Service
public class AuthService {

    private final MemberDao memberDao;

    public AuthService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberDao.findByLoginId(request.loginId())
                .filter(foundMember -> foundMember.getPassword().equals(request.password()))
                .orElseThrow(() -> new AuthenticationException("잘못된 로그인 정보입니다."));

        return LoginResponse.from(member);
    }
}

package roomescape.auth.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.LoginMember;
import roomescape.auth.SessionConst;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.exception.AuthenticationException;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberDao memberDao;

    public LoginMemberArgumentResolver(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(LoginMember.class);
        boolean isMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
        return hasAnnotation && isMemberType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new AuthenticationException("로그인이 필요합니다.");
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new AuthenticationException("로그인이 필요합니다.");
        }

        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER_ID);
        if (memberId == null) {
            throw new AuthenticationException("로그인이 필요합니다.");
        }

        try {
            return memberDao.findById(memberId);
        } catch (EmptyResultDataAccessException e) {
            throw new AuthenticationException("로그인이 필요합니다.");
        }
    }
}

package roomescape.auth.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.SessionConst;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.response.ErrorResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final MemberDao memberDao;

    public AuthInterceptor(ObjectMapper objectMapper, MemberDao memberDao) {
        this.objectMapper = objectMapper;
        this.memberDao = memberDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (!requiresAuthentication(request)) {
            return true;
        }

        Long memberId = getLoginMemberId(request);
        if (memberId == null) {
            sendError(request, response, HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.", "AUTHENTICATION_FAILED");
            return false;
        }

        if (!requiresAdminApi(request)) {
            return true;
        }

        Member member;
        try {
            member = memberDao.findById(memberId);
        } catch (EmptyResultDataAccessException e) {
            sendError(request, response, HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.", "AUTHENTICATION_FAILED");
            return false;
        }

        if (canAccessAdminApi(member, request)) {
            return true;
        }

        sendError(request, response, HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다.", "ADMIN_AUTHORIZATION_FAILED");
        return false;
    }

    private Long getLoginMemberId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER_ID);
        if (memberId instanceof Long) {
            return (Long) memberId;
        }
        return null;
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if (requiresAdminApi(request)) {
            return true;
        }

        if (uri.equals("/api/v1/logout")) {
            return method.equals("POST");
        }

        if (uri.equals("/api/v1/reservations")) {
            return method.equals("POST");
        }

        if (uri.equals("/api/v1/reservations/mine")) {
            return method.equals("GET");
        }

        return uri.matches("/api/v1/reservations/\\d+") && (method.equals("DELETE") || method.equals("PUT"));
    }

    private boolean requiresAdminApi(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/v1/admin/");
    }

    private boolean canAccessAdminApi(Member member, HttpServletRequest request) {
        if (member.isAdmin()) {
            return true;
        }
        return member.isManager() && request.getRequestURI().startsWith("/api/v1/admin/reservations");
    }

    private void sendError(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpStatus status,
            String message,
            String errorCode
    ) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.from(
                status.value(),
                message,
                errorCode,
                request.getRequestURI()
        );

        response.setStatus(status.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}

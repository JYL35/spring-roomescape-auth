package roomescape.auth.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.auth.service.AuthService;

@RequestMapping("/api/v1")
@RestController
public class AuthController {

    private static final String LOGIN_MEMBER_ID = "loginMemberId";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session) {
        LoginResponse response = authService.login(request);
        session.setAttribute(LOGIN_MEMBER_ID, response.id());
        return ResponseEntity.ok(response);
    }
}

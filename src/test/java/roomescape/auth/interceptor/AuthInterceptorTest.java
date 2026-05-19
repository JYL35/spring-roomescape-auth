package roomescape.auth.interceptor;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static roomescape.auth.TestAuthSupport.login;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthInterceptorTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update(
                "INSERT INTO member (id, login_id, password, name, role) VALUES (?, ?, ?, ?, ?)",
                1L, "eden", "password123", "Eden", "USER"
        );
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", 1L, "10:00");
        jdbcTemplate.update(
                "INSERT INTO theme (id, name, description, img_url) VALUES (?, ?, ?, ?)",
                1L, "Theme", "Description", "image.png"
        );
    }

    @Test
    void rejectReservationCreationWithoutLogin() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(401)
                .body("status", is(401))
                .body("errorCode", is("AUTHENTICATION_FAILED"));
    }

    @Test
    void allowReservationCreationAfterLogin() {
        SessionFilter sessionFilter = login("eden");

        RestAssured.given().log().all()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));
    }

    private Map<String, Object> reservationParams() {
        return Map.of(
                "memberId", 1,
                "date", "2026-12-31",
                "timeId", 1,
                "themeId", 1
        );
    }
}

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
import static roomescape.auth.TestAuthSupport.adminLogin;
import static roomescape.auth.TestAuthSupport.login;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthInterceptorTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO store (id, name) VALUES (?, ?)", 1L, "강남점");
        jdbcTemplate.update(
                "INSERT INTO member (id, login_id, password, name, role, store_id) VALUES (?, ?, ?, ?, ?, ?)",
                1L, "eden", "password123", "Eden", "USER", 1L
        );
        jdbcTemplate.update(
                "INSERT INTO member (id, login_id, password, name, role, store_id) VALUES (?, ?, ?, ?, ?, ?)",
                2L, "admin", "admin123", "Admin", "ADMIN", 1L
        );
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", 1L, "10:00");
        jdbcTemplate.update(
                "INSERT INTO theme (id, name, description, img_url, store_id) VALUES (?, ?, ?, ?, ?)",
                1L, "Theme", "Description", "image.png", 1L
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

    @Test
    void rejectAdminApiWithoutLogin() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", "11:00"))
                .when().post("/api/v1/admin/times")
                .then().log().all()
                .statusCode(401)
                .body("status", is(401))
                .body("errorCode", is("AUTHENTICATION_FAILED"));
    }

    @Test
    void rejectAdminApiForNonAdminMember() {
        SessionFilter sessionFilter = login("eden");

        RestAssured.given().log().all()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", "11:00"))
                .when().post("/api/v1/admin/times")
                .then().log().all()
                .statusCode(403)
                .body("status", is(403))
                .body("errorCode", is("ADMIN_AUTHORIZATION_FAILED"));
    }

    @Test
    void allowAdminApiForAdminMember() {
        SessionFilter sessionFilter = adminLogin();

        RestAssured.given().log().all()
                .filter(sessionFilter)
                .when().delete("/api/v1/admin/reservations/999")
                .then().log().all()
                .statusCode(404)
                .body("errorCode", is("RESERVATION_NOT_FOUND"));
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

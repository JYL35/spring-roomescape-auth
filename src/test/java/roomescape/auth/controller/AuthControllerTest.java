package roomescape.auth.controller;

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
import static org.hamcrest.Matchers.notNullValue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update(
                "INSERT INTO member (id, login_id, password, name, role) VALUES (?, ?, ?, ?, ?)",
                1L, "eden", "password123", "Eden", "USER"
        );
    }

    @Test
    void 로그인() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("loginId", "eden", "password", "password123"))
                .when().post("/api/v1/login")
                .then().log().all()
                .statusCode(200)
                .cookie("JSESSIONID", notNullValue())
                .body("id", is(1))
                .body("loginId", is("eden"))
                .body("name", is("Eden"));
    }

    @Test
    void 로그인_실패() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("loginId", "eden", "password", "wrong-password"))
                .when().post("/api/v1/login")
                .then().log().all()
                .statusCode(401)
                .body("status", is(401))
                .body("errorCode", is("AUTHENTICATION_FAILED"));
    }

    @Test
    void 로그아웃() {
        SessionFilter sessionFilter = new SessionFilter();

        RestAssured.given().log().all()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .body(Map.of("loginId", "eden", "password", "password123"))
                .when().post("/api/v1/login")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .filter(sessionFilter)
                .when().post("/api/v1/logout")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .filter(sessionFilter)
                .when().get("/api/v1/reservations/mine")
                .then().log().all()
                .statusCode(401)
                .body("status", is(401))
                .body("errorCode", is("AUTHENTICATION_FAILED"));
    }

    @Test
    void 로그인하지_않고_로그아웃하면_401을_반환한다() {
        RestAssured.given().log().all()
                .when().post("/api/v1/logout")
                .then().log().all()
                .statusCode(401)
                .body("status", is(401))
                .body("errorCode", is("AUTHENTICATION_FAILED"));
    }
}

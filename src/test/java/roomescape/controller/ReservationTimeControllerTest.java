package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static roomescape.auth.TestAuthSupport.adminLogin;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 시간_조회() {
        createTime("10:00");
        createTime("11:00");
        RestAssured.given().log().all()
                .when().get("/api/v1/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[1].id", is(2));
    }

    private void createTime(String startAt) {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", startAt);

        RestAssured.given()
                .filter(adminSession())
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/api/v1/admin/times");
    }

    private io.restassured.filter.session.SessionFilter adminSession() {
        jdbcTemplate.update(
                "MERGE INTO member KEY(id) VALUES (?, ?, ?, ?, ?)",
                -1L, "admin", "admin123", "관리자", "ADMIN"
        );
        return adminLogin();
    }
}

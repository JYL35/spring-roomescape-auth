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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static roomescape.auth.TestAuthSupport.adminLogin;
import static roomescape.auth.TestAuthSupport.login;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminReservationControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 예약_삭제() {
        createMember(3L, "brown", "브라운");
        createTime("10:00");
        createTheme("이든의 공포 하우스", "이든이 귀신으로 나옴");
        createReservation(reservationParams());

        RestAssured.given().log().all()
                .filter(adminSession())
                .when().delete("/api/v1/admin/reservations/1")
                .then().log().all()
                .statusCode(204);
        RestAssured.given().log().all()
                .filter(adminSession())
                .when().get("/api/v1/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_404를_반환한다() {
        RestAssured.given().log().all()
                .filter(adminSession())
                .when().delete("/api/v1/admin/reservations/1")
                .then().log().all()
                .statusCode(404)
                .body("errorCode", is("RESERVATION_NOT_FOUND"));
    }

    @Test
    void 예약_삭제_시_잘못된_타입의_ID를_전달하면_400을_반환한다() {
        RestAssured.given().log().all()
                .filter(adminSession())
                .when().delete("/api/v1/admin/reservations/invalid-id")
                .then().log().all()
                .statusCode(400)
                .body("status", is(400))
                .body("errorCode", is("TYPE_MISMATCH"))
                .body("message", containsString("타입이 일치하지 않습니다"));
    }

    @Test
    void 예약_삭제_시_ID를_입력하지_않으면_404를_반환한다() {
        RestAssured.given().log().all()
                .filter(adminSession())
                .when().delete("/api/v1/admin/reservations/")
                .then().log().all()
                .statusCode(404)
                .body("status", is(404))
                .body("errorCode", is("PATH_NOT_FOUND"))
                .body("message", containsString("요청하신 경로를 찾을 수 없습니다"));
    }

    @Test
    void 매니저는_자기_매장_예약만_조회한다() {
        createMember(3L, "brown", "브라운");
        createManager(10L, "gangnam-manager", "강남 매니저", 1L);
        createTime("10:00");
        createTheme("강남 테마", "강남점 테마", 1L);
        createTheme("잠실 테마", "잠실점 테마", 2L);
        createReservation(reservationParams(Map.of("themeId", 1)));
        createReservation(reservationParams(Map.of("themeId", 2)));

        RestAssured.given().log().all()
                .filter(login("gangnam-manager"))
                .when().get("/api/v1/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].theme.storeId", is(1));
    }

    @Test
    void 매니저는_자기_매장_예약을_삭제할_수_있다() {
        createMember(3L, "brown", "브라운");
        createManager(10L, "gangnam-manager", "강남 매니저", 1L);
        createTime("10:00");
        createTheme("강남 테마", "강남점 테마", 1L);
        createReservation(reservationParams());

        RestAssured.given().log().all()
                .filter(login("gangnam-manager"))
                .when().delete("/api/v1/admin/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 매니저는_다른_매장_예약을_삭제할_수_없다() {
        createMember(3L, "brown", "브라운");
        createManager(10L, "gangnam-manager", "강남 매니저", 1L);
        createTime("10:00");
        createTheme("잠실 테마", "잠실점 테마", 2L);
        createReservation(reservationParams());

        RestAssured.given().log().all()
                .filter(login("gangnam-manager"))
                .when().delete("/api/v1/admin/reservations/1")
                .then().log().all()
                .statusCode(403)
                .body("errorCode", is("AUTHORIZATION_FAILED"));
    }

    @Test
    void 매니저는_자기_매장_예약을_수정할_수_있다() {
        createMember(3L, "brown", "브라운");
        createManager(10L, "gangnam-manager", "강남 매니저", 1L);
        createTime("10:00");
        createTime("11:00");
        createTheme("강남 테마", "강남점 테마", 1L);
        createReservation(reservationParams());

        RestAssured.given().log().all()
                .filter(login("gangnam-manager"))
                .contentType(ContentType.JSON)
                .body(Map.of("date", "2026-12-31", "timeId", 2, "themeId", 1))
                .when().put("/api/v1/admin/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 매니저는_다른_매장_예약으로_수정할_수_없다() {
        createMember(3L, "brown", "브라운");
        createManager(10L, "gangnam-manager", "강남 매니저", 1L);
        createTime("10:00");
        createTime("11:00");
        createTheme("강남 테마", "강남점 테마", 1L);
        createTheme("잠실 테마", "잠실점 테마", 2L);
        createReservation(reservationParams());

        RestAssured.given().log().all()
                .filter(login("gangnam-manager"))
                .contentType(ContentType.JSON)
                .body(Map.of("date", "2026-12-31", "timeId", 2, "themeId", 2))
                .when().put("/api/v1/admin/reservations/1")
                .then().log().all()
                .statusCode(403)
                .body("errorCode", is("AUTHORIZATION_FAILED"));
    }

    private Map<String, Object> reservationParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 3);
        params.put("date", "2026-12-31");
        params.put("timeId", 1);
        params.put("themeId", 1);
        return params;
    }

    private Map<String, Object> reservationParams(Map<String, Object> overrides) {
        Map<String, Object> params = reservationParams();
        params.putAll(overrides);
        return params;
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

    private void createTheme(String name, String description) {
        createTheme(name, description, 1L);
    }

    private void createTheme(String name, String description, Long storeId) {
        createStore(storeId, storeId == 1L ? "강남점" : "잠실점");
        Map<String, Object> themeParams = new HashMap<>();
        themeParams.put("name", name);
        themeParams.put("description", description);
        themeParams.put("imgUrl", "링크~");
        themeParams.put("storeId", storeId);

        RestAssured.given()
                .filter(adminSession())
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/api/v1/admin/themes");
    }

    private void createReservation(Map<String, Object> params) {
        RestAssured.given().log().all()
                .filter(login("brown"))
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/api/v1/reservations");
    }

    private void createMember(Long id, String loginId, String name) {
        createStore(1L, "강남점");
        jdbcTemplate.update(
                "INSERT INTO member (id, login_id, password, name, role, store_id) VALUES (?, ?, ?, ?, ?, ?)",
                id, loginId, "password123", name, "USER", 1L
        );
    }

    private void createManager(Long id, String loginId, String name, Long storeId) {
        createStore(storeId, storeId == 1L ? "강남점" : "잠실점");
        jdbcTemplate.update(
                "INSERT INTO member (id, login_id, password, name, role, store_id) VALUES (?, ?, ?, ?, ?, ?)",
                id, loginId, "password123", name, "MANAGER", storeId
        );
    }

    private io.restassured.filter.session.SessionFilter adminSession() {
        createAdminMember();
        return adminLogin();
    }

    private void createAdminMember() {
        createStore(1L, "강남점");
        jdbcTemplate.update(
                "MERGE INTO member KEY(id) VALUES (?, ?, ?, ?, ?, ?)",
                -1L, "admin", "admin123", "관리자", "ADMIN", 1L
        );
    }

    private void createStore(Long id, String name) {
        jdbcTemplate.update("MERGE INTO store KEY(id) VALUES (?, ?)", id, name);
    }
}

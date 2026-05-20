package roomescape.auth;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;

import java.util.Map;

public final class TestAuthSupport {

    private TestAuthSupport() {
    }

    public static SessionFilter login(String loginId) {
        return login(loginId, "password123");
    }

    public static SessionFilter login(String loginId, String password) {
        SessionFilter sessionFilter = new SessionFilter();
        RestAssured.given().log().all()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .body(Map.of("loginId", loginId, "password", password))
                .when().post("/api/v1/login")
                .then().log().all()
                .statusCode(200);
        return sessionFilter;
    }

    public static SessionFilter adminLogin() {
        return login("admin", "admin123");
    }
}

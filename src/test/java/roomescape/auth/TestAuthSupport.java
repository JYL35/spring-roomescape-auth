package roomescape.auth;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;

import java.util.Map;

public final class TestAuthSupport {

    private TestAuthSupport() {
    }

    public static SessionFilter login(String loginId) {
        SessionFilter sessionFilter = new SessionFilter();
        RestAssured.given().log().all()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .body(Map.of("loginId", loginId, "password", "password123"))
                .when().post("/api/v1/login")
                .then().log().all()
                .statusCode(200);
        return sessionFilter;
    }
}

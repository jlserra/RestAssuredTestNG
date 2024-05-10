package oauth;

import initialTopics.ReusableComponents;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class OauthTesting {

    SessionFilter sessionFilter = new SessionFilter();
    String accessToken = "";

    @Test
    public void authorizeUser(){

        RestAssured.baseURI = "https://rahulshettyacademy.com/oauthapi/";
        String response = given()
                .formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .formParam("grant_type", "client_credentials")
                .formParam("scope", "trust")
                .filter(sessionFilter)
                .when()
                .log().all()
                .post("oauth2/resourceOwner/token")
                .then()
                .log().headers()
                .log().body()
                .assertThat()
                .statusCode(200)
                .extract().response().asString();

        JsonPath js = ReusableComponents.stringToJson(response);
        accessToken = js.get("access_token");

    }

    @Test
    public void getCourseDetails(){

        RestAssured.baseURI = "https://rahulshettyacademy.com/oauthapi/";
        String response = given()
                .queryParam("access_token",accessToken)
                .filter(sessionFilter)
                .when()
                .log().all()
                .get("getCourseDetails")
                .then()
                .log().headers()
                .log().body()
                .assertThat()
                .statusCode(401)
                .extract().response().asString();

        JsonPath js = ReusableComponents.stringToJson(response);
        int coursesCount = js.get("courses.webAutomation.size()");
        for (int i = 0; i < coursesCount; i++) {
            System.out.println(js.getString("courses.webAutomation["+i+"].courseTitle"));
        }

    }
}

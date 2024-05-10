package pojo.executables;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import pojo.pojoClasses.ResponseGetCourses;

import static io.restassured.RestAssured.given;

public class Pojo {

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

        JsonPath js = new JsonPath(response);
        accessToken = js.get("access_token");

    }

    @Test
    public void getCourseDetails(){

        RestAssured.baseURI = "https://rahulshettyacademy.com/oauthapi/";
        ResponseGetCourses response = given()
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
                .extract().response().as(ResponseGetCourses.class);

        System.out.println(response.getInstructor());
        System.out.println(response.getUrl());
        System.out.println(response.getServices());
        System.out.println(response.getExpertise());
        System.out.println("WebAutomation");
        for (int i = 0; i < response.getCourses().getWebAutomation().size(); i++) {
            System.out.println(response.getCourses().getWebAutomation().get(i).getCourseTitle());
            System.out.println(response.getCourses().getWebAutomation().get(i).getPrice());
        }
        System.out.println("API");
        for (int i = 0; i < response.getCourses().getApi().size(); i++) {
            System.out.println(response.getCourses().getApi().get(i).getCourseTitle());
            System.out.println(response.getCourses().getApi().get(i).getPrice());
        }
        System.out.println("Mobile");
        for (int i = 0; i < response.getCourses().getMobile().size(); i++) {
            System.out.println(response.getCourses().getMobile().get(i).getCourseTitle());
            System.out.println(response.getCourses().getMobile().get(i).getPrice());
        }
    }
}

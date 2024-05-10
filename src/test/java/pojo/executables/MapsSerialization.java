package pojo.executables;

import io.restassured.RestAssured;
import org.testng.annotations.Test;
import pojo.pojoClasses.RequestAddPlace;
import pojo.pojoClasses.requestAddPlace.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class MapsSerialization {

    @Test
    public void addPlace() throws IOException {

        RequestAddPlace addPlace = new RequestAddPlace();
        addPlace.setAccuracy(50);
        addPlace.setName("Juan Dela Cruz");
        addPlace.setLocation(new Location() {{
            setLat(-38.69);
            setLng(-32.72);
        }});
        addPlace.setPhone_number("09067590222");
        addPlace.setAddress("Summer Street, Manila");
        addPlace.setTypes(new ArrayList<String>(Arrays.asList("Type 1", "Type 2", "Type 3")));
        addPlace.setWebsite("https://www.google.com");
        addPlace.setLanguage("French-IN");

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given()
                .header("Content-Type", "application/json")
                .queryParam("key", "qaclick")
                .body(addPlace)
                .when()
                .log().body()
                .post("/maps/api/place/add/json")
                .then()
                .assertThat().log().body()
                .log().status()
                .assertThat().statusCode(200)
                .body("scope", equalTo("APP"))
                .header("Content-Length", "194")
                .extract().response().asString();
    }
}

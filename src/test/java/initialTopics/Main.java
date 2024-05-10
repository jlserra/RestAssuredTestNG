package initialTopics;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("ADD PLACE--------------------------");
        //Add API
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given()
                .header("Content-Type", "application/json")
                .queryParam("key", "qaclick")
                 // Payload type - payload from class
//                .body(Payload.addPlace())
                 // Payload type - payload from external json file
                .body(Files.readAllBytes(Paths.get("src/main/resources/payload.json")))
                .when()
                .post("/maps/api/place/add/json")
                .then()
                .assertThat().log().body()
                .log().status()
                .assertThat().statusCode(200)
                .body("scope", equalTo("APP"))
                .header("Content-Length", "194")
                .extract().response().asString();

        System.out.println(response);
        JsonPath jsonPath = new JsonPath(response);
        String placeId = jsonPath.getString("place_id");
        System.out.println("place_id: " + jsonPath.getString("place_id"));

        System.out.println("UPDATE PLACE--------------------------");

        //Update API
        String newAddress = "Summer Walk, Africa";

        given()
                .log().body()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\r\n" +
                        "\"place_id\":\"" + placeId + "\",\r\n" +
                        "\"address\":\"" + newAddress + "\",\r\n" +
                        "\"key\":\"qaclick123\"\r\n" +
                        "}")
                .when()
                .put("maps/api/place/update/json")
                .then()
                .assertThat().log().body()
                .log().status()
                .statusCode(200)
                .body("msg", equalTo("Address successfully updated"))
                .extract().response().asString();

        //Get API
        System.out.println("GET PLACE--------------------------");

        String getPlaceResponse = given()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .when()
                .get("maps/api/place/get/json")
                .then()
                .assertThat().log().body()
                .log().status()
                .statusCode(200)
                .extract().response().asString();

        JsonPath getPlaceJson = new JsonPath(getPlaceResponse);
        String actualAddress = getPlaceJson.getString("address");

    }
}
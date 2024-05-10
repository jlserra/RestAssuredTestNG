package pojo.executables;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;
import pojo.pojoClasses.RequestAddPlace;
import pojo.pojoClasses.requestAddPlace.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Specbuilders {

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

        RequestSpecification requestSpecAddPlace = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Content-Type", "application/json")
                .addQueryParam("key", "qaclick")
                .log(LogDetail.BODY)
                .setBody(addPlace)
                .build();


        ResponseSpecification responseSpecAddPlace = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody("scope", equalTo("APP"))
                .log(LogDetail.BODY)
                .expectHeader("Content-Length", "194")
                .build();

        String response = given()
                .spec(requestSpecAddPlace)
                .when()
                .post("/maps/api/place/add/json")
                .then()
                .spec(responseSpecAddPlace)
                .extract().response().asString();

    }

    @Test()
    public void test(){
        String text = "aaabbedd";
        String currentChar = "";
        String charCheck = "";
        String done = "";
        int counter = 0;
        for(int i = 0; i < text.length(); i++){
            for(int x = 0 ; x < text.length(); x++){
                currentChar = String.valueOf(text.charAt(i));
                charCheck = String.valueOf(text.charAt(x));
                    if(currentChar.equalsIgnoreCase(charCheck)){
                        counter++;
                    }
            }
            if(!done.contains(currentChar)){
                System.out.println(currentChar + " - " + counter);
            }
            done += currentChar;
            counter = 0;
        }
    }

}

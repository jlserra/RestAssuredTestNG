package initialTopics;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.Payload;

import static io.restassured.RestAssured.given;

public class DynamicJson {

    @Test(dataProvider = "BooksData")
    public void addBook(String aisle, String isbn) {

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given()
                .log().body()
                .header("Content-Type", "application/json")
                .body(Payload.addBook(aisle, isbn))
                .when()
                .post("/Library/Addbook.php")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .extract().response().asString();

        JsonPath js = ReusableComponents.stringToJson(response);
        String bookID = js.get("ID");
        System.out.println("bookID: " + bookID);
    }

    @DataProvider(name = "BooksData")
    public Object[][] getData() {
        return new Object[][]{{"12345", "abc"}, {"12346", "efg"}, {"12347", "hij"}};
    }
}

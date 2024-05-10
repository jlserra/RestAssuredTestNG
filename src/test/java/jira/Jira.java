package jira;

import initialTopics.ReusableComponents;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class Jira {

    SessionFilter sessionFilter = new SessionFilter();
    String BaseURL = "http://localhost:5050/";
    String sessionID = "";
    String issueID = "";
    String issueKey = "";
    String issueCommentID = "";

    @Test(priority = 1)
    public void userLogin() {
        RestAssured.baseURI = BaseURL;
        String response = given()
                .relaxedHTTPSValidation()
                .log().body()
                .filter(sessionFilter)
                .header("Content-Type", "application/json")
                .body(JiraPayload.jiraLogin("jserra", "Password1."))
                .when()
                .post("/rest/auth/1/session")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .extract().response().asString();

        JsonPath js = ReusableComponents.stringToJson(response);
        sessionID = js.get("session.value");

    }

    @Test(dataProvider = "addIssueData", priority = 2)
    public void addIssue(String key, String summary, String description, String issuetype) {
        RestAssured.baseURI = BaseURL;
        String response = given()
                .relaxedHTTPSValidation()
                .log().body()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + sessionID)
                .body(JiraPayload.jiraCreateIssues(key, summary, description, issuetype))
                .when()
                .post("/rest/api/2/issue")
                .then()
                .log().body()
                .assertThat()
                .statusCode(201)
                .extract().response().asString();

        JsonPath js = ReusableComponents.stringToJson(response);
        issueID = js.get("id");
        issueKey = js.get("key");

        System.out.println("issueID: " + issueID);
        System.out.println("issueKey: " + issueKey);
    }

    @Test(dataProvider = "commentIssueData", priority = 3)
    public void commentIssue(String body) {
        RestAssured.baseURI = BaseURL;
        String response = given()
                .relaxedHTTPSValidation()
                .pathParam("key", issueKey)
                .log().body()
                .header("Content-Type", "application/json")
                .filter(sessionFilter)
                .body(JiraPayload.jiraCommentIssue(body))
                .when()
                .post("/rest/api/2/issue/{key}/comment")
                .then()
                .log().body()
                .assertThat()
                .statusCode(201)
                .extract().response().asString();


        JsonPath js = ReusableComponents.stringToJson(response);
        issueCommentID = js.get("id");

        System.out.println("issueCommentID: " + issueCommentID);

    }

    @Test(priority = 4)
    public void addAttachment() {
        RestAssured.baseURI = BaseURL;
        String response = given()
                .relaxedHTTPSValidation()
                .log().body()
                .pathParam("key", issueKey)
                .header("Content-Type", "multipart/form-data")
                .header("X-Atlassian-Token","no-check")
                .filter(sessionFilter)
                .multiPart("file",new File("src/main/resources/payload.json"))
                .when()
                .post("/rest/api/2/issue/{key}/attachments")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .extract().response().asString();
    }

    @Test(priority = 5)
    public void getIssue() {
        RestAssured.baseURI = BaseURL;
        String response = given()
                .relaxedHTTPSValidation()
                .log().body()
                .pathParam("key", issueKey)
                .queryParam("fields","summary, description, comment")
                .header("Content-Type", "application/json")
                .filter(sessionFilter)
                .when()
                .get("/rest/api/2/issue/{key}")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .extract().response().asString();

        JsonPath js = ReusableComponents.stringToJson(response);
        int commentsCount = js.get("fields.comment.comments.size()");
        for (int i = 0; i < commentsCount; i++) {
            System.out.println(js.getString("fields.comment.comments["+i+"].id"));
            String actualComment = js.getString("fields.comment.comments["+i+"].body");
            System.out.println(actualComment);
            Assert.assertEquals(actualComment,commentIssueData()[i].toString());
        }
    }



    @Test
    public void getComment(){
        System.out.println(commentIssueData()[0].toString());
    }

    @DataProvider(name = "addIssueData")
    public Object[][] addIssueData() {
        String rand = ReusableComponents.getRandomNumber();
        return new Object[][]{{"AT", "Summary " + rand, "Description " + rand, "Bug"}};
    }

    @DataProvider(name = "commentIssueData")
    public Object[] commentIssueData() {
        return new Object[]{"comment 1","comment 2"};
    }

//Test Data Provider Sample
//    @Test(dataProvider = "addIssueData")
//    public void testIssue(String key, String summary, String description, String issuetype) {
//        System.out.println("Key : " + key);
//        System.out.println("Summary : " + summary);
//        System.out.println("Description : " + description);
//        System.out.println("Issuetype : " + issuetype);
//    }

}

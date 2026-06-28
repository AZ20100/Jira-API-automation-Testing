package com.testing.base;

import io.qameta.allure.*;
import com.testing.base.JiraBaseTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Jira REST API Regression Suite")
@Feature("Issues Management")

public class JiraIssuesTest extends JiraBaseTest {
    @Test(priority = 16)
    @Story("Comments Lifecycle")
    @Description("Verify that a user can successfully add a text comment to an active Jira issue.")
    @Severity(SeverityLevel.CRITICAL)


    public void testAddTextComment() {
        String commentPayload = "{\n" +
                "  \"body\": {\n" +
                "    \"type\": \"doc\",\n" +
                "    \"version\": 1,\n" +
                "    \"content\": [\n" +
                "      { \"type\": \"paragraph\", \"content\": [ { \"type\": \"text\", \"text\": \"Initial test automation comment for validation.\" } ] }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        commentId = given()
                .pathParam("key", createdKey)
                .body(commentPayload)
                .when()
                .post("rest/api/3/issue/{key}/comment")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract().path("id");
    }

    @Test(priority = 17, dependsOnMethods = "testAddTextComment")
    public void testVerifyCommentExists() {
        given()
                .pathParam("key", createdKey)
                .when()
                .get("rest/api/3/issue/{key}/comment")
                .then()
                .statusCode(200)
                .body("comments.id", hasItem(commentId));
    }

    @Test(priority = 18, dependsOnMethods = "testAddTextComment")
    public void testEditCommentContent() {
        String updatedPayload = "{\n" +
                "  \"body\": {\n" +
                "    \"type\": \"doc\",\n" +
                "    \"version\": 1,\n" +
                "    \"content\": [\n" +
                "      { \"type\": \"paragraph\", \"content\": [ { \"type\": \"text\", \"text\": \"Updated test automation comment content.\" } ] }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        given()
                .pathParam("key", createdKey)
                .pathParam("commentId", commentId)
                .body(updatedPayload)
                .when()
                .put("rest/api/3/issue/{key}/comment/{commentId}")
                .then()
                .statusCode(200)
                .body("body.content[0].content[0].text", equalTo("Updated test automation comment content."));
    }

    @Test(priority = 19, dependsOnMethods = "testAddTextComment")
    public void testDeleteComment() {
        given()
                .pathParam("key", createdKey)
                .pathParam("commentId", commentId)
                .when()
                .delete("rest/api/3/issue/{key}/comment/{commentId}")
                .then()
                .statusCode(204);
    }

    @Test(priority = 20)
    public void testAssignIssueToUser() {
        Map<String, String> payload = new HashMap<>();
        payload.put("accountId", "712020:94ef29b0-faa2-4b65-8399-c710f10a97df");

        given()
                .pathParam("key", createdKey)
                .body(payload)
                .when()
                .put("rest/api/3/issue/{key}/assignee")
                .then()
                .statusCode(204);
    }



    @Test(priority = 22)
    public void testSetAssigneeToNull() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("accountId", null);

        given()
                .pathParam("key", createdKey)
                .body(payload)
                .when()
                .put("rest/api/3/issue/{key}/assignee")
                .then()
                .statusCode(204);
    }

    @Test(priority = 23)
    public void testFetchAvailableTransitions() {
        given()
                .pathParam("key", createdKey)
                .when()
                .get("rest/api/3/issue/{key}/transitions")
                .then()
                .statusCode(200)
                .body("transitions", is(notNullValue()));
    }

    @Test(priority = 24)
    public void testTransitionToInProgress() {
        String transitionPayload = "{ \"transition\": { \"id\": \"21\" } }";

        given()
                .pathParam("key", createdKey)
                .body(transitionPayload)
                .when()
                .post("rest/api/3/issue/{key}/transitions")
                .then()
                .statusCode(204);
    }

    @Test(priority = 25)
    public void testVerifyStatusIsInProgress() {
        given()
                .pathParam("key", createdKey)
                .when()
                .get("rest/api/3/issue/{key}")
                .then()
                .statusCode(200)
                .body("fields.status.name", equalTo("In Progress"));
    }

    @Test(priority = 26)
    public void testTransitionToDone() {
        String transitionPayload = "{ \"transition\": { \"id\": \"31\" } }";

        given()
                .pathParam("key", createdKey)
                .body(transitionPayload)
                .when()
                .post("rest/api/3/issue/{key}/transitions")
                .then()
                .statusCode(204);
    }



    @Test(priority = 28)
    public void testNegativeAddCommentToDeletedIssue() {
        String simpleBody = "{ \"body\": { \"type\": \"doc\", \"version\": 1, \"content\": [] } }";

        given()
                .pathParam("key", "SCRUM-999999")
                .body(simpleBody)
                .when()
                .post("rest/api/3/issue/{key}/comment")
                .then()
                .statusCode(404);
    }

    @Test(priority = 29)
    public void testNegativeForceInvalidTransition() {
        String invalidPayload = "{ \"transition\": { \"id\": \"999999\" } }";

        given()
                .pathParam("key", createdKey)
                .body(invalidPayload)
                .when()
                .post("rest/api/3/issue/{key}/transitions")
                .then()
                .statusCode(Matchers.oneOf(400, 404));
    }

    @Test(priority = 30)
    public void testNegativeAssignToInvalidUser() {
        Map<String, String> payload = new HashMap<>();
        payload.put("accountId", "invalid-id-string-123");

        given()
                .pathParam("key", createdKey)
                .body(payload)
                .when()
                .put("rest/api/3/issue/{key}/assignee")
                .then()
                .statusCode(404);
    }
}
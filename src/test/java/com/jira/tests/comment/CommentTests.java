package com.jira.tests.comment;

import com.jira.models.request.*;
import com.jira.clients.CommentClient;
import com.jira.clients.IssueClient;
import com.jira.models.response.CommentResponse;
import com.jira.models.response.IssueResponse;
import io.qameta.allure.*;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

@Epic("Jira API Automation")
@Feature("Comment Management")
public class CommentTests {

    private IssueClient issueClient;
    private CommentClient commentClient;
    private String subTaskKey;
    private String commentId;

    @BeforeClass
    public void setup() {
        issueClient = new IssueClient();
        commentClient = new CommentClient();

        CreateIssueRequest parentRequest = CreateIssueRequest.builder()
                .fields(Fields.builder()
                        .project(Project.builder().key("SCRUM").build())
                        .summary("Authentication")
                        .issueType(IssueType.builder().name("Task").build())
                        .build())
                .build();
        Response parentResponse = issueClient.createIssue(parentRequest);
        Assert.assertEquals(parentResponse.getStatusCode(), 201,
                "Setup failed: could not create parent issue");
        String parentKey = parentResponse.as(IssueResponse.class).getKey();


        CreateIssueRequest subTaskRequest = CreateIssueRequest.builder()
                .fields(Fields.builder()
                        .project(Project.builder().key("SCRUM").build())
                        .summary("Login")
                        .issueType(IssueType.builder().name("Subtask").build())
                        .parent(Parent.builder().key(parentKey).build())
                        .build())
                .build();

        Response subTaskResponse = issueClient.createIssue(subTaskRequest);
        Assert.assertEquals(subTaskResponse.getStatusCode(), 201,
                "Setup failed: could not create subtask");
        subTaskKey = subTaskResponse.as(IssueResponse.class).getKey();
    }

    @Test(priority = 1)
    @Story("Add Comment")
    @Description("Verify that a comment can be added to a SubTask successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void addComment() {

        CommentRequest request = buildCommentRequest("User should be logged out");

        Response response = commentClient.addComment(subTaskKey, request);

        Assert.assertEquals(response.getStatusCode(), 201);
        CommentResponse commentResponse = response.as(CommentResponse.class);
        Assert.assertNotNull(commentResponse.getId());
        commentId = commentResponse.getId();
    }

    @Test(priority = 2, dependsOnMethods = "addComment")
    @Story("Get Comments")
    @Description("Verify that the added comment exists in the comments list")
    @Severity(SeverityLevel.NORMAL)
    public void getComments() {

        Response response = commentClient.getComment(subTaskKey);

        Assert.assertEquals(response.getStatusCode(), 200);
        CommentResponse commentResponse = response.as(CommentResponse.class);
        Assert.assertTrue(commentResponse.getTotal() > 0);
        Assert.assertEquals(commentResponse.getComments().get(0).getId(), commentId);
    }

        @Test(priority = 3, dependsOnMethods = "addComment")
        @Story("Update Comment")
        @Description("Verify that a comment can be updated with new text")
        @Severity(SeverityLevel.NORMAL)
        public void updateComment() {

            CommentRequest request = buildCommentRequest("User should be logged out and registered");

            Response response = commentClient.updateComment(subTaskKey, commentId, request);

            Assert.assertEquals(response.getStatusCode(), 200);
            CommentResponse commentResponse = response.as(CommentResponse.class);
            String actualText = commentResponse.getBody()
                    .getContent().get(0)
                    .getContent().get(0)
                    .getText();
            Assert.assertEquals(actualText, "User should be logged out and registered");
        }

        @Test(priority = 4, dependsOnMethods = "addComment")
        @Story("Delete Comment")
        @Description("Verify that a comment can be deleted successfully")
        @Severity(SeverityLevel.CRITICAL)
        public void deleteComment() {

            Response response = commentClient.deleteComment(subTaskKey, commentId);

            Assert.assertEquals(response.getStatusCode(), 204);
        }


        @Test(priority = 5, dependsOnMethods = "deleteComment")
        @Story("Delete Comment")
        @Description("Verify that a deleted comment returns 404")
        @Severity(SeverityLevel.CRITICAL)
        public void getDeletedComment() {

            Response response = commentClient.getCommentByID(subTaskKey, commentId);

            Assert.assertEquals(response.getStatusCode(), 404);
        }



        private CommentRequest buildCommentRequest(String text) {
    return CommentRequest.builder()
            .body(CommentRequest.Body.builder()
                    .type("doc")
                    .version(1)
                    .content(List.of(
                            CommentRequest.Body.Content.builder()
                                    .type("paragraph")
                                    .content(List.of(
                                            CommentRequest.Body.Content.InnerContent.builder()
                                                    .type("text")
                                                    .text(text)
                                                    .build()
                                    ))
                                    .build()
                    ))
                    .build())
            .build();
}
}
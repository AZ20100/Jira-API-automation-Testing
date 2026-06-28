package com.jira.tests.issue;

import com.jira.clients.IssueClient;
import com.jira.models.request.*;
import com.jira.models.request.Description;
import com.jira.models.response.IssueResponse;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.http.protocol.ResponseServer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Jira API Automation")
@Feature("Issue Management")
public class IssueTests {

    private IssueClient issueClient;
    private String issueKey;
    private String highPriorityIssueKey;
    private String deleteIssueKey;

    @BeforeClass
    public void setup(){
        issueClient = new IssueClient();
    }

    @Test (priority = 1)
    @Story("Create Issue")
    @io.qameta.allure.Description("Verify that a new Task issue can be created successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void createIssueSeccessfully(){
        CreateIssueRequest request = CreateIssueRequest.builder()
                .fields(Fields.builder()
                        .project(Project.builder().key("SCRUM").build())
                        .summary("Authentication")
                        .issueType(IssueType.builder().name("Task").build())
                        .build())
        .build();

        Response response = issueClient.createIssue(request);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),201);
        IssueResponse issueResponse = response.as(IssueResponse.class);
        Assert.assertNotNull(issueResponse.getKey());

        issueKey = issueResponse.getKey();
    }

    @Test (priority = 2, dependsOnMethods = "createIssueSeccessfully")
    @Story("Get Issue")
    @io.qameta.allure.Description("Verify that the created issue can be retrieved with correct data")
    @Severity(SeverityLevel.CRITICAL)
    public void getCreatedIssue(){
        Response response = issueClient.getIssue(issueKey);

        Assert.assertEquals(response.getStatusCode(), 200);
        IssueResponse issue = response.as(IssueResponse.class);
        Assert.assertEquals(issue.getFields().getSummary(),"Authentication");
        Assert.assertEquals(issue.getFields().getIssueType().getName(),"Task");
        Assert.assertEquals(issue.getFields().getStatus().getName(),"To Do");
    }

    @Test (priority = 3, dependsOnMethods = "createIssueSeccessfully")
    @Story("Update Issue")
    @io.qameta.allure.Description("Verify that issue summary can be updated successfully")
    @Severity(SeverityLevel.NORMAL)
    public void UpdateIssueSummery() {
        CreateIssueRequest request = CreateIssueRequest.builder()
                .fields(Fields.builder()
                        .summary("Authentication Tests")
                        .build())
                .build();

        Response response = issueClient.updateIssue(issueKey, request);

        Assert.assertEquals(response.getStatusCode(), 204);
    }

    @Test (priority = 4, dependsOnMethods = "UpdateIssueSummery")
    @Story("Get Issue")
    @io.qameta.allure.Description("Verify that the updated summary is reflected correctly")
    @Severity(SeverityLevel.NORMAL)
    public void getUpdatedIssueSummery() {
        Response response = issueClient.getIssue(issueKey);

        Assert.assertEquals(response.getStatusCode(), 200);
        IssueResponse issue = response.as(IssueResponse.class);
        Assert.assertEquals(issue.getFields().getSummary(),"Authentication Tests");
    }

    @Test (priority = 5, dependsOnMethods = "createIssueSeccessfully")
    @Story("Update Issue")
    @io.qameta.allure.Description("Verify that issue description can be updated with ADF format")
    @Severity(SeverityLevel.NORMAL)
    public void UpdateIssueDescriprion() {
        CreateIssueRequest request = CreateIssueRequest.builder()
                .fields(Fields.builder()
                        .description(buildDescription("User should be logged out"))
                        .build())
                .build();

                Response response = issueClient.updateIssue(issueKey,request);
                Assert.assertEquals(response.getStatusCode(), 204);
    }

    @Test (priority = 6, dependsOnMethods = "UpdateIssueDescriprion")
    @Story("Get Issue")
    @io.qameta.allure.Description("Verify that the updated description text is correct")
    @Severity(SeverityLevel.NORMAL)
    public void getUpdatedIssueDescriprion() {
        Response response = issueClient.getIssue(issueKey);

        Assert.assertEquals(response.getStatusCode(), 200);
        IssueResponse issue = response.as(IssueResponse.class);
        String actualText = issue.getFields()
                .getDescription()
                .getContent().get(0)
                .getContent().get(0)
                .getText();
        Assert.assertEquals(actualText, "User should be logged out");

    }

    @Test(priority = 7)
    @Story("Create Issue")
    @io.qameta.allure.Description("Verify that a High Priority issue can be created successfully")
    @Severity(SeverityLevel.NORMAL)
    private void createHighPriorityIssue(){
        CreateIssueRequest request = CreateIssueRequest.builder()
                .fields(Fields.builder()
                        .project(Project.builder().key("SCRUM").build())
                        .summary("Logout")
                        .issueType(IssueType.builder().name("Task").build())
                        .priority(Priority.builder().name("High").build())
                        .build())
                .build();

        Response response = issueClient.createIssue(request);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),201);
         IssueResponse issueResponse = response.as(IssueResponse.class);
         Assert.assertNotNull(issueResponse.getKey());

         highPriorityIssueKey = issueResponse.getKey();
    }

    @Test (priority = 8,dependsOnMethods = "createHighPriorityIssue")
    @Story("Get Issue")
    @io.qameta.allure.Description("Verify that the High Priority issue has correct priority value")
    @Severity(SeverityLevel.NORMAL)
    public void getcreateHighPriorityIssue(){
        Response response = issueClient.getIssue(highPriorityIssueKey);

        Assert.assertEquals(response.getStatusCode(),200);
        IssueResponse issue = response.as(IssueResponse.class);
        Assert.assertEquals(issue.getFields().getSummary(), "Logout");
        Assert.assertEquals(issue.getFields().getPriority().getName(), "High");
    }


    @Test(priority = 11)
    @Story("Create Issue - Negative")
    @io.qameta.allure.Description("Verify that creating an issue with empty body returns 400")
    @Severity(SeverityLevel.CRITICAL)
    private void CreateIssueWithEmptyBody(){

        CreateIssueRequest request= CreateIssueRequest.builder().build();
        Response response = issueClient.createIssue(request);
        Assert.assertEquals(response.getStatusCode(), 400);
    }


    @Test (priority = 12)
    @Story("Update Issue - Negative")
    @io.qameta.allure.Description("Verify that updating a non-existent issue returns 404")
    @Severity(SeverityLevel.CRITICAL)
    public void updateDeletedIssue(){
        CreateIssueRequest request = CreateIssueRequest.builder()
                .fields(Fields.builder()
                        .summary("Sign up")
                        .build())
                .build();

        Response response = issueClient.updateIssue("SCRUM=900", request);
        Assert.assertEquals(response.getStatusCode(),404);
    }


    @Test (priority = 13)
    @Story("Delete Issue")
    @io.qameta.allure.Description("Verify that an issue can be created for deletion")
    @Severity(SeverityLevel.NORMAL)
    public void CreateIssueToDelete(){
        CreateIssueRequest request = CreateIssueRequest.builder()
                .fields(Fields.builder()
                        .project(Project.builder().key("SCRUM").build())
                        .summary("Issue to be deleted")
                        .issueType(IssueType.builder().name("Task").build())
                        .build())
                .build();

        Response response = issueClient.createIssue(request);

        Assert.assertEquals(response.getStatusCode(), 201);
        deleteIssueKey = response.as(IssueResponse.class).getKey();
        Assert.assertNotNull(deleteIssueKey);
    }

    @Test (priority = 14, dependsOnMethods = "CreateIssueToDelete")
    @Story("Delete Issue")
    @io.qameta.allure.Description("Verify that an issue can be deleted successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteIssue(){
        Response response = issueClient.deleteIssue(deleteIssueKey);

        Assert.assertEquals(response.getStatusCode(),204);
    }


@Test(priority = 15, dependsOnMethods = "deleteIssue")
@Story("Delete Issue")
@io.qameta.allure.Description("Verify that a deleted issue returns 404")
@Severity(SeverityLevel.CRITICAL)
public void getDeletedIssue(){
        Response response = issueClient.getIssue(deleteIssueKey);
        Assert.assertEquals(response.getStatusCode(), 404);
}




    private Description buildDescription(String text){
        return Description.builder()
                .type("doc")
                .version(1)
                .content(List.of(Description.Content.builder()
                        .type("paragraph")
                        .content(List.of(
                                Description.InnerContent.builder()
                                        .type("text")
                                        .text(text)
                                        .build()
                        ))
                        .build()))
                .build();
    }
}

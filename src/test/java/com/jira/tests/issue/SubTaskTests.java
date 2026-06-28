package com.jira.tests.issue;

import com.jira.clients.IssueClient;
import com.jira.models.request.*;
import com.jira.models.response.IssueResponse;
import io.qameta.allure.*;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("Jira API Automation")
@Feature("SubTask Management")
public class SubTaskTests {
    private IssueClient issueClient;
    private String parentIssueKey;
    private String subTaskKey;

    @BeforeClass
    public void setup(){
        issueClient = new IssueClient();

        CreateIssueRequest parentRequest = CreateIssueRequest.builder()
                .fields(Fields.builder()
                        .project(Project.builder().key("SCRUM").build())
                        .summary("Authentication")
                        .issueType(IssueType.builder().name("Task").build())
                        .build())
                .build();

        Response response = issueClient.createIssue(parentRequest);
        Assert.assertEquals(response.getStatusCode(), 201);
        parentIssueKey = response.as(IssueResponse.class).getKey();

    }
    @Test(priority = 1)
    @Story("Create SubTask")
    @Description("Verify that a SubTask can be created under a parent issue")
    @Severity(SeverityLevel.CRITICAL)
    public void createSubTask(){
        CreateIssueRequest request = CreateIssueRequest.builder()
                .fields(Fields.builder()
                        .project(Project.builder().key("SCRUM").build())
                        .summary("Login")
                        .issueType(IssueType.builder().name("Subtask").build())
                        .parent(Parent.builder().key(parentIssueKey).build())
                        .build())
                .build();

        Response response = issueClient.createIssue(request);

        Assert.assertEquals(response.getStatusCode(), 201);
        IssueResponse issueResponse = response.as(IssueResponse.class);
        Assert.assertNotNull(issueResponse.getKey());
        subTaskKey = issueResponse.getKey();
    }

    @Test (priority = 2, dependsOnMethods = "createSubTask")
    @Story("Get SubTask")
    @Description("Verify that the SubTask has correct type and parent key")
    @Severity(SeverityLevel.CRITICAL)
    public void getSubTask(){
        Response response = issueClient.getIssue(subTaskKey);

        Assert.assertEquals(response.getStatusCode(), 200);
        IssueResponse issue = response.as(IssueResponse.class);
        Assert.assertEquals(issue.getFields().getSummary(), "Login");
        Assert.assertEquals(issue.getFields().getIssueType().getName(), "Subtask");
        Assert.assertEquals(issue.getFields().getParent().getKey(),parentIssueKey);
    }
}

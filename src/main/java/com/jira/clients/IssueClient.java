package com.jira.clients;

import com.jira.base.BaseApi;
import com.jira.models.request.CreateIssueRequest;
import io.restassured.response.Response;
import lombok.Data;

import static io.restassured.RestAssured.given;

public class IssueClient extends BaseApi {

    private static final String ISSUE_PATH = "/issue";
    private static final String ISSUE_KEY_PATH = "/issue/{issueKey}";

    public Response createIssue(CreateIssueRequest request){
    Response response =
            given(requestSpecification)
                    .body(request)
                    .when()
                    .post(ISSUE_PATH);
    return response;
}

public Response getIssue(String issueKey){
        return given(requestSpecification)
                .pathParams("issueKey", issueKey)
                .when()
                .get(ISSUE_KEY_PATH);
}

public Response updateIssue(String issueKey, CreateIssueRequest request){
        return given(requestSpecification)
                .pathParam("issueKey", issueKey)
                .body(request)
                .when()
                .put(ISSUE_KEY_PATH);
}

public Response deleteIssue(String issueKey){
        return given(requestSpecification)
                .pathParam("issueKey", issueKey)
                .when()
                .delete(ISSUE_KEY_PATH);
}
}

package com.jira.clients;

import com.jira.base.BaseApi;
import com.jira.models.request.CommentRequest;
import com.jira.models.request.Parent;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CommentClient extends BaseApi {

    private static final String COMMENT_PATH = "/issue/{issueKey}/comment";
    private static final String COMMENT_ID_PATH = "/issue/{issueKey}/comment/{commentId}";

    public Response addComment (String issueKey, CommentRequest request){
        return given(requestSpecification)
                .pathParam("issueKey", issueKey)
                .body(request)
                .when()
                .post(COMMENT_PATH);
    }

    public Response getComment(String issueKey){
        return given(requestSpecification)
                .pathParam("issueKey", issueKey)
                .when()
                .get(COMMENT_PATH);
    }

    public Response getCommentByID(String issueKey, String commentId){
        return given(requestSpecification)
                .pathParam("issueKey", issueKey)
                .pathParam("commentId", commentId)
                .when()
                .get(COMMENT_ID_PATH) ;
    }

    public Response updateComment(String issueKey, String commentId, CommentRequest request){
        return given(requestSpecification)
                .pathParam("issueKey", issueKey)
                .pathParam("commentId", commentId)
                .body(request)
                .when()
                .put(COMMENT_ID_PATH);
    }

    public Response deleteComment(String issueKey, String commentId){
        return given(requestSpecification)
                .pathParam("issueKey", issueKey)
                .pathParam("commentId", commentId)
                .when()
                .delete(COMMENT_ID_PATH);
    }



}

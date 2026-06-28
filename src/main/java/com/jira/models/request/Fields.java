package com.jira.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Fields {

private Project project;
private String summary;

@JsonProperty("issuetype")
private IssueType issueType;
private Priority priority;
private Parent parent;
private Description description;

}

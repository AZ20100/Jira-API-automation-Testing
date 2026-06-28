package com.jira.models.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateIssueRequest {

private Fields fields;
}

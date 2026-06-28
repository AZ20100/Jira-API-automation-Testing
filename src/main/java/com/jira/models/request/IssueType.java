package com.jira.models.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IssueType {
    private String name;

}

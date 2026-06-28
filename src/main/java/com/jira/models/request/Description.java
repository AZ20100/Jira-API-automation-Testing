package com.jira.models.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Description {

private String type;
    private int version;
    private List<Content> content;


@Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Content {
    private String type;
    private List<InnerContent> content;
}

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class InnerContent{
        private String type;
        private String text;
    }
}


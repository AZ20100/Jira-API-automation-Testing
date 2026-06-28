package com.jira.models.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequest {
    private Body body;


    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Body{
        private String type;
        private int version;
        private List<Content> content;

        @Data
        @Builder
        public static class Content{
            private String type;
            private List<InnerContent> content;

            @Data
            @Builder
            public static class InnerContent{
                private String type;
                private String text;
            }
        }
    }
}

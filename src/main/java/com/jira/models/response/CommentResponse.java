package com.jira.models.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentResponse {

    private String id;
    private Body body;
    private int total;
    private List<CommentResponse> comments;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body{
        private String type;
        private int version;
        private List<Content> content;


        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Content{
            private String type;
            private List<InnerContent> content;


            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class InnerContent{
                private String type;
                private String text;
            }
        }
    }


}

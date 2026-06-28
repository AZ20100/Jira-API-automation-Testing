package com.jira.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class IssueResponse {
    private String id;
    private String key;
    private String self;
    private Fields fields;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fields{
        private String summary;

        @JsonProperty("issuetype")
        private IssueType issueType;

        private Status status;
        private Priority priority;
        private Parent parent;
        private Description description;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IssueType{
        private String name;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status{
        private String name;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Priority{
        private String name;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Parent{
        private String key;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Description{
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

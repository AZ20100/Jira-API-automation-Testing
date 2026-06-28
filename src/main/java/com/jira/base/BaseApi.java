package com.jira.base;
import com.jira.config.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.*;


public class BaseApi {
    protected RequestSpecification requestSpecification;

    public BaseApi() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.gerProperty("baseUrl"))
                .setContentType(ContentType.JSON)
                .addHeader("Accept","application/json")
                .setAuth(
                        io.restassured.RestAssured.preemptive().basic(
                                ConfigReader.gerProperty("email"),
                                ConfigReader.gerProperty("token")
                        )
                )
                .build();
        }
}

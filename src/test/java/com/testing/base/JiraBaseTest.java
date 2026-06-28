package com.testing.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import java.io.FileInputStream;
import java.util.Properties;

public class JiraBaseTest {

    protected String createdKey = "SCRUM-117";
    protected String commentId;
    private static Properties config = new Properties();

    @BeforeClass
    public void setup() throws Exception {

        // Load config
        FileInputStream fis = new FileInputStream(
                "src/test/resources/config.properties"
        );
        config.load(fis);

        RestAssured.baseURI = config.getProperty("BASE_URL");

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAuth(RestAssured.preemptive().basic(
                        config.getProperty("EMAIL"),
                        config.getProperty("API_TOKEN")
                ))
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
}
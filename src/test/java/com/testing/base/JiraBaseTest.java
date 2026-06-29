package com.testing.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import java.io.FileInputStream;
import java.util.Properties;

public class JiraBaseTest {

    public static String createdKey;
    protected String commentId;
    private static Properties config = new Properties();

    @BeforeClass
    public void setup() throws Exception {

        // Load config
        FileInputStream fis = new FileInputStream(
                "src/test/resources/config.properties"
        );
        config.load(fis);

        RestAssured.baseURI = config.getProperty("baseUrl");

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAuth(RestAssured.preemptive().basic(
                        config.getProperty("email"),
                        config.getProperty("token")
                ))
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
}
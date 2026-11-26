package com.testmuhendisligi.api.base;

import com.testmuhendisligi.api.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;

/**
 * Tum API regresyon test siniflari icin ortak kurulum ve
 * yeniden kullanilabilir Request/Response spesifikasyonlarini saglar.
 */
public abstract class BaseApiTest {

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification defaultResponseSpec;

    @BeforeAll
    static void setUpBaseSpecifications() {
        // Yalnizca dogrulama basarisiz oldugunda istek ve yanit detaylarini logla
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(ApiConfig.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();

        defaultResponseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan(ApiConfig.getMaxResponseTimeMs()), TimeUnit.MILLISECONDS)
                .build();
    }
}

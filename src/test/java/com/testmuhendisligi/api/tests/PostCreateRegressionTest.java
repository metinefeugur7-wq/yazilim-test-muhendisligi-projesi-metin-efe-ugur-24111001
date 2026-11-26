package com.testmuhendisligi.api.tests;

import com.testmuhendisligi.api.base.BaseApiTest;
import com.testmuhendisligi.api.client.PostApiClient;
import com.testmuhendisligi.api.config.ApiConfig;
import com.testmuhendisligi.api.model.Post;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("regression")
@DisplayName("POST /posts Otomatik Regresyon Testleri (Request Body)")
class PostCreateRegressionTest extends BaseApiTest {

    private PostApiClient postApiClient;

    @BeforeEach
    void initClient() {
        postApiClient = new PostApiClient(requestSpec);
    }

    @Test
    @Tag("smoke")
    @DisplayName("POST /posts (POJO Request Body) -> 201 Created, yanit gövdesi ve süre kontrolü")
    void shouldCreatePostUsingPojoRequestBody() {
        // Arrange: JSON request body icin POJO nesnesi hazirlama
        Post newPost = new Post(
                1,
                "Yapay Zeka Destekli Otomatik Regresyon Testi",
                "Rest Assured, JUnit 5 ve Maven ile olusturulan ornek POST istegi."
        );

        // Act: POST istegi gonderme
        Response response = postApiClient.createPost(newPost);

        // Assert: Status code, response body beklenen degerler ve yanit suresi kontrolu
        response.then()
                .spec(defaultResponseSpec)
                .statusCode(201)
                .time(lessThan(ApiConfig.getMaxResponseTimeMs()), TimeUnit.MILLISECONDS)
                .body("id", equalTo(101))
                .body("userId", equalTo(newPost.getUserId()))
                .body("title", equalTo(newPost.getTitle()))
                .body("body", equalTo(newPost.getBody()));

        // POJO olarak deserilestirip ek tip guvenli kontrol
        Post createdPost = response.as(Post.class);
        assertAll("Olusturulan post POJO dogrulamasi",
                () -> assertNotNull(createdPost.getId(), "Olusturulan kaydin id degeri null olmamali"),
                () -> assertEquals(101, createdPost.getId(), "JSONPlaceholder yeni kayit icin 101 dondurmeli"),
                () -> assertEquals(newPost.getTitle(), createdPost.getTitle(), "title gonderilen degerle eslesmeli"),
                () -> assertEquals(newPost.getBody(), createdPost.getBody(), "body gonderilen degerle eslesmeli"),
                () -> assertEquals(newPost.getUserId(), createdPost.getUserId(), "userId gonderilen degerle eslesmeli")
        );
    }

    @Test
    @DisplayName("POST /posts (Map JSON Request Body) -> 201 Created, alan eslesmesi ve yanit suresi kontrolu")
    void shouldCreatePostUsingMapRequestBody() {
        // Arrange: Map yapisiyla dinamik JSON request body olusturma
        Map<String, Object> payload = Map.of(
                "userId", 7,
                "title", "API Regresyon Paketi",
                "body", "Status code, response body ve performans esigi dogrulamasi."
        );

        // Act & Assert
        postApiClient.createPost(payload)
                .then()
                .spec(defaultResponseSpec)
                .statusCode(201)
                .time(lessThan(ApiConfig.getMaxResponseTimeMs()), TimeUnit.MILLISECONDS)
                .body("id", notNullValue())
                .body("id", greaterThan(100))
                .body("userId", equalTo(7))
                .body("title", equalTo("API Regresyon Paketi"))
                .body("body", equalTo("Status code, response body ve performans esigi dogrulamasi."));
    }
}

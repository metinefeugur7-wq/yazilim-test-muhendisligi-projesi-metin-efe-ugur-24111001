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

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("regression")
@DisplayName("GET /posts Otomatik Regresyon Testleri")
class PostGetRegressionTest extends BaseApiTest {

    private PostApiClient postApiClient;

    @BeforeEach
    void initClient() {
        postApiClient = new PostApiClient(requestSpec);
    }

    @Test
    @Tag("smoke")
    @DisplayName("GET /posts/1 -> 200 OK, beklenen alan degerleri ve yanit suresi kontrolu")
    void shouldReturnExpectedPostByIdWithinResponseTime() {
        Response response = postApiClient.getPostById(1);

        // 1. Status code, Content-Type, yanit suresi (< X ms) ve body deger kontrolleri
        response.then()
                .spec(defaultResponseSpec)
                .statusCode(200)
                .time(lessThan(ApiConfig.getMaxResponseTimeMs()), TimeUnit.MILLISECONDS)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", equalTo("sunt aut facere repellat provident occaecati excepturi optio reprehenderit"))
                .body("body", not(emptyOrNullString()))
                .body("body", containsString("quia et suscipit"));

        // 2. POJO deserilestirme ile tip guvenli dogrulama
        Post actualPost = response.as(Post.class);
        assertAll("Tekil post POJO dogrulamasi",
                () -> assertNotNull(actualPost, "Post nesnesi bos olmamali"),
                () -> assertEquals(1, actualPost.getId(), "id alani 1 olmali"),
                () -> assertEquals(1, actualPost.getUserId(), "userId alani 1 olmali"),
                () -> assertFalse(actualPost.getTitle().isBlank(), "title alani bos olmamali")
        );
    }

    @Test
    @DisplayName("GET /posts -> 200 OK, 100 kayit listelenmesi ve yanit suresi kontrolu")
    void shouldReturnAllPostsListWithinResponseTime() {
        postApiClient.getAllPosts()
                .then()
                .spec(defaultResponseSpec)
                .statusCode(200)
                .time(lessThan(ApiConfig.getMaxResponseTimeMs()), TimeUnit.MILLISECONDS)
                .body("$", hasSize(100))
                .body("[0].id", equalTo(1))
                .body("[0].userId", equalTo(1));
    }

    @Test
    @DisplayName("GET /posts?userId=1 -> 200 OK, yalnizca ilgili kullaniciya ait kayitlarin donmesi")
    void shouldFilterPostsByUserIdWithinResponseTime() {
        postApiClient.getPostsByUserId(1)
                .then()
                .spec(defaultResponseSpec)
                .statusCode(200)
                .time(lessThan(ApiConfig.getMaxResponseTimeMs()), TimeUnit.MILLISECONDS)
                .body("$", hasSize(10))
                .body("userId", everyItem(equalTo(1)));
    }

    @Test
    @DisplayName("GET /posts/999999 (Negatif Senaryo) -> 404 Not Found ve yanit suresi kontrolu")
    void shouldReturnNotFoundForNonExistentPost() {
        postApiClient.getPostById(999999)
                .then()
                .statusCode(404)
                .time(lessThan(ApiConfig.getMaxResponseTimeMs()), TimeUnit.MILLISECONDS);
    }
}

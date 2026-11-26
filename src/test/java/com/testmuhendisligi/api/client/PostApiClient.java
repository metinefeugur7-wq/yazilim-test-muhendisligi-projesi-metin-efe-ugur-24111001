package com.testmuhendisligi.api.client;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * /posts kaynagina yapilan HTTP cagrilarini test dogrulamalarindan ayiran istemci katmani.
 */
public class PostApiClient {

    private static final String POSTS_ENDPOINT = "/posts";
    private final RequestSpecification requestSpec;

    public PostApiClient(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    public Response getPostById(int postId) {
        return given()
                .spec(requestSpec)
                .pathParam("id", postId)
                .when()
                .get(POSTS_ENDPOINT + "/{id}");
    }

    public Response getAllPosts() {
        return given()
                .spec(requestSpec)
                .when()
                .get(POSTS_ENDPOINT);
    }

    public Response getPostsByUserId(int userId) {
        return given()
                .spec(requestSpec)
                .queryParam("userId", userId)
                .when()
                .get(POSTS_ENDPOINT);
    }

    public Response createPost(Object requestBody) {
        return given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(POSTS_ENDPOINT);
    }
}

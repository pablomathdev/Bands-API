package com.github.pablomathdev.api;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;

import com.github.pablomathdev.utils.ExecuteSQL;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application-test.properties")
public class GenreAPITest {

	static final String CREATE_GENRE_SUCCESS = "classpath:data/create_genre_test_success.json";

	@LocalServerPort
	private int port;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private ExecuteSQL executeSQL;

	@BeforeEach
	private void setUp() {
		enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		basePath = "/api/genres";

		clearDatabase();

		prepareData();

	}

	@Test
	public void should_ReturnStatusCode201_WhenGenreIsCreated() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_GENRE_SUCCESS);

		given()
		.body(resource.getInputStream())
		.contentType(ContentType.JSON)
		.accept(ContentType.JSON)
		.when()
		.post()
		.then()
	    .statusCode(200);
		

	}

	public void prepareData() {
		executeSQL.run("data_test.sql");
	}

	public void clearDatabase() {
		executeSQL.run("clear_database_test.sql");
	}
}

package com.github.pablomathdev.api;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
@ExtendWith(MockitoExtension.class)
public class GenreAPITest {

	private static final String CREATE_GENRE_SUCCESS = "classpath:data/create_genre_test_success.json";
	private static final String CREATE_GENRE_ERROR_GENRE_EXISTING = "classpath:data/create_genre_test_error_genre_existing.json";
	private static final String UPDATE_GENRE_SUCCESS = "classpath:data/update_genre_test_success.json";
	
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
		basePath = "/v1/genres";

		clearDatabase();

		prepareData();

	}

	@Test
	public void should_ReturnStatusCode201_WhenGenreIsCreated() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_GENRE_SUCCESS);

		given().body(resource.getInputStream()).contentType(ContentType.JSON).accept(ContentType.JSON).when().post()
				.then().statusCode(200);

	}

	@Test
	public void should_ReturnStatusCode409_WhenGenreAlreadyExists() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_GENRE_ERROR_GENRE_EXISTING);

		given().body(resource.getInputStream()).contentType(ContentType.JSON).accept(ContentType.JSON).when().post()
				.then().statusCode(409);

	}

	@Test
	public void should_ReturnStatusCode200AndAllGenres_WhenGenreExist() {

		given().accept(ContentType.JSON).when().get().then().statusCode(200).assertThat().body("size()",
				Matchers.is(3));

	}
	@Test
	public void should_ReturnStatusCode204_WhenGenreNotExists() {

		clearDatabase();

		given().accept(ContentType.JSON).when().get().then().statusCode(204);

	}
	@Test
	public void should_ReturnStatusCode204_WhenGenreIsRemoved() {



		given()
		.accept(ContentType.JSON)
		.when()
		.delete("/power metal")
		.then()
		.statusCode(204);

	}
	@Test
	public void should_ReturnStatusCode404_WhenGenreNotExists() {



		given()
		.accept(ContentType.JSON)
		.when()
		.delete("/alternative rock")
		.then()
		.statusCode(404);

	}
	@Test
	public void should_ReturnStatusCode409_WhenGenreAssociatedWithOtherEntities() {



		given()
		.accept(ContentType.JSON)
		.when()
		.delete("/heavy metal")
		.then()
		.statusCode(409);

	}
	
	@Test
	public void should_ReturnStatusCode200_WhenGenreIsUpdated() throws IOException {

		Resource resource = resourceLoader.getResource(UPDATE_GENRE_SUCCESS);
		
		given()
		.contentType(ContentType.JSON)
		.body(resource.getInputStream())
		.accept(ContentType.JSON)
		.when()
		.put("/1")
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

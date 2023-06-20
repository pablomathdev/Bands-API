package com.github.pablomathdev.api;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application-test.properties")
public class BandAPITest {

	static final String CREATE_BAND_SUCCESS = "classpath:data/create_band_test_success.json";
	static final String CREATE_BAND_ERROR_BAND_WITH_NON_EXISTENT_GENRE = "classpath:data/create_band_test_error_non-existent_genre.json";
	static final String CREATE_BAND_ERROR_BAND_EXISTING = "classpath:data/create_band_test_error_band_existing.json";

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private ExecuteSQL executeSQL;

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() {
		enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		basePath = "/v1/bands";

		clearDatabase();

		prepareData();

	}

	@Test
	public void should_ReturnStatusCode201_WhenBandIsCreated() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_BAND_SUCCESS);

		given().body(resource.getInputStream()).contentType(ContentType.JSON).accept(ContentType.JSON).when().post()
				.then().statusCode(201);

	}

	@Test
	public void should_ReturnStatusCode400_WhenGenreInBandNotExists() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_BAND_ERROR_BAND_WITH_NON_EXISTENT_GENRE);

		given().body(resource.getInputStream()).contentType(ContentType.JSON).accept(ContentType.JSON).when().post()
				.then().statusCode(400);

	}

	@Test
	public void should_ReturnStatusCode409_WhenBandAlreadyExist() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_BAND_ERROR_BAND_EXISTING);

		given().body(resource.getInputStream()).contentType(ContentType.JSON).accept(ContentType.JSON).when().post()
				.then().statusCode(409);

	}

	@Test
	public void should_ReturnStatusCode200AndAllBands_WhenBandExist() {

		given().accept(ContentType.JSON).when().get().then().statusCode(200).assertThat().body("size()",
				Matchers.is(1));

	}

	@Test
	public void should_ReturnStatusCode204_WhenBandNotExists() {

		clearDatabase();

		given().accept(ContentType.JSON).when().get().then().statusCode(204);

	}
	
	@Test 
	public void should_ReturnStatusCode204_WhenBandIsRemoved() {
		
		given()
		.accept(ContentType.JSON)
		.when()
		.delete("/metallica")
		.then()
		.statusCode(204);
	}
	
	@Test
	public void should_ReturnStatusCode404_WhenBandNotExists() {
		given()
		.accept(ContentType.JSON)
		.when()
		.delete("/nirvana")
		.then()
		.statusCode(404);
	}

	public void prepareData() {
		executeSQL.run("data_test.sql");
	}

	public void clearDatabase() {
		executeSQL.run("clear_database_test.sql");
	}
}

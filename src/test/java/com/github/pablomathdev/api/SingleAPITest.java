package com.github.pablomathdev.api;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.hamcrest.Matchers;
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
public class SingleAPITest {

	static final String CREATE_SINGLE_SUCCESS = "classpath:data/create_single_test_success.json";
	static final String CREATE_SINGLE_ERROR_SINGLE_WITH_NON_EXISTENT_BAND = "classpath:data/create_single_test_error_non-existent_band.json";
	static final String CREATE_SINGLE_ERROR_SINGLE_WITH_NON_EXISTENT_GENRE = "classpath:data/create_single_test_error_non-existent_genre.json";
	static final String CREATE_SINGLE_ERROR_SINGLE_EXISTING = "classpath:data/create_single_test_error_single_existing.json";

	@LocalServerPort
	private int port;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private ExecuteSQL executeSQL;

	@BeforeEach
	private void setUp() {
        
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.basePath = "/v1/singles";
		RestAssured.port = port;

		clearDatabase();

		prepareData();

	}

	@Test
	public void should_ReturnStatusCode201_WhenSingleIsCreated() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_SINGLE_SUCCESS);

		given().body(resource.getInputStream()).contentType(ContentType.JSON).accept(ContentType.JSON).when().post()
				.then().statusCode(201);

	}

	@Test
	public void should_ReturnStatusCode400_WhenGenreInSingleNotExists() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_SINGLE_ERROR_SINGLE_WITH_NON_EXISTENT_GENRE);

		given().body(resource.getInputStream()).contentType(ContentType.JSON).accept(ContentType.JSON).when().post()
				.then().statusCode(400);

	}

	@Test
	public void should_ReturnStatusCode400_WhenBandInSingleNotExists() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_SINGLE_ERROR_SINGLE_WITH_NON_EXISTENT_BAND);

		given().body(resource.getInputStream()).contentType(ContentType.JSON).accept(ContentType.JSON).when().post()
				.then().statusCode(400);

	}

	@Test
	public void should_ReturnStatusCode409_WhenSingleAlreadyExist() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_SINGLE_ERROR_SINGLE_EXISTING);

		given().body(resource.getInputStream()).contentType(ContentType.JSON).accept(ContentType.JSON).when().post()
				.then().statusCode(409);

	}

	@Test
	public void should_ReturnStatusCode200_WhenSingleExists() {
		given()
		.accept(ContentType.JSON)
		.when()
		.get()
		.then()
		.statusCode(200)
		.assertThat()
		.body("size()",Matchers.is(1));

		
	}
	@Test
	public void should_ReturnStatusCode204_WhenSinglesNotExists() {
		
		clearDatabase();
		
		given().accept(ContentType.JSON).when().get().then().statusCode(204);

		
	}	
	
	@Test
	public void should_ReturnStatusCode204_WhenSingleIsRemoved() {
		
		given()
		.queryParam("singleTitle", "Whiplash")
		.queryParam("bandName", "Metallica")
		.accept(ContentType.JSON)
		.when()
		.delete()
		.then()
		.statusCode(204);

		
	}	
	@Test
	public void should_ReturnStatusCode404_WhenSingleNotFound() {
		
		given()
		.queryParam("singleTitle", "Master Of Puppets")
		.queryParam("bandName", "Metallica")
		.accept(ContentType.JSON)
		.when()
		.delete()
		.then()
		.statusCode(404);

		
	}	
	
	public void clearDatabase() {
		executeSQL.run("clear_database_test.sql");
	}

	public void prepareData() {
		executeSQL.run("data_test.sql");
	}

}
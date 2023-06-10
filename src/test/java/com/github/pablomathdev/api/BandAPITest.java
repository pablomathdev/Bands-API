package com.github.pablomathdev.api;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;

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
	private JdbcTemplate jdbcTemplate;

	@LocalServerPort
	private int port;

	@BeforeAll
	public void setUp() {
		enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		basePath = "/api/bands";

	}

	@AfterAll
	public void dropDatabaseTest() {

		jdbcTemplate.execute("DROP DATABASE develop_test");
	}

	@Sql(scripts = {"classpath:sql/insert_genre.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(encoding = "UTF-8"))
	@Test
	public void should_ReturnStatusCode201_WhenBandIsCreated() throws IOException {
 
		Resource resource = resourceLoader.getResource(CREATE_BAND_SUCCESS);
		
		
		given().body(resource.getInputStream()).contentType(ContentType.JSON)
				.accept(ContentType.JSON).when().post().then().statusCode(201);

	}

	@Test
	public void should_ReturnStatusCode400_WhenGenreInBandNotExists() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_BAND_ERROR_BAND_WITH_NON_EXISTENT_GENRE);
		
		
		given().body(resource.getInputStream())
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().post().then().statusCode(400);

	}

	@Sql(scripts = { "classpath:sql/insert_band.sql","classpath:sql/insert_genre.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void should_ReturnStatusCode409_WhenBandAlreadyExist() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_BAND_ERROR_BAND_EXISTING);
		
		given().body(resource.getInputStream()).contentType(ContentType.JSON)
				.accept(ContentType.JSON).when().post().then().statusCode(409);

	}
	@Sql(scripts = {"classpath:sql/insert_bands.sql"},executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void should_ReturnStatusCode200AndAllBands_WhenBandExist() {

		
	     given()
	     .accept(ContentType.JSON)
	     .when()
	     .get()
	     .then()
	     .statusCode(200)
	     .assertThat()
	     .body("size()",Matchers.is(2));

	}
	@Test
	public void should_ReturnStatusCode204_WhenBandNotExists() {

		
	     given()
	     .accept(ContentType.JSON)
	     .when()
	     .get()
	     .then()
	     .statusCode(204);
	     

	}

}

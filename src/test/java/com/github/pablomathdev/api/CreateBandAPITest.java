package com.github.pablomathdev.api;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.github.pablomathdev.JsonFileReader;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application-test.properties")
public class CreateBandAPITest {

	static final String PATH_BAND = "src/test/java/com/github/pablomathdev/resource.json";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@LocalServerPort
	private int port;

	@BeforeAll
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/api/bands";

	}

	@AfterAll
	public void dropDatabaseTest() {

		jdbcTemplate.execute("DROP DATABASE develop_test");
	}

	@Sql(scripts = { "../insert_genre.sql" })
	@Test
	public void should_ReturnStatusCode201_WhenBandIsCreated() throws IOException {

		RestAssured.given().body(JsonFileReader.readJsonFile(PATH_BAND)).contentType(ContentType.JSON)
				.accept(ContentType.JSON).when().post().then().statusCode(201);

	}
}

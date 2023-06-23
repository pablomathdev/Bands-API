package com.github.pablomathdev.api;

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
public class AlbumAPITest {

	static final String CREATE_ALBUM_SUCCESS = "classpath:data/create_album_test_success.json";
	static final String CREATE_ALBUM_ERROR_ALBUM_WITH_NON_EXISTENT_GENRE = "classpath:data/create_album_test_error_non-existent_genre.json";
	static final String CREATE_ALBUM_ERROR_ALBUM_EXISTING = "classpath:data/create_album_test_error_album_existing.json";
	
	@LocalServerPort
	private int port;

	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private ExecuteSQL executeSQL;
	
	@BeforeEach
	private void setUp() {
		
		RestAssured.basePath="/v1/albums";
		RestAssured.port=port;
		
		executeSQL.run("data_test.sql");
		
	}
	
	
	@Test
	public void should_ReturnStatusCode201_WhenAlbumIsCreated() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_ALBUM_SUCCESS);

		given().body(resource.getInputStream()).contentType(ContentType.JSON).accept(ContentType.JSON).when().post()
				.then().statusCode(201);

	}
	
	@Test
	public void should_ReturnStatusCode400_WhenGenreInAlbumNotExists() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_ALBUM_ERROR_ALBUM_WITH_NON_EXISTENT_GENRE);

		given().body(resource.getInputStream()).contentType(ContentType.JSON).accept(ContentType.JSON).when().post()
				.then().statusCode(400);

	}
	@Test
	public void should_ReturnStatusCode409_WhenAlbumAlreadyExist() throws IOException {

		Resource resource = resourceLoader.getResource(CREATE_ALBUM_ERROR_ALBUM_EXISTING);

		given().body(resource.getInputStream()).contentType(ContentType.JSON).accept(ContentType.JSON).when().post()
				.then().statusCode(409);

	}
}

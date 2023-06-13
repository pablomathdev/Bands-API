package com.github.pablomathdev.integration;

import static com.github.pablomathdev.Factory.genreFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.github.pablomathdev.application.services.GenreService;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.GenreAlreadyExistsException;
import com.github.pablomathdev.utils.ExecuteSQL;

@SpringBootTest
@TestPropertySource(value = "/application-test.properties")
public class GenreServiceIntegrationTest {

	@Autowired
	private ExecuteSQL executeSQL;

	@Autowired
	private GenreService genreService;

	@BeforeEach
	private void clearDatabaseTest() {
		executeSQL.run("clear_database_test.sql");
		
	}
	
	@BeforeEach
	private void prepareData() {
		executeSQL.run("data_test.sql");
	}
	

	@Test
	public void should_CreateGenreSuccessfully() {

		Genre genre = genreFactory("Classic Rock");

		Genre genreSaved = genreService.create(genre);

		assertNotNull(genreSaved.getId());
		assertEquals(genre.getName(), genreSaved.getName());

	}

	@Test
	public void should_ThrowGenreAlreadyExistsException_WhenGenreAlreadyExists() {

		Genre genre = genreFactory("Trash Metal");

		assertThrows(GenreAlreadyExistsException.class, () -> genreService.create(genre));

	}


}

package com.github.pablomathdev.integration;

import static com.github.pablomathdev.Factory.genreFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.TestPropertySource;

import com.github.pablomathdev.application.services.GenreService;
import com.github.pablomathdev.domain.entities.Genre;

@SpringBootTest
@TestPropertySource(value = "/application-test.properties")
public class GenreServiceIntegrationTest {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private GenreService genreService;

	@BeforeEach
	private void clearDatabase() throws IOException, ScriptException, SQLException{

		Resource resource = resourceLoader.getResource("classpath:sql/clear_database_test.sql");

		ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(),resource);
	}

	@Test
	public void should_CreateGenreSuccessfully() {

		Genre genre = genreFactory("Classic Rock");

		Genre genreSaved = genreService.create(genre);

		assertNotNull(genreSaved.getId());
		assertEquals(genre.getName(), genreSaved.getName());

	}

}

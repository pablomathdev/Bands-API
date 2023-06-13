package com.github.pablomathdev.integration;

import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.originFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.github.pablomathdev.application.services.BandService;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.BandAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BandServiceIntegrationTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private BandService bandService;

	@AfterAll
	public void dropDatabaseTest() {
		jdbcTemplate.execute("DROP DATABASE develop_test");
	}

	@Test
	public void should_CreateBandSuccessfully() {

		Origin origin = originFactory("Los Angeles", "United States", 1983);
		Genre genre = genreFactory("Trash Metal");
		Set<Genre> genres = new HashSet<>();
		genres.add(genre);

		Band band = bandFactory("Megadeth", origin, genres);

		Band bandSaved = bandService.create(band);

		assertNotNull(bandSaved.getId());
		assertEquals(bandSaved.getName(), band.getName());

	}

	@Sql(scripts = { "classpath:sql/insert_genre.sql" },executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void should_ThrowGenreNotFoundException_WhenGenreNotExists() {
		Origin origin = originFactory("Aberdeen", "United States", 1987);
		Genre genre = genreFactory("Grunge");
		Set<Genre> genres = new HashSet<>();
		genres.add(genre);

		Band band = bandFactory("Nirvana", origin, genres);

		Throwable exception = assertThrows(GenreNotFoundException.class, () -> {
			bandService.create(band);
		});

		assertEquals(String.format("Genre %s Not Found!", genre.getName()), exception.getMessage());

	}

	@Sql(scripts = { "classpath:sql/insert_band.sql" },executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void should_ThrowBandAlreadyExistsException_WhenBandAlreadyExists() {
		Origin origin = originFactory("Los Angeles", "United States", 1981);
		Genre genre = genreFactory("Trash Metal");
		Set<Genre> genres = new HashSet<>();
		genres.add(genre);

		Band band = bandFactory("Metallica", origin, genres);

		Throwable exception = assertThrows(BandAlreadyExistsException.class, () -> {
			bandService.create(band);
		});

		assertEquals(String.format("Band %s Already Exists!", band.getName()), exception.getMessage());

	}

}

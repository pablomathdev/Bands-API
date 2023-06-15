package com.github.pablomathdev.integration;

import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.originFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.github.pablomathdev.application.services.BandService;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.BandAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;
import com.github.pablomathdev.utils.ExecuteSQL;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BandServiceIntegrationTest {

	@Autowired
	private ExecuteSQL executeSQL;

	@Autowired
	private BandService bandService;

	@BeforeEach
	public void clearDatabaseTest() {
		executeSQL.run("clear_database_test.sql");
	}

	@BeforeEach
	public void prepareData() {
		executeSQL.run("data_test.sql");
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

package com.github.pablomathdev.integration;

import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.github.pablomathdev.application.services.AlbumService;
import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.AlbumAlreadyExistsException;
import com.github.pablomathdev.utils.ExecuteSQL;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class AlbumServiceIntegrationTest {

	@Autowired
	private ExecuteSQL executeSQL;

	@Autowired
	private AlbumService albumService;

	@BeforeEach
	public void clearDatabaseTest() {
		executeSQL.run("clear_database_test.sql");
	}
	
	@BeforeEach
	public void prepareData() {
		executeSQL.run("data_test.sql");
	}

	@Test
	public void should_CreateAlbumSuccessfully() {
		Genre genre = genreFactory("Trash Metal");
		Band band = bandFactory("Metallica", null, null);
		Album album = new Album();
		album.setBand(band);
		List<Genre> genres = new ArrayList<>();
		genres.add(genre);
		album.setGenres(genres);
		album.setTitle("Master Of Puppets");
		album.setReleaseDate(LocalDate.parse("1986-03-03"));

		Album albumSaved = albumService.create(album);
		assertEquals(album.getTitle(), albumSaved.getTitle());
		assertNotNull(albumSaved.getId());
		assertFalse(albumSaved.getGenres().isEmpty());

	}

	@Test
	public void should_ThrowAlbumAlreadyExistsException_WhenAlbumAlreadyExists() {
		Genre genre = genreFactory("Trash Metal");
		Band band = bandFactory("Metallica", null, null);
		Album album = new Album();
		album.setBand(band);
		album.setGenres(List.of(genre));
		album.setTitle("Metallica (The Black Album)");
		album.setReleaseDate(LocalDate.parse("1991-08-12"));
		
		

		assertThrows(AlbumAlreadyExistsException.class, () -> albumService.create(album));

	}
}
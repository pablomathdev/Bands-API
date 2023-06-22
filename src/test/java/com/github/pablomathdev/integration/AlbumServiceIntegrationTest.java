package com.github.pablomathdev.integration;

import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;

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
import com.github.pablomathdev.utils.ExecuteSQL;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class AlbumServiceIntegrationTest {

	@Autowired
	private ExecuteSQL executeSQL;

	@Autowired
	private AlbumService albumService;

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
		album.setTracks(null);
		
		
		albumService.create(album);

	}
}

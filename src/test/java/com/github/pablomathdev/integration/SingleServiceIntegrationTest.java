package com.github.pablomathdev.integration;

import static com.github.pablomathdev.Factory.singleFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.github.pablomathdev.application.services.SingleService;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Single;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.SingleAlreadyExistsException;
import com.github.pablomathdev.utils.ExecuteSQL;


@SpringBootTest
@TestPropertySource("/application-test.properties")
public class SingleServiceIntegrationTest {

	@Autowired
	private ExecuteSQL executeSQL;

	@Autowired
	private SingleService singleService;

	@BeforeEach
	public void clearDatabaseTest() {
		executeSQL.run("clear_database_test.sql");
	}

	@BeforeEach
	public void prepareData() {
		executeSQL.run("data_test.sql");
	}

	@Test
	public void should_CreateSingleSuccessfully() {
		
		Band band = new Band();
		band.setName("Metallica");
		Genre genre = new Genre();
		genre.setName("Trash Metal");
		List<Genre> genres = new ArrayList<>();
		genres.add(genre);
		
		Single single = singleFactory("Master of Puppets",band,genres,LocalDate.parse("1986-07-02"),null);

		Single singleSaved = singleService.create(single);
		
		assertNotNull(singleSaved.getId());
		assertEquals(single.getTitle(), singleSaved.getTitle());
		
		

	}

	@Test
	public void should_ThrowSingleAlreadyExistsException_WhenSingleAlreadyExists() {
		
		Band band = new Band();
		band.setName("Metallica");
		Genre genre = new Genre();
		genre.setName("Trash Metal");
		List<Genre> genres = new ArrayList<>();
		genres.add(genre);
		
		Single single = singleFactory("Whiplash",band,genres,LocalDate.parse("1983-11-08"),null);
		
		
		assertThrows(SingleAlreadyExistsException.class, () -> singleService.create(single));

	}

//	@Test
//	public void should_ThrowBandNotFoundException_WhenBandOfAlbumNotExists() {
//		Genre genre = genreFactory("Trash Metal");
//		Band band = bandFactory("Nirvana", null, null);
//		Album album = new Album();
//		album.setBand(band);
//		album.setGenres(List.of(genre));
//		album.setTitle("Nevermind");
//		album.setReleaseDate(LocalDate.parse("1991-09-24"));
//
//		assertThrows(BandNotFoundException.class, () -> albumService.create(album));
//
//	}
//
//	@Test
//	public void should_ReturnResultListOfAlbums_WhenAlbumsExists() {
//
//		List<Album> result = albumService.findAll();
//
//		assertEquals(1, result.size());
//
//	}
//
//	@Test
//	public void should_ReturnResultListEmptyOfAlbums_WhenAlbumsNotExists() {
//
//		clearDatabaseTest();
//
//		List<Album> result = albumService.findAll();
//
//		assertTrue(result.isEmpty());
//
//	}
//	
//	@Test
//	public void should_DeleteAlbum_WhenALbumExists() {
//		
//		
//		albumService.delete("Metallica (The Black Album)","Metallica");
//		
//		List< Album> albums =albumService.findAll();
//		
//		boolean albumIsRemoved = albums.stream().noneMatch(album -> album.getTitle()
//				.equals("Metallica (The Black Album)") && album.getBand().getName()
//				.equals("Metallica"));
//		
//		assertTrue(albumIsRemoved);
//	}
}

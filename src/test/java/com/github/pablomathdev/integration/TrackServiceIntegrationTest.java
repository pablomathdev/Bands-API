package com.github.pablomathdev.integration;

import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.trackFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.github.pablomathdev.application.services.TrackService;
import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Track;
import com.github.pablomathdev.utils.ExecuteSQL;


@SpringBootTest
@TestPropertySource("/application-test.properties")
public class TrackServiceIntegrationTest {

	@Autowired
	private ExecuteSQL executeSQL;

	@Autowired
	private TrackService trackService;

	@BeforeEach
	public void clearDatabaseTest() {
		executeSQL.run("clear_database_test.sql");
	}

	@BeforeEach
	public void prepareData() {
		executeSQL.run("data_test.sql");
	}

	@Test
	public void should_CreateTrackSuccessfully() {
		Genre genre = genreFactory("Heavy Metal");
		Album album = new Album();
		Band band = new Band();
		band.setName("Metallica");
		album.setTitle("Metallica (The Black Album)");
		album.setBand(band);
		Track track = trackFactory("Sad But true",album,null,LocalDate.parse("1993-02-08"), List.of(genre));
		

		Track trackSaved = trackService.create(track);
		assertEquals(album.getTitle(),trackSaved.getAlbum().getTitle());
		assertEquals(genre.getName(),trackSaved.getGenres().stream().findFirst().get().getName());
		assertNotNull(trackSaved.getId());
		

	}

//	@Test
//	public void should_ThrowAlbumAlreadyExistsException_WhenAlbumAlreadyExists() {
//		Genre genre = genreFactory("Trash Metal");
//		Band band = bandFactory("Metallica", null, null);
//		Album album = new Album();
//		album.setBand(band);
//		album.setGenres(List.of(genre));
//		album.setTitle("Metallica (The Black Album)");
//		album.setReleaseDate(LocalDate.parse("1991-08-12"));
//
//		assertThrows(AlbumAlreadyExistsException.class, () -> albumService.create(album));
//
//	}
//
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

//	@Test
//	public void should_ReturnResultListOfAlbums_WhenAlbumsExists() {
//
//		List<Album> result = albumService.findAll();
//
//		assertEquals(1, result.size());
//
//	}

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

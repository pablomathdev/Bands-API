package com.github.pablomathdev.units;

import static com.github.pablomathdev.Factory.albumFactory;
import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.originFactory;
import static com.github.pablomathdev.Factory.trackFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.pablomathdev.application.services.TrackService;
import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.domain.entities.Track;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.TrackAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.AlbumNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.TrackNotFoundException;
import com.github.pablomathdev.domain.repositories.IAlbumRepository;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.repositories.ITrackRepository;

@ExtendWith(MockitoExtension.class)
public class TrackServiceTests {

	@Mock
	private ITrackRepository trackRepository;

	@Mock
	private IBandRepository bandRepository;
	
	@Mock
	private IAlbumRepository albumRepository;

	@Mock
	private IGenreRepository genreRepository;

	@InjectMocks
	private TrackService trackService;

	@Test
	public void should_InvokeTrackRepositoryExists_withCorrectArguments() {

		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		Track track = trackFactory("any_title", album, null, LocalDate.parse("1999-09-09"), List.of(genre));

		trackService.create(track);

		verify(trackRepository).exists(eq(track.getTitle()), eq(band.getName()));

	}

	@Test
	public void should_ThrowTrackAlreadyExistsException_WhenTrackAlreadyExists() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		Track track = trackFactory("any_title", album, null, LocalDate.parse("1999-09-09"), List.of(genre));

		

		when(trackRepository.exists(track.getTitle(), track.getAlbum().getBand().getName())).thenReturn(true);

		assertThrows(TrackAlreadyExistsException.class, () -> trackService.create(track));

	}

	@Test
	public void should_InvockTrackRepositorySave_WithCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		Track track = trackFactory("any_title",album, null, LocalDate.parse("1999-09-09"), List.of(genre));

	
		trackService.create(track);

		verify(trackRepository).save(eq(track));

	}

	@Test
	public void should_ReturnTrack_WhenTrackIsCreated() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		Track track = trackFactory("any_title",album, null, LocalDate.parse("1999-09-09"), List.of(genre));
		
		when(trackRepository.save(any())).thenReturn(track);
		

		Track result = trackService.create(track);

		assertEquals(track.getTitle(), result.getTitle());

	}

	@Test
	public void should_ThrowBandNotFoundException_WhenbandNotExists() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		Track track = trackFactory("any_title",album, null, LocalDate.parse("1999-09-09"), List.of(genre));
		

		when(trackRepository.exists(track.getTitle(), track.getAlbum().getBand().getName())).thenReturn(false);

		when(albumRepository.findAlbumByTitleAndBandName(anyString(),anyString())).thenThrow(AlbumNotFoundException.class);

		assertThrows(AlbumNotFoundException.class, () ->trackService.create(track));

	}

	@Test
	public void should_ThrowGenreNotFoundException_WhenGenreNotExists() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		Track track = trackFactory("any_title",album, null, LocalDate.parse("1999-09-09"), List.of(genre));
		
		when(trackRepository.exists(track.getTitle(), track.getAlbum().getBand().getName())).thenReturn(false);

		when(genreRepository.findByName(any())).thenThrow(GenreNotFoundException.class);

		assertThrows(GenreNotFoundException.class, () -> trackService.create(track));

	}

	@Test
	public void should_ReturnResultListOfTracks_WhenTracksExists() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		Track track = trackFactory("any_title",album, null, LocalDate.parse("1999-09-09"), List.of(genre));
		
		when(trackRepository.findAll()).thenReturn(List.of(track));

		List<Track> result = trackService.findAll();

		assertFalse(result.isEmpty());

	}

	@Test
	public void should_ReturnResultListEmpty_WhenTrackNotExists() {

		when(trackRepository.findAll()).thenReturn(List.of());
		
	    List<Track> result = trackService.findAll();
				
	    assertTrue(result.isEmpty());

	}

	@Test
	public void should_InvokeTrackRepositoryFindTrackByTitleAndAlbumTitle_WithCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		Track track = trackFactory("any_title",album, null, LocalDate.parse("1999-09-09"), List.of(genre));

		when(trackRepository.findTrackByTitleAndAlbumTitle(anyString(), anyString())).thenReturn(track);

		trackService.delete(track.getTitle(), track.getAlbum().getTitle());

		verify(trackRepository).delete(eq(track));

	}

	@Test
	public void should_ThrowTrackNotFoundException_WhenTrackNotFound() {
	
	
		when(trackRepository.findTrackByTitleAndAlbumTitle(anyString(), anyString())).thenThrow(EntityNotFoundException.class);
		
	

		assertThrows(TrackNotFoundException.class,()-> trackService.delete(anyString(),anyString()));
	

	}

}

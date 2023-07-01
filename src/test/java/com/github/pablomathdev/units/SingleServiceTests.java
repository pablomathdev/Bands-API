package com.github.pablomathdev.units;

import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.originFactory;
import static com.github.pablomathdev.Factory.singleFactory;
import static com.github.pablomathdev.Factory.trackFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

import com.github.pablomathdev.application.services.SingleService;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.domain.entities.Single;
import com.github.pablomathdev.domain.entities.Track;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.SingleAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.BandNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.repositories.ISingleRepository;


@ExtendWith(MockitoExtension.class)
public class SingleServiceTests {

	@Mock
	private ISingleRepository singleRepository;

	@Mock
	private IBandRepository bandRepository;

	@Mock
	private IGenreRepository genreRepository;

	@InjectMocks
	private SingleService singleService;

	@Test
	public void should_InvokeSingleRepositoryExists_withCorrectArguments() {

		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);


		singleService.create(single);

		verify(singleRepository).exists(eq(single.getTitle()), eq(band.getName()));

	}

	@Test
	public void should_ThrowSingleAlreadyExistsException_WhenSingleAlreadyExists() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);

		

		when(singleRepository.exists(single.getTitle(), single.getBand().getName())).thenReturn(true);

		assertThrows(SingleAlreadyExistsException.class, () -> singleService.create(single));

	}

	@Test
	public void should_InvockSingleRepositorySave_WithCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);

		

		singleService.create(single);

		verify(singleRepository).save(eq(single));

	}

	@Test
	public void should_ReturnSingle_WhenSingleIsCreated() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);

		when(singleRepository.save(any())).thenReturn(single);

		Single result = singleService.create(single);

		assertEquals(single.getTitle(), result.getTitle());

	}

	@Test
	public void should_ThrowBandNotFoundException_WhenBandNotExists() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);

		when(singleRepository.exists(single.getTitle(), single.getBand().getName())).thenReturn(false);

		when(bandRepository.findByName(any())).thenThrow(BandNotFoundException.class);

		assertThrows(BandNotFoundException.class, () ->singleService.create(single));

	}

	@Test
	public void should_ThrowGenreNotFoundException_WhenGenreNotExists() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);

		when(singleRepository.exists(single.getTitle(), single.getBand().getName())).thenReturn(false);

		when(genreRepository.findByName(any())).thenThrow(GenreNotFoundException.class);

		assertThrows(GenreNotFoundException.class, () -> singleService.create(single));

	}

	@Test
	public void should_ReturnResultListOfSingle_WhenSinglesExists() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);
	

		when(singleRepository.findAll()).thenReturn(List.of(single));

		List<Single> result = singleService.findAll();

		assertFalse(result.isEmpty());

	}

	@Test
	public void should_ReturnResultListEmpty_WhenSingleNotExists() {

		when(singleRepository.findAll()).thenReturn(List.of());
		
	  List<Single> result = singleService.findAll();
				
	   assertTrue(result.isEmpty());

	}

//	@Test
//	public void should_InvokeAlbumRepositoryFindAlbumByTitleAndBandName_WithCorrectArguments() {
//		Genre genre = genreFactory("any_genre");
//		Origin origin = originFactory("any_city", "any_country", 1999);
//		Band band = bandFactory("any_name", origin, List.of(genre));
//		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
//				List.of(new Track()));
//
//		when(albumRepository.findAlbumByTitleAndBandName(anyString(), anyString())).thenReturn(album);
//
//		albumService.delete(album.getTitle(), band.getName());
//
//		verify(albumRepository).delete(eq(album));
//
//	}
//
//	@Test
//	public void should_ThrowAlbumNotFoundException_WhenAlbumNotFound() {
//	
//	
//		when(albumRepository.findAlbumByTitleAndBandName(anyString(), anyString())).thenThrow(EntityNotFoundException.class);
//		
//	
//
//		assertThrows(AlbumNotFoundException.class,()-> 	albumService.delete(anyString(),anyString()));
//	
//
//	}

}

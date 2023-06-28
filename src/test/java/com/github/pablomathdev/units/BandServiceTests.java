package com.github.pablomathdev.units;

import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.originFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.pablomathdev.application.services.BandService;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.BandAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.BandNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;

import jakarta.persistence.PersistenceException;;


@ExtendWith(MockitoExtension.class)
class BandServiceTests {

	static final String INVALID_GENRE_NAME = "Invalid_genre_name";

	@Captor
	ArgumentCaptor<String> genreNameCaptor;

	@Mock
	IBandRepository bandRepository;

	@Mock
	IGenreRepository genreRepository;

	@InjectMocks
	private BandService bandService;

	@Test
	public void should_call_save_method_repository_with_band() {

		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre = genreFactory("any_genre");

		List<Genre> genres = new ArrayList<>();
		genres.add(genre);
		Band band = bandFactory("any_band", origin, genres);

		bandService.create(band);

		Mockito.verify(bandRepository).save(eq(band));

	}

	@Test
	public void should_InvokeGenreRepositoryfindByName_withCorrectArguments() {
		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre1 = genreFactory("genre_1");
		Genre genre2 = genreFactory("genre_2");
		List<Genre> genres = new ArrayList<>();
		genres.add(genre1);
		genres.add(genre2);

		Band band = bandFactory("any_band", origin, genres);

		bandService.create(band);

		Mockito.verify(genreRepository, Mockito.times(2)).findByName(genreNameCaptor.capture());

		List<String> capturedGenreNames = genreNameCaptor.getAllValues();
		List<String> expectedGenreNames = List.of("genre_1", "genre_2");

		assertTrue(capturedGenreNames.contains(expectedGenreNames.get(0)));
		assertTrue(capturedGenreNames.contains(expectedGenreNames.get(1)));

	}

	@Test
	public void should_ThrowGenreNotFoundByNameException_WhenGenreRepositoryFindByNameThrowsEntityNotFoundException() {
		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genreInvalid = genreFactory(INVALID_GENRE_NAME);
		List<Genre> genres = new ArrayList<>();
		genres.add(genreInvalid);

		Band band = bandFactory("any_band", origin, genres);

		when(genreRepository.findByName(INVALID_GENRE_NAME))
				.thenThrow(new EntityNotFoundException("Genre" + INVALID_GENRE_NAME + "Not Found!"));
		Throwable exception = assertThrows(GenreNotFoundException.class, () -> bandService.create(band));

		assertEquals("Genre" + INVALID_GENRE_NAME + "Not Found!", exception.getMessage());

	}

	@Test
	public void should_ThrowAlreadyExistsException_WhenBandRepositoryExistsThrows() {
		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre1 = genreFactory("any_genre");
		List<Genre> genres = new ArrayList<>();
		genres.add(genre1);

		Band band = bandFactory("any_band", origin, genres);

		when(bandRepository.exists(band.getName())).thenReturn(true);

		Throwable exception = assertThrows(BandAlreadyExistsException.class, () -> bandService.create(band));

		assertEquals(String.format("Band %s already exists", band.getName()), exception.getMessage());

	}

	@Test
	public void should_ReturnBand_WhenTheBandRepositorySavesABand() {
		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre = genreFactory("any_genre");
		List<Genre> genres = new ArrayList<>();
		genres.add(genre);

		Band band = bandFactory("any_band", origin, genres);

		Band bandExpected = bandFactory("band_expected", origin, genres);
		bandExpected.setId(1);

		when(bandRepository.save(band)).thenReturn(bandExpected);

		Band bandSaved = bandService.create(band);

		assertEquals(bandExpected, bandSaved);
		assertEquals(bandExpected.getName(), bandSaved.getName());
		assertNotNull(bandExpected.getId());
		assertNotNull(bandExpected.getGenres());

	}

	@Test
	public void should_ThrowEntitySaveException_WhenBandRepositorySaveThorwPersistenceException() {
		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre1 = genreFactory("any_genre");
		List<Genre> genres = new ArrayList<>();
		genres.add(genre1);

		Band band = bandFactory("any_band", origin, genres);

		when(bandRepository.save(band))
				.thenThrow(new PersistenceException(String.format("Failed to save the band %s", band.getName()), null));

		Throwable exception = assertThrows(EntitySaveException.class, () -> bandService.create(band));

		assertEquals(String.format("Failed to save the band %s", band.getName()), exception.getMessage());
	}

	@Test
	public void should_FindReturnBands_WhenBandRepositoryFindAllReturnBands() {
		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre = genreFactory("any_genre");
		List<Genre> genres = new ArrayList<>();
		genres.add(genre);
		Band band1 = bandFactory("any_band_1", origin, genres);
		Band band2 = bandFactory("any_band_2", origin, genres);

		List<Band> result = List.of(band1, band2);

		when(bandRepository.findAll()).thenReturn(result);

		List<Band> actual = bandService.find();

		assertEquals(2, actual.size());

	}

	@Test
	public void should_FindReturnEmpty_WhenBandRepositoryFindAllNotReturnBands() {

		List<Band> result = List.of();

		when(bandRepository.findAll()).thenReturn(result);

		List<Band> actual = bandService.find();

		assertTrue(actual.isEmpty());

	}

	@Test
	public void should_InvokeBandRepositoryDelete_WithCorrectArguments() {
		Origin origin = originFactory("any_city", "any_country", 1999);

		Band band = bandFactory("any_band", origin, null);

		when(bandRepository.findByName(any())).thenReturn(band);

		bandService.delete(band.getName());

		verify(bandRepository).delete(eq(band));
	}

	@Test
	public void should_ThrowBandNotFoundException_WhenBandRepositoryDeleteThrowEntityNotFoundException() {
		Origin origin = originFactory("any_city","any_country" , 1999);
		
		Band band = bandFactory("any_band", origin,null);
		
		when(bandRepository.findByName(any())).thenThrow(EntityNotFoundException.class);
		
		
		assertThrows(BandNotFoundException.class,()->bandService.delete(band.getName()));
		
		
	}

}

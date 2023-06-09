package com.github.pablomathdev.units;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.pablomathdev.Factory;
import com.github.pablomathdev.application.services.BandService;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.domain.exceptions.BandAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.GenreNotFoundException;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;

import jakarta.persistence.PersistenceException;;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CreateBandServiceTests {

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

		Origin origin = Factory.originFactory("any_city", "any_country", 1999);
		Genre genre = Factory.genreFactory("any_genre");

		Set<Genre> set = new HashSet<>();
		set.add(genre);
		Band band = Factory.bandFactory("any_band", origin, set);

		bandService.create(band);

		Mockito.verify(bandRepository).save(eq(band));

	}

	@Test
	public void should_InvokeGenreRepositoryfindByName_withCorrectArguments() {
		Origin origin = Factory.originFactory("any_city", "any_country", 1999);
		Genre genre1 = Factory.genreFactory("genre_1");
		Genre genre2 = Factory.genreFactory("genre_2");
		Set<Genre> set = new HashSet<>();
		set.add(genre1);
		set.add(genre2);

		Band band = Factory.bandFactory("any_band", origin, set);

		bandService.create(band);

		Mockito.verify(genreRepository, Mockito.times(2)).findByName(genreNameCaptor.capture());

		List<String> capturedGenreNames = genreNameCaptor.getAllValues();
		List<String> expectedGenreNames = List.of("genre_1", "genre_2");

		assertTrue(capturedGenreNames.contains(expectedGenreNames.get(0)));
		assertTrue(capturedGenreNames.contains(expectedGenreNames.get(1)));

	}

	@Test
	public void should_ThrowGenreNotFoundByNameException_WhenGenreRepositoryFindByNameThrowsEntityNotFoundException() {
		Origin origin = Factory.originFactory("any_city", "any_country", 1999);
		Genre genreInvalid = Factory.genreFactory(INVALID_GENRE_NAME);
		Set<Genre> set = new HashSet<>();
		set.add(genreInvalid);

		Band band = Factory.bandFactory("any_band", origin, set);

		when(genreRepository.findByName(INVALID_GENRE_NAME))
				.thenThrow(new EntityNotFoundException("Genre" + INVALID_GENRE_NAME + "Not Found!"));
		Throwable exception = assertThrows(GenreNotFoundException.class, () -> bandService.create(band));
		assertEquals("Genre" + INVALID_GENRE_NAME + "Not Found!", exception.getMessage());

	}

	@Test
	public void should_ThrowAlreadyExistsException_WhenBandRepositoryExistsThrows() {
		Origin origin = Factory.originFactory("any_city", "any_country", 1999);
		Genre genre1 = Factory.genreFactory("any_genre");
		Set<Genre> set = new HashSet<>();
		set.add(genre1);

		Band band = Factory.bandFactory("any_band", origin, set);

		when(bandRepository.exists(band.getName())).thenReturn(true);

		Throwable exception = assertThrows(BandAlreadyExistsException.class, () -> bandService.create(band));

		assertEquals(String.format("Band %s Already Exists!",band.getName()), exception.getMessage());

	}

	@Test
	public void should_ReturnBand_WhenTheBandRepositorySavesABand() {
		Origin origin = Factory.originFactory("any_city", "any_country", 1999);
		Genre genre1 = Factory.genreFactory("any_genre");
		Set<Genre> set = new HashSet<>();
		set.add(genre1);

		Band band = Factory.bandFactory("any_band", origin, set);

		Band bandExpected = Factory.bandFactory("band_expected", origin, set);
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
		Origin origin = Factory.originFactory("any_city", "any_country", 1999);
		Genre genre1 = Factory.genreFactory("any_genre");
		Set<Genre> set = new HashSet<>();
		set.add(genre1);

		Band band = Factory.bandFactory("any_band", origin, set);

		when(bandRepository.save(band))
				.thenThrow(new PersistenceException(String.format("Failed to save the band %s", band.getName()), null));

		Throwable exception = assertThrows(EntitySaveException.class, () -> bandService.create(band));

		assertEquals(String.format("Failed to save the band %s", band.getName()), exception.getMessage());
	}

}

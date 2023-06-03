package com.github.pablomathdev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.github.pablomathdev.application.CreateBandService;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.domain.exceptions.AlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.BandAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.BusinessException;
import com.github.pablomathdev.domain.exceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.GenreNotFoundByNameException;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CreateBandServiceTests {

	@Captor
	ArgumentCaptor<String> genreNameCaptor;

	@Mock
	IBandRepository bandRepository;

	@Mock
	IGenreRepository genreRepository;

	@InjectMocks
	private CreateBandService createBandService;

	@Test
	public void should_call_save_method_repository_with_band() {

		Origin origin = originFactory("San Francisco", "United States", 1981);
		Genre genre = genreFactory("Trash Metal");

		Set<Genre> set = new HashSet<>();
		set.add(genre);
		Band band = bandFactory("Metallica", origin, set);

		createBandService.execute(band);

		Mockito.verify(bandRepository).save(Mockito.eq(band));

	}

	@Test
	public void should_call_findByName_genre_repository_with_name() {
		Origin origin = originFactory("San Francisco", "United States", 1981);
		Genre genre1 = genreFactory("Trash Metal");
		Genre genre2 = genreFactory("Heavy Metal");
		Set<Genre> set = new HashSet<>();
		set.add(genre1);
		set.add(genre2);

		Band band = bandFactory("Metallica", origin, set);

		createBandService.execute(band);

		Mockito.verify(genreRepository, Mockito.times(2)).findByName(genreNameCaptor.capture());

		List<String> capturedGenreNames = genreNameCaptor.getAllValues();
		List<String> expectedGenreNames = List.of("Trash Metal", "Heavy Metal");

		assertTrue(capturedGenreNames.contains(expectedGenreNames.get(0)));
		assertTrue(capturedGenreNames.contains(expectedGenreNames.get(1)));

	}

	static final String INVALID_GENRE_NAME = "Pop";

	@Test
	public void should_Throw_Exception_When_Genre_Repository_Throws() {
		Origin origin = originFactory("San Francisco", "United States", 1981);
		Genre genreInvalid = genreFactory(INVALID_GENRE_NAME);
		Set<Genre> set = new HashSet<>();
		set.add(genreInvalid);

		Band band = bandFactory("Metallica", origin, set);

		Mockito.when(genreRepository.findByName(genreInvalid.getName())).thenThrow(
				new EntityNotFoundException(String.format("there is no genre named: %s", genreInvalid.getName())));

		Throwable exception = assertThrows(GenreNotFoundByNameException.class, () -> createBandService.execute(band));
		assertEquals("there is no genre named: " + genreInvalid.getName(), exception.getMessage());

	}

	@Test
	public void should_ThrowAlreadyExistsException_WhenBandRepositorySaveThrows() {
		Origin origin = originFactory("San Francisco", "United States", 1981);
		Genre genre1 = genreFactory("Trash Metal");
		Set<Genre> set = new HashSet<>();
		set.add(genre1);

		Band band = bandFactory("Metallica", origin, set);

		when(bandRepository.save(band)).thenThrow(new AlreadyExistsException());

		Throwable exception = assertThrows(BandAlreadyExistsException.class, () -> createBandService.execute(band));

		assertEquals("This Band Already Exists", exception.getMessage());

	}

	@Test
	public void should_return_a_band_When_the_band_repository_saves_a_band() {
		Origin origin = originFactory("San Francisco", "United States", 1981);
		Genre genre1 = genreFactory("Trash Metal");
		Set<Genre> set = new HashSet<>();
		set.add(genre1);

		Band band = bandFactory("Metallica", origin, set);

		Band bandExpected = bandFactory("Metallica", origin, set);
		bandExpected.setId(1);

		Mockito.when(bandRepository.save(band)).thenReturn(bandExpected);

		Band bandSaved = createBandService.execute(band);

		assertEquals(bandExpected, bandSaved);
		assertEquals(bandExpected.getName(), bandSaved.getName());
		assertNotNull(bandExpected.getId());
		assertNotNull(bandExpected.getGenres());

	}

	@Test
	public void should_ThrowEntitySaveException_WhenBandRepositorySaveThorwPersistenceException() {
		Origin origin = originFactory("San Francisco", "United States", 1981);
		Genre genre1 = genreFactory("Trash Metal");
		Set<Genre> set = new HashSet<>();
		set.add(genre1);

		Band band = bandFactory("Metallica", origin, set);

		when(bandRepository.save(band))
		.thenThrow(new EntitySaveException(String.format("Failed to save the band %s", band.getName()), null));

		
		Throwable exception = assertThrows(BusinessException.class, () -> createBandService.execute(band));

		assertEquals(String.format("Failed to save the band %s", band.getName()), exception.getMessage());
	}

	private Band bandFactory(String name, Origin origin, Set<Genre> genres) {
		Band band = new Band();
		band.setName(name);
		band.setOrigin(origin);
		band.setGenres(genres);
		return band;

	}

	private Origin originFactory(String city, String country, Integer formationYear) {
		Origin origin = new Origin();
		origin.setCity(city);
		origin.setCountry(country);
		origin.setFormationYear(formationYear);
		return origin;

	}

	private Genre genreFactory(String name) {
		Genre genre = new Genre();
		genre.setName(name);
		return genre;
	}
}

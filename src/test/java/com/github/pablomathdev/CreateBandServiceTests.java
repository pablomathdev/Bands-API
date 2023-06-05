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

import com.github.pablomathdev.application.services.CreateBandService;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.domain.exceptions.BandAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.GenreNotFoundException;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.exceptions.EntityNotFoundException;
import jakarta.persistence.PersistenceException;

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

		Origin origin = Factory.originFactory("San Francisco", "United States", 1981);
		Genre genre = Factory.genreFactory("Trash Metal");

		Set<Genre> set = new HashSet<>();
		set.add(genre);
		Band band = Factory.bandFactory("Metallica", origin, set);

		createBandService.execute(band);

		Mockito.verify(bandRepository).save(Mockito.eq(band));

	}

	@Test
	public void should_InvokeGenreRepositoryfindByName_withCorrectArguments() {
		Origin origin = Factory.originFactory("San Francisco", "United States", 1981);
		Genre genre1 = Factory.genreFactory("Trash Metal");
		Genre genre2 = Factory.genreFactory("Heavy Metal");
		Set<Genre> set = new HashSet<>();
		set.add(genre1);
		set.add(genre2);

		Band band = Factory.bandFactory("Metallica", origin, set);

		createBandService.execute(band);

		Mockito.verify(genreRepository, Mockito.times(2)).findByName(genreNameCaptor.capture());

		List<String> capturedGenreNames = genreNameCaptor.getAllValues();
		List<String> expectedGenreNames = List.of("Trash Metal", "Heavy Metal");

		assertTrue(capturedGenreNames.contains(expectedGenreNames.get(0)));
		assertTrue(capturedGenreNames.contains(expectedGenreNames.get(1)));

	}

	static final String INVALID_GENRE_NAME = "Pop";

	@Test
	public void should_ThrowGenreNotFoundByNameException_WhenGenreRepositoryFindByNameThrowsEntityNotFoundException() {
		Origin origin = Factory.originFactory("San Francisco", "United States", 1981);
		Genre genreInvalid = Factory.genreFactory(INVALID_GENRE_NAME);
		Set<Genre> set = new HashSet<>();
		set.add(genreInvalid);

		Band band = Factory.bandFactory("Metallica", origin, set);

		when(genreRepository.findByName(genreInvalid.getName()))
		.thenThrow(new EntityNotFoundException("Genre" + genreInvalid.getName() + "Not Found!"));
		Throwable exception = assertThrows(GenreNotFoundException.class, () -> createBandService.execute(band));
		assertEquals("Genre" + genreInvalid.getName() + "Not Found!", exception.getMessage());

	}

	@Test
	public void should_ThrowAlreadyExistsException_WhenBandRepositorySaveThrows() {
		Origin origin = Factory.originFactory("San Francisco", "United States", 1981);
		Genre genre1 = Factory.genreFactory("Trash Metal");
		Set<Genre> set = new HashSet<>();
		set.add(genre1);

		Band band = Factory.bandFactory("Metallica", origin, set);

		when(bandRepository.exists(band.getName())).thenReturn(true);

		Throwable exception = assertThrows(BandAlreadyExistsException.class, () -> createBandService.execute(band));

		assertEquals("This Band Already Exists", exception.getMessage());

	}

	@Test
	public void should_ReturnBand_WhenTheBandRepositorySavesABand() {
		Origin origin = Factory.originFactory("San Francisco", "United States", 1981);
		Genre genre1 = Factory.genreFactory("Trash Metal");
		Set<Genre> set = new HashSet<>();
		set.add(genre1);

		Band band = Factory.bandFactory("Metallica", origin, set);

		Band bandExpected = Factory.bandFactory("Metallica", origin, set);
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
		Origin origin = Factory.originFactory("San Francisco", "United States", 1981);
		Genre genre1 = Factory.genreFactory("Trash Metal");
		Set<Genre> set = new HashSet<>();
		set.add(genre1);

		Band band = Factory.bandFactory("Metallica", origin, set);

		when(bandRepository.save(band))
				.thenThrow(new PersistenceException(String.format("Failed to save the band %s", band.getName()), null));

		Throwable exception = assertThrows(EntitySaveException.class, () -> createBandService.execute(band));

		assertEquals(String.format("Failed to save the band %s", band.getName()), exception.getMessage());
	}

}

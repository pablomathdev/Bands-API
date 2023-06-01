package com.github.pablomathdev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CreateBandServiceTests {

	@Captor
	ArgumentCaptor<Band> bandCaptor;

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
		Genre genre = genreFactory(1, "Trash Metal");

		Set<Genre> set = new HashSet<>();
		set.add(genre);
		Band band = bandFactory(1, "Metallica", origin, set);

		createBandService.execute(band);

		Mockito.verify(bandRepository).save(bandCaptor.capture());
		assertEquals(band, bandCaptor.getValue());

	}

	@Test
	public void should_call_findByName_genre_repository_with_name() {
		Origin origin = originFactory("San Francisco", "United States", 1981);
		Genre genre1 = genreFactory(1, "Trash Metal");
		Genre genre2 = genreFactory(2, "Heavy Metal");
		Set<Genre> set = new HashSet<>();
		set.add(genre1);
		set.add(genre2);

		Band band = bandFactory(1, "Metallica", origin, set);

		createBandService.execute(band);

		Mockito.verify(genreRepository, Mockito.times(2)).findByName(genreNameCaptor.capture());

		List<String> capturedNames = genreNameCaptor.getAllValues();
		List<String> expectedNames = List.of("Trash Metal", "Heavy Metal");

		assertTrue(capturedNames.contains(expectedNames.get(0)));
		assertTrue(capturedNames.contains(expectedNames.get(1)));

	}

	private Band bandFactory(Integer id, String name, Origin origin, Set<Genre> genres) {
		Band band = new Band();
		band.setId(id);
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

	private Genre genreFactory(Integer id, String name) {
		Genre genre = new Genre();
		genre.setId(id);
		genre.setName(name);
		return genre;
	}
}

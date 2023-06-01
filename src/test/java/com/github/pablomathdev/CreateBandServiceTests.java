package com.github.pablomathdev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
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
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.repositories.IRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CreateBandServiceTests {

	@Captor
	ArgumentCaptor<Band> bandCaptor;
	
	@Captor
	ArgumentCaptor<String> GenreNameCaptor;

	@Mock
	IRepository<Band,Integer> bandRepository;
	
	@Mock
	IGenreRepository genreRepository;

	@InjectMocks
	CreateBandService createBandService;

	@Test
	public void should_call_save_method_repository_with_band() {
        
		Origin origin = originFactory("San Francisco", "United States", 1981);
		Genre genre = genreFactory(1, "Trash Metal");
		Set<Genre> set= new HashSet<>();
		set.add(genre);
		Band band = bandFactory(1, "Metallica",origin,set);
		
		

		createBandService.execute(band);
		
		

		Mockito.verify(bandRepository).save(bandCaptor.capture());
		assertEquals(band, bandCaptor.getValue());

	}
	@Test
	public void should_call_findByName_genre_repository_with_name() {
		Origin origin = originFactory("San Francisco", "United States", 1981);
		Genre genre = genreFactory(1, "Trash Metal");
		Set<Genre> set= new HashSet<>();
		set.add(genre);
		Band band = bandFactory(1, "Metallica",origin,set);
		
		
		createBandService.execute(band);
		
		Mockito.verify(genreRepository).findByName(GenreNameCaptor.capture());
		assertEquals("Trash Metal", GenreNameCaptor.getValue());
		
	}
	


	private Band bandFactory(Integer id, String name,Origin origin ,Set<Genre> genres) {
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
	private Genre genreFactory(Integer id,String name) {
		Genre genre = new Genre();
		genre.setId(id);
		genre.setName(name);
		return genre;
	}
}

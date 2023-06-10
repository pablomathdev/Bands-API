package com.github.pablomathdev.units;


import static com.github.pablomathdev.Factory.genreFactory;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.pablomathdev.application.services.GenreService;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.repositories.IGenreRepository;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTests {

	@Mock
	private IGenreRepository genreRepository;
	
	
	@InjectMocks
	private GenreService genreService;
	
	@Test
	public void should_InvokeGenreRepository_WithCorrectArguments() {
		
		Genre genre = genreFactory("any_genre");
		
		
		genreService.create(genre);
		
		
		verify(genreRepository).save(eq(genre));
	}
}

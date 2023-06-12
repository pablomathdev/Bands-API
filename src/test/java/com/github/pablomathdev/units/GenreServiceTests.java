package com.github.pablomathdev.units;

import static com.github.pablomathdev.Factory.genreFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.pablomathdev.application.services.GenreService;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.GenreAlreadyExistsException;
import com.github.pablomathdev.domain.repositories.IGenreRepository;

import jakarta.persistence.PersistenceException;

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

	@Test
	public void should_ThrowEntitySaveException_WhenGenreRespositorySaveThrowsPersistenceException() {

		Genre genre = genreFactory("any_genre");

		when(genreRepository.save(genre)).thenThrow(PersistenceException.class);

		Throwable exception = assertThrows(EntitySaveException.class, () -> genreService.create(genre));

		assertEquals(String.format("Failed to save the genre %s", genre.getName()), exception.getMessage());

	}

	@Test
	public void should_ThrowGenreAlreadyExistsException_WhenGenreRepositoryExistsReturnTrue() {
		Genre genre = genreFactory("any_genre");

		when(genreRepository.exists(genre.getName())).thenReturn(true);

		Throwable exception = assertThrows(GenreAlreadyExistsException.class, () -> genreService.create(genre));

		assertEquals(String.format("Genre %s Already Exists!", genre.getName()), exception.getMessage());

	}

	@Test
	public void should_ReturnGenre_WhenGenreRepositoryExistsIsFalse() {
		Genre genre = genreFactory("any_genre");

		when(genreRepository.exists(genre.getName())).thenReturn(false);
		when(genreRepository.save(genre)).thenReturn(genre);

		Genre genreSaved = genreService.create(genre);

		assertEquals(genre, genreSaved);

	}

}

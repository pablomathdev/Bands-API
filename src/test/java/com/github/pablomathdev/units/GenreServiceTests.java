package com.github.pablomathdev.units;

import static com.github.pablomathdev.Factory.genreFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.github.pablomathdev.Factory;
import com.github.pablomathdev.application.services.GenreService;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.EntityRelationshipException;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.GenreAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;
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

		assertEquals(String.format("Genre %s already exists", genre.getName()), exception.getMessage());

	}

	@Test
	public void should_ReturnGenre_WhenGenreRepositoryExistsIsFalse() {
		Genre genre = genreFactory("any_genre");

		when(genreRepository.exists(genre.getName())).thenReturn(false);
		when(genreRepository.save(genre)).thenReturn(genre);

		Genre genreSaved = genreService.create(genre);

		assertEquals(genre, genreSaved);

	}

	@Test
	public void should_FindReturnGenres_WhenGenreRepositoryFindAllReturnGenres() {

		Genre genre1 = Factory.genreFactory("any_genre_1");
		Genre genre2 = Factory.genreFactory("any_genre_2");

		List<Genre> result = List.of(genre1, genre2);

		when(genreRepository.findAll()).thenReturn(result);

		List<Genre> actual = genreService.find();

		assertEquals(2, actual.size());

	}

	@Test
	public void should_FindReturnEmpty_WhenGenreRepositoryFindAllNotReturnGenres() {

		List<Genre> result = List.of();

		when(genreRepository.findAll()).thenReturn(result);

		List<Genre> actual = genreService.find();

		assertTrue(actual.isEmpty());

	}

	@Test
	public void should_InvokeGenreRepositoryDelete_WithCorrectArguments() {

		Genre genre = genreFactory("any_genre");

		when(genreRepository.findByName(any())).thenReturn(genre);

		genreService.delete(genre.getName());

		verify(genreRepository).delete(eq(genre));

	}

	@Test
	public void should_ThrowGenreNotFoundException_WhenGenrNotExists() {

		Genre genre = genreFactory("any_genre");

		when(genreRepository.findByName(any())).thenThrow(EntityNotFoundException.class);

		assertThrows(GenreNotFoundException.class, () -> genreService.delete(genre.getName()));

	}

	@Test
	public void should_ThrowEntityRelationshipExceptionion_WhenGenreRepositoryDeleteThrowDataIntegrityViolationException() {

		Genre genre = genreFactory("any_genre");

		doThrow(DataIntegrityViolationException.class).when(genreRepository).delete(any());
		;

		assertThrows(EntityRelationshipException.class, () -> genreService.delete(genre.getName()));

	}

	@Test
	public void should_InvokeGenreRepositoryFindById_WithCorrectArguments() {

		Genre genre = genreFactory("any_genre");

		Integer id = 1;

		when(genreRepository.findById(id)).thenReturn(genre);

		genreService.update(genre, id);

		verify(genreRepository).findById(eq(id));

	}

	@Test
	public void should_ReturnGenreUpdated_WhenGenreIsUpdated() {

		Genre genreExisting = genreFactory("any_genre");

		Genre genreUpdate = genreFactory("update_genre");

		Integer id = 1;

		when(genreRepository.findById(id)).thenReturn(genreExisting);
		when(genreRepository.save(any(Genre.class))).thenReturn(genreUpdate);

		Genre genreUpdated = genreService.update(genreUpdate, id);

		assertEquals(genreUpdate.getName(), genreUpdated.getName());

	}

	@Test
	public void should_ThrowEntityNotFoundException_WhenGenreNotFound() {


		Integer id = 1;

		when(genreRepository.findById(id)).thenThrow(EntityNotFoundException.class);

		assertThrows(EntityNotFoundException.class, () -> genreService.update(any(Genre.class), id));

	}

}

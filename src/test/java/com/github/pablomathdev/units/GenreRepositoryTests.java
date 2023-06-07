package com.github.pablomathdev.units;

import static com.github.pablomathdev.Factory.genreFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.infraestructure.GenreRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GenreRepositoryTests {

	static final String SELECT_GENRE_BY_NAME = "select g from Genre g where g.name = :name";
	@Mock
	TypedQuery<Genre> typedQueryGenre;

	@Mock
	EntityManager entityManager;

	@InjectMocks
	GenreRepositoryImpl genreRepositoryImpl;

	@Test
	public void should_InvokeEntityManagerPersit_WithCorrectArguments() {

		Genre genre = genreFactory("any_genre");

		genreRepositoryImpl.save(genre);

		verify(entityManager).persist(eq(genre));

	}

	@Test
	public void should_InvokeTypedQuery_WithCorrectArguments() {
		Genre genre = genreFactory("any_genre");

		List<Genre> result = new ArrayList<>();
		result.add(genre);

		when(entityManager.createQuery(SELECT_GENRE_BY_NAME, Genre.class)).thenReturn(typedQueryGenre);
		when(typedQueryGenre.getResultList()).thenReturn(result);
		genreRepositoryImpl.findByName(genre.getName());

		verify(typedQueryGenre).setParameter(eq("name"), eq(genre.getName()));

	}

	@Test
	public void should_ThrowEntitySaveException_WhenEntityManagerPersistThrowsPersistenceException() {
		Genre genre = genreFactory("genre");

		doThrow(new PersistenceException()).when(entityManager).persist(genre);

		Throwable exception = assertThrows(EntitySaveException.class, () -> genreRepositoryImpl.save(genre));

		assertEquals(String.format("Failed to save the genre %s", genre.getName()), exception.getMessage());
	}

	@Test
	public void should_FindByNameReturnAGenre_WhenTypedQueryGetResultListReturnAGenre() {

		Genre genre = genreFactory("any_genre");

		List<Genre> results = new ArrayList<>();
		Genre genreExpected = new Genre();
		genreExpected.setName("any_genre");
		results.add(genreExpected);

		when(typedQueryGenre.getResultList()).thenReturn(results);

		when(entityManager.createQuery(SELECT_GENRE_BY_NAME, Genre.class)).thenReturn(typedQueryGenre);

		Genre expected = genreRepositoryImpl.findByName(genre.getName());

		assertEquals(expected.getName(), genre.getName());

	}

	@Test
	public void should_FindByNameThrowEntityNotFoundException_WhenTypedQueryGetResultListIsEmpty() {

		Genre genre = genreFactory("any_genre");

		List<Genre> results = new ArrayList<>();

		when(typedQueryGenre.getResultList()).thenReturn(results);

		when(entityManager.createQuery(SELECT_GENRE_BY_NAME, Genre.class)).thenReturn(typedQueryGenre);

		Throwable exception = assertThrows(EntityNotFoundException.class,
				() -> genreRepositoryImpl.findByName(genre.getName()));

		assertEquals(EntityNotFoundException.class, exception.getClass());

	}

}

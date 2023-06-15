package com.github.pablomathdev.units;

import static com.github.pablomathdev.Factory.genreFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.infraestructure.GenreRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
public class GenreRepositoryTests {

	static final String SELECT_GENRE_BY_NAME = "select g from Genre g where g.name = :name";

	static final String COUNT_GENRE = "select count(g) from Genre g where g.name = :name";

	@Mock
	TypedQuery<Genre> typedQueryGenre;

	@Mock
	TypedQuery<Long> typedQueryLong;

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

		assertThrows(PersistenceException.class, () -> genreRepositoryImpl.save(genre));

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

		assertThrows(EntityNotFoundException.class, () -> genreRepositoryImpl.findByName(genre.getName()));

	}

	@Test
	public void should_ExistsReturnTrue_whenTypedQueryGetSingleResultReturnOneResult() {

		Genre genre = genreFactory("any_genre");

		when(typedQueryLong.getSingleResult()).thenReturn(1L);

		when(entityManager.createQuery(COUNT_GENRE, Long.class)).thenReturn(typedQueryLong);

		Boolean exists = genreRepositoryImpl.exists(genre.getName());

		assertTrue(exists);

	}

	@Test
	public void should_ExistsReturnFalse_whenTypedQueryGetSingleResultReturnZeroResult() {

		Genre genre = genreFactory("any_genre");

		when(typedQueryLong.getSingleResult()).thenReturn(0L);

		when(entityManager.createQuery(COUNT_GENRE, Long.class)).thenReturn(typedQueryLong);

		Boolean exists = genreRepositoryImpl.exists(genre.getName());

		assertFalse(exists);

	}

	@Test 
	public void should_ReturnEmptyList_WhenTypedQueryGetResultListIsEmpty() {
      when(entityManager.createQuery("from Genre",Genre.class)).thenReturn(typedQueryGenre);
		
		List<Genre> results = new ArrayList<>();
		
		when(typedQueryGenre.getResultList()).thenReturn(results);
		
		
		 List<Genre> listGenreExpected =  genreRepositoryImpl.findAll();
		
		 assertTrue(listGenreExpected.isEmpty());
	}

	@Test 
	public void should_ReturnGenre_WhenTypedQueryGetResultListNotIsEmpty() {
     
		Genre genre1 = genreFactory("any_genre_1");
		
		when(entityManager.createQuery("from Genre",Genre.class)).thenReturn(typedQueryGenre);
		
      
      
		List<Genre> results = new ArrayList<>();
		results.add(genre1);
		when(typedQueryGenre.getResultList()).thenReturn(results);
		
		
		 List<Genre> listGenreExpected =  genreRepositoryImpl.findAll();
		
		 assertFalse(listGenreExpected.isEmpty());
	}
}

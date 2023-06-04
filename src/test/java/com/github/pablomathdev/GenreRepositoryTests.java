package com.github.pablomathdev;

import static com.github.pablomathdev.Factory.genreFactory;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.infraestructure.GenreRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GenreRepositoryTests {

	@Mock
	TypedQuery<Genre> typedQueryGenre;

	@Mock
	EntityManager entityManager;

	@InjectMocks
	GenreRepositoryImpl genreRepositoryImpl;

	@Test
	public void should_InvokeEntityManagerPersit_WithCorrectArguments() {


		String jpql = "select g from Genre g where g.name = :name";

		when(entityManager.createQuery(jpql, Genre.class)).thenReturn(typedQueryGenre);
		
		Genre genre = genreFactory("Heavy Metal");

		genreRepositoryImpl.save(genre);

		verify(entityManager).persist(eq(genre));

	}

	@Test
	public void should_InvokeTypedQuery_WithCorrectArguments() {
		Genre genre = genreFactory("Heavy Metal");

		String jpql = "select g from Genre g where g.name = :name";

		
		when(entityManager.createQuery(jpql, Genre.class)).thenReturn(typedQueryGenre);
		
		genreRepositoryImpl.findByName(genre.getName());


		verify(typedQueryGenre).setParameter(eq("name"), eq(genre.getName()));

	}

}

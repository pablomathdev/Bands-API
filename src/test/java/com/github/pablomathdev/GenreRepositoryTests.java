package com.github.pablomathdev;
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
import static com.github.pablomathdev.Factory.genreFactory;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GenreRepositoryTests {

	@Mock
	EntityManager entityManager;

	@InjectMocks
	GenreRepositoryImpl genreRepositoryImpl;

	@Test
	public void should_InvokeEntityManagerPersit_WithCorrectArguments() {

	Genre genre = genreFactory("Heavy Metal");
	
	
	genreRepositoryImpl.save(genre);
	
	
        verify(entityManager).persist(eq(genre));
		
		
		
	}

}

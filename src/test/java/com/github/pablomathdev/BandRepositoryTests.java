package com.github.pablomathdev;

import static org.mockito.ArgumentMatchers.eq;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.infraestructure.BandRepositoryImpl;

import jakarta.persistence.EntityManager;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BandRepositoryTests {
    
	
	@Mock
	EntityManager entityManager;
	
	
	@InjectMocks
	BandRepositoryImpl bandRepositoryImpl;
	
	
	@Test
	public void should_call_find_method_Entity_Manager_with_band() {
		
		Origin origin = Factory.originFactory("Aberdeen","United States",1987);
		Genre genre = Factory.genreFactory("Alternative Rock");
		Set<Genre> set = new HashSet<>();
		set.add(genre);
		Band band = Factory.bandFactory("Nirvana",origin,set);
		
		
		bandRepositoryImpl.save(band);
		
		Mockito.verify(entityManager).persist(eq(band));
		
		
	}
	
	
	
}

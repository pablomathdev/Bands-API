package com.github.pablomathdev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.github.pablomathdev.domain.exceptions.BandAlreadyExistsException;
import com.github.pablomathdev.infraestructure.BandRepositoryImpl;

import jakarta.persistence.EntityExistsException;
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
	@Test
	public void should_ThrowBandAlreadyExistsException_WhenBandAlreadyExistsDuringSave() {
		
		Origin origin = Factory.originFactory("Aberdeen","United States",1987);
		Genre genre = Factory.genreFactory("Alternative Rock");
		Set<Genre> set = new HashSet<>();
		set.add(genre);
		Band band = Factory.bandFactory("Nirvana",origin,set);
		
		
		
		
		Mockito.doThrow(new EntityExistsException()).when(entityManager).persist(band);

		Throwable exception = assertThrows(BandAlreadyExistsException.class, () -> bandRepositoryImpl.save(band));
		assertEquals("This Band Already Exists", exception.getMessage());
		
		
	}
	
	@Test
	public void should_InvokeEntityManagerFind_withCorrectArguments() {
		
		Origin origin = Factory.originFactory("Aberdeen","United States",1987);
		Genre genre = Factory.genreFactory("Alternative Rock");
		Set<Genre> set = new HashSet<>();
		set.add(genre);
		Band band = Factory.bandFactory("Nirvana",origin,set);
		band.setId(2);
		
		
	    bandRepositoryImpl.findById(2);
		
		Mockito.verify(entityManager).find(eq(Band.class),eq(2));
		
		
		
	}
	
	
	
}

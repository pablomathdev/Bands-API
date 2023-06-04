package com.github.pablomathdev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

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
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.infraestructure.BandRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BandRepositoryTests {

	static final String SELECT_BAND_BY_NAME = "select b from Band b where b.name = :name";
	
	@Mock
	TypedQuery<Band> typedQueryBand;

	@Mock
	EntityManager entityManager;

	@InjectMocks
	BandRepositoryImpl bandRepositoryImpl;

	@Test
	public void should_InvokeEntityManagerPersist_withCorrectArguments() {

		Origin origin = Factory.originFactory("Aberdeen", "United States", 1987);
		Genre genre = Factory.genreFactory("Alternative Rock");
		Set<Genre> set = new HashSet<>();
		set.add(genre);
		Band band = Factory.bandFactory("Nirvana", origin, set);

		

		when(typedQueryBand.getSingleResult()).thenReturn(new Band());
		when(entityManager.createQuery(SELECT_BAND_BY_NAME, Band.class)).thenReturn(typedQueryBand);

		bandRepositoryImpl.save(band);

		Mockito.verify(entityManager).persist(eq(band));

	}

	@Test
	public void should_ThrowEntitySaveException_WhenEntityManagerPersistThrows() {

		Origin origin = Factory.originFactory("Aberdeen", "United States", 1987);
		Genre genre = Factory.genreFactory("Alternative Rock");
		Set<Genre> set = new HashSet<>();
		set.add(genre);
		Band band = Factory.bandFactory("Nirvana", origin, set);
		
		
		
		when(entityManager.createQuery(SELECT_BAND_BY_NAME, Band.class)).thenReturn(typedQueryBand);
		when(bandRepositoryImpl.findByName("Nirvana")).thenReturn(band);
		
		doThrow(new PersistenceException()).when(entityManager).persist(band);

		
		
		Throwable exception = assertThrows(EntitySaveException.class, () -> bandRepositoryImpl.save(band));

		assertEquals("Failed to save the band Nirvana", exception.getMessage());

	}

	@Test
	public void should_InvokeEntityManagerFind_withCorrectArguments() {

		Origin origin = Factory.originFactory("Aberdeen", "United States", 1987);
		Genre genre = Factory.genreFactory("Alternative Rock");
		Set<Genre> set = new HashSet<>();
		set.add(genre);
		Band band = Factory.bandFactory("Nirvana", origin, set);
		band.setId(2);

		bandRepositoryImpl.findById(2);

		Mockito.verify(entityManager).find(eq(Band.class), eq(2));

	}

	@Test
	public void should_InvokeTypedQuery_withCorrectArguments() {

		Origin origin = Factory.originFactory("Aberdeen", "United States", 1987);
		Genre genre = Factory.genreFactory("Alternative Rock");
		Set<Genre> set = new HashSet<>();
		set.add(genre);
		Band band = Factory.bandFactory("Nirvana", origin, set);


		when(entityManager.createQuery(SELECT_BAND_BY_NAME, Band.class)).thenReturn(typedQueryBand);

		bandRepositoryImpl.findByName(band.getName());

		Mockito.verify(typedQueryBand).setParameter(eq("name"), eq(band.getName()));

	}

}

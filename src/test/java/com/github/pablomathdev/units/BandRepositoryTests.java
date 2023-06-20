package com.github.pablomathdev.units;

import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.originFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.infraestructure.BandRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
public class BandRepositoryTests {

	static final String SELECT_BAND_BY_NAME = "select b from Band b where b.name = :name";
	static final String COUNT_BAND = "select count(b) from Band b where b.name = :name";

	@Mock
	TypedQuery<Band> typedQueryBand;

	@Mock
	TypedQuery<Long> typedQueryLong;

	@Mock
	EntityManager entityManager;

	@InjectMocks
	BandRepositoryImpl bandRepositoryImpl;

	@Test
	public void should_InvokeEntityManagerPersist_withCorrectArguments() {

		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre = genreFactory("any_genre");
		List<Genre> set = new ArrayList<>();
		set.add(genre);
		Band band = bandFactory("any_band", origin, set);

		bandRepositoryImpl.save(band);

		verify(entityManager).persist(eq(band));

	}

	@Test
	public void should_InvokeTypedQueryFindByName_withCorrectArguments() {

		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre = genreFactory("any_genre");
		List<Genre> set = new ArrayList<>();
		set.add(genre);
		Band band = bandFactory("any_band", origin, set);

		List<Band> result = new ArrayList<>();
		result.add(band);

		when(entityManager.createQuery(SELECT_BAND_BY_NAME, Band.class)).thenReturn(typedQueryBand);

		bandRepositoryImpl.findByName(band.getName());

		verify(typedQueryBand).setParameter(eq("name"), eq(band.getName()));

	}

	@Test
	public void should_FindByNameReturnABand_WhenTheTypedQueryReturnABand() {
		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre = genreFactory("any_genre");
		List<Genre> set = new ArrayList<>();
		set.add(genre);
		Band band = bandFactory("any_band", origin, set);

		Band bandExpected = bandFactory("any_band", origin, set);
		bandExpected.setId(1);

		when(entityManager.createQuery(SELECT_BAND_BY_NAME, Band.class)).thenReturn(typedQueryBand);

		when(typedQueryBand.getSingleResult()).thenReturn(bandExpected);

		bandRepositoryImpl.findByName(band.getName());

		assertEquals(bandExpected.getName(), band.getName());
		assertNotNull(bandExpected.getId());

	}

	@Test
	public void should_ThrowNoResultException_WhenTypedQueryThrowsNoResultException() {
		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre = genreFactory("any_genre");
		List<Genre> set = new ArrayList<>();
		set.add(genre);
		Band band = bandFactory("any_band", origin, set);

		when(entityManager.createQuery(SELECT_BAND_BY_NAME, Band.class)).thenReturn(typedQueryBand);

		when(typedQueryBand.getSingleResult()).thenThrow(NoResultException.class);

		assertThrows(EntityNotFoundException.class, () -> bandRepositoryImpl.findByName(band.getName()));

	}

	@Test
	public void should_InvokeTypedQueryExists_withCorrectArguments() {

		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre = genreFactory("any_genre");
		List<Genre> set = new ArrayList<>();
		set.add(genre);
		Band band = bandFactory("any_band", origin, set);

		when(typedQueryLong.getSingleResult()).thenReturn(1L);

		when(entityManager.createQuery(COUNT_BAND, Long.class)).thenReturn(typedQueryLong);

		bandRepositoryImpl.exists(band.getName());

		verify(typedQueryLong).setParameter(eq("name"), eq(band.getName()));

	}

	@Test
	public void should_ExistsReturnFalse_WhenTypedQueryGetSingleResultIsDifferentOfOne() {

		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre = genreFactory("any_genre");
		List<Genre> set = new ArrayList<>();
		set.add(genre);
		Band band = bandFactory("any_band", origin, set);

		when(typedQueryLong.getSingleResult()).thenReturn(0L);

		when(entityManager.createQuery(COUNT_BAND, Long.class)).thenReturn(typedQueryLong);

		boolean expected = bandRepositoryImpl.exists(band.getName());

		assertFalse(expected);

	}

	@Test
	public void should_ExistsReturnTrue_WhenTypedQueryGetSingleResultIsEqualOne() {

		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre = genreFactory("any_genre");
		List<Genre> set = new ArrayList<>();
		set.add(genre);
		Band band = bandFactory("any_band", origin, set);

		when(typedQueryLong.getSingleResult()).thenReturn(1L);

		when(entityManager.createQuery(COUNT_BAND, Long.class)).thenReturn(typedQueryLong);

		boolean expected = bandRepositoryImpl.exists(band.getName());

		assertTrue(expected);

	}

	@Test 
	public void should_ReturnEmptyList_WhenTypedQueryGetResultListIsEmpty() {
		
		when(entityManager.createQuery("from Band",Band.class)).thenReturn(typedQueryBand);
		
		List<Band> results = new ArrayList<>();
		
		when(typedQueryBand.getResultList()).thenReturn(results);
		
		
		 List<Band> listBandExpected =  bandRepositoryImpl.findAll();
		
		 assertTrue(listBandExpected.isEmpty());
	     
	}

	@Test
	public void should_ReturnBands_WhenTypedQueryGetResultListNotIsEmpty() {
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band1 = bandFactory("any_band_1", origin, null);
		Band band2 = bandFactory("any_band_2", origin, null);

		when(entityManager.createQuery("from Band", Band.class)).thenReturn(typedQueryBand);

		List<Band> results = new ArrayList<>();
		results.add(band1);
		results.add(band2);
		when(typedQueryBand.getResultList()).thenReturn(results);

		List<Band> listBandExpected = bandRepositoryImpl.findAll();

		assertFalse(listBandExpected.isEmpty());

	}
	
	@Test
	public void should_InvokeEntityManagerRemove_withCorrectArguments() {
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band1 = bandFactory("any_band_1", origin, null);
		
		bandRepositoryImpl.delete(band1);
		verify(entityManager).remove(eq(band1));
	}

}

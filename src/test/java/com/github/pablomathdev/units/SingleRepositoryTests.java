package com.github.pablomathdev.units;

import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.originFactory;
import static com.github.pablomathdev.Factory.singleFactory;
import static com.github.pablomathdev.Factory.trackFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.domain.entities.Single;
import com.github.pablomathdev.domain.entities.Track;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.infraestructure.repositories.SingleRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
public class SingleRepositoryTests {
	static final String COUNT_SINGLE = "select count(s) from Single s where s.title = :singleTitle AND s.band.name = :bandName";
	static final String SELECT_SINGLE_BY_NAME = "select s from Single s where s.title = :title";
	static final String FIND_SINGLE_BY_TITLE_AND_BAND_NAME = "select s from Single s where s.title =:singleTitle AND s.band.name =:bandName";
	
	@Mock
	private EntityManager entityManager;

	@Mock
	private TypedQuery<Single> typedQuerySingle;

	@Mock
	private TypedQuery<Long> typedQueryLong;

	@InjectMocks
	private SingleRepositoryImpl singleRepositoryImpl;

	@Test
	public void should_InvokeEntityManagerPersist_WithCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);

		singleRepositoryImpl.save(single);

		verify(entityManager).persist(eq(single));
	}

	@Test
	public void should_ReturnSingle_WhenEntityManagerPersistSaveSingle() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);

		
		
		Single expected = singleRepositoryImpl.save(single);

		assertEquals(single, expected);

	}

	@Test
	public void should_ReturnListOfSingles_WhenEntityManagerGetResultListNotIsEmpty() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);


		when(entityManager.createQuery("from Single",Single.class)).thenReturn(typedQuerySingle);
		when(typedQuerySingle.getResultList()).thenReturn(List.of(single));

		List<Single> result = singleRepositoryImpl.findAll();

		assertFalse(result.isEmpty());

	}

	@Test
	public void should_ReturnEmptyList_WhenTypedQueryGetResultListIsEmpty() {
		
		when(entityManager.createQuery("from Single",Single.class)).thenReturn(typedQuerySingle);
		when(typedQuerySingle.getResultList()).thenReturn(List.of());
		
		
		List<Single> result  = singleRepositoryImpl.findAll();
		
		
		assertTrue(result.isEmpty());
		
		
	
	}

	@Test
	public void should_InvokeTypedQueryExists_withCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);

		when(typedQueryLong.getSingleResult()).thenReturn(1L);

		when(entityManager.createQuery(COUNT_SINGLE, Long.class)).thenReturn(typedQueryLong);

		singleRepositoryImpl.exists(single.getTitle(), single.getBand().getName());

		verify(typedQueryLong).setParameter(eq("singleTitle"), eq(single.getTitle()));
		verify(typedQueryLong).setParameter(eq("bandName"), eq(single.getBand().getName()));

	}

	@Test
	public void should_InvokeTypedQueryFindByName_withCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);


		when(entityManager.createQuery(SELECT_SINGLE_BY_NAME, Single.class)).thenReturn(typedQuerySingle);

		singleRepositoryImpl.findByName(single.getTitle());

		verify(typedQuerySingle).setParameter(eq("title"), eq(single.getTitle()));

	}

	@Test
	public void should_FindByNameReturnASingle_WhenTheTypedQueryReturnASingle() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);


		when(entityManager.createQuery(SELECT_SINGLE_BY_NAME, Single.class)).thenReturn(typedQuerySingle);

		when(typedQuerySingle.getSingleResult()).thenReturn(single);

		Single result = singleRepositoryImpl.findByName(single.getTitle());

		assertEquals(single.getTitle(), result.getTitle());

	}

	@Test
	public void should_ThrowNoResultException_WhenTypedQueryThrowsNoResultException() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);

		when(entityManager.createQuery(SELECT_SINGLE_BY_NAME, Single.class)).thenReturn(typedQuerySingle);

		when(typedQuerySingle.getSingleResult()).thenThrow(NoResultException.class);

		assertThrows(EntityNotFoundException.class, () -> singleRepositoryImpl.findByName(single.getTitle()));

	}

	@Test
	public void should_InvokeEntityManagerRemove_withCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_title", origin, List.of(genre));
		Track track = trackFactory("any_title", null, null, null, null);
		
		Single single = singleFactory("any_title", band,List.of(genre),LocalDate.parse("1999-09-09"),track);

		
		singleRepositoryImpl.delete(single);

		verify(entityManager).remove(eq(single));

	}

	@Test
	public void should_SingleRepositoryFindSingleByTitleAndBandNameInvokeTypedQuery_WithCorrectArguments() {
		
		when(entityManager.createQuery(FIND_SINGLE_BY_TITLE_AND_BAND_NAME, Single.class)).thenReturn(typedQuerySingle);

		String singleTitle = "any_single_title";
		String bandName = "any_band_name";
		
		singleRepositoryImpl.findSingleByTitleAndBandName(singleTitle,bandName);

		verify(typedQuerySingle).setParameter(eq("singleTitle"), eq(singleTitle));
		verify(typedQuerySingle).setParameter(eq("bandName"), eq(bandName));
		
		
	}

//	@Test
//	public void should_AlbumRepositoryFindAlbumByTitleAndBandNameReturnAlbum_WhenAlbumExists() {
//		Genre genre = genreFactory("any_genre");
//		Origin origin = originFactory("any_city", "any_country", 1999);
//		Band band = bandFactory("any_name", origin, List.of(genre));
//		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
//				List.of(new Track()));
//
//		when(entityManager.createQuery(FIND_ALBUM_BY_TITLE_AND_BAND_NAME, Album.class)).thenReturn(typedQueryAlbum);
//
//		when(typedQueryAlbum.getSingleResult()).thenReturn(album);
//
//	
//
//	  Album result = albumRepositoryImpl.findAlbumByTitleAndBandName(anyString(),anyString());
//		
//	
//		assertEquals(album.getTitle(), result.getTitle());
//		assertEquals(album.getBand().getName(), result.getBand().getName());
//
//	}
//	@Test
//	public void should_AlbumRepositoryFindAlbumByTitleAndBandNameThrowEntityNotFoundException_WhenAlbumNotFound() {
//		
//		when(entityManager.createQuery(FIND_ALBUM_BY_TITLE_AND_BAND_NAME, Album.class)).thenReturn(typedQueryAlbum);
//
//		when(typedQueryAlbum.getSingleResult()).thenThrow(NoResultException.class);
//
//
//	   assertThrows(EntityNotFoundException.class,()->albumRepositoryImpl.findAlbumByTitleAndBandName(anyString(),anyString()) );
//
//	}

}

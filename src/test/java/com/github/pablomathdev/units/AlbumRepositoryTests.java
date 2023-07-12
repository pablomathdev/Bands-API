package com.github.pablomathdev.units;

import static com.github.pablomathdev.Factory.albumFactory;
import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.originFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.github.pablomathdev.domain.entities.Track;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.AlbumNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.infraestructure.repositories.AlbumRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
public class AlbumRepositoryTests {
	static final String COUNT_ALBUM = "select count(a) from Album a where a.title = :albumTitle AND a.band.name = :bandName";
	static final String SELECT_ALBUM_BY_NAME = "select a from Album a where a.title = :title";
	static final String FIND_ALBUM_BY_TITLE_AND_BAND_NAME = "select a from Album a where a.title =:albumTitle AND a.band.name =:bandName";
	@Mock
	private EntityManager entityManager;

	@Mock
	private TypedQuery<Album> typedQueryAlbum;

	@Mock
	private TypedQuery<Long> typedQueryLong;

	@InjectMocks
	private AlbumRepositoryImpl albumRepositoryImpl;

	@Test
	public void should_InvokeEntityManagerPersist_WithCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		albumRepositoryImpl.save(album);

		verify(entityManager).persist(eq(album));
	}

	@Test
	public void should_ReturnAlbum_WhenEntityManagerPersistSaveAlbum() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		Album expected = albumRepositoryImpl.save(album);

		assertEquals(album, expected);

	}

	@Test
	public void should_ReturnListOfAlbums_WhenEntityManagerGetResultListNotIsEmpty() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		when(entityManager.createQuery("from Album", Album.class)).thenReturn(typedQueryAlbum);
		when(typedQueryAlbum.getResultList()).thenReturn(List.of(album));

		List<Album> result = albumRepositoryImpl.findAll();

		assertFalse(result.isEmpty());

	}

	@Test
	public void should_ReturnEmptyList_WhenTypedQueryGetResultListIsEmpty() {
		
		when(entityManager.createQuery("from Album",Album.class)).thenReturn(typedQueryAlbum);
		when(typedQueryAlbum.getResultList()).thenReturn(List.of());
		
		
		List<Album> result  = albumRepositoryImpl.findAll();
		
		
		assertTrue(result.isEmpty());
		
		
	
	}

	@Test
	public void should_InvokeTypedQueryExists_withCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		when(typedQueryLong.getSingleResult()).thenReturn(1L);

		when(entityManager.createQuery(COUNT_ALBUM, Long.class)).thenReturn(typedQueryLong);

		albumRepositoryImpl.exists(album.getTitle(), album.getBand().getName());

		verify(typedQueryLong).setParameter(eq("albumTitle"), eq(album.getTitle()));
		verify(typedQueryLong).setParameter(eq("bandName"), eq(album.getBand().getName()));

	}

	@Test
	public void should_InvokeTypedQueryFindByName_withCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		List<Album> result = new ArrayList<>();
		result.add(album);

		when(entityManager.createQuery(SELECT_ALBUM_BY_NAME, Album.class)).thenReturn(typedQueryAlbum);

		albumRepositoryImpl.findByName(album.getTitle());

		verify(typedQueryAlbum).setParameter(eq("title"), eq(album.getTitle()));

	}

	@Test
	public void should_FindByNameReturnAAlbum_WhenTheTypedQueryReturnAAlbum() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		when(entityManager.createQuery(SELECT_ALBUM_BY_NAME, Album.class)).thenReturn(typedQueryAlbum);

		when(typedQueryAlbum.getSingleResult()).thenReturn(album);

		Album result = albumRepositoryImpl.findByName(album.getTitle());

		assertEquals(album.getTitle(), result.getTitle());

	}

	@Test
	public void should_ThrowNoResultException_WhenTypedQueryThrowsNoResultException() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		when(entityManager.createQuery(SELECT_ALBUM_BY_NAME, Album.class)).thenReturn(typedQueryAlbum);

		when(typedQueryAlbum.getSingleResult()).thenThrow(NoResultException.class);

		assertThrows(EntityNotFoundException.class, () -> albumRepositoryImpl.findByName(album.getTitle()));

	}

	@Test
	public void should_InvokeEntityManagerRemove_withCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		albumRepositoryImpl.delete(album);

		verify(entityManager).remove(eq(album));

	}

	@Test
	public void should_AlbumRepositoryFindAlbumByTitleAndBandNameInvokeTypedQuery_WithCorrectArguments() {
		
		when(entityManager.createQuery(FIND_ALBUM_BY_TITLE_AND_BAND_NAME, Album.class)).thenReturn(typedQueryAlbum);

		String albumTitle = "any_album_title";
		String bandName = "any_band_name";
		
		albumRepositoryImpl.findAlbumByTitleAndBandName(albumTitle,bandName);

		verify(typedQueryAlbum).setParameter(eq("albumTitle"), eq(albumTitle));
		verify(typedQueryAlbum).setParameter(eq("bandName"), eq(bandName));
		
		
	}

	@Test
	public void should_AlbumRepositoryFindAlbumByTitleAndBandNameReturnAlbum_WhenAlbumExists() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		when(entityManager.createQuery(FIND_ALBUM_BY_TITLE_AND_BAND_NAME, Album.class)).thenReturn(typedQueryAlbum);

		when(typedQueryAlbum.getSingleResult()).thenReturn(album);

		Album result = albumRepositoryImpl.findAlbumByTitleAndBandName(anyString(), anyString());

		assertEquals(album.getTitle(), result.getTitle());
		assertEquals(album.getBand().getName(), result.getBand().getName());

	}

	@Test
	public void should_AlbumRepositoryFindAlbumByTitleAndBandNameThrowEntityNotFoundException_WhenAlbumNotFound() {
		
		when(entityManager.createQuery(FIND_ALBUM_BY_TITLE_AND_BAND_NAME, Album.class)).thenReturn(typedQueryAlbum);

		when(typedQueryAlbum.getSingleResult()).thenThrow(NoResultException.class);


	   assertThrows(EntityNotFoundException.class,()->albumRepositoryImpl.findAlbumByTitleAndBandName(anyString(),anyString()) );

	}

	@Test
	public void should_ReturnAlbumUpdated_WhenAlbumIsUpdated() {

		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"), null);

		when(entityManager.merge(any(Album.class))).thenReturn(album);

		Album albumUpdated = albumRepositoryImpl.update(album);

		assertEquals(album.getTitle(), albumUpdated.getTitle());
		assertEquals(album.getBand(), albumUpdated.getBand());

	}

	@Test
	public void should_ReturnAlbumById_WhenAlbumExists() {

		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"), null);

		Integer id = 1;

		when(entityManager.find(Album.class, id)).thenReturn(album);

		Album albumReturned = albumRepositoryImpl.findById(id);

		assertEquals(album.getTitle(), albumReturned.getTitle());
		assertEquals(album.getBand(), albumReturned.getBand());

	}

	@Test
	public void should_ThrowAlbumNotFoundException_WhenAlbumNotExists() {

		Integer id = 1;

		when(entityManager.find(Album.class, id)).thenReturn(null);

		assertThrows(AlbumNotFoundException.class, () -> albumRepositoryImpl.findById(id));

	}

}

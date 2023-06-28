package com.github.pablomathdev.units;

import static com.github.pablomathdev.Factory.albumFactory;
import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.originFactory;
import static com.github.pablomathdev.Factory.trackFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.github.pablomathdev.domain.entities.Track;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.infraestructure.repositories.TrackRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;


@ExtendWith(MockitoExtension.class)
public class TrackRepositoryTests {
	static final String COUNT_TRACK= "select count(a) from Track a where a.title = :trackTitle AND a.band.name = :bandName";
	static final String SELECT_TRACK_BY_NAME = "select a from Track a where a.title = :title";
	static final String FIND_TRACK_BY_TITLE_AND_BAND_NAME = "select a from Track a where a.title =:trackTitle AND a.band.name =:bandName";
	@Mock
	private EntityManager entityManager;

	@Mock
	private TypedQuery<Track> typedQueryTrack;

	@Mock
	private TypedQuery<Long> typedQueryLong;

	@InjectMocks
	private TrackRepositoryImpl trackRepositoryImpl;
	

	@Test
	public void should_InvokeEntityManagerPersist_WithCorrectArguments() {
		
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city","any_country",1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title",band, List.of(genre),LocalDate.parse("1999-09-09"), List.of(new Track()));
		
		
		Track track = trackFactory("any_title",band,album,null,LocalDate.parse("1999-09-09"),List.of(genre));

		trackRepositoryImpl.save(track);

		verify(entityManager).persist(eq(track));
	}

	@Test
	public void should_ReturnTrack_WhenEntityManagerPersistSaveTrack() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city","any_country",1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title",band, List.of(genre),LocalDate.parse("1999-09-09"), List.of(new Track()));
		
		
		Track track = trackFactory("any_title",band,album,null,LocalDate.parse("1999-09-09"),List.of(genre));

		Track result = trackRepositoryImpl.save(track);

		assertEquals(track, result);

	}

	@Test
	public void should_ReturnListOfTracks_WhenEntityManagerGetResultListNotIsEmpty() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city","any_country",1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title",band, List.of(genre),LocalDate.parse("1999-09-09"), List.of(new Track()));
		
		
		Track track = trackFactory("any_title",band,album,null,LocalDate.parse("1999-09-09"),List.of(genre));

		when(entityManager.createQuery("from Track", Track.class)).thenReturn(typedQueryTrack);
		when(typedQueryTrack.getResultList()).thenReturn(List.of(track));

		List<Track> result = trackRepositoryImpl.findAll();

		assertFalse(result.isEmpty());

	}

	@Test
	public void should_ReturnEmptyList_WhenTypedQueryGetResultListIsEmpty() {
		
		when(entityManager.createQuery("from Track",Track.class)).thenReturn(typedQueryTrack);
		when(typedQueryTrack.getResultList()).thenReturn(List.of());
		
		
		List<Track> result  = trackRepositoryImpl.findAll();
		
		
		assertTrue(result.isEmpty());
		
		
	
	}

	@Test
	public void should_InvokeTypedQueryExists_withCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city","any_country",1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title",band, List.of(genre),LocalDate.parse("1999-09-09"), List.of(new Track()));
		
		Track track = trackFactory("any_title",band,album,null,LocalDate.parse("1999-09-09"),List.of(genre));


		when(typedQueryLong.getSingleResult()).thenReturn(1L);

		when(entityManager.createQuery(COUNT_TRACK, Long.class)).thenReturn(typedQueryLong);

		trackRepositoryImpl.exists(track.getTitle(), track.getBand().getName());

		verify(typedQueryLong).setParameter(eq("trackTitle"), eq(track.getTitle()));
		verify(typedQueryLong).setParameter(eq("bandName"), eq(track.getBand().getName()));

	}

	@Test
	public void should_InvokeTypedQueryFindByName_withCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city","any_country",1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title",band, List.of(genre),LocalDate.parse("1999-09-09"), List.of(new Track()));
		
		Track track = trackFactory("any_title",band,album,null,LocalDate.parse("1999-09-09"),List.of(genre));

		when(entityManager.createQuery(SELECT_TRACK_BY_NAME, Track.class)).thenReturn(typedQueryTrack);

		trackRepositoryImpl.findByName(track.getTitle());

		verify(typedQueryTrack).setParameter(eq("title"), eq(track.getTitle()));

	}

	@Test
	public void should_FindByNameReturnATrack_WhenTheTypedQueryReturnATrack() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city","any_country",1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title",band, List.of(genre),LocalDate.parse("1999-09-09"), List.of(new Track()));
		
		Track track = trackFactory("any_title",band,album,null,LocalDate.parse("1999-09-09"),List.of(genre));

		when(entityManager.createQuery(SELECT_TRACK_BY_NAME, Track.class)).thenReturn(typedQueryTrack);

		when(typedQueryTrack.getSingleResult()).thenReturn(track);

		Track result = trackRepositoryImpl.findByName(track.getTitle());

		assertEquals(track.getTitle(), result.getTitle());

	}

	@Test
	public void should_ThrowEntityNotFoundException_WhenTypedQueryThrowsNoResultException() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city","any_country",1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title",band, List.of(genre),LocalDate.parse("1999-09-09"), List.of(new Track()));
		
		Track track = trackFactory("any_title",band,album,null,LocalDate.parse("1999-09-09"),List.of(genre));

		when(entityManager.createQuery(SELECT_TRACK_BY_NAME, Track.class)).thenReturn(typedQueryTrack);

		when(typedQueryTrack.getSingleResult()).thenThrow(NoResultException.class);

		assertThrows(EntityNotFoundException.class, () ->trackRepositoryImpl.findByName(track.getTitle()));

	}

	@Test
	public void should_InvokeEntityManagerRemove_withCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city","any_country",1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title",band, List.of(genre),LocalDate.parse("1999-09-09"), List.of(new Track()));
		
		Track track = trackFactory("any_title",band,album,null,LocalDate.parse("1999-09-09"),List.of(genre));

		trackRepositoryImpl.delete(track);

		verify(entityManager).remove(eq(track));

	}

	@Test
	public void should_TrackRepositoryFindTrackByTitleAndBandNameInvokeTypedQuery_WithCorrectArguments() {
		
		when(entityManager.createQuery(FIND_TRACK_BY_TITLE_AND_BAND_NAME, Track.class)).thenReturn(typedQueryTrack);

		String trackTitle = "any_track_title";
		String bandName = "any_band_name";
		
		trackRepositoryImpl.findTrackByTitleAndBandName(trackTitle,bandName);

		verify(typedQueryTrack).setParameter(eq("trackTitle"), eq(trackTitle));
		verify(typedQueryTrack).setParameter(eq("bandName"), eq(bandName));
		
		
	}

	@Test
	public void should_AlbumRepositoryFindTrackByTitleAndBandNameReturnTrack_WhenTrackExists() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city","any_country",1999);
		Band band = bandFactory("any_band", origin, List.of(genre));
		Album album = albumFactory("any_title",band, List.of(genre),LocalDate.parse("1999-09-09"), List.of(new Track()));
		
		Track track = trackFactory("any_title",band,album,null,LocalDate.parse("1999-09-09"),List.of(genre));

		when(entityManager.createQuery(FIND_TRACK_BY_TITLE_AND_BAND_NAME, Track.class)).thenReturn(typedQueryTrack);

		when(typedQueryTrack.getSingleResult()).thenReturn(track);

	

	 Track result = trackRepositoryImpl.findTrackByTitleAndBandName(anyString(),anyString());
		
	
		assertEquals(track.getTitle(), result.getTitle());
		assertEquals(track.getBand().getName(), result.getBand().getName());

	}
	@Test
	public void should_TrackRepositoryFindTrackByTitleAndBandNameThrowEntityNotFoundException_WhenTrackNotFound() {
		
		when(entityManager.createQuery(FIND_TRACK_BY_TITLE_AND_BAND_NAME, Track.class)).thenReturn(typedQueryTrack);

		when(typedQueryTrack.getSingleResult()).thenThrow(NoResultException.class);


	   assertThrows(EntityNotFoundException.class,()->trackRepositoryImpl.findTrackByTitleAndBandName(anyString(),anyString()) );

	}

}

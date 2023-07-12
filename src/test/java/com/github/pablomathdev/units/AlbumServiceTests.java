package com.github.pablomathdev.units;

import static com.github.pablomathdev.Factory.albumFactory;
import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.originFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.pablomathdev.application.services.AlbumService;
import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.domain.entities.Track;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.AlbumAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.AlbumNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.BandNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;
import com.github.pablomathdev.domain.repositories.IAlbumRepository;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.infraestructure.mappers.AlbumUpdateMapper;


@ExtendWith(MockitoExtension.class)
public class AlbumServiceTests {

	@Mock
	private IAlbumRepository albumRepository;

	@Mock
	private IBandRepository bandRepository;

	@Mock
	private IGenreRepository genreRepository;
	
	@Mock
	private AlbumUpdateMapper albumUpdateMapper;
	

	@InjectMocks
	private AlbumService albumService;

	@Test
	public void should_InvokeAlbumRepositoryExists_withCorrectArguments() {

		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		albumService.create(album);

		verify(albumRepository).exists(eq(album.getTitle()), eq(band.getName()));

	}

	@Test
	public void should_ThrowAlbumAlreadyExistsException_WhenAlbumAlreadyExists() {

		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		when(albumRepository.exists(album.getTitle(), album.getBand().getName())).thenReturn(true);

		assertThrows(AlbumAlreadyExistsException.class, () -> albumService.create(album));

	}

	@Test
	public void should_InvockAlbumRepositorySave_WithCorrectArguments() {

		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		albumService.create(album);

		Mockito.verify(albumRepository).save(eq(album));

	}

	@Test
	public void should_ReturnAlbum_WhenAlbumIsCreated() {

		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		when(albumRepository.save(any())).thenReturn(album);

		Album result = albumService.create(album);

		assertEquals(album.getTitle(), result.getTitle());

	}

	@Test
	public void should_ThrowBandNotFoundException_WhenbandNotExists() {

		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		when(albumRepository.exists(album.getTitle(), album.getBand().getName())).thenReturn(false);

		when(bandRepository.findByName(any())).thenThrow(BandNotFoundException.class);

		assertThrows(BandNotFoundException.class, () -> albumService.create(album));

	}

	@Test
	public void should_ThrowGenreNotFoundException_WhenGenreNotExists() {

		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		when(albumRepository.exists(album.getTitle(), album.getBand().getName())).thenReturn(false);

		when(genreRepository.findByName(any())).thenThrow(GenreNotFoundException.class);

		assertThrows(GenreNotFoundException.class, () -> albumService.create(album));

	}

	@Test
	public void should_ReturnResultListOfAlbums_WhenAlbumsExists() {

		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		when(albumRepository.findAll()).thenReturn(List.of(album));

		List<Album> result = albumService.findAll();

		assertFalse(result.isEmpty());

	}

	@Test
	public void should_ReturnResultListEmpty_WhenAlbumsNotExists() {

		when(albumRepository.findAll()).thenReturn(List.of());
		
	  List<Album> result = albumService.findAll();
				
	   assertTrue(result.isEmpty());

	}

	@Test
	public void should_InvokeAlbumRepositoryFindAlbumByTitleAndBandName_WithCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		when(albumRepository.findAlbumByTitleAndBandName(anyString(), anyString())).thenReturn(album);

		albumService.delete(album.getTitle(), band.getName());

		verify(albumRepository).delete(eq(album));

	}

	@Test
	public void should_ThrowAlbumNotFoundException_WhenAlbumNotFound() {
	
	
		when(albumRepository.findAlbumByTitleAndBandName(anyString(), anyString())).thenThrow(EntityNotFoundException.class);
		
	

		assertThrows(AlbumNotFoundException.class,()-> 	albumService.delete(anyString(),anyString()));
	

	}
	
	@Test
	public void should_InvokeAlbumRepositoryUpdate_WithCorrectArguments() {
		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		
		List<Genre> genres = new ArrayList<>();
		genres.add(genre);
		
		Album existingAlbum = albumFactory("any_album",band,genres,null, null);

		Album updateAlbum = albumFactory("update_album",band,genres,null, null);
		
		Integer id = 1;

		when(albumRepository.findById(id)).thenReturn(existingAlbum);
        when(albumUpdateMapper.map(any(),any())).thenReturn(updateAlbum);
		
		albumService.update(updateAlbum, id);

		verify(albumRepository).update(eq(updateAlbum));


	}

//	@Test
//	public void should_ReturnBandUpdated() {
//		Origin origin = originFactory("any_city", "any_country", 1999);
//		Genre genre = genreFactory("any_genre");
//		List<Genre> genres = new ArrayList<>();
//		genres.add(genre);
//		
//		
//		Band existingBand = bandFactory("any_band", origin, genres);
//
//		Band updateBand = bandFactory("update_band", origin, genres);
//		
//		Integer id = 1;
//
//		when(bandRepository.findById(id)).thenReturn(existingBand);
//        when(bandUpdateMapper.map(any(),any())).thenReturn(updateBand);
//		when(bandRepository.update(any())).thenReturn(updateBand);
//        
//		Band updatedBand = bandService.update(updateBand, id);
//
//		
//		assertEquals(updatedBand.getName(), updateBand.getName());
//		
//	}

}

package com.github.pablomathdev.units;

import static com.github.pablomathdev.Factory.albumFactory;
import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.originFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.BandNotFoundException;
import com.github.pablomathdev.domain.repositories.IAlbumRepository;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTests {

	@Mock
	private IAlbumRepository albumRepository;
	
	@Mock
	private IBandRepository bandRepository;
	
	@Mock
	private IGenreRepository genreRepository;

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

		when(albumRepository.exists(album.getTitle(),album.getBand().getName())).thenReturn(true);
		
		
		 assertThrows(AlbumAlreadyExistsException.class,()-> albumService.create(album)); 

	

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

		when(albumRepository.exists(album.getTitle(),album.getBand().getName())).thenReturn(false);
		
		when(bandRepository.findByName(any())).thenThrow(BandNotFoundException.class);
		
		
		 assertThrows(BandNotFoundException.class,()-> albumService.create(album)); 

	

	}


}

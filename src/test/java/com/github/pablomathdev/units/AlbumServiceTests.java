package com.github.pablomathdev.units;

import static com.github.pablomathdev.Factory.albumFactory;
import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.originFactory;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.pablomathdev.application.services.AlbumService;
import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.domain.entities.Track;
import com.github.pablomathdev.domain.repositories.IAlbumRepository;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTests {

	@Mock
	private IAlbumRepository albumRepository;

	@InjectMocks
	private AlbumService albumService;

	@Test
	public void should_InvokeTypedQueryExists_withCorrectArguments() {

		Genre genre = genreFactory("any_genre");
		Origin origin = originFactory("any_city", "any_country", 1999);
		Band band = bandFactory("any_name", origin, List.of(genre));
		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
				List.of(new Track()));

		albumService.create(album);

		verify(albumRepository).exists(eq(album.getTitle()), eq(album.getBand().getName()));

	}
//	@Test
//	public void should_InvokcAlbumRepositorySave_WithCorrectArguments() {
//
//		Genre genre = genreFactory("any_genre");
//		Origin origin = originFactory("any_city", "any_country", 1999);
//		Band band = bandFactory("any_name", origin, List.of(genre));
//		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
//				List.of(new Track()));
//
//		albumService.create(album);
//
//		Mockito.verify(albumRepository).save(eq(album));
//
//	}
//	@Test
//	public void should_InvokeAlbumRepositoryfindByName_withCorrectArguments() {
//		
//
//		Genre genre = genreFactory("any_genre");
//		Origin origin = originFactory("any_city", "any_country", 1999);
//		Band band = bandFactory("any_name", origin, List.of(genre));
//		Album album = albumFactory("any_title", band, List.of(genre), LocalDate.parse("1999-09-09"),
//				List.of(new Track()));
//
//		albumService.create(album);
//
//		Mockito.verify(albumRepository).findByName(eq(album.getTitle()));
//	}

}

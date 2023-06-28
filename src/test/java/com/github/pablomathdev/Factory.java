package com.github.pablomathdev;

import java.time.LocalDate;
import java.util.List;

import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.domain.entities.Single;
import com.github.pablomathdev.domain.entities.Track;

public class Factory {

	public static Band bandFactory(String name, Origin origin, List<Genre> genres) {
		Band band = new Band();
		band.setName(name);
		band.setOrigin(origin);
		band.setGenres(genres);
		return band;

	}

	public static Origin originFactory(String city, String country, Integer formationYear) {
		Origin origin = new Origin();
		origin.setCity(city);
		origin.setCountry(country);
		origin.setFormationYear(formationYear);
		return origin;

	}

	public static Genre genreFactory(String name) {
		Genre genre = new Genre();
		genre.setName(name);
		return genre;
	}

	public static Album albumFactory(String title, Band band, List<Genre> genres, LocalDate releaseDate,
			List<Track> tracks) {
		Album album = new Album();
		album.setTitle(title);
		album.setBand(band);
		album.setGenres(genres);
		album.setReleaseDate(releaseDate);
		album.setTracks(tracks);

		return album;

	}

	public static Track trackFactory(String title, Album album, Single single, LocalDate releaseDate,
			List<Genre> genres) {
		Track track = new Track();
		track.setTitle(title);
		track.setAlbum(album);
		track.setGenres(genres);
		track.setReleaseDate(releaseDate);
		track.setSingle(single);

		return track;
	}
}

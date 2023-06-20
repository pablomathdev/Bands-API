package com.github.pablomathdev;

import java.util.List;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;

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
}

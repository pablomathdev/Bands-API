package com.github.pablomathdev.application;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.AlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.BandAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.BusinessException;
import com.github.pablomathdev.domain.exceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.GenreNotFoundByNameException;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.services.ICreateService;

import lombok.Setter;

@Setter
@Service
public class CreateBandService implements ICreateService<Band> {

	@Autowired
	IBandRepository bandRepository;

	@Autowired
	IGenreRepository genreRepository;

	@Override
	public Band execute(Band band) {

		Set<Genre> genres = new HashSet<>();

		try {

			band.getGenres().forEach((g) -> {

				Genre genre = genreRepository.findByName(g.getName());

				
					genres.add(genre);
				
			});
			 return bandRepository.save(band);

		} catch (EntityNotFoundException e) {
			throw new GenreNotFoundByNameException(e.getMessage());
		}catch (AlreadyExistsException e) {
			throw new BandAlreadyExistsException(e);
		}catch (EntitySaveException e) {
			throw new BusinessException(e.getMessage());
		}

	}

}
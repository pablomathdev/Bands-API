package com.github.pablomathdev.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.repositories.BandRepository;
import com.github.pablomathdev.domain.services.CreateService;

@Service
public class CreateBandService implements CreateService<Band> {

	@Autowired
	BandRepository bandRepository;

	@Override
	public Band execute(Band entity) {

		 bandRepository.save(entity);
		return null;
	}

}

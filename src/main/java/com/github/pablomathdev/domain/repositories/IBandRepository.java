package com.github.pablomathdev.domain.repositories;

import com.github.pablomathdev.domain.entities.Band;

public interface IBandRepository extends IRepository<Band, Integer> {

	boolean exists(String name);
}

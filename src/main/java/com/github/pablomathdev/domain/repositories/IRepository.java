package com.github.pablomathdev.domain.repositories;

import java.util.Optional;

public interface IRepository<T,ID> {

	Optional<T> save(T object);
	Optional<T> findByName(String name);
}

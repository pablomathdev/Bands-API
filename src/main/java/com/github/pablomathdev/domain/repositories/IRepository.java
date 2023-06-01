package com.github.pablomathdev.domain.repositories;

import java.util.Optional;

public interface IRepository<T,ID> {

	T save(T object);
	Optional<T> findById(ID id );
}

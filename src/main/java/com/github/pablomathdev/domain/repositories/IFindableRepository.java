package com.github.pablomathdev.domain.repositories;

import java.util.Optional;

public interface IFindableRepository<T, ID> {
	Optional<T> findById(ID id);
}

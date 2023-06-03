package com.github.pablomathdev.domain.repositories;

public interface IRepository<T,ID> {

	T save(T object);
	T findByName(String name);
}

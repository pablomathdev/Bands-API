package com.github.pablomathdev.domain.repositories;

import java.util.List;

public interface IRepository<T,ID> {
	List<T> findAll();
	T save(T object);
	void delete(T object);
	T update(T object);
	T findById(ID id);
	
}

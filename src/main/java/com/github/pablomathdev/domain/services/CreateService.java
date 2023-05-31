package com.github.pablomathdev.domain.services;

public interface CreateService<T> {

	T execute(T entity);
	
}

package com.fastfoot.service;

import java.util.List;

public interface CRUDService<T, S> {
	
	public List<T> getAll();
	
	public T getById(S id);
	
	public void delete(S id);
	
	public void deleteAll();
	
	public T create(T t);
	
	public T update(T t);

}

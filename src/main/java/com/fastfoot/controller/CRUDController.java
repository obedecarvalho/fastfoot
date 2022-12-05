package com.fastfoot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface CRUDController<T, S> {
	
	public ResponseEntity<T> create(T t);//POST
	
	public ResponseEntity<List<T>> getAll();//GET
	
	public ResponseEntity<T> getById(S id);//GET
	
	public ResponseEntity<T> update(S id, T t);//PUT
	
	public ResponseEntity<HttpStatus> delete(S id);//DELETE
	
	public ResponseEntity<HttpStatus> deleteAll();//DELETE

}

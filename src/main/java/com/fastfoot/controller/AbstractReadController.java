package com.fastfoot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface AbstractReadController<T, S> {
	
	public ResponseEntity<List<T>> getAll();//GET
	
	public ResponseEntity<T> getById(S id);//GET

}

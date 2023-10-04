package com.fastfoot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface AbstractReadJogavelController<T, S> {

	public ResponseEntity<List<T>> getAll(Long idJogo);//GET
	
	public ResponseEntity<T> getById(S id);//GET
}

package com.fastfoot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.model.entity.Jogo;
import com.fastfoot.service.CriarJogoService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CriarJogoController {
	
	@Autowired
	private CriarJogoService criarJogoService;

	@GetMapping("/criarNovoJogo")
	public ResponseEntity<Jogo> criarNovoJogo() {
		try {
			return ResponseEntity.ok(criarJogoService.criarJogo());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}

package com.fastfoot.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.scheduler.model.dto.SemanaDTO;
import com.fastfoot.scheduler.service.JogarPartidasSemanaService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class JogarPartidasSemanaController {
	
	@Autowired
	private JogarPartidasSemanaService jogarPartidasSemanaService;
	
	@GetMapping("/jogarPartidasSemana")
	public ResponseEntity<SemanaDTO> jogarPartidasSemana() {
		try {
			return ResponseEntity.ok(jogarPartidasSemanaService.jogarPartidasSemana());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}

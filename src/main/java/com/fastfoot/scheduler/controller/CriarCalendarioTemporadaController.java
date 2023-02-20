package com.fastfoot.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.scheduler.model.dto.TemporadaDTO;
import com.fastfoot.scheduler.service.CriarCalendarioTemporadaService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CriarCalendarioTemporadaController {
	
	@Autowired
	private CriarCalendarioTemporadaService criarCalendarioTemporadaService;

	@GetMapping("/criarNovaTemporada")
	public ResponseEntity<TemporadaDTO> criarNovaTemporada() {
		try {
			return ResponseEntity.ok(criarCalendarioTemporadaService.criarTemporada());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}

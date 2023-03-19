package com.fastfoot.financial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.financial.service.CalcularClubeSaldoSemanaTodosClubesService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CalcularClubeSaldoSemanaController {
	
	@Autowired
	private CalcularClubeSaldoSemanaTodosClubesService calcularClubeSaldoSemanaTodosClubesService;
	
	@GetMapping("/calcularClubeSaldoSemana")
	public ResponseEntity<Boolean> calcularClubeSaldoSemana() {
		try {
			calcularClubeSaldoSemanaTodosClubesService.calcularClubeSaldoSemana();
			return ResponseEntity.ok(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}

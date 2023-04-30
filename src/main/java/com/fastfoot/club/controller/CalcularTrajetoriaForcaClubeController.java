package com.fastfoot.club.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.club.service.CalcularTrajetoriaForcaClubeTodosClubesService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CalcularTrajetoriaForcaClubeController {
	
	@Autowired
	private CalcularTrajetoriaForcaClubeTodosClubesService calcularTrajetoriaForcaClubeTodosClubesService;

	@GetMapping("/calcularTrajetoriaForcaClube")
	public ResponseEntity<Boolean> calcularTrajetoriaForcaClube() {
		try {
			calcularTrajetoriaForcaClubeTodosClubesService.calcularTrajetoriaForcaClube();
			return ResponseEntity.ok(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}
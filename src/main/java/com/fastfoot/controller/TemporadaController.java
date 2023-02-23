package com.fastfoot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.transfer.service.GerenciarTemporadaService;

@RestController
@CrossOrigin("*")
public class TemporadaController {

	/*@Autowired
	private JogarPartidasSemanaService jogarProximaSemanaService;*/
	
	/*@Autowired
	private CriarCalendarioTemporadaService criarCalendarioTemporadaService;*/
	
	@Autowired
	private GerenciarTemporadaService gerenciarTemporadaService;

	/*@GetMapping("/novaTemporada")
	public ResponseEntity<TemporadaDTO> criarTemporada() {
		try {
			return ResponseEntity.ok(criarCalendarioTemporadaService.criarTemporada());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}*/
	
	/*@Deprecated
	@GetMapping("/gerarTransferencias")
	public ResponseEntity<Boolean> gerarTransferencias() {
		try {
			gerenciarTemporadaService.gerarTransferencias();
			return ResponseEntity.ok(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}*/
	
	@GetMapping("/calcularClubeSaldoSemana")
	public ResponseEntity<Boolean> calcularClubeSaldoSemana() {
		try {
			gerenciarTemporadaService.calcularClubeSaldoSemana();
			return ResponseEntity.ok(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	/*@Deprecated
	@GetMapping("/gerarMudancaClubeNivel")
	public ResponseEntity<Boolean> gerarMudancaClubeNivel() {
		try {
			gerenciarTemporadaService.gerarMudancaClubeNivel();
			return ResponseEntity.ok(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}*/
	
	@GetMapping("/calcularTrajetoriaForcaClube")
	public ResponseEntity<Boolean> calcularTrajetoriaForcaClube() {
		try {
			gerenciarTemporadaService.calcularTrajetoriaForcaClube();
			return ResponseEntity.ok(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/*@GetMapping("/proximaSemana")
	public ResponseEntity<SemanaDTO> proximaSemana() {
		try {
			return ResponseEntity.ok(jogarProximaSemanaService.jogarPartidasSemana());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}*/

	/*@GetMapping("/jogarTemporadaCompleta")
	public ResponseEntity<SemanaDTO> jogarTemporadaCompleta() {
		try {
			SemanaDTO semana = null;

			do {
				semana = semanaService.proximaSemana();
			} while (semana.getNumero() < 25);

			return ResponseEntity.ok(semana);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}*/

}

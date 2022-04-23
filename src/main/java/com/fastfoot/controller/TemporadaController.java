package com.fastfoot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.scheduler.model.dto.CampeonatoDTO;
import com.fastfoot.scheduler.model.dto.SemanaDTO;
import com.fastfoot.scheduler.model.dto.TemporadaDTO;
import com.fastfoot.scheduler.service.SemanaService;
import com.fastfoot.scheduler.service.TemporadaService;



@RestController
@CrossOrigin("*")
public class TemporadaController {

	@Autowired
	private TemporadaService temporadaService;

	@Autowired
	private SemanaService semanaService;

	@GetMapping("/novaTemporada")
	public ResponseEntity<TemporadaDTO> criarTemporada() {
		try {
			return ResponseEntity.ok(temporadaService.criarTemporada());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/*@GetMapping("/classificarClubesTemporadaAtual")
	public ResponseEntity<Boolean> classificarClubesTemporadaAtual() {
		try {
			temporadaService.classificarClubesTemporadaAtual();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}*/

	@GetMapping("/proximaSemana")
	public ResponseEntity<SemanaDTO> proximaSemana() {//TODO: renomear 'jogarProximaSemana'
		try {
			return ResponseEntity.ok(semanaService.proximaSemana2());
		} catch (Exception e) {
			e.printStackTrace();//TODO
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/campeonatosTemporadaAtual")
	public ResponseEntity<List<CampeonatoDTO>> getCampeonatosTemporada(@RequestParam(name = "nivel") String nivel) {//'NACIONAL', 'COPA NACIONAL', 'CONTINENTAL'
		try {
			return ResponseEntity.ok(temporadaService.getCampeonatosTemporada(nivel));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/temporadas")
	public ResponseEntity<List<TemporadaDTO>> getTemporadas(){//TODO: renomear get***Itens??? //Olhar outros metodos como esse para padronizar nomes
		try {
			return ResponseEntity.ok(temporadaService.getTemporadas());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}

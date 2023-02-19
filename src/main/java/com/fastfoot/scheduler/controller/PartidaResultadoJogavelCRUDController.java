package com.fastfoot.scheduler.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.service.crud.PartidaResultadoJogavelCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PartidaResultadoJogavelCRUDController {
	
	@Autowired
	private PartidaResultadoJogavelCRUDService partidaResultadoJogavelCRUDService;

	@GetMapping("/partidasJogavel")
	public ResponseEntity<List<PartidaResultadoJogavel>> getAll() {
		try {

			List<PartidaResultadoJogavel> partidas = partidaResultadoJogavelCRUDService.getAll();

			if (ValidatorUtil.isEmpty(partidas)) {
				return ResponseEntity.noContent().build();
			}

			return ResponseEntity.ok(partidas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/partidasJogavel/clube/{id}")
	public ResponseEntity<List<PartidaResultadoJogavel>> getByClube(@PathVariable("id") Integer idClube,
			@RequestParam(name = "temporadaAtual", required = false) Boolean temporadaAtual) {
		try {
			
			List<PartidaResultadoJogavel> partidas = null;

			if (temporadaAtual != null && temporadaAtual) {
				partidas = partidaResultadoJogavelCRUDService.getByClubeAndTemporadaAtual(new Clube(idClube));
			} else {
				partidas = partidaResultadoJogavelCRUDService.getByClube(new Clube(idClube));
			}

			if (ValidatorUtil.isEmpty(partidas)) {
				return ResponseEntity.noContent().build();
			}

			return ResponseEntity.ok(partidas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/partidasJogavel/campeonato/{id}")
	public ResponseEntity<List<PartidaResultadoJogavel>> getByCampeonato(@PathVariable("id") Long idCampeonato) {
		try {

			List<PartidaResultadoJogavel> partidas = partidaResultadoJogavelCRUDService.getByIdCampeonato(idCampeonato);

			if (ValidatorUtil.isEmpty(partidas)) {
				return ResponseEntity.noContent().build();
			}

			return ResponseEntity.ok(partidas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/partidasJogavel/semana/{numero}")
	public ResponseEntity<List<PartidaResultadoJogavel>> getBySemana(@PathVariable("numero") Integer numeroSemana) {
		try {

			List<PartidaResultadoJogavel> partidas = partidaResultadoJogavelCRUDService.getByNumeroSemanaTemporadaAtual(numeroSemana);

			if (ValidatorUtil.isEmpty(partidas)) {
				return ResponseEntity.noContent().build();
			}

			return ResponseEntity.ok(partidas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

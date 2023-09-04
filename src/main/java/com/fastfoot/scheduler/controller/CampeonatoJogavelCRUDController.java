package com.fastfoot.scheduler.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.CampeonatoJogavel;
import com.fastfoot.scheduler.service.crud.CampeonatoJogavelCRUDService;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CampeonatoJogavelCRUDController {
	
	@Autowired
	private CampeonatoJogavelCRUDService campeonatoJogavelCRUDService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;

	@GetMapping("/campeonatosJogavel")
	public ResponseEntity<List<CampeonatoJogavel>> getAll() {
		try {

			List<CampeonatoJogavel> campeonatos = campeonatoJogavelCRUDService.getAll();

			if (ValidatorUtil.isEmpty(campeonatos)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(campeonatos);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/campeonatosJogavel/temporadaAtual")
	public ResponseEntity<List<CampeonatoJogavel>> getByTemporadaAtual(@RequestParam(name = "idJogo", required = true) Long idJogo) {
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}

			List<CampeonatoJogavel> campeonatos = campeonatoJogavelCRUDService.getByTemporadaAtual(jogo);

			if (ValidatorUtil.isEmpty(campeonatos)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(campeonatos);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/campeonatosJogavel/temporadaAtual/nacional")
	public ResponseEntity<List<CampeonatoJogavel>> getNacionalByTemporadaAtual(@RequestParam(name = "idJogo", required = true) Long idJogo) {
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}

			List<CampeonatoJogavel> campeonatos = campeonatoJogavelCRUDService.getNacionalByTemporadaAtual(jogo);

			if (ValidatorUtil.isEmpty(campeonatos)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(campeonatos);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/campeonatosJogavel/temporadaAtual/copaNacional")
	public ResponseEntity<List<CampeonatoJogavel>> getCopaNacionalByTemporadaAtual(@RequestParam(name = "idJogo", required = true) Long idJogo) {
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}

			List<CampeonatoJogavel> campeonatos = campeonatoJogavelCRUDService.getCopaNacionalByTemporadaAtual(jogo);

			if (ValidatorUtil.isEmpty(campeonatos)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(campeonatos);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/campeonatosJogavel/temporadaAtual/continental")
	public ResponseEntity<List<CampeonatoJogavel>> getContinentalByTemporadaAtual(@RequestParam(name = "idJogo", required = true) Long idJogo) {
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}

			List<CampeonatoJogavel> campeonatos = campeonatoJogavelCRUDService.getContinentalByTemporadaAtual(jogo);

			if (ValidatorUtil.isEmpty(campeonatos)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(campeonatos);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

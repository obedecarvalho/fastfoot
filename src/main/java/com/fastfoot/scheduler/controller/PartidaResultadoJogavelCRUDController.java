package com.fastfoot.scheduler.controller;

import java.util.Collections;
import java.util.Comparator;
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
import com.fastfoot.club.service.crud.ClubeCRUDService;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.service.crud.PartidaResultadoJogavelCRUDService;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PartidaResultadoJogavelCRUDController {
	
	@Autowired
	private PartidaResultadoJogavelCRUDService partidaResultadoJogavelCRUDService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;
	
	@Autowired
	private ClubeCRUDService clubeCRUDService;

	private Comparator<PartidaResultadoJogavel> COMPARATOR_SEMANA = new Comparator<PartidaResultadoJogavel>() {
		
		@Override
		public int compare(PartidaResultadoJogavel o1, PartidaResultadoJogavel o2) {
			return o1.getRodada().getSemana().getNumero().compareTo(o2.getRodada().getSemana().getNumero());
		}
	};
	
	private Comparator<PartidaResultadoJogavel> COMPARATOR_SEMANA_NIVEL_CAMP = new Comparator<PartidaResultadoJogavel>() {
		
		@Override
		public int compare(PartidaResultadoJogavel o1, PartidaResultadoJogavel o2) {
			int compare = o1.getRodada().getSemana().getNumero().compareTo(o2.getRodada().getSemana().getNumero());
			
			if (compare == 0) {
				compare = o1.getNivelCampeonato().compareTo(o2.getNivelCampeonato());
			}
			
			return compare;
		}
	};

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
	public ResponseEntity<List<PartidaResultadoJogavel>> getByClube(@PathVariable("id") Long idClube,
			@RequestParam(name = "temporadaAtual", required = false) Boolean temporadaAtual) {
		try {
			
			Clube clube = clubeCRUDService.getById(idClube);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.noContent().build();
			}
			
			List<PartidaResultadoJogavel> partidas = null;

			if (temporadaAtual != null && temporadaAtual) {
				partidas = partidaResultadoJogavelCRUDService.getByClubeAndTemporadaAtual(clube);
			} else {
				partidas = partidaResultadoJogavelCRUDService.getByClube(clube);
			}

			if (ValidatorUtil.isEmpty(partidas)) {
				return ResponseEntity.noContent().build();
			}
			
			Collections.sort(partidas, COMPARATOR_SEMANA);

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
			
			Collections.sort(partidas, COMPARATOR_SEMANA);

			return ResponseEntity.ok(partidas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/partidasJogavel/semana/{numero}")
	public ResponseEntity<List<PartidaResultadoJogavel>> getBySemana(@RequestParam(name = "idJogo", required = true) Long idJogo, @PathVariable("numero") Integer numeroSemana) {
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}

			List<PartidaResultadoJogavel> partidas = partidaResultadoJogavelCRUDService.getByNumeroSemanaTemporadaAtual(jogo, numeroSemana);

			if (ValidatorUtil.isEmpty(partidas)) {
				return ResponseEntity.noContent().build();
			}
			
			Collections.sort(partidas, COMPARATOR_SEMANA_NIVEL_CAMP);

			return ResponseEntity.ok(partidas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

package com.fastfoot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.scheduler.model.dto.PartidaResultadoDTO;
import com.fastfoot.scheduler.service.PartidaResultadoService;
import com.fastfoot.service.util.ValidatorUtil;

@Deprecated
@RestController
@CrossOrigin("*")
public class PartidaResultadoController {
	
	@Autowired
	private PartidaResultadoService partidaResultadoService;
	
	//@GetMapping("/partidas/campeonato/{idCampeonato}")
	@Deprecated
	public ResponseEntity<List<PartidaResultadoDTO>> getPartidasCampeonato(@PathVariable(name = "idCampeonato") Long idCampeonato, @RequestParam(name = "nivel") String nivel){
		List<PartidaResultadoDTO> partidas = partidaResultadoService.getPartidasPorCampeonato(idCampeonato, nivel);
		if (ValidatorUtil.isEmpty(partidas)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(partidas);
	}
	
	//@GetMapping("/partidas/clube/{idClube}")
	@Deprecated
	public ResponseEntity<List<PartidaResultadoDTO>> getPartidasClube(@PathVariable(name = "idClube") Integer idClube){
		List<PartidaResultadoDTO> partidas = partidaResultadoService.getPartidasPorClube(idClube);
		if (ValidatorUtil.isEmpty(partidas)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(partidas);
	}
	
	@Deprecated
	//@GetMapping("/partidas/semana/{numeroSemana}")
	public ResponseEntity<List<PartidaResultadoDTO>> getPartidasSemana(@PathVariable(name = "numeroSemana") Integer numeroSemana){
		List<PartidaResultadoDTO> partidas = partidaResultadoService.getPartidasPorSemana(numeroSemana);
		if (ValidatorUtil.isEmpty(partidas)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(partidas);
	}

	//@GetMapping("/partidas/amistosas/{numeroSemana}")
	@Deprecated
	public ResponseEntity<List<PartidaResultadoDTO>> getPartidasAmistosas(@PathVariable(name = "numeroSemana") Integer numeroSemana){
		List<PartidaResultadoDTO> partidas = partidaResultadoService.getPartidasAmistosasPorSemana(numeroSemana);
		if (ValidatorUtil.isEmpty(partidas)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(partidas);
	}
}

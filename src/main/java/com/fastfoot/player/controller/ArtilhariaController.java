package com.fastfoot.player.controller;

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
import com.fastfoot.player.model.dto.ArtilhariaDTO;
import com.fastfoot.player.service.ArtilhariaService;
import com.fastfoot.scheduler.service.TemporadaService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ArtilhariaController {
	
	@Autowired
	private ArtilhariaService artilhariaService;
	
	@Autowired
	private TemporadaService temporadaService;

	@GetMapping("/artilharia")
	public ResponseEntity<List<ArtilhariaDTO>> getAll(
			@RequestParam(name = "amistoso", required = false, defaultValue = "false") Boolean amistoso) {
		try {

			List<ArtilhariaDTO> estatisticas = artilhariaService.getAll(amistoso);
			
	
			if (ValidatorUtil.isEmpty(estatisticas)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(estatisticas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/artilharia/temporadaAtual")
	public ResponseEntity<List<ArtilhariaDTO>> getByTemporadaAtual(
			@RequestParam(name = "amistoso", required = false, defaultValue = "false") Boolean amistoso) {
		try {

			List<ArtilhariaDTO> estatisticas = artilhariaService.getByTemporada(temporadaService.getTemporadaAtual(),
					amistoso);
			
	
			if (ValidatorUtil.isEmpty(estatisticas)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(estatisticas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/artilharia/campeonato/{id}")
	public ResponseEntity<List<ArtilhariaDTO>> getByCampeonato(@PathVariable("id") Long idCampeonato){
		try {

			List<ArtilhariaDTO> estatisticas = artilhariaService.getByCampeonato(idCampeonato);
			
	
			if (ValidatorUtil.isEmpty(estatisticas)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(estatisticas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/artilharia/clube/{id}")
	public ResponseEntity<List<ArtilhariaDTO>> getByClube(@PathVariable("id") Integer idClube,
			@RequestParam(name = "amistoso", required = false, defaultValue = "false") Boolean amistoso) {
		try {

			List<ArtilhariaDTO> estatisticas = artilhariaService.getByClube(new Clube(idClube), amistoso);
			
	
			if (ValidatorUtil.isEmpty(estatisticas)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(estatisticas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
			
}

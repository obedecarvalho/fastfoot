package com.fastfoot.club.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.club.model.entity.TreinadorResumoTemporada;
import com.fastfoot.club.service.crud.TreinadorResumoTemporadaCRUDService;
import com.fastfoot.controller.AbstractReadJogavelController;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TreinadorResumoTemporadaReadController implements AbstractReadJogavelController<TreinadorResumoTemporada, Long> {

	@Autowired
	private TreinadorResumoTemporadaCRUDService treinadorResumoTemporadaCRUDService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;
	
	@Override
	@GetMapping("/treinadoresResumoTemporada")
	public ResponseEntity<List<TreinadorResumoTemporada>> getAll(@RequestParam(name = "idJogo", required = true) Long idJogo) {
		
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}
			
			List<TreinadorResumoTemporada> treinadorResumoTemporadas;
			
			treinadorResumoTemporadas = treinadorResumoTemporadaCRUDService.getByJogo(jogo);
	
			if (ValidatorUtil.isEmpty(treinadorResumoTemporadas)) {
				return ResponseEntity.noContent().build();
			}
			
			return ResponseEntity.ok(treinadorResumoTemporadas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/treinadoresResumoTemporada/{id}")
	public ResponseEntity<TreinadorResumoTemporada> getById(@PathVariable("id") Long id) {
		
		try {
		
			TreinadorResumoTemporada treinadorResumoTemporada = treinadorResumoTemporadaCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(treinadorResumoTemporada)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(treinadorResumoTemporada);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

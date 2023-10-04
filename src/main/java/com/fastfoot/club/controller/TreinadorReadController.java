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

import com.fastfoot.club.model.entity.Treinador;
import com.fastfoot.club.service.crud.TreinadorCRUDService;
import com.fastfoot.controller.AbstractReadJogavelController;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TreinadorReadController implements AbstractReadJogavelController<Treinador, Long> {
	
	@Autowired
	private TreinadorCRUDService treinadorCRUDService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;
	
	@Override
	@GetMapping("/treinadores")
	public ResponseEntity<List<Treinador>> getAll(@RequestParam(name = "idJogo", required = true) Long idJogo) {
		
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}
			
			List<Treinador> treinadores;
			
			treinadores = treinadorCRUDService.getByJogo(jogo);
	
			if (ValidatorUtil.isEmpty(treinadores)) {
				return ResponseEntity.noContent().build();
			}
			
			return ResponseEntity.ok(treinadores);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/treinadores/{id}")
	public ResponseEntity<Treinador> getById(@PathVariable("id") Long id) {
		
		try {
		
			Treinador treinador = treinadorCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(treinador)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(treinador);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

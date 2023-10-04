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

import com.fastfoot.club.model.entity.TreinadorTituloRanking;
import com.fastfoot.club.service.crud.TreinadorTituloRankingCRUDService;
import com.fastfoot.controller.AbstractReadJogavelController;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TreinadorTituloRankingReadController implements AbstractReadJogavelController<TreinadorTituloRanking, Long> {

	@Autowired
	private TreinadorTituloRankingCRUDService treinadorTituloRankingCRUDService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;
	
	@Override
	@GetMapping("/treinadoresTituloRanking")
	public ResponseEntity<List<TreinadorTituloRanking>> getAll(@RequestParam(name = "idJogo", required = true) Long idJogo) {
		
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}
			
			List<TreinadorTituloRanking> treinadorTituloRankings;
			
			treinadorTituloRankings = treinadorTituloRankingCRUDService.getByJogo(jogo);
	
			if (ValidatorUtil.isEmpty(treinadorTituloRankings)) {
				return ResponseEntity.noContent().build();
			}
			
			return ResponseEntity.ok(treinadorTituloRankings);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/treinadoresTituloRanking/{id}")
	public ResponseEntity<TreinadorTituloRanking> getById(@PathVariable("id") Long id) {
		
		try {
		
			TreinadorTituloRanking treinadorTituloRanking = treinadorTituloRankingCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(treinadorTituloRanking)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(treinadorTituloRanking);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

package com.fastfoot.club.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.service.crud.ClubeRankingCRUDService;
import com.fastfoot.controller.CRUDController;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.crud.TemporadaCRUDService;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ClubeRankingCRUDController implements CRUDController<ClubeRanking, Long> {
	
	@Autowired
	private ClubeRankingCRUDService clubeRankingCRUDService;
	
	@Autowired
	private TemporadaCRUDService temporadaCRUDService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;

	@Override
	@PostMapping("/clubeRankings")
	public ResponseEntity<ClubeRanking> create(@RequestBody ClubeRanking t) {
		
		try {
		
			ClubeRanking clube = clubeRankingCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<ClubeRanking>(clube, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}
	
	@Override
	public ResponseEntity<List<ClubeRanking>> getAll() {
		return null;
	}

	//@Override
	@GetMapping("/clubeRankings")
	public ResponseEntity<List<ClubeRanking>> getAll(
			@RequestParam(name = "idJogo", required = true) Long idJogo,
			@RequestParam(name = "liga", required = false) Integer liga,
			@RequestParam(name = "ano", required = false) Integer ano,
			@RequestParam(name = "idClube", required = false) Long idClube) {
		
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}
			
			List<ClubeRanking> clubes;
			
			if (!ValidatorUtil.isEmpty(liga)) {
				if (ValidatorUtil.isEmpty(ano)) {
					Temporada temporada = temporadaCRUDService.getTemporadaAtual(jogo);
					clubes = clubeRankingCRUDService.getByLigaAndAno(jogo, liga, temporada.getAno() - 1);
				} else {
					clubes = clubeRankingCRUDService.getByLigaAndAno(jogo, liga, ano);
				}
			} else if (!ValidatorUtil.isEmpty(idClube)) {
				clubes = clubeRankingCRUDService.getByClube(new Clube(idClube));//TODO: colocar em endpoint especifico
			} else {
				clubes = clubeRankingCRUDService.getByJogo(jogo);
			}
	
			if (ValidatorUtil.isEmpty(clubes)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(clubes);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/clubeRankings/{id}")
	public ResponseEntity<ClubeRanking> getById(@PathVariable("id") Long id) {
		
		try {
		
			ClubeRanking clube = clubeRankingCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(clube);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/clubeRankings/{id}")
	public ResponseEntity<ClubeRanking> update(@PathVariable("id") Long id, @RequestBody ClubeRanking t) {
		
		try {
		
			ClubeRanking clube = clubeRankingCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
			
			clube = clubeRankingCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(clube);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/clubeRankings/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			clubeRankingCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/clubeRankings")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			clubeRankingCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

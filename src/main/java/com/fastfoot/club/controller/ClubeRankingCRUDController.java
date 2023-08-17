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
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.crud.TemporadaCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ClubeRankingCRUDController implements CRUDController<ClubeRanking, Integer> {
	
	@Autowired
	private ClubeRankingCRUDService clubeRankingCRUDService;
	
	@Autowired
	private TemporadaCRUDService temporadaCRUDService;

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
			@RequestParam(name = "liga", required = false) Integer liga,
			@RequestParam(name = "ano", required = false) Integer ano,
			@RequestParam(name = "idClube", required = false) Integer idClube) {
		
		try {
			
			List<ClubeRanking> clubes;
			
			if (!ValidatorUtil.isEmpty(liga)) {
				if (ValidatorUtil.isEmpty(ano)) {
					Temporada temporada = temporadaCRUDService.getTemporadaAtual();
					clubes = clubeRankingCRUDService.getByLigaAndAno(liga, temporada.getAno() - 1);
				} else {
					clubes = clubeRankingCRUDService.getByLigaAndAno(liga, ano);
				}
			} else if (!ValidatorUtil.isEmpty(idClube)) {
				clubes = clubeRankingCRUDService.getByClube(new Clube(idClube));
			} else {
				clubes = clubeRankingCRUDService.getAll();
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
	public ResponseEntity<ClubeRanking> getById(@PathVariable("id") Integer id) {
		
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
	public ResponseEntity<ClubeRanking> update(@PathVariable("id") Integer id, @RequestBody ClubeRanking t) {
		
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
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Integer id) {
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

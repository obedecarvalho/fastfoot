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

import com.fastfoot.club.model.entity.ClubeTituloRanking;
import com.fastfoot.club.service.crud.ClubeTituloRankingCRUDService;
import com.fastfoot.controller.CRUDController;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ClubeTituloRankingCRUDController implements CRUDController<ClubeTituloRanking, Long> {
	
	@Autowired
	private ClubeTituloRankingCRUDService clubeTituloRankingCRUDService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;

	@Override
	@PostMapping("/clubeTituloRanking")
	public ResponseEntity<ClubeTituloRanking> create(@RequestBody ClubeTituloRanking t) {
		
		try {
		
			ClubeTituloRanking clube = clubeTituloRankingCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<ClubeTituloRanking>(clube, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}
	
	@Override
	public ResponseEntity<List<ClubeTituloRanking>> getAll() {
		return null;
	}

	//@Override
	@GetMapping("/clubeTituloRanking")
	public ResponseEntity<List<ClubeTituloRanking>> getAll(
			@RequestParam(name = "idJogo", required = true) Long idJogo,
			@RequestParam(name = "liga", required = false) Integer liga,
			@RequestParam(name = "ligaStr", required = false) String ligaStr) {
		
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}
			
			List<ClubeTituloRanking> clubes;
			
			if (!ValidatorUtil.isEmpty(liga)) {
				clubes = clubeTituloRankingCRUDService.getByLiga(jogo, liga);
			} else if (!ValidatorUtil.isEmpty(ligaStr)) {
				clubes = clubeTituloRankingCRUDService.getByLiga(jogo, ligaStr);
			} else {
				clubes = clubeTituloRankingCRUDService.getByJogo(jogo);
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
	@GetMapping("/clubeTituloRanking/{id}")
	public ResponseEntity<ClubeTituloRanking> getById(@PathVariable("id") Long id) {
		
		try {
		
			ClubeTituloRanking clube = clubeTituloRankingCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(clube);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/clubeTituloRanking/{id}")
	public ResponseEntity<ClubeTituloRanking> update(@PathVariable("id") Long id, @RequestBody ClubeTituloRanking t) {
		
		try {
		
			ClubeTituloRanking clube = clubeTituloRankingCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
			
			clube = clubeTituloRankingCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(clube);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/clubeTituloRanking/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			clubeTituloRankingCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/clubeTituloRanking")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			clubeTituloRankingCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

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
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ClubeTituloRankingCRUDController implements CRUDController<ClubeTituloRanking, Integer> {
	
	@Autowired
	private ClubeTituloRankingCRUDService clubeTituloRankingCRUDService;

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
		return null;//TODO
	}

	//@Override
	@GetMapping("/clubeTituloRanking")
	public ResponseEntity<List<ClubeTituloRanking>> getAll(@RequestParam(name = "liga", required = false) Integer liga,
			@RequestParam(name = "ligaStr", required = false) String ligaStr) {
		
		try {
			
			List<ClubeTituloRanking> clubes;
			
			if (!ValidatorUtil.isEmpty(liga)) {
				clubes = clubeTituloRankingCRUDService.getByLiga(liga);
			} else if (!ValidatorUtil.isEmpty(ligaStr)) {
				clubes = clubeTituloRankingCRUDService.getByLiga(ligaStr);
			} else {
				clubes = clubeTituloRankingCRUDService.getAll();
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
	public ResponseEntity<ClubeTituloRanking> getById(@PathVariable("id") Integer id) {
		
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
	public ResponseEntity<ClubeTituloRanking> update(@PathVariable("id") Integer id, @RequestBody ClubeTituloRanking t) {
		
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
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Integer id) {
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

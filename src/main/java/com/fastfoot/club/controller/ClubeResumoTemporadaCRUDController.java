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
import com.fastfoot.club.model.entity.ClubeResumoTemporada;
import com.fastfoot.club.service.crud.ClubeResumoTemporadaCRUDService;
import com.fastfoot.controller.CRUDController;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ClubeResumoTemporadaCRUDController implements CRUDController<ClubeResumoTemporada, Long> {

	@Autowired
	private ClubeResumoTemporadaCRUDService clubeResumoTemporadaCRUDService;

	@Override
	@PostMapping("/clubeResumoTemporada")
	public ResponseEntity<ClubeResumoTemporada> create(@RequestBody ClubeResumoTemporada t) {
		
		try {
		
			ClubeResumoTemporada clube = clubeResumoTemporadaCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<ClubeResumoTemporada>(clube, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}
	
	@Override
	public ResponseEntity<List<ClubeResumoTemporada>> getAll() {
		return null;
	}

	//@Override
	@GetMapping("/clubeResumoTemporada")
	public ResponseEntity<List<ClubeResumoTemporada>> getAll(@RequestParam(name = "idClube", required = false) Integer idClube) {
		
		try {
			
			List<ClubeResumoTemporada> clubes;
			
			if (ValidatorUtil.isEmpty(idClube)) {
				clubes = clubeResumoTemporadaCRUDService.getAll();
			} else {
				clubes = clubeResumoTemporadaCRUDService.getByClube(new Clube(idClube));
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
	@GetMapping("/clubeResumoTemporada/{id}")
	public ResponseEntity<ClubeResumoTemporada> getById(@PathVariable("id") Long id) {
		
		try {
		
			ClubeResumoTemporada clube = clubeResumoTemporadaCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(clube);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/clubeResumoTemporada/{id}")
	public ResponseEntity<ClubeResumoTemporada> update(@PathVariable("id") Long id, @RequestBody ClubeResumoTemporada t) {
		
		try {
		
			ClubeResumoTemporada clube = clubeResumoTemporadaCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
			
			clube = clubeResumoTemporadaCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(clube);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/clubeResumoTemporada/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			clubeResumoTemporadaCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/clubeResumoTemporada")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			clubeResumoTemporadaCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

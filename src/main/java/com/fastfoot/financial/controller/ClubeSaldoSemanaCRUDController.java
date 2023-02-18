package com.fastfoot.financial.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.controller.CRUDController;
import com.fastfoot.financial.model.entity.ClubeSaldoSemana;
import com.fastfoot.financial.service.crud.ClubeSaldoSemanaCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ClubeSaldoSemanaCRUDController implements CRUDController<ClubeSaldoSemana, Long> {

	@Autowired
	private ClubeSaldoSemanaCRUDService clubeSaldoSemanaCRUDService;

	@Override
	@PostMapping("/clubeSaldoSemana")
	public ResponseEntity<ClubeSaldoSemana> create(@RequestBody ClubeSaldoSemana t) {
		
		try {
		
			ClubeSaldoSemana clube = clubeSaldoSemanaCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<ClubeSaldoSemana>(clube, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}

	@Override
	@GetMapping("/clubeSaldoSemana")
	public ResponseEntity<List<ClubeSaldoSemana>> getAll() {
		
		try {
			
			List<ClubeSaldoSemana> clubes = clubeSaldoSemanaCRUDService.getAll();
			

	
			if (ValidatorUtil.isEmpty(clubes)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(clubes);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/clubeSaldoSemana/{id}")
	public ResponseEntity<ClubeSaldoSemana> getById(@PathVariable("id") Long id) {
		
		try {
		
			ClubeSaldoSemana clube = clubeSaldoSemanaCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(clube);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/clubeSaldoSemana/{id}")
	public ResponseEntity<ClubeSaldoSemana> update(@PathVariable("id") Long id, @RequestBody ClubeSaldoSemana t) {
		
		try {
		
			ClubeSaldoSemana clube = clubeSaldoSemanaCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
			
			clube = clubeSaldoSemanaCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(clube);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/clubeSaldoSemana/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			clubeSaldoSemanaCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/clubeSaldoSemana")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			clubeSaldoSemanaCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

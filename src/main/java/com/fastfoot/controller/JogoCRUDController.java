package com.fastfoot.controller;

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

import com.fastfoot.model.entity.Jogo;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class JogoCRUDController implements CRUDController<Jogo, Long>{
	
	@Autowired
	private JogoCRUDService jogoCRUDService;

	@Override
	@PostMapping("/jogos")
	public ResponseEntity<Jogo> create(@RequestBody Jogo t) {
		try {
			
			Jogo jogo = jogoCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<Jogo>(jogo, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/jogos")
	public ResponseEntity<List<Jogo>> getAll() {
		try {
			
			List<Jogo> jogos = jogoCRUDService.getAll();
	
			if (ValidatorUtil.isEmpty(jogos)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(jogos);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/jogos/{id}")
	public ResponseEntity<Jogo> getById(@PathVariable("id") Long id) {
		try {
			
			Jogo jogo = jogoCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(jogo);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/jogos/{id}")
	public ResponseEntity<Jogo> update(@PathVariable("id") Long id, @RequestBody Jogo t) {
		try {
			
			Jogo jogo = jogoCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.notFound().build();
			}
			
			jogo = jogoCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(jogo);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/jogos/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			jogoCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/jogos")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			jogoCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

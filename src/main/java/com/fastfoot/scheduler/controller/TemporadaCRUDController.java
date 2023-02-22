package com.fastfoot.scheduler.controller;

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
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.crud.TemporadaCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TemporadaCRUDController implements CRUDController<Temporada, Long> {

	@Autowired
	private TemporadaCRUDService temporadaCRUDService;

	@Override
	@PostMapping("/temporadas")
	public ResponseEntity<Temporada> create(@RequestBody Temporada t) {
		try {
			
			Temporada temporada = temporadaCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(temporada)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<Temporada>(temporada, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@Override
	@GetMapping("/temporadas")
	public ResponseEntity<List<Temporada>> getAll() {
		try {

			List<Temporada> temporadas = temporadaCRUDService.getAll();
			
			if (ValidatorUtil.isEmpty(temporadas)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(temporadas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/temporadas/temporadaAtual")
	public ResponseEntity<Temporada> getTemporadaAtual() {
		try {

			Temporada temporada = temporadaCRUDService.getTemporadaAtual();

			if (ValidatorUtil.isEmpty(temporada)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(temporada);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/temporadas/{id}")
	public ResponseEntity<Temporada> getById(@PathVariable("id") Long id) {
		try {
			
			Temporada temporada = temporadaCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(temporada)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(temporada);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/temporadas/{id}")
	public ResponseEntity<Temporada> update(@PathVariable("id") Long id, @RequestBody Temporada t) {
		try {
			
			Temporada temporada = temporadaCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(temporada)) {
				return ResponseEntity.notFound().build();
			}
			
			temporada = temporadaCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(temporada)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(temporada);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/temporadas/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			temporadaCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/temporadas")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			temporadaCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

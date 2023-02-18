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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.controller.CRUDController;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.service.crud.CampeonatoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CampeonatoCRUDController implements CRUDController<Campeonato, Long> {

	@Autowired
	private CampeonatoCRUDService campeonatoCRUDService;

	@Override
	@PostMapping("/campeonatos")
	public ResponseEntity<Campeonato> create(@RequestBody Campeonato t) {
		try {
			
			Campeonato campeonato = campeonatoCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(campeonato)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<Campeonato>(campeonato, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@Override
	public ResponseEntity<List<Campeonato>> getAll() {
		return null;
	}
	
	@GetMapping("/campeonatos")
	public ResponseEntity<List<Campeonato>> getAll(@RequestParam(name = "temporadaAtual", required = false) Boolean temporadaAtual) {
		try {

			List<Campeonato> campeonatos;
			
			if (temporadaAtual != null && temporadaAtual) {
				campeonatos = campeonatoCRUDService.getAllTemporadaAtual();
			} else {
				campeonatos = campeonatoCRUDService.getAll();
			}

			if (ValidatorUtil.isEmpty(campeonatos)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(campeonatos);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/campeonatos/{id}")
	public ResponseEntity<Campeonato> getById(@PathVariable("id") Long id) {
		try {
			
			Campeonato campeonato = campeonatoCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(campeonato)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(campeonato);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/campeonatos/{id}")
	public ResponseEntity<Campeonato> update(@PathVariable("id") Long id, @RequestBody Campeonato t) {
		try {
			
			Campeonato campeonato = campeonatoCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(campeonato)) {
				return ResponseEntity.notFound().build();
			}
			
			campeonato = campeonatoCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(campeonato)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(campeonato);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/campeonatos/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			campeonatoCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/campeonatos")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			campeonatoCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

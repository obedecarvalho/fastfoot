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
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.service.crud.ClassificacaoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ClassificacaoCRUDController implements CRUDController<Classificacao, Long> {

	@Autowired
	private ClassificacaoCRUDService classificacaoCRUDService;

	@Override
	@PostMapping("/classificacoes")
	public ResponseEntity<Classificacao> create(@RequestBody Classificacao t) {
		try {
			
			Classificacao classificacao = classificacaoCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(classificacao)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<Classificacao>(classificacao, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@Override
	public ResponseEntity<List<Classificacao>> getAll() {
		return null;
	}

	@GetMapping("/classificacoes")
	public ResponseEntity<List<Classificacao>> getAll(
			@RequestParam(name = "idCampeonato", required = false) Long idCampeonato) {
		try {
			
			List<Classificacao> classificacoes;

			if (ValidatorUtil.isEmpty(idCampeonato)) {
				classificacoes = classificacaoCRUDService.getAll();
			} else {
				classificacoes = classificacaoCRUDService.getByIdCampeonato(idCampeonato);
			}
	
			if (ValidatorUtil.isEmpty(classificacoes)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(classificacoes);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/classificacoes/{id}")
	public ResponseEntity<Classificacao> getById(@PathVariable("id") Long id) {
		try {
			
			Classificacao classificacao = classificacaoCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(classificacao)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(classificacao);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/classificacoes/{id}")
	public ResponseEntity<Classificacao> update(@PathVariable("id") Long id, @RequestBody Classificacao t) {
		try {
			
			Classificacao classificacao = classificacaoCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(classificacao)) {
				return ResponseEntity.notFound().build();
			}
			
			classificacao = classificacaoCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(classificacao)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(classificacao);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/classificacoes/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			classificacaoCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/classificacoes")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			classificacaoCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}



}

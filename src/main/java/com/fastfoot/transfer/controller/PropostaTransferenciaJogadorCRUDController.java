package com.fastfoot.transfer.controller;

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
import com.fastfoot.service.util.ValidatorUtil;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;
import com.fastfoot.transfer.service.crud.PropostaTransferenciaJogadorCRUDService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PropostaTransferenciaJogadorCRUDController implements CRUDController<PropostaTransferenciaJogador, Long> {
	
	@Autowired
	private PropostaTransferenciaJogadorCRUDService propostaTransferenciaJogadorCRUDService;

	@Override
	@PostMapping("/propostaTransferenciaJogador")
	public ResponseEntity<PropostaTransferenciaJogador> create(@RequestBody PropostaTransferenciaJogador t) {
		try {
			
			PropostaTransferenciaJogador propostaTransferenciaJogador = propostaTransferenciaJogadorCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(propostaTransferenciaJogador)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<PropostaTransferenciaJogador>(propostaTransferenciaJogador, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	public ResponseEntity<List<PropostaTransferenciaJogador>> getAll() {
		return null;
	}
	
	@GetMapping("/propostaTransferenciaJogador")
	public ResponseEntity<List<PropostaTransferenciaJogador>> getAll(@RequestParam(name = "propostaAceita", required = false) Boolean propostaAceita) {
		try {
			
			List<PropostaTransferenciaJogador> propostas;

			if (propostaAceita == null) {
				propostas = propostaTransferenciaJogadorCRUDService.getAll();
			} else {
				propostas = propostaTransferenciaJogadorCRUDService.getByPropostaAceita(propostaAceita);
			}
	
			if (ValidatorUtil.isEmpty(propostas)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(propostas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/propostaTransferenciaJogador/{id}")
	public ResponseEntity<PropostaTransferenciaJogador> getById(@PathVariable("id") Long id) {
		try {
			
			PropostaTransferenciaJogador propostaTransferenciaJogador = propostaTransferenciaJogadorCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(propostaTransferenciaJogador)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(propostaTransferenciaJogador);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/propostaTransferenciaJogador/{id}")
	public ResponseEntity<PropostaTransferenciaJogador> update(@PathVariable("id") Long id, @RequestBody PropostaTransferenciaJogador t) {
		try {
			
			PropostaTransferenciaJogador propostaTransferenciaJogador = propostaTransferenciaJogadorCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(propostaTransferenciaJogador)) {
				return ResponseEntity.notFound().build();
			}
			
			propostaTransferenciaJogador = propostaTransferenciaJogadorCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(propostaTransferenciaJogador)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(propostaTransferenciaJogador);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/propostaTransferenciaJogador/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			propostaTransferenciaJogadorCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/propostaTransferenciaJogador")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			propostaTransferenciaJogadorCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

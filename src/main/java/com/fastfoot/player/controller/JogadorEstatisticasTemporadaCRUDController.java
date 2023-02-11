package com.fastfoot.player.controller;

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
import com.fastfoot.controller.CRUDController;
import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;
import com.fastfoot.player.service.JogadorEstatisticasTemporadaCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class JogadorEstatisticasTemporadaCRUDController implements CRUDController<JogadorEstatisticasTemporada, Long> {

	@Autowired
	private JogadorEstatisticasTemporadaCRUDService jogadorEstatisticasTemporadaCRUDService;

	@Override
	@PostMapping("/jogadorEstatisticasTemporada")
	public ResponseEntity<JogadorEstatisticasTemporada> create(@RequestBody JogadorEstatisticasTemporada t) {
		try {
			
			JogadorEstatisticasTemporada jogadorEstatisticasTemporada = jogadorEstatisticasTemporadaCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(jogadorEstatisticasTemporada)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<JogadorEstatisticasTemporada>(jogadorEstatisticasTemporada, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@Override
	public ResponseEntity<List<JogadorEstatisticasTemporada>> getAll() {
		return null;//TODO
	}

	@GetMapping("/jogadorEstatisticasTemporada")
	public ResponseEntity<List<JogadorEstatisticasTemporada>> getAll(@RequestParam(name = "idClube", required = false) Integer idClube) {
		try {

			List<JogadorEstatisticasTemporada> estatisticas;
			
			if (idClube == null) {
				estatisticas = jogadorEstatisticasTemporadaCRUDService.getAll();
			} else {
				estatisticas = jogadorEstatisticasTemporadaCRUDService.getAgrupadoTemporadaAtualByClube(new Clube(idClube));
			}
	
			if (ValidatorUtil.isEmpty(estatisticas)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(estatisticas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/jogadorEstatisticasTemporada/{id}")
	public ResponseEntity<JogadorEstatisticasTemporada> getById(@PathVariable("id") Long id) {
		try {
			
			JogadorEstatisticasTemporada jogadorEstatisticasTemporada = jogadorEstatisticasTemporadaCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(jogadorEstatisticasTemporada)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(jogadorEstatisticasTemporada);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/jogadorEstatisticasTemporada/{id}")
	public ResponseEntity<JogadorEstatisticasTemporada> update(@PathVariable("id") Long id, @RequestBody JogadorEstatisticasTemporada t) {
		try {
			
			JogadorEstatisticasTemporada jogadorEstatisticasTemporada = jogadorEstatisticasTemporadaCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(jogadorEstatisticasTemporada)) {
				return ResponseEntity.notFound().build();
			}
			
			jogadorEstatisticasTemporada = jogadorEstatisticasTemporadaCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(jogadorEstatisticasTemporada)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(jogadorEstatisticasTemporada);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/jogadorEstatisticasTemporada/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			jogadorEstatisticasTemporadaCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/jogadorEstatisticasTemporada")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			jogadorEstatisticasTemporadaCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

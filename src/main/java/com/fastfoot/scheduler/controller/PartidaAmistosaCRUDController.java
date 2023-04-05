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
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.service.crud.PartidaAmistosaCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PartidaAmistosaCRUDController implements CRUDController<PartidaAmistosaResultado, Long> {

	@Autowired
	private PartidaAmistosaCRUDService partidaAmistosaCRUDService;

	@Override
	@PostMapping("/partidasAmistosas")
	public ResponseEntity<PartidaAmistosaResultado> create(@RequestBody PartidaAmistosaResultado t) {
		try {
			
			PartidaAmistosaResultado partida = partidaAmistosaCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(partida)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<PartidaAmistosaResultado>(partida, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}

	@Override
	public ResponseEntity<List<PartidaAmistosaResultado>> getAll() {
		return null;
	}

	@GetMapping("/partidasAmistosas")
	public ResponseEntity<List<PartidaAmistosaResultado>> getAll(
			@RequestParam(name = "idClube", required = false) Integer idClube,
			@RequestParam(name = "numeroSemana", required = false) Integer numeroSemana) {
		try {
			
			List<PartidaAmistosaResultado> partidas;
			
			if (!ValidatorUtil.isEmpty(idClube) || !ValidatorUtil.isEmpty(numeroSemana)) {
				partidas = partidaAmistosaCRUDService.getByIdClubeNumeroSemana(idClube, numeroSemana);
			} else {
				partidas = partidaAmistosaCRUDService.getAll();
			}

			if (ValidatorUtil.isEmpty(partidas)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(partidas);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/partidasAmistosas/{id}")
	public ResponseEntity<PartidaAmistosaResultado> getById(@PathVariable("id") Long id) {
		try {
			
			PartidaAmistosaResultado partida = partidaAmistosaCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(partida)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(partida);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/partidasAmistosas/{id}")
	public ResponseEntity<PartidaAmistosaResultado> update(@PathVariable("id") Long id, @RequestBody PartidaAmistosaResultado t) {
		try {
			
			PartidaAmistosaResultado partida = partidaAmistosaCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(partida)) {
				return ResponseEntity.notFound().build();
			}
			
			partida = partidaAmistosaCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(partida)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(partida);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/partidasAmistosas/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			partidaAmistosaCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/partidasAmistosas")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			partidaAmistosaCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

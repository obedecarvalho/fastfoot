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
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.service.PartidaCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PartidaCRUDController implements CRUDController<PartidaResultado, Long>{
	
	@Autowired
	private PartidaCRUDService partidaCRUDService;

	@Override
	@PostMapping("/partidas")
	public ResponseEntity<PartidaResultado> create(@RequestBody PartidaResultado t) {
		try {
			
			PartidaResultado partida = partidaCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(partida)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<PartidaResultado>(partida, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}
	
	@Override
	public ResponseEntity<List<PartidaResultado>> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@GetMapping("/partidas")
	public ResponseEntity<List<PartidaResultado>> getAll(
			@RequestParam(name = "idClube", required = false) Integer idClube,
			@RequestParam(name = "idCampeonato", required = false) Long idCampeonato,
			@RequestParam(name = "numeroSemana", required = false) Integer numeroSemana) {
		try {
			
			List<PartidaResultado> partidas;
			
			if (!ValidatorUtil.isEmpty(idClube) || !ValidatorUtil.isEmpty(idCampeonato)
					|| !ValidatorUtil.isEmpty(numeroSemana)) {
				partidas = partidaCRUDService.getByIdClubeIdCampeonatoNumeroSemana(idClube, idCampeonato, numeroSemana);
			} else {
				partidas = partidaCRUDService.getAll();
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
	@GetMapping("/partidas/{id}")
	public ResponseEntity<PartidaResultado> getById(@PathVariable("id") Long id) {
		try {
			
			PartidaResultado partida = partidaCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(partida)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(partida);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/partidas/{id}")
	public ResponseEntity<PartidaResultado> update(@PathVariable("id") Long id, @RequestBody PartidaResultado t) {
		try {
			
			PartidaResultado partida = partidaCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(partida)) {
				return ResponseEntity.notFound().build();
			}
			
			partida = partidaCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(partida)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(partida);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/partidas/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			partidaCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/partidas")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			partidaCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

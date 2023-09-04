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
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.service.crud.PartidaEliminatoriaCRUDService;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PartidaEliminatoriaCRUDController implements CRUDController<PartidaEliminatoriaResultado, Long>{
	
	@Autowired
	private PartidaEliminatoriaCRUDService partidaEliminatoriaCRUDService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;

	@Override
	@PostMapping("/partidasEliminatorias")
	public ResponseEntity<PartidaEliminatoriaResultado> create(@RequestBody PartidaEliminatoriaResultado t) {
		try {
			
			PartidaEliminatoriaResultado partida = partidaEliminatoriaCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(partida)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<PartidaEliminatoriaResultado>(partida, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}

	@Override
	public ResponseEntity<List<PartidaEliminatoriaResultado>> getAll() {
		return null;
	}

	@GetMapping("/partidasEliminatorias")
	public ResponseEntity<List<PartidaEliminatoriaResultado>> getAll(
			@RequestParam(name = "idJogo", required = true) Long idJogo,
			@RequestParam(name = "idClube", required = false) Long idClube,
			@RequestParam(name = "idCampeonato", required = false) Long idCampeonato,
			@RequestParam(name = "numeroSemana", required = false) Integer numeroSemana) {
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}
			
			List<PartidaEliminatoriaResultado> partidas;
			
			if (!ValidatorUtil.isEmpty(idClube) || !ValidatorUtil.isEmpty(idCampeonato)
					|| !ValidatorUtil.isEmpty(numeroSemana)) {
				partidas = partidaEliminatoriaCRUDService.getByIdClubeIdCampeonatoNumeroSemana(jogo, idClube, idCampeonato, numeroSemana);
			} else {
				partidas = partidaEliminatoriaCRUDService.getAll();
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
	@GetMapping("/partidasEliminatorias/{id}")
	public ResponseEntity<PartidaEliminatoriaResultado> getById(@PathVariable("id") Long id) {
		try {
			
			PartidaEliminatoriaResultado partida = partidaEliminatoriaCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(partida)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(partida);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/partidasEliminatorias/{id}")
	public ResponseEntity<PartidaEliminatoriaResultado> update(@PathVariable("id") Long id, @RequestBody PartidaEliminatoriaResultado t) {
		try {
			
			PartidaEliminatoriaResultado partida = partidaEliminatoriaCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(partida)) {
				return ResponseEntity.notFound().build();
			}
			
			partida = partidaEliminatoriaCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(partida)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(partida);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/partidasEliminatorias/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			partidaEliminatoriaCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/partidasEliminatorias")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			partidaEliminatoriaCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

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
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.service.crud.CampeonatoEliminatorioCRUDService;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CampeonatoEliminatorioCRUDController implements CRUDController<CampeonatoEliminatorio, Long> {

	@Autowired
	private CampeonatoEliminatorioCRUDService campeonatoEliminatorioCRUDService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;

	@Override
	@PostMapping("/campeonatosEliminatorios")
	public ResponseEntity<CampeonatoEliminatorio> create(@RequestBody CampeonatoEliminatorio t) {
		try {
			
			CampeonatoEliminatorio campeonato = campeonatoEliminatorioCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(campeonato)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<CampeonatoEliminatorio>(campeonato, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@Override
	public ResponseEntity<List<CampeonatoEliminatorio>> getAll() {
		return null;
	}
	
	@GetMapping("/campeonatosEliminatorios")
	public ResponseEntity<List<CampeonatoEliminatorio>> getAll(
			@RequestParam(name = "idJogo", required = true) Long idJogo,
			@RequestParam(name = "temporadaAtual", required = false) Boolean temporadaAtual) {
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}

			List<CampeonatoEliminatorio> campeonatos;
			
			if (temporadaAtual != null && temporadaAtual) {
				campeonatos = campeonatoEliminatorioCRUDService.getAllTemporadaAtual(jogo);
			} else {
				campeonatos = campeonatoEliminatorioCRUDService.getAll();
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
	@GetMapping("/campeonatosEliminatorios/{id}")
	public ResponseEntity<CampeonatoEliminatorio> getById(@PathVariable("id") Long id) {
		try {
			
			CampeonatoEliminatorio campeonato = campeonatoEliminatorioCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(campeonato)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(campeonato);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/campeonatosEliminatorios/{id}")
	public ResponseEntity<CampeonatoEliminatorio> update(@PathVariable("id") Long id, @RequestBody CampeonatoEliminatorio t) {
		try {
			
			CampeonatoEliminatorio campeonato = campeonatoEliminatorioCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(campeonato)) {
				return ResponseEntity.notFound().build();
			}
			
			campeonato = campeonatoEliminatorioCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(campeonato)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(campeonato);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/campeonatosEliminatorios/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			campeonatoEliminatorioCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/campeonatosEliminatorios")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			campeonatoEliminatorioCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

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
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.service.crud.CampeonatoMistoCRUDService;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CampeonatoMistoCRUDController implements CRUDController<CampeonatoMisto, Long> {

	@Autowired
	private CampeonatoMistoCRUDService campeonatoMistoCRUDService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;

	@Override
	@PostMapping("/campeonatosMistos")
	public ResponseEntity<CampeonatoMisto> create(@RequestBody CampeonatoMisto t) {
		try {
			
			CampeonatoMisto campeonato = campeonatoMistoCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(campeonato)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<CampeonatoMisto>(campeonato, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@Override
	public ResponseEntity<List<CampeonatoMisto>> getAll() {
		return null;
	}

	@GetMapping("/campeonatosMistos")
	public ResponseEntity<List<CampeonatoMisto>> getAll(
			@RequestParam(name = "idJogo", required = true) Long idJogo,
			@RequestParam(name = "temporadaAtual", required = false) Boolean temporadaAtual) {
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}

			List<CampeonatoMisto> campeonatos;
			
			if (temporadaAtual != null && temporadaAtual) {
				campeonatos = campeonatoMistoCRUDService.getAllTemporadaAtual(jogo);
			} else {
				campeonatos = campeonatoMistoCRUDService.getAll();
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
	@GetMapping("/campeonatosMistos/{id}")
	public ResponseEntity<CampeonatoMisto> getById(@PathVariable("id") Long id) {
		try {
			
			CampeonatoMisto campeonato = campeonatoMistoCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(campeonato)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(campeonato);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/campeonatosMistos/{id}")
	public ResponseEntity<CampeonatoMisto> update(@PathVariable("id") Long id, @RequestBody CampeonatoMisto t) {
		try {
			
			CampeonatoMisto campeonato = campeonatoMistoCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(campeonato)) {
				return ResponseEntity.notFound().build();
			}
			
			campeonato = campeonatoMistoCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(campeonato)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(campeonato);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/campeonatosMistos/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			campeonatoMistoCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/campeonatosMistos")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			campeonatoMistoCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

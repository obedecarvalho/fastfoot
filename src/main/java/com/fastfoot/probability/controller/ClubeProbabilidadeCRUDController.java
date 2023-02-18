package com.fastfoot.probability.controller;

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
import com.fastfoot.probability.model.entity.ClubeProbabilidade;
import com.fastfoot.probability.service.crud.ClubeProbabilidadeCRUDService;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ClubeProbabilidadeCRUDController implements CRUDController<ClubeProbabilidade, Long> {

	@Autowired
	private ClubeProbabilidadeCRUDService clubeCRUDService;

	@Override
	@PostMapping("/clubesProbabilidade")
	public ResponseEntity<ClubeProbabilidade> create(@RequestBody ClubeProbabilidade t) {
		
		try {
		
			ClubeProbabilidade clube = clubeCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<ClubeProbabilidade>(clube, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}
	
	@Override
	@GetMapping("/clubesProbabilidade")
	public ResponseEntity<List<ClubeProbabilidade>> getAll() {
		
		try {
			
			List<ClubeProbabilidade> clubes = clubeCRUDService.getAll();
			
			if (ValidatorUtil.isEmpty(clubes)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(clubes);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/clubesProbabilidade/semanaAtual")
	public ResponseEntity<List<ClubeProbabilidade>> getBySemanaAtual(
			@RequestParam(name = "idCampeonato", required = true) Long idCampeonato,
			@RequestParam(name = "comClassificacao", required = false) Boolean comClassificacao) {
		
		try {
			
			List<ClubeProbabilidade> clubes = null;
			
			if (comClassificacao != null && comClassificacao) {
				clubes = clubeCRUDService.getByCampeonatoAndSemanaAtualComClassificacao(new Campeonato(idCampeonato));
			} else {
				clubes = clubeCRUDService.getByCampeonatoAndSemanaAtual(new Campeonato(idCampeonato));
			}

			if (ValidatorUtil.isEmpty(clubes)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(clubes);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/clubesProbabilidade/{id}")
	public ResponseEntity<ClubeProbabilidade> getById(@PathVariable("id") Long id) {
		
		try {
		
			ClubeProbabilidade clube = clubeCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(clube);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/clubesProbabilidade/{id}")
	public ResponseEntity<ClubeProbabilidade> update(@PathVariable("id") Long id, @RequestBody ClubeProbabilidade t) {
		
		try {
		
			ClubeProbabilidade clube = clubeCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
			
			clube = clubeCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(clube);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/clubesProbabilidade/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			clubeCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/clubesProbabilidade")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			clubeCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

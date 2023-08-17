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
import com.fastfoot.probability.model.entity.CampeonatoClubeProbabilidade;
import com.fastfoot.probability.service.crud.CampeonatoClubeProbabilidadeCRUDService;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CampeonatoClubeProbabilidadeCRUDController implements CRUDController<CampeonatoClubeProbabilidade, Long> {

	@Autowired
	private CampeonatoClubeProbabilidadeCRUDService campeonatoClubeProbabilidadeCRUDService;

	@Override
	@PostMapping("/campeonatoClubeProbabilidades")
	public ResponseEntity<CampeonatoClubeProbabilidade> create(@RequestBody CampeonatoClubeProbabilidade t) {
		
		try {
		
			CampeonatoClubeProbabilidade clube = campeonatoClubeProbabilidadeCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<CampeonatoClubeProbabilidade>(clube, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}
	
	@Override
	@GetMapping("/campeonatoClubeProbabilidades")
	public ResponseEntity<List<CampeonatoClubeProbabilidade>> getAll() {
		
		try {
			
			List<CampeonatoClubeProbabilidade> clubes = campeonatoClubeProbabilidadeCRUDService.getAll();
			
			if (ValidatorUtil.isEmpty(clubes)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(clubes);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/campeonatoClubeProbabilidades/rodadaAtual")
	public ResponseEntity<List<CampeonatoClubeProbabilidade>> getBySemanaAtual(
			@RequestParam(name = "idCampeonato", required = true) Long idCampeonato,
			@RequestParam(name = "comClassificacao", required = false) Boolean comClassificacao) {
		
		try {
			
			List<CampeonatoClubeProbabilidade> clubes = null;
			
			if (comClassificacao != null && comClassificacao) {
				clubes = campeonatoClubeProbabilidadeCRUDService.getByCampeonatoAndRodadaAtualComClassificacao(new Campeonato(idCampeonato));
			} else {
				clubes = campeonatoClubeProbabilidadeCRUDService.getByCampeonatoRodadaAtual(new Campeonato(idCampeonato));
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
	@GetMapping("/campeonatoClubeProbabilidades/{id}")
	public ResponseEntity<CampeonatoClubeProbabilidade> getById(@PathVariable("id") Long id) {
		
		try {
		
			CampeonatoClubeProbabilidade clube = campeonatoClubeProbabilidadeCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(clube);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/campeonatoClubeProbabilidades/{id}")
	public ResponseEntity<CampeonatoClubeProbabilidade> update(@PathVariable("id") Long id, @RequestBody CampeonatoClubeProbabilidade t) {
		
		try {
		
			CampeonatoClubeProbabilidade clube = campeonatoClubeProbabilidadeCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
			
			clube = campeonatoClubeProbabilidadeCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(clube);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/campeonatoClubeProbabilidades/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			campeonatoClubeProbabilidadeCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/campeonatoClubeProbabilidades")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			campeonatoClubeProbabilidadeCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

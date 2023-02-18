package com.fastfoot.match.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.controller.CRUDController;
import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.match.service.crud.PartidaEstatisticasCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PartidaEstatisticasCRUDController implements CRUDController<PartidaEstatisticas, Long> {

	@Autowired
	private PartidaEstatisticasCRUDService partidaEstatisticasCRUDService;

	@Override
	@PostMapping("/partidaEstatisticas")
	public ResponseEntity<PartidaEstatisticas> create(@RequestBody PartidaEstatisticas t) {
		
		try {
		
			PartidaEstatisticas clube = partidaEstatisticasCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<PartidaEstatisticas>(clube, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}

	@Override
	@GetMapping("/partidaEstatisticas")
	public ResponseEntity<List<PartidaEstatisticas>> getAll() {
		
		try {
			
			List<PartidaEstatisticas> clubes = partidaEstatisticasCRUDService.getAll();
			

	
			if (ValidatorUtil.isEmpty(clubes)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(clubes);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/partidaEstatisticas/{id}")
	public ResponseEntity<PartidaEstatisticas> getById(@PathVariable("id") Long id) {
		
		try {
		
			PartidaEstatisticas clube = partidaEstatisticasCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(clube);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/partidaEstatisticas/{id}")
	public ResponseEntity<PartidaEstatisticas> update(@PathVariable("id") Long id, @RequestBody PartidaEstatisticas t) {
		
		try {
		
			PartidaEstatisticas clube = partidaEstatisticasCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
			
			clube = partidaEstatisticasCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(clube);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/partidaEstatisticas/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			partidaEstatisticasCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/partidaEstatisticas")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			partidaEstatisticasCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

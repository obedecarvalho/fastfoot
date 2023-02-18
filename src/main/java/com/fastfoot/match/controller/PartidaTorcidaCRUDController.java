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
import com.fastfoot.match.model.entity.PartidaTorcida;
import com.fastfoot.match.service.crud.PartidaTorcidaCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PartidaTorcidaCRUDController implements CRUDController<PartidaTorcida, Long> {

	@Autowired
	private PartidaTorcidaCRUDService partidaTorcidaCRUDService;

	@Override
	@PostMapping("/partidaTorcida")
	public ResponseEntity<PartidaTorcida> create(@RequestBody PartidaTorcida t) {
		
		try {
		
			PartidaTorcida clube = partidaTorcidaCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<PartidaTorcida>(clube, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}

	@Override
	@GetMapping("/partidaTorcida")
	public ResponseEntity<List<PartidaTorcida>> getAll() {
		
		try {
			
			List<PartidaTorcida> clubes = partidaTorcidaCRUDService.getAll();
			

	
			if (ValidatorUtil.isEmpty(clubes)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(clubes);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/partidaTorcida/{id}")
	public ResponseEntity<PartidaTorcida> getById(@PathVariable("id") Long id) {
		
		try {
		
			PartidaTorcida clube = partidaTorcidaCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(clube);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/partidaTorcida/{id}")
	public ResponseEntity<PartidaTorcida> update(@PathVariable("id") Long id, @RequestBody PartidaTorcida t) {
		
		try {
		
			PartidaTorcida clube = partidaTorcidaCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
			
			clube = partidaTorcidaCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(clube);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/partidaTorcida/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			partidaTorcidaCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/partidaTorcida")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			partidaTorcidaCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

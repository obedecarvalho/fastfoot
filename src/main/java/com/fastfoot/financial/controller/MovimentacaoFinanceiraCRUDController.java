package com.fastfoot.financial.controller;

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
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.service.crud.MovimentacaoFinanceiraCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class MovimentacaoFinanceiraCRUDController implements CRUDController<MovimentacaoFinanceira, Long> {

	@Autowired
	private MovimentacaoFinanceiraCRUDService movimentacaoFinanceiraCRUDService;

	@Override
	@PostMapping("/movimentacaoFinanceira")
	public ResponseEntity<MovimentacaoFinanceira> create(@RequestBody MovimentacaoFinanceira t) {
		
		try {
		
			MovimentacaoFinanceira clube = movimentacaoFinanceiraCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<MovimentacaoFinanceira>(clube, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}

	@Override
	@GetMapping("/movimentacaoFinanceira")
	public ResponseEntity<List<MovimentacaoFinanceira>> getAll() {
		
		try {
			
			List<MovimentacaoFinanceira> clubes = movimentacaoFinanceiraCRUDService.getAll();
			
			if (ValidatorUtil.isEmpty(clubes)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(clubes);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/movimentacaoFinanceira/{id}")
	public ResponseEntity<MovimentacaoFinanceira> getById(@PathVariable("id") Long id) {
		
		try {
		
			MovimentacaoFinanceira clube = movimentacaoFinanceiraCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(clube);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/movimentacaoFinanceira/{id}")
	public ResponseEntity<MovimentacaoFinanceira> update(@PathVariable("id") Long id, @RequestBody MovimentacaoFinanceira t) {
		
		try {
		
			MovimentacaoFinanceira clube = movimentacaoFinanceiraCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
			
			clube = movimentacaoFinanceiraCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(clube);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/movimentacaoFinanceira/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			movimentacaoFinanceiraCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/movimentacaoFinanceira")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			movimentacaoFinanceiraCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

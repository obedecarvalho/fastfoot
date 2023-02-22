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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.controller.CRUDController;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.service.crud.EscalacaoClubeCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class EscalacaoClubeCRUDController implements CRUDController<EscalacaoClube, Long> {
	
	@Autowired
	private EscalacaoClubeCRUDService escalacaoClubePartidaCRUDService;

	@Override
	@PostMapping("/escalacaoClube")
	public ResponseEntity<EscalacaoClube> create(@RequestBody EscalacaoClube t) {
		try {
			
			EscalacaoClube jogador = escalacaoClubePartidaCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(jogador)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<EscalacaoClube>(jogador, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@Override
	public ResponseEntity<List<EscalacaoClube>> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@GetMapping("/escalacaoClube")
	public ResponseEntity<List<EscalacaoClube>> getAll(@RequestParam(name = "idClube", required = false) Integer idClube) {
		try {

			List<EscalacaoClube> jogadores;

			if (ValidatorUtil.isEmpty(idClube)) {
				jogadores = escalacaoClubePartidaCRUDService.getAll();
			} else {
				jogadores = escalacaoClubePartidaCRUDService.getByClube(new Clube(idClube));
			}
	
			if (ValidatorUtil.isEmpty(jogadores)) {
				return ResponseEntity.noContent().build();
			}
	
			return ResponseEntity.ok(jogadores);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/escalacaoClube/{id}")
	public ResponseEntity<EscalacaoClube> getById(@PathVariable("id") Long id) {
		
		try {
			
			EscalacaoClube jogador = escalacaoClubePartidaCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(jogador)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(jogador);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
		
		
	}

	@Override
	@PutMapping("/escalacaoClube/{id}")
	public ResponseEntity<EscalacaoClube> update(@PathVariable("id") Long id, @RequestBody EscalacaoClube t) {
		try {
			
			EscalacaoClube jogador = escalacaoClubePartidaCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(jogador)) {
				return ResponseEntity.notFound().build();
			}
			
			jogador = escalacaoClubePartidaCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(jogador)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(jogador);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/escalacaoClube/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			escalacaoClubePartidaCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/escalacaoClube")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			escalacaoClubePartidaCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

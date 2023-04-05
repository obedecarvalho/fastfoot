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
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.match.service.crud.EscalacaoJogadorPosicaoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class EscalacaoJogadorPosicaoCRUDController implements CRUDController<EscalacaoJogadorPosicao, Long> {
	
	@Autowired
	private EscalacaoJogadorPosicaoCRUDService escalacaoJogadorPosicaoCRUDService;

	@Override
	@PostMapping("/escalacaoJogadorPosicao")
	public ResponseEntity<EscalacaoJogadorPosicao> create(@RequestBody EscalacaoJogadorPosicao t) {
		try {
			
			EscalacaoJogadorPosicao jogador = escalacaoJogadorPosicaoCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(jogador)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<EscalacaoJogadorPosicao>(jogador, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@Override
	public ResponseEntity<List<EscalacaoJogadorPosicao>> getAll() {
		return null;
	}

	@GetMapping("/escalacaoJogadorPosicao")
	public ResponseEntity<List<EscalacaoJogadorPosicao>> getAll(@RequestParam(name = "idClube", required = false) Integer idClube) {
		try {

			List<EscalacaoJogadorPosicao> jogadores;

			if (ValidatorUtil.isEmpty(idClube)) {
				jogadores = escalacaoJogadorPosicaoCRUDService.getAll();
			} else {
				jogadores = escalacaoJogadorPosicaoCRUDService.getByClube(new Clube(idClube));
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
	@GetMapping("/escalacaoJogadorPosicao/{id}")
	public ResponseEntity<EscalacaoJogadorPosicao> getById(@PathVariable("id") Long id) {
		
		try {
			
			EscalacaoJogadorPosicao jogador = escalacaoJogadorPosicaoCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(jogador)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(jogador);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
		
		
	}

	@Override
	@PutMapping("/escalacaoJogadorPosicao/{id}")
	public ResponseEntity<EscalacaoJogadorPosicao> update(@PathVariable("id") Long id, @RequestBody EscalacaoJogadorPosicao t) {
		try {
			
			EscalacaoJogadorPosicao jogador = escalacaoJogadorPosicaoCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(jogador)) {
				return ResponseEntity.notFound().build();
			}
			
			jogador = escalacaoJogadorPosicaoCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(jogador)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(jogador);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/escalacaoJogadorPosicao/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			escalacaoJogadorPosicaoCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/escalacaoJogadorPosicao")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			escalacaoJogadorPosicaoCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

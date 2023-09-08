package com.fastfoot.player.controller;

import java.util.Collections;
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
import com.fastfoot.club.service.crud.ClubeCRUDService;
import com.fastfoot.controller.CRUDController;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.service.crud.JogadorCRUDService;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class JogadorCRUDController implements CRUDController<Jogador, Long> {
	
	@Autowired
	private JogadorCRUDService jogadorCRUDService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;
	
	@Autowired
	private ClubeCRUDService clubeCRUDService;

	@Override
	@PostMapping("/jogadores")
	public ResponseEntity<Jogador> create(@RequestBody Jogador t) {
		try {
			
			Jogador jogador = jogadorCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(jogador)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<Jogador>(jogador, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@Override
	public ResponseEntity<List<Jogador>> getAll() {
		return null;
	}

	@GetMapping("/jogadores")
	public ResponseEntity<List<Jogador>> getAll(
			@RequestParam(name = "idJogo", required = false) Long idJogo,
			@RequestParam(name = "idClube", required = false) Long idClube) {
		try {
			
			if (ValidatorUtil.isEmpty(idClube) && ValidatorUtil.isEmpty(idJogo)) {
				return ResponseEntity.badRequest().build();
			}
			
			Jogo jogo = null;
			Clube clube = null;
			
			if (!ValidatorUtil.isEmpty(idJogo)) {
				jogo = jogoCRUDService.getById(idJogo);
				if (ValidatorUtil.isEmpty(jogo)) {
					return ResponseEntity.noContent().build();
				}
			}
			
			if (!ValidatorUtil.isEmpty(idClube)) {
				clube = clubeCRUDService.getById(idClube);
				if (ValidatorUtil.isEmpty(clube)) {
					return ResponseEntity.noContent().build();
				}
			}

			List<Jogador> jogadores;

			if (!ValidatorUtil.isEmpty(idJogo)) {
				jogadores = jogadorCRUDService.getAllAtivos(jogo);
			} else {
				jogadores = jogadorCRUDService.getAtivosByClube(clube);
			}
	
			if (ValidatorUtil.isEmpty(jogadores)) {
				return ResponseEntity.noContent().build();
			}
			
			Collections.sort(jogadores, JogadorFactory.getComparatorPosicaoForcaGeral());
	
			return ResponseEntity.ok(jogadores);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/jogadores/{id}")
	public ResponseEntity<Jogador> getById(@PathVariable("id") Long id) {
		
		try {
			
			Jogador jogador = jogadorCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(jogador)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(jogador);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
		
		
	}

	@Override
	@PutMapping("/jogadores/{id}")
	public ResponseEntity<Jogador> update(@PathVariable("id") Long id, @RequestBody Jogador t) {
		try {
			
			Jogador jogador = jogadorCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(jogador)) {
				return ResponseEntity.notFound().build();
			}
			
			jogador = jogadorCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(jogador)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(jogador);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/jogadores/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			jogadorCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/jogadores")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			jogadorCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

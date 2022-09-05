package com.fastfoot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.dto.EscalacaoClubeDTO;
import com.fastfoot.match.model.dto.EscalacaoJogadorDTO;
import com.fastfoot.match.service.EscalarClubeService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@CrossOrigin("*")
public class EscalacaoClubeController {
	
	@Autowired
	private EscalarClubeService escalarClubeService;

	@GetMapping("/escalacaoClube/{idClube}")
	public ResponseEntity<EscalacaoClubeDTO> getEscalacaoClube(@PathVariable(name = "idClube") Integer idClube) {
		Clube c = new Clube(idClube);
		EscalacaoClubeDTO escalacaoDTO = escalarClubeService.getEscalacaoClube(c);		
		if (ValidatorUtil.isEmpty(escalacaoDTO)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(escalacaoDTO);
	}
	
	@PostMapping("/salvarEscalacaoClube/{idClube}")
	public ResponseEntity<List<EscalacaoJogadorDTO>> salvarEscalacaoClube(
			@PathVariable(name = "idClube") Integer idClube,
			@RequestBody(required = true) EscalacaoClubeDTO escalacaoClube) {
		List<EscalacaoJogadorDTO> partidas = null;
		
		if (ValidatorUtil.isEmpty(partidas)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(partidas);
	}
}

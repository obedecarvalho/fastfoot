package com.fastfoot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.player.model.dto.JogadorDTO;
import com.fastfoot.player.service.JogadorService;
import com.fastfoot.service.util.ValidatorUtil;

@Deprecated
@RestController
@CrossOrigin("*")
public class JogadorController {

	@Autowired
	private JogadorService jogadorService;
	
	@Deprecated
	@GetMapping("/jogadoresPorClube/{idClube}")
	public ResponseEntity<List<JogadorDTO>> getJogadoresPorClube(@PathVariable(name = "idClube") Integer idClube) {
		List<JogadorDTO> partidas = JogadorDTO.convertToDTO(jogadorService.getJogadoresPorClube(idClube));
		if (ValidatorUtil.isEmpty(partidas)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(partidas);
	}
}

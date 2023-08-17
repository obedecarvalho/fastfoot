package com.fastfoot.match.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.dto.EscalacaoClubeDTO;
import com.fastfoot.match.service.ConsultarEscalacaoClubeService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ConsultarEscalacaoClubeController {
	
	@Autowired
	private ConsultarEscalacaoClubeService consultarEscalacaoClubeService;

	@GetMapping("/consultarEscalacaoClube/{idClube}")
	public ResponseEntity<EscalacaoClubeDTO> consultarEscalacaoClube(@PathVariable(name = "idClube") Integer idClube) {
		Clube c = new Clube(idClube);
		EscalacaoClubeDTO escalacaoDTO = consultarEscalacaoClubeService.consultarEscalacaoClube(c);		
		if (ValidatorUtil.isEmpty(escalacaoDTO)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(escalacaoDTO);
	}

}

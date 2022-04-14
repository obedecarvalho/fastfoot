package com.fastfoot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.scheduler.model.dto.ClassificacaoDTO;
import com.fastfoot.scheduler.service.ClassificacaoService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@CrossOrigin("*")
public class ClassificacaoController {
	
	@Autowired
	private ClassificacaoService classificacaoService; 
	
	@GetMapping("/classificacao/campeonato/{idCampeonato}")
	public ResponseEntity<List<ClassificacaoDTO>> getClassificacaoCampeonato(@PathVariable(name = "idCampeonato") Long idCampeonato, @RequestParam(name = "nivel") String nivel) {
		List<ClassificacaoDTO> classificacao = classificacaoService.getClassificacaoCampeonato(idCampeonato, nivel);
		if (ValidatorUtil.isEmpty(classificacao)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(classificacao);
	}

}

package com.fastfoot.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.dto.SemanaDTO;
import com.fastfoot.scheduler.service.JogarPartidasSemanaService;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class JogarPartidasSemanaController {
	
	@Autowired
	private JogarPartidasSemanaService jogarPartidasSemanaService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;
	
	@GetMapping("/jogarPartidasSemana/{idJogo}")
	public ResponseEntity<SemanaDTO> jogarPartidasSemana(@PathVariable("idJogo") Long idJogo) {
		try {
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(jogarPartidasSemanaService.jogarPartidasSemana(jogo));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}

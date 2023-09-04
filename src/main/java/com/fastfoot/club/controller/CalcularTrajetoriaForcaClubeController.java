package com.fastfoot.club.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.club.service.CalcularTrajetoriaForcaClubeTodosClubesService;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CalcularTrajetoriaForcaClubeController {
	
	@Autowired
	private CalcularTrajetoriaForcaClubeTodosClubesService calcularTrajetoriaForcaClubeTodosClubesService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;

	@GetMapping("/calcularTrajetoriaForcaClube/{idJogo}")
	public ResponseEntity<Boolean> calcularTrajetoriaForcaClube(@PathVariable("idJogo") Long idJogo) {
		try {
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}
			calcularTrajetoriaForcaClubeTodosClubesService.calcularTrajetoriaForcaClube(jogo);
			return ResponseEntity.ok(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}

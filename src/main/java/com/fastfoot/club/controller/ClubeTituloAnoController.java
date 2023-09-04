package com.fastfoot.club.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.club.model.dto.ClubeTituloAnoDTO;
import com.fastfoot.club.service.ConsultarClubeCampeaoService;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ClubeTituloAnoController {

	@Autowired
	private ConsultarClubeCampeaoService consultarClubeCampeaoService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;

	@GetMapping("/clubeCampeao/{ano}")
	public ResponseEntity<List<ClubeTituloAnoDTO>> consultarClubeCampeaoByAno(@RequestParam(name = "idJogo", required = true) Long idJogo,
			@PathVariable(name = "ano") Integer ano) {
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}

			List<ClubeTituloAnoDTO> campeoes = consultarClubeCampeaoService.consultarClubeCampeaoByAno(jogo, ano);

			if (ValidatorUtil.isEmpty(campeoes)) {
				return ResponseEntity.noContent().build();
			}

			return ResponseEntity.ok(campeoes);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

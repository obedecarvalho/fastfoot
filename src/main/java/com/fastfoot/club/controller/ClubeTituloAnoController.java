package com.fastfoot.club.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.club.model.dto.ClubeTituloAnoDTO;
import com.fastfoot.club.service.ConsultarClubeCampeaoService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ClubeTituloAnoController {

	@Autowired
	private ConsultarClubeCampeaoService consultarClubeCampeaoService;

	@GetMapping("/clubeCampeao/{ano}")
	public ResponseEntity<List<ClubeTituloAnoDTO>> consultarClubeCampeaoByAno(@PathVariable("ano") Integer ano) {
		try {

			List<ClubeTituloAnoDTO> campeoes = consultarClubeCampeaoService.consultarClubeCampeaoByAno(ano);

			if (ValidatorUtil.isEmpty(campeoes)) {
				return ResponseEntity.noContent().build();
			}

			return ResponseEntity.ok(campeoes);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}

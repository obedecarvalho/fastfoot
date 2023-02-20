package com.fastfoot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.club.model.dto.ClubeTituloAnoDTO;
import com.fastfoot.service.ClubeService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@CrossOrigin("*")
public class ClubeController {

	@Autowired
	private ClubeService clubeService;
	
	@GetMapping("/anoClubeRankingItens")
	public ResponseEntity<List<Integer>> getAnoClubeRankingItens(){
		List<Integer> anos = clubeService.getAnosClubeRanking();
		if (ValidatorUtil.isEmpty(anos)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(anos);
	}

	@GetMapping("/clubes/campeoes/{ano}")
	public ResponseEntity<List<ClubeTituloAnoDTO>> getClubesCampeoesPorAno(@PathVariable(name = "ano") Integer ano) {
		List<ClubeTituloAnoDTO> clubesCampeoes = clubeService.getClubesCampeoesPorAno(ano);
		
		if (ValidatorUtil.isEmpty(clubesCampeoes)) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(clubesCampeoes);
	}

	/*@GetMapping("/clubesTitulosRankings/{liga}")
	public ResponseEntity<List<ClubeTituloRankingDTO>> getClubesTitulosRankings(@PathVariable(name = "liga") String liga) {
		List<ClubeTituloRankingDTO> partidas = clubeService.getClubesTitulosRankings(liga);
		
		if (ValidatorUtil.isEmpty(partidas)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(partidas);
	}*/

}

package com.fastfoot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.scheduler.model.dto.ClubeDTO;
import com.fastfoot.scheduler.model.dto.ClubeRankingDTO;
import com.fastfoot.service.ClubeService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@CrossOrigin("*")
public class ClubeController {

	@Autowired
	private ClubeService clubeService;
	
	@GetMapping("/clubesPorLiga/{liga}")
	public ResponseEntity<List<ClubeDTO>> getClubesPorLiga(@PathVariable(name = "liga") String liga) {//'GENEBE', 'SPAPOR', 'ITAFRA', 'ENGLND'
		List<ClubeDTO> partidas = clubeService.getClubesPorLiga(liga);
		if (ValidatorUtil.isEmpty(partidas)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(partidas);
	}
	
	@GetMapping("/clubesRankings/{liga}")
	public ResponseEntity<List<ClubeRankingDTO>> getClubesRankings(@RequestParam(name = "ano", required = false) Integer ano, @PathVariable(name = "liga") String liga) {
		List<ClubeRankingDTO> partidas = null;
		if (ValidatorUtil.isEmpty(ano)) {
			partidas = clubeService.getClubesRankings(liga);
		} else {
			partidas = clubeService.getClubesRankings(liga, ano);
		}
		if (ValidatorUtil.isEmpty(partidas)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(partidas);
	}
	
	@GetMapping("/anoClubeRankingItens")
	public ResponseEntity<List<Integer>> getAnoClubeRankingItens(){
		List<Integer> anos = clubeService.getAnosClubeRanking();
		if (ValidatorUtil.isEmpty(anos)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(anos);
	}
}

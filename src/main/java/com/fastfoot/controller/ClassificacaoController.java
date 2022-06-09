package com.fastfoot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.probability.model.dto.ClubeProbabilidadeDTO;
import com.fastfoot.probability.service.CalcularProbabilidadeService;
import com.fastfoot.probability.service.ClubeProbabilidadeService;
import com.fastfoot.scheduler.model.dto.ClassificacaoDTO;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.CampeonatoRepository;
import com.fastfoot.scheduler.service.ClassificacaoService;
import com.fastfoot.scheduler.service.TemporadaService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@CrossOrigin("*")
public class ClassificacaoController {
	
	@Autowired
	private ClassificacaoService classificacaoService; 
	
	@Autowired
	private ClubeProbabilidadeService clubeProbabilidadeService;
	
	@GetMapping("/classificacao/campeonato/{idCampeonato}")
	public ResponseEntity<List<ClassificacaoDTO>> getClassificacaoCampeonato(@PathVariable(name = "idCampeonato") Long idCampeonato, @RequestParam(name = "nivel") String nivel) {
		List<ClassificacaoDTO> classificacao = classificacaoService.getClassificacaoCampeonato(idCampeonato, nivel);
		if (ValidatorUtil.isEmpty(classificacao)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(classificacao);
	}

	//
	@GetMapping("/probabilidade/campeonato/{idCampeonato}")
	public ResponseEntity<List<ClubeProbabilidadeDTO>> getProbabilidadesCampeonato(@PathVariable(name = "idCampeonato") Long idCampeonato) {
		List<ClubeProbabilidadeDTO> probabilidades = clubeProbabilidadeService.getProbabilidadePorCampeonato(idCampeonato);
		if (ValidatorUtil.isEmpty(probabilidades)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(probabilidades);
	}
	//

	
	@Autowired
	private CalcularProbabilidadeService calcularProbabilidadeService; 
	
	@Autowired
	private TemporadaService temporadaService;
	
	@Autowired
	private CampeonatoRepository campeonatoRepository;

	@GetMapping("/calcularProbabilidades")
	public ResponseEntity<Boolean> calcularProbabilidades(){
		
		Temporada t = temporadaService.getTemporadaAtual();
		
		List<Campeonato> campeonatos = campeonatoRepository.findByTemporada(t);
		
		List<CompletableFuture<Boolean>> calculoProbabilidadesFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		Semana semana = new Semana();
		
		semana.setNumero(t.getSemanaAtual());
		
		/*for (Campeonato c : campeonatos) {
			calculoProbabilidadesFuture.add(calcularProbabilidadeService.calcularProbabilidadesCampeonato(c));
		}*/
		
		//
		calculoProbabilidadesFuture.add(calcularProbabilidadeService.calcularProbabilidadesCampeonato(semana, campeonatos.get(0)));
		
		calculoProbabilidadesFuture.add(calcularProbabilidadeService.calcularProbabilidadesCampeonato(semana, campeonatos.get(1)));
		
		CompletableFuture.allOf(calculoProbabilidadesFuture.toArray(new CompletableFuture<?>[0])).join();
		
		
		return ResponseEntity.ok(true);
	}
}

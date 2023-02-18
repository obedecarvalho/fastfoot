package com.fastfoot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.model.Liga;
import com.fastfoot.probability.model.dto.ClubeProbabilidadeDTO;
import com.fastfoot.probability.service.CalcularProbabilidadeEstatisticasSimplesService;
import com.fastfoot.probability.service.ClubeProbabilidadeService;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.dto.ClassificacaoDTO;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.CampeonatoRepository;
import com.fastfoot.scheduler.service.ClassificacaoService;
import com.fastfoot.scheduler.service.TemporadaService;
import com.fastfoot.service.util.ValidatorUtil;

@Deprecated
@RestController
@CrossOrigin("*")
public class ClassificacaoController {
	
	@Autowired
	private ClassificacaoService classificacaoService; 
	
	@Autowired
	private ClubeProbabilidadeService clubeProbabilidadeService;
	
	//@GetMapping("/classificacao/campeonato/{idCampeonato}")
	@Deprecated
	public ResponseEntity<List<ClassificacaoDTO>> getClassificacaoCampeonato(@PathVariable(name = "idCampeonato") Long idCampeonato, @RequestParam(name = "nivel") String nivel) {
		List<ClassificacaoDTO> classificacao = classificacaoService.getClassificacaoCampeonato(idCampeonato, nivel);
		if (ValidatorUtil.isEmpty(classificacao)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(classificacao);
	}

	//@GetMapping("/probabilidade/campeonato/{idCampeonato}")
	@Deprecated
	public ResponseEntity<List<ClubeProbabilidadeDTO>> getProbabilidadesCampeonato(@PathVariable(name = "idCampeonato") Long idCampeonato) {
		List<ClubeProbabilidadeDTO> probabilidades = clubeProbabilidadeService.getProbabilidadePorCampeonato(idCampeonato);
		if (ValidatorUtil.isEmpty(probabilidades)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(probabilidades);
	}
	
	/*@Autowired
	private CalcularProbabilidadeService calcularProbabilidadeService;*/ 
	
	@Autowired
	private TemporadaService temporadaService;
	
	@Autowired
	private CampeonatoRepository campeonatoRepository;
	
	@Autowired
	private CalcularProbabilidadeEstatisticasSimplesService calcularProbabilidadeCompletoService;

	//@GetMapping("/calcularProbabilidades")
	@Deprecated
	public ResponseEntity<Boolean> calcularProbabilidades(){
		
		Temporada t = temporadaService.getTemporadaAtual();
		
		List<Campeonato> campeonatos = campeonatoRepository.findByTemporada(t);
		
		List<CompletableFuture<Boolean>> calculoProbabilidadesFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		Semana semana = new Semana();
		
		semana.setNumero(t.getSemanaAtual());
		
		semana.setTemporada(t);
		
		Map<Liga, Map<NivelCampeonato, List<Campeonato>>> campeonatosMap =
				campeonatos.stream().collect(Collectors.groupingBy(Campeonato::getLiga, Collectors.groupingBy(Campeonato::getNivelCampeonato)));
		
		for (Liga l : Liga.getAll()) {
			calculoProbabilidadesFuture.add(calcularProbabilidadeCompletoService.calcularProbabilidadesCampeonato(
					semana, campeonatosMap.get(l).get(NivelCampeonato.NACIONAL).get(0),
					campeonatosMap.get(l).get(NivelCampeonato.NACIONAL_II).get(0)));
		}
		
		CompletableFuture.allOf(calculoProbabilidadesFuture.toArray(new CompletableFuture<?>[0])).join();
		
		
		return ResponseEntity.ok(true);
	}
}

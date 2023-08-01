package com.fastfoot.club.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.service.CalcularTorcidaPartidaService;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.PartidaAmistosaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;

@Service
public class CalcularPrevisaoReceitaIngressosService {
	
	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaResultadoRepository;
	
	@Autowired
	private PartidaResultadoRepository partidaResultadoRepository;
	
	@Autowired
	private PartidaAmistosaResultadoRepository partidaAmistosaResultadoRepository;
	
	@Autowired
	private CalcularTorcidaPartidaService calcularTorcidaPartidaService;

	@Deprecated
	public Double calcularPrevisaoReceitaIngressos(Clube clube) {
		
		Double receita = 0.0d;
		
		List<PartidaResultado> partidasMandante = partidaResultadoRepository.findByClubeMandanteAndPartidaJogada(clube,
				false);

		List<PartidaResultado> partidasVisitante = partidaResultadoRepository
				.findByClubeVisitanteAndPartidaJogada(clube, false);

		List<PartidaEliminatoriaResultado> partidasEliminatoriaMandante = partidaEliminatoriaResultadoRepository
				.findByClubeMandanteAndPartidaJogada(clube, false);

		List<PartidaEliminatoriaResultado> partidasEliminatoriaVisitante = partidaEliminatoriaResultadoRepository
				.findByClubeVisitanteAndPartidaJogada(clube, false);

		List<PartidaAmistosaResultado> partidasAmistosaMandante = partidaAmistosaResultadoRepository
				.findByClubeMandanteAndPartidaJogada(clube, false);

		List<PartidaAmistosaResultado> partidasAmistosaVisitante = partidaAmistosaResultadoRepository
				.findByClubeVisitanteAndPartidaJogada(clube, false);
		
		receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(partidasMandante, true);
		receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(partidasVisitante, false);
		receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(partidasEliminatoriaMandante, true);
		receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(partidasEliminatoriaVisitante, false);
		receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(partidasAmistosaMandante, true);
		receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(partidasAmistosaVisitante, false);
		
		return receita;

	}

	@Deprecated
	@Async("defaultExecutor")
	public CompletableFuture<Map<Clube, Double>> calcularPrevisaoReceitaIngressos(List<Clube> clubes){

		Map<Clube, Double> clubeReceita = new HashMap<Clube, Double>();

		for (Clube clube : clubes) {
			clubeReceita.put(clube, calcularPrevisaoReceitaIngressos(clube));
		}

		return CompletableFuture.completedFuture(clubeReceita);
	}

	public Map<Clube, Double> calcularPrevisaoReceitaIngressos(Temporada temporada){
		
		List<PartidaResultado> partidas = partidaResultadoRepository.findByTemporadaAndPartidaJogada(temporada, false);
		
		List<PartidaEliminatoriaResultado> partidasEliminatorias = partidaEliminatoriaResultadoRepository.findByTemporadaAndPartidaJogada(temporada, false);
		
		List<PartidaAmistosaResultado> partidasAmistosas = partidaAmistosaResultadoRepository.findByTemporadaAndPartidaJogada(temporada, false);
		
		Map<Clube, List<PartidaResultado>> partidasMandante = partidas.stream()
				.collect(Collectors.groupingBy(PartidaResultado::getClubeMandante));

		Map<Clube, List<PartidaResultado>> partidasVisitante = partidas.stream()
				.collect(Collectors.groupingBy(PartidaResultado::getClubeVisitante));

		Map<Clube, List<PartidaEliminatoriaResultado>> partidasEliminatoriasMandante = partidasEliminatorias.stream()
				.filter(p -> p.getClubeMandante() != null)
				.collect(Collectors.groupingBy(PartidaEliminatoriaResultado::getClubeMandante));

		Map<Clube, List<PartidaEliminatoriaResultado>> partidasEliminatoriasVisitante = partidasEliminatorias.stream()
				.filter(p -> p.getClubeVisitante() != null)
				.collect(Collectors.groupingBy(PartidaEliminatoriaResultado::getClubeVisitante));

		Map<Clube, List<PartidaAmistosaResultado>> partidasAmistosasMandante = partidasAmistosas.stream()
				.collect(Collectors.groupingBy(PartidaAmistosaResultado::getClubeMandante));

		Map<Clube, List<PartidaAmistosaResultado>> partidasAmistosasVisitante = partidasAmistosas.stream()
				.collect(Collectors.groupingBy(PartidaAmistosaResultado::getClubeVisitante));

		Map<Clube, Double> clubeReceita = new HashMap<Clube, Double>();
		
		Double receita;
		

		for (Clube clube : partidasMandante.keySet()) {
			
			receita = 0.0d;
			
			receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(partidasMandante.get(clube), true);
			receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(partidasEliminatoriasMandante.get(clube), true);
			receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(partidasAmistosasMandante.get(clube), true);
			receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(partidasVisitante.get(clube), false);
			receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(partidasEliminatoriasVisitante.get(clube), false);
			receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(partidasAmistosasVisitante.get(clube), false);

			clubeReceita.put(clube, receita);
		}

		return clubeReceita;
	}
}

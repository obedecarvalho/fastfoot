package com.fastfoot.club.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
			
			receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(temporada.getJogo(), partidasMandante.get(clube), true);
			receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(temporada.getJogo(), partidasEliminatoriasMandante.get(clube), true);
			receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(temporada.getJogo(), partidasAmistosasMandante.get(clube), true);
			receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(temporada.getJogo(), partidasVisitante.get(clube), false);
			receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(temporada.getJogo(), partidasEliminatoriasVisitante.get(clube), false);
			receita += calcularTorcidaPartidaService.calcularPrevisaoEntradaIngressos(temporada.getJogo(), partidasAmistosasVisitante.get(clube), false);

			clubeReceita.put(clube, receita);
		}

		return clubeReceita;
	}
}

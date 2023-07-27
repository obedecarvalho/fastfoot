package com.fastfoot.club.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.service.CalcularTorcidaPartidaService;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
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

	@Async("defaultExecutor")
	public CompletableFuture<Map<Clube, Double>> calcularPrevisaoReceitaIngressos(List<Clube> clubes){//TODO: reduzir consultas banco

		Map<Clube, Double> clubeReceita = new HashMap<Clube, Double>();

		for (Clube clube : clubes) {
			clubeReceita.put(clube, calcularPrevisaoReceitaIngressos(clube));
		}

		return CompletableFuture.completedFuture(clubeReceita);
	}
}

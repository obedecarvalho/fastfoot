package com.fastfoot.match.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.match.model.PartidaTorcidaPorcentagem;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.service.ParametroService;

@Service
public class CalcularTorcidaPartidaService {
	
	private static final Double PORCENTAGEM_MANDANTE = 0.65;
	
	private static final Double PORCENTAGEM_VISITANTE = 0.35;
	
	protected static final Random R = new Random();
	
	private static final Double PORC_STDEV = 0.05;
	
	@Autowired
	private ParametroService parametroService;

	public void calcularTorcidaPartida(List<PartidaResultadoJogavel> partida) {
		for (PartidaResultadoJogavel p : partida) {
			calcularTorcidaPartida(p);
		}
	}

	public void calcularTorcidaPartida(PartidaResultadoJogavel partida) {

		int tamanhoEstadio = partida.getClubeMandante().getClubeNivel().getTamanhoTorcida();

		Double porcPublicoAlvo = getPorcentagemPublicoAlvo(partida);

		if (porcPublicoAlvo == null) return;//TODO

		Long publicoMandante = sortearTorcida(tamanhoEstadio,
				partida.getClubeMandante().getClubeNivel().getTamanhoTorcida(), PORCENTAGEM_MANDANTE, porcPublicoAlvo);

		Long publicoVisitante = sortearTorcida(tamanhoEstadio,
				partida.getClubeVisitante().getClubeNivel().getTamanhoTorcida(), PORCENTAGEM_VISITANTE,
				porcPublicoAlvo);

		System.err.println(String.format("%d, %d, %f, %s, %d", tamanhoEstadio, /*publicoMandante, publicoVisitante,*/
				publicoMandante + publicoVisitante, porcPublicoAlvo, partida.getNivelCampeonato(), partida.getRodada().getNumero()));
	}
	
	private Double getPorcentagemPublicoAlvo(PartidaResultadoJogavel partida) {

		if (partida.getNivelCampeonato().isCopaNacional()) {
			
			Integer numRodadasCN = parametroService.getNumeroRodadasCopaNacional();

			if (partida.getRodada().getNumero() == numRodadasCN) {// Final
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FINAL);
			} else if (partida.getRodada().getNumero() == (numRodadasCN - 1)) {// Semi Final
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_SEMI_FINAL);
			} else if (partida.getRodada().getNumero() == (numRodadasCN - 2)) {// Quartas Final
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_QUARTAS_FINAL);
			} else if (partida.getRodada().getNumero() == (numRodadasCN - 3)) {// Oitavas Final
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_OITAVAS_FINAL);
			} else if (partida.getRodada().getNumero() == (numRodadasCN - 4)) {// Preliminar II
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FASE_PRELIMINAR);
			} else if (partida.getRodada().getNumero() == (numRodadasCN - 5)) {// Preliminar I
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FASE_PRELIMINAR);
			}
		}

		if (partida.getNivelCampeonato().isCopaNacionalII()) {

			if (partida.getRodada().getNumero() == 4) {// Final
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FINAL);
			} else if (partida.getRodada().getNumero() == 3) {// Semi Final
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_SEMI_FINAL);
			} else if (partida.getRodada().getNumero() == 2) {// Quartas Final
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_QUARTAS_FINAL);
			} else if (partida.getRodada().getNumero() == 1) {// Oitavas Final
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_OITAVAS_FINAL);
			}
		}

		if (partida.getNivelCampeonato().isCIOuCIIOuCIII()) {

			if (partida.getRodada().getNumero() == 1) {
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FASE_GRUPOS);
			} else if (partida.getRodada().getNumero() == 2) {
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FASE_GRUPOS);
			} else if (partida.getRodada().getNumero() == 3) {
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FASE_GRUPOS);
			} else if (partida.getRodada().getNumero() == 4) {
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_QUARTAS_FINAL);
			} else if (partida.getRodada().getNumero() == 5) {
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_SEMI_FINAL);
			} else if (partida.getRodada().getNumero() == 6) {
				return PartidaTorcidaPorcentagem.get(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FINAL);
			}

		}

		return null;
	}

	private Long sortearTorcida(Integer tamanhoEstadio, Integer tamanhoTorcidaClube, Double ajuste,
			Double porcPublicoAlvo) {

		double porcPublicoAlvoFinal = porcPublicoAlvo + (R.nextGaussian() * PORC_STDEV);
		
		if (porcPublicoAlvoFinal > 1.0) porcPublicoAlvoFinal = 1.0;

		double torcidaMax = tamanhoEstadio * ajuste * porcPublicoAlvoFinal;
		double torcidaAlvo = tamanhoTorcidaClube * porcPublicoAlvoFinal;

		return Math.round(Math.min(torcidaMax, torcidaAlvo));

	}
}

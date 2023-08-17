package com.fastfoot.bets.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.probability.model.ClubeProbabilidadeDefesa;
import com.fastfoot.probability.model.ClubeProbabilidadeFinalizacao;
import com.fastfoot.probability.model.TipoCampeonatoClubeProbabilidade;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class CalcularPartidaProbabilidadeResultadoService {

	/*
	 * 
	 *
	 * SIMILARIDADE (com SIMULAR_PARTIDA)://Partida Probabilidade
		ESTATISTICAS_FINALIZACAO_DEFESA		24,48%		41,46%
		ESTATISTICAS_FINALIZACAO_POISSON	20,21%		34,27%
		ESTATISTICAS_FINALIZACAO			16,04%		24,27%
		SIMULAR_PARTIDA_HABILIDADE_GRUPO	39,27%		-
	 *
	 *
	 *
	 * TEMPO GASTO://Partida Probabilidade
		#SimularPartida					13,36%
		#SimularPartidaHabilidadeGrupo	 7,60%
		#EstatisticaFinalizacaoDefesa	47,47%
		#EstatisticaFinalizacao			27,04%
		#EstatisticaGolsPoisson			 4,53%
	 *
	 *
	 *
	 * SIMILARIDADE (com SIMULAR_PARTIDA)://Probabilidade Campeonato
		SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO			32,03%	36,72%
		SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO_DEFESA		21,09%	34,38%
		SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO_POISSON	16,41%	28,91%
		SIMULAR_PARTIDA_HABILIDADE_GRUPO					30,47%	-
	 *
	 *
	 *
	 * TEMPO GASTO://Probabilidade Campeonato
		#SIMULAR_PARTIDA									26,87%
		#SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO			18,34%
		#SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO_DEFESA	31,77%
		#SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO_POISSON	 7,56%
		#SIMULAR_PARTIDA_HABILIDADE_GRUPO					15,45%
	 *
	 *
	 */

	@Autowired
	private CalcularPartidaProbabilidadeResultadoSimularPartidaService calcularPartidaProbabilidadeResultadoSimularPartidaService;

	@Autowired
	private CalcularPartidaProbabilidadeResultadoSimularPartidaHabilidadeGrupoService calcularPartidaProbabilidadeResultadoSimularPartidaHabilidadeGrupoService;

	@Autowired
	private CalcularPartidaProbabilidadeResultadoEstatisticaFinalizacaoDefesaService calcularPartidaProbabilidadeResultadoEstatisticaFinalizacaoDefesaService;

	@Autowired
	private CalcularPartidaProbabilidadeResultadoEstatisticaFinalizacaoService calcularPartidaProbabilidadeResultadoEstatisticaFinalizacaoService;

	@Autowired
	private CalcularPartidaProbabilidadeResultadoEstatisticaGolsPoissonService calcularPartidaProbabilidadeResultadoEstatisticaGolsPoissonService;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularPartidaProbabilidadeResultado(RodadaJogavel rodada,
			TipoCampeonatoClubeProbabilidade tipoClubeProbabilidade) {

		if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA.equals(tipoClubeProbabilidade)) {
			calcularPartidaProbabilidadeResultadoSimularPartidaService.calcularPartidaProbabilidadeResultado(rodada);
		} else if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO
				.equals(tipoClubeProbabilidade)) {
			calcularPartidaProbabilidadeResultadoEstatisticaFinalizacaoService.calcularPartidaProbabilidadeResultado(rodada);
		} else if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO_DEFESA
				.equals(tipoClubeProbabilidade)) {
			calcularPartidaProbabilidadeResultadoEstatisticaFinalizacaoDefesaService.calcularPartidaProbabilidadeResultado(rodada);
		} else if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO_POISSON
				.equals(tipoClubeProbabilidade)) {
			calcularPartidaProbabilidadeResultadoEstatisticaGolsPoissonService.calcularPartidaProbabilidadeResultado(rodada);
		} else if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_HABILIDADE_GRUPO.equals(tipoClubeProbabilidade)) {
			calcularPartidaProbabilidadeResultadoSimularPartidaHabilidadeGrupoService.calcularPartidaProbabilidadeResultado(rodada);
		}

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	public PartidaProbabilidadeResultado calcularPartidaProbabilidadeResultado(PartidaResultadoJogavel partidaResultado,
			Map<Clube, ClubeProbabilidadeFinalizacao> clubeProbabilidadeFinalizacoes,
			Map<Clube, ClubeProbabilidadeDefesa> clubesProbabilidadeDefesa, EscalacaoClube escalacaoMandante,
			EscalacaoClube escalacaoVisitante, TipoCampeonatoClubeProbabilidade tipoClubeProbabilidade) {

		if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA.equals(tipoClubeProbabilidade)) {
			if (!ValidatorUtil.isEmpty(escalacaoMandante) && !ValidatorUtil.isEmpty(escalacaoMandante)) {
				return calcularPartidaProbabilidadeResultadoSimularPartidaService.calcularPartidaProbabilidadeResultado(partidaResultado,
						escalacaoMandante, escalacaoVisitante);
			} else {
				return calcularPartidaProbabilidadeResultadoSimularPartidaService.calcularPartidaProbabilidadeResultado(partidaResultado);
			}
		} else if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO
				.equals(tipoClubeProbabilidade)) {
			return calcularPartidaProbabilidadeResultadoEstatisticaFinalizacaoService.calcularPartidaProbabilidadeResultado(partidaResultado,
					clubeProbabilidadeFinalizacoes);
		} else if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO_DEFESA
				.equals(tipoClubeProbabilidade)) {
			return calcularPartidaProbabilidadeResultadoEstatisticaFinalizacaoDefesaService.calcularPartidaProbabilidadeResultado(partidaResultado,
					clubeProbabilidadeFinalizacoes, clubesProbabilidadeDefesa);
		} else if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO_POISSON
				.equals(tipoClubeProbabilidade)) {
			return calcularPartidaProbabilidadeResultadoEstatisticaGolsPoissonService.calcularPartidaProbabilidadeResultado(partidaResultado,
					clubeProbabilidadeFinalizacoes);
		} else if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_HABILIDADE_GRUPO.equals(tipoClubeProbabilidade)) {
			if (!ValidatorUtil.isEmpty(escalacaoMandante) && !ValidatorUtil.isEmpty(escalacaoMandante)) {
				return calcularPartidaProbabilidadeResultadoSimularPartidaHabilidadeGrupoService
						.calcularPartidaProbabilidadeResultado(partidaResultado, escalacaoMandante, escalacaoVisitante);
			} else {
				return calcularPartidaProbabilidadeResultadoSimularPartidaHabilidadeGrupoService.calcularPartidaProbabilidadeResultado(partidaResultado);
			}
		}

		return null;
	}

}

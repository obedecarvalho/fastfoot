package com.fastfoot.bets.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.bets.model.TipoProbabilidadeResultadoPartida;
import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.bets.model.repository.PartidaProbabilidadeResultadoRepository;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.service.CarregarEscalacaoJogadoresPartidaService;
import com.fastfoot.match.service.EscalarClubeService;
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.HabilidadeValorJogavel;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.CarregarJogadorEnergiaService;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;

@Service
public class CalcularPartidaProbabilidadeResultadoSimularPartidaHabilidadeGrupoService extends CalcularPartidaProbabilidadeResultadoSimularPartidaAbstractService {

	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private PartidaResultadoRepository partidaResultadoRepository;
	
	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaResultadoRepository;
	
	@Autowired
	private PartidaProbabilidadeResultadoRepository partidaProbabilidadeResultadoRepository;
	
	@Autowired
	private EscalarClubeService escalarClubeService;

	@Autowired
	private CarregarJogadorEnergiaService carregarJogadorEnergiaService;

	@Autowired
	private CarregarEscalacaoJogadoresPartidaService carregarEscalacaoJogadoresPartidaService;

	public CompletableFuture<Boolean> calcularPartidaProbabilidadeResultado(RodadaJogavel rodada) {

		List<PartidaProbabilidadeResultado> probabilidades = new ArrayList<PartidaProbabilidadeResultado>();
		
		List<? extends PartidaResultadoJogavel> partidas = null;
		
		if (rodada instanceof Rodada) {
			partidas = partidaResultadoRepository.findByRodada((Rodada) rodada);
		} else if (rodada instanceof RodadaEliminatoria) {
			partidas = partidaEliminatoriaResultadoRepository.findByRodada((RodadaEliminatoria) rodada);
		}

		carregarEscalacaoJogadoresPartidaService.carregarEscalacaoHabilidadeGrupo(rodada.getSemana().getTemporada().getJogo(), partidas);

		for (PartidaResultadoJogavel p : partidas) {
			probabilidades.add(calcularPartidaProbabilidadeResultado(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante()));
			//probabilidades.add(simularPartida(p));
		}

		partidaProbabilidadeResultadoRepository.saveAll(probabilidades);

		return CompletableFuture.completedFuture(Boolean.TRUE);

	}

	public PartidaProbabilidadeResultado calcularPartidaProbabilidadeResultado(PartidaResultadoJogavel partidaResultado) {
		
		List<Jogador> jogadoresMandante = jogadorRepository
				.findByClubeAndStatusJogadorFetchHabilidadesGrupo(partidaResultado.getClubeMandante(), StatusJogador.ATIVO);
		List<Jogador> jogadoresVisitante = jogadorRepository
				.findByClubeAndStatusJogadorFetchHabilidadesGrupo(partidaResultado.getClubeVisitante(), StatusJogador.ATIVO);
		
		carregarJogadorEnergiaService.carregarJogadorEnergia(partidaResultado.getClubeMandante(), jogadoresMandante);
		carregarJogadorEnergiaService.carregarJogadorEnergia(partidaResultado.getClubeVisitante(), jogadoresVisitante);
		
		EscalacaoClube escalacaoMandante = escalarClubeService
				.gerarEscalacaoInicial(partidaResultado.getClubeMandante(), jogadoresMandante, partidaResultado);//TODO: jogadores estão com energia alterada após jogar partidas? considerar 'recuperacao'?
		EscalacaoClube escalacaoVisitante = escalarClubeService
				.gerarEscalacaoInicial(partidaResultado.getClubeVisitante(), jogadoresVisitante, partidaResultado);//TODO: jogadores estão com energia alterada após jogar partidas? considerar 'recuperacao'?
		
		return calcularPartidaProbabilidadeResultado(partidaResultado, escalacaoMandante, escalacaoVisitante);
	}

	@Override
	public PartidaProbabilidadeResultado calcularPartidaProbabilidadeResultado(PartidaResultadoJogavel partidaResultado, EscalacaoClube escalacaoMandante, EscalacaoClube escalacaoVisitante) {
		return calcularPartidaProbabilidadeResultado(partidaResultado, escalacaoMandante, escalacaoVisitante, TipoProbabilidadeResultadoPartida.SIMULAR_PARTIDA_HABILIDADE_GRUPO, true);
	}

	@Override
	protected HabilidadeValorJogavel criarHabilidadeValorJogavelFora(Integer valor) {
		return new HabilidadeGrupoValor(HabilidadeGrupo.FORA, valor);
	}

}

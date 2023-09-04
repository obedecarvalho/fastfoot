package com.fastfoot.scheduler.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.match.model.PartidaJogadorEstatisticaDTO;
import com.fastfoot.match.model.dto.PartidaTorcidaSalvarDTO;
import com.fastfoot.match.model.repository.PartidaEstatisticasRepository;
import com.fastfoot.match.model.repository.PartidaLanceRepository;
import com.fastfoot.match.model.repository.PartidaTorcidaRepository;
import com.fastfoot.match.service.CalcularTorcidaPartidaService;
import com.fastfoot.match.service.CarregarEscalacaoJogadoresPartidaService;
import com.fastfoot.match.service.JogarPartidaHabilidadeGrupoService;
import com.fastfoot.match.service.JogarPartidaHabilidadeService;
import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.repository.HabilidadeValorEstatisticaRepository;
import com.fastfoot.player.model.repository.JogadorEnergiaRepository;
import com.fastfoot.player.model.repository.JogadorEstatisticasSemanaRepository;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.model.repository.PartidaAmistosaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;
import com.fastfoot.scheduler.service.util.ClassificacaoUtil;
import com.fastfoot.scheduler.service.util.PromotorEliminatoria;

@Service
public class JogarRodadaService {
	
	//###	REPOSITORY	###
	@Autowired
	private PartidaResultadoRepository partidaRepository;

	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaRepository;
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;
	
	@Autowired
	private PartidaLanceRepository partidaLanceRepository;

	@Autowired
	private PartidaAmistosaResultadoRepository partidaAmistosaResultadoRepository;
	
	@Autowired
	private HabilidadeValorEstatisticaRepository habilidadeValorEstatisticaRepository;

	@Autowired
	private JogadorEstatisticasSemanaRepository jogadorEstatisticasSemanaRepository;
	
	@Autowired
	private PartidaEstatisticasRepository partidaEstatisticasRepository;

	@Autowired
	private PartidaTorcidaRepository partidaTorcidaRepository;

	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;
	
	@Autowired
	private JogadorEnergiaRepository jogadorEnergiaRepository;

	//###	SERVICE	###
	@Autowired
	private CalcularTorcidaPartidaService calcularTorcidaPartidaService;
	
	@Autowired
	private JogarPartidaHabilidadeService jogarPartidaHabilidadeService;
	
	@Autowired
	private JogarPartidaHabilidadeGrupoService jogarPartidaHabilidadeGrupoService;

	@Autowired
	private CarregarEscalacaoJogadoresPartidaService carregarEscalacaoJogadoresPartidaService;

	private static final Boolean SALVAR_LANCES = false;
	
	private static final Boolean SALVAR_HABILIDADE_VALOR_ESTATISTICAS = false;//#DEVEL

	@Async("defaultExecutor")
	public CompletableFuture<RodadaJogavel> executarRodada(Rodada rodada, Boolean agrupado) {
		
		PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO = new PartidaJogadorEstatisticaDTO();
		PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO = new PartidaTorcidaSalvarDTO();

		carregarPartidas(rodada);

		if (agrupado) {
			carregarEscalacaoJogadoresPartidaService.carregarEscalacaoHabilidadeGrupo(rodada.getSemana().getTemporada().getJogo(), rodada.getPartidas());
		} else {
		carregarEscalacaoJogadoresPartidaService.carregarEscalacao(rodada.getSemana().getTemporada().getJogo(), rodada.getPartidas());
		}
		
		calcularTorcidaPartidaService.calcularTorcidaPartida(rodada, partidaTorcidaSalvarDTO);
		
		jogarPartidas(rodada, partidaJogadorEstatisticaDTO, agrupado);
		
		salvarPartidas(rodada);

		if (rodada.isUltimaRodadaPontosCorridos()){
			carregarClassificacao(rodada);
			classificarComDesempate(rodada);
			salvarClassificacao(rodada);
		}
		
		salvarPartidaJogadorEstatisticas(partidaJogadorEstatisticaDTO);
		
		salvarPartidaTorcida(partidaTorcidaSalvarDTO);
		
		if (SALVAR_LANCES) {
			partidaLanceRepository.saveAll(partidaJogadorEstatisticaDTO.getPartidaLances());
		}

		return CompletableFuture.completedFuture(rodada);
	}

	private void jogarPartidas(Rodada rodada, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO, Boolean agrupado) {
		if(rodada.getCampeonato() != null) {
			jogarRodada(rodada, partidaJogadorEstatisticaDTO,
					rodada.getCampeonato().getClassificacao(), agrupado);
			
			if (!rodada.isUltimaRodadaPontosCorridos()) {
				ClassificacaoUtil.ordernarClassificacao(rodada.getCampeonato().getClassificacao(), false);
			}
		} else if (rodada.getGrupoCampeonato() != null) {
			jogarRodada(rodada, partidaJogadorEstatisticaDTO,
					rodada.getGrupoCampeonato().getClassificacao(), agrupado);
			
			if (!rodada.isUltimaRodadaPontosCorridos()) {
				ClassificacaoUtil.ordernarClassificacao(rodada.getGrupoCampeonato().getClassificacao(), false);
			}
		}
	}

	private void carregarPartidas(Rodada rodada) {
		rodada.setPartidas(partidaRepository.findByRodada(rodada));
		
		if(rodada.getCampeonato() != null) {
			rodada.getCampeonato().setClassificacao(classificacaoRepository.findByCampeonato(rodada.getCampeonato()));
		} else if (rodada.getGrupoCampeonato() != null) {
			rodada.getGrupoCampeonato()
					.setClassificacao(classificacaoRepository.findByGrupoCampeonato(rodada.getGrupoCampeonato()));
		}
	}

	private void salvarPartidas(Rodada r) {
		
		partidaEstatisticasRepository.saveAll(
				r.getPartidas().stream().map(PartidaResultado::getPartidaEstatisticas).collect(Collectors.toList()));

		partidaRepository.saveAllAndFlush(r.getPartidas());

		//if (SALVAR_LANCES) { salvarLances(r.getPartidas()); }

		if(r.getCampeonato() != null) {
			classificacaoRepository.saveAllAndFlush(r.getCampeonato().getClassificacao());
		} else if (r.getGrupoCampeonato() != null) {
			classificacaoRepository.saveAllAndFlush(r.getGrupoCampeonato().getClassificacao());
		}
	}

	private void carregarClassificacao(Rodada r) {
		if(r.getCampeonato() != null) {
			r.getCampeonato().setClassificacao(classificacaoRepository.findByCampeonato(r.getCampeonato()));
		} else if (r.getGrupoCampeonato() != null) {
			r.getGrupoCampeonato()
					.setClassificacao(classificacaoRepository.findByGrupoCampeonato(r.getGrupoCampeonato()));
		}
	}

	/**
	 * Classifica os times com Desempate. Para ser executado na ultima rodada. 
	 * @param semana
	 */
	private void classificarComDesempate(Rodada r) {

		if(r.getCampeonato() != null) {
			if (r.getNumero() == Constantes.NRO_RODADAS_CAMP_NACIONAL) {
				List<PartidaResultado> partidas = partidaRepository.findByCampeonato(r.getCampeonato());
				ClassificacaoUtil.ordernarClassificacao(r.getCampeonato().getClassificacao(), partidas);
			}
		} else if (r.getGrupoCampeonato() != null) {
			if (r.getNumero() == Constantes.NRO_PARTIDAS_FASE_GRUPOS) {
				List<PartidaResultado> partidas = partidaRepository.findByGrupoCampeonato(r.getGrupoCampeonato());
				ClassificacaoUtil.ordernarClassificacao(r.getGrupoCampeonato().getClassificacao(), partidas);
			}
		}
	}

	private void salvarClassificacao(Rodada r) {
		if(r.getCampeonato() != null) {
			classificacaoRepository.saveAllAndFlush(r.getCampeonato().getClassificacao());
		} else if (r.getGrupoCampeonato() != null) {
			classificacaoRepository.saveAllAndFlush(r.getGrupoCampeonato().getClassificacao());
		}
	}

	@Async("defaultExecutor")
	public CompletableFuture<RodadaJogavel> executarRodada(RodadaEliminatoria rodada, Boolean agrupado) {
		
		PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO = new PartidaJogadorEstatisticaDTO();
		PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO = new PartidaTorcidaSalvarDTO();

		carregarPartidas(rodada);

		if (agrupado) {
			carregarEscalacaoJogadoresPartidaService.carregarEscalacaoHabilidadeGrupo(rodada.getSemana().getTemporada().getJogo(), rodada.getPartidas());
		} else {
		carregarEscalacaoJogadoresPartidaService.carregarEscalacao(rodada.getSemana().getTemporada().getJogo(), rodada.getPartidas());
		}
		
		calcularTorcidaPartidaService.calcularTorcidaPartida(rodada, partidaTorcidaSalvarDTO);
		
		jogarRodada(rodada, partidaJogadorEstatisticaDTO, agrupado);
		
		salvarPartidas(rodada);
		
		salvarPartidaJogadorEstatisticas(partidaJogadorEstatisticaDTO);
		
		salvarPartidaTorcida(partidaTorcidaSalvarDTO);
		
		if (SALVAR_LANCES) {
			partidaLanceRepository.saveAll(partidaJogadorEstatisticaDTO.getPartidaLances());
		}
		
		return CompletableFuture.completedFuture(rodada);
	}

	private void carregarPartidas(RodadaEliminatoria rodada) {
		rodada.setPartidas(partidaEliminatoriaRepository.findByRodada(rodada));
	}

	private void salvarPartidas(RodadaEliminatoria r) {
		
		partidaEstatisticasRepository.saveAll(r.getPartidas().stream()
				.map(PartidaEliminatoriaResultado::getPartidaEstatisticas).collect(Collectors.toList()));

		partidaEliminatoriaRepository.saveAllAndFlush(r.getPartidas());
		partidaEliminatoriaRepository.saveAll(r.getPartidas().stream().filter(p -> p.getProximaPartida() != null)
				.map(p -> p.getProximaPartida()).collect(Collectors.toList()));

		//if (SALVAR_LANCES) { salvarLances(r.getPartidas()); }
	}

	@Async("defaultExecutor")
	public CompletableFuture<RodadaJogavel> executarRodada(RodadaAmistosa rodada, Boolean agrupado) {
		
		PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO = new PartidaJogadorEstatisticaDTO();
		PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO = new PartidaTorcidaSalvarDTO();

		carregarPartidas(rodada);
		if (agrupado) {
			carregarEscalacaoJogadoresPartidaService.carregarEscalacaoHabilidadeGrupo(rodada.getSemana().getTemporada().getJogo(), rodada.getPartidas());
		} else {
		carregarEscalacaoJogadoresPartidaService.carregarEscalacao(rodada.getSemana().getTemporada().getJogo(), rodada.getPartidas());
		}
		calcularTorcidaPartidaService.calcularTorcidaPartida(rodada, partidaTorcidaSalvarDTO);
		jogarRodada(rodada, partidaJogadorEstatisticaDTO, agrupado);
		salvarPartidas(rodada);

		salvarPartidaJogadorEstatisticas(partidaJogadorEstatisticaDTO);
		salvarPartidaTorcida(partidaTorcidaSalvarDTO);
		
		if (SALVAR_LANCES) {
			partidaLanceRepository.saveAll(partidaJogadorEstatisticaDTO.getPartidaLances());
		}

		return CompletableFuture.completedFuture(rodada);
	}
	
	private void salvarPartidaJogadorEstatisticas(PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {

		if (SALVAR_HABILIDADE_VALOR_ESTATISTICAS) {
		habilidadeValorEstatisticaRepository.saveAll(partidaJogadorEstatisticaDTO.getHabilidadeValorEstatistica());
		}
		
		jogadorEstatisticasSemanaRepository.saveAll(partidaJogadorEstatisticaDTO.getJogadorEstatisticasSemana());
		
		jogadorEnergiaRepository.saveAll(partidaJogadorEstatisticaDTO.getJogadorEnergias());
		
	}

	private void carregarPartidas(RodadaAmistosa rodada) {
		rodada.setPartidas(partidaAmistosaResultadoRepository.findByRodada(rodada));
	}

	private void salvarPartidas(RodadaAmistosa r) {
		
		partidaEstatisticasRepository.saveAll(r.getPartidas().stream()
				.map(PartidaAmistosaResultado::getPartidaEstatisticas).collect(Collectors.toList()));

		partidaAmistosaResultadoRepository.saveAllAndFlush(r.getPartidas());
		
		//if (SALVAR_LANCES) { salvarLances(r.getPartidas()); }
	}
	
	private void salvarPartidaTorcida(PartidaTorcidaSalvarDTO dto) {
		partidaTorcidaRepository.saveAll(dto.getPartidaTorcidaList());
		movimentacaoFinanceiraRepository.saveAll(dto.getMovimentacaoFinanceira());
	}

	private void jogarRodada(Rodada rodada, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO, List<Classificacao> classificacao, Boolean agrupado) {

		for (PartidaResultado p : rodada.getPartidas()) {
			if (agrupado) {
				jogarPartidaHabilidadeGrupoService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaJogadorEstatisticaDTO);
			} else {
			jogarPartidaHabilidadeService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaJogadorEstatisticaDTO);
			}
		}
		
		ClassificacaoUtil.atualizarClassificacao(classificacao, rodada.getPartidas());
	}

	private void jogarRodada(RodadaEliminatoria rodada, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO, Boolean agrupado) {

		for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
			if (agrupado) {
				jogarPartidaHabilidadeGrupoService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaJogadorEstatisticaDTO);	
			} else {
			jogarPartidaHabilidadeService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaJogadorEstatisticaDTO);
			}

			if (p.getProximaPartida() != null) {
				PromotorEliminatoria.promoverProximaPartidaEliminatoria(p);
			}
		}
	}

	private void jogarRodada(RodadaAmistosa rodada, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO, Boolean agrupado) {
		for (PartidaAmistosaResultado p : rodada.getPartidas()) {
			if (agrupado) {
				jogarPartidaHabilidadeGrupoService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaJogadorEstatisticaDTO);
			} else {
			jogarPartidaHabilidadeService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaJogadorEstatisticaDTO);
			}
		}
	}
}

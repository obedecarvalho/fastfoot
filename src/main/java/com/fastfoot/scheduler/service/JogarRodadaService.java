package com.fastfoot.scheduler.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.match.model.PartidaDadosSalvarDTO;
import com.fastfoot.match.model.repository.EscalacaoClubeRepository;
import com.fastfoot.match.model.repository.EscalacaoJogadorPosicaoRepository;
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
	
	@Autowired
	private EscalacaoClubeRepository escalacaoClubeRepository;
	
	@Autowired
	private EscalacaoJogadorPosicaoRepository escalacaoJogadorPosicaoRepository;

	//###	SERVICE	###
	@Autowired
	private CalcularTorcidaPartidaService calcularTorcidaPartidaService;
	
	@Autowired
	private JogarPartidaHabilidadeService jogarPartidaHabilidadeService;
	
	@Autowired
	private JogarPartidaHabilidadeGrupoService jogarPartidaHabilidadeGrupoService;

	@Autowired
	private CarregarEscalacaoJogadoresPartidaService carregarEscalacaoJogadoresPartidaService;

	private static final Boolean SALVAR_LANCES = false;//#DEVEL
	
	private static final Boolean SALVAR_ESCALACAO = true;//#DEVEL
	
	private static final Boolean SALVAR_HABILIDADE_VALOR_ESTATISTICAS = false;//#DEVEL

	@Async("defaultExecutor")
	public CompletableFuture<RodadaJogavel> executarRodada(Rodada rodada, Boolean agrupado) {
		
		PartidaDadosSalvarDTO partidaDadosSalvarDTO = new PartidaDadosSalvarDTO();

		carregarPartidas(rodada);

		if (agrupado) {
			carregarEscalacaoJogadoresPartidaService.carregarEscalacaoHabilidadeGrupo(rodada.getSemana().getTemporada().getJogo(), rodada.getPartidas());
		} else {
			carregarEscalacaoJogadoresPartidaService.carregarEscalacao(rodada.getSemana().getTemporada().getJogo(), rodada.getPartidas());
		}
		
		calcularTorcidaPartidaService.calcularTorcidaPartida(rodada, partidaDadosSalvarDTO);
		
		jogarPartidas(rodada, partidaDadosSalvarDTO, agrupado);
		
		salvarPartidas(rodada);

		if (rodada.isUltimaRodadaPontosCorridos()){
			carregarClassificacao(rodada);
			classificarComDesempate(rodada);
			salvarClassificacao(rodada);
		}
		
		salvarPartidaJogadorEstatisticas(partidaDadosSalvarDTO);
		
		salvarPartidaTorcida(partidaDadosSalvarDTO);
		
		if (SALVAR_LANCES) {
			partidaLanceRepository.saveAll(partidaDadosSalvarDTO.getPartidaLances());
		}
		
		if (SALVAR_ESCALACAO) {
			salvarEscalacao(partidaDadosSalvarDTO);
		}

		return CompletableFuture.completedFuture(rodada);
	}

	private void jogarPartidas(Rodada rodada, PartidaDadosSalvarDTO partidaDadosSalvarDTO, Boolean agrupado) {
		if(rodada.getCampeonato() != null) {
			jogarRodada(rodada, partidaDadosSalvarDTO,
					rodada.getCampeonato().getClassificacao(), agrupado);
			
			if (!rodada.isUltimaRodadaPontosCorridos()) {
				ClassificacaoUtil.ordernarClassificacao(rodada.getCampeonato().getClassificacao(), false);
			}
		} else if (rodada.getGrupoCampeonato() != null) {
			jogarRodada(rodada, partidaDadosSalvarDTO,
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
		
		PartidaDadosSalvarDTO partidaDadosSalvarDTO = new PartidaDadosSalvarDTO();

		carregarPartidas(rodada);

		if (agrupado) {
			carregarEscalacaoJogadoresPartidaService.carregarEscalacaoHabilidadeGrupo(rodada.getSemana().getTemporada().getJogo(), rodada.getPartidas());
		} else {
			carregarEscalacaoJogadoresPartidaService.carregarEscalacao(rodada.getSemana().getTemporada().getJogo(), rodada.getPartidas());
		}
		
		calcularTorcidaPartidaService.calcularTorcidaPartida(rodada, partidaDadosSalvarDTO);
		
		jogarRodada(rodada, partidaDadosSalvarDTO, agrupado);
		
		salvarPartidas(rodada);
		
		salvarPartidaJogadorEstatisticas(partidaDadosSalvarDTO);
		
		salvarPartidaTorcida(partidaDadosSalvarDTO);
		
		if (SALVAR_LANCES) {
			partidaLanceRepository.saveAll(partidaDadosSalvarDTO.getPartidaLances());
		}
		
		if (SALVAR_ESCALACAO) {
			salvarEscalacao(partidaDadosSalvarDTO);
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
		
		PartidaDadosSalvarDTO partidaDadosSalvarDTO = new PartidaDadosSalvarDTO();

		carregarPartidas(rodada);
		if (agrupado) {
			carregarEscalacaoJogadoresPartidaService.carregarEscalacaoHabilidadeGrupo(rodada.getSemana().getTemporada().getJogo(), rodada.getPartidas());
		} else {
			carregarEscalacaoJogadoresPartidaService.carregarEscalacao(rodada.getSemana().getTemporada().getJogo(), rodada.getPartidas());
		}
		calcularTorcidaPartidaService.calcularTorcidaPartida(rodada, partidaDadosSalvarDTO);
		jogarRodada(rodada, partidaDadosSalvarDTO, agrupado);
		salvarPartidas(rodada);

		salvarPartidaJogadorEstatisticas(partidaDadosSalvarDTO);
		salvarPartidaTorcida(partidaDadosSalvarDTO);
		
		if (SALVAR_LANCES) {
			partidaLanceRepository.saveAll(partidaDadosSalvarDTO.getPartidaLances());
		}
		
		if (SALVAR_ESCALACAO) {
			salvarEscalacao(partidaDadosSalvarDTO);
		}

		return CompletableFuture.completedFuture(rodada);
	}
	
	private void salvarPartidaJogadorEstatisticas(PartidaDadosSalvarDTO partidaDadosSalvarDTO) {

		if (SALVAR_HABILIDADE_VALOR_ESTATISTICAS) {
		habilidadeValorEstatisticaRepository.saveAll(partidaDadosSalvarDTO.getHabilidadeValorEstatistica());
		}
		
		jogadorEstatisticasSemanaRepository.saveAll(partidaDadosSalvarDTO.getJogadorEstatisticasSemana());
		
		jogadorEnergiaRepository.saveAll(partidaDadosSalvarDTO.getJogadorEnergias());
		
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
	
	private void salvarPartidaTorcida(PartidaDadosSalvarDTO partidaDadosSalvarDTO) {
		partidaTorcidaRepository.saveAll(partidaDadosSalvarDTO.getPartidaTorcidaList());
		movimentacaoFinanceiraRepository.saveAll(partidaDadosSalvarDTO.getMovimentacaoFinanceira());
	}
	
	private void salvarEscalacao(PartidaDadosSalvarDTO partidaDadosSalvarDTO) {
		escalacaoClubeRepository.saveAll(partidaDadosSalvarDTO.getEscalacaoClubes());
		escalacaoJogadorPosicaoRepository.saveAll(partidaDadosSalvarDTO.getEscalacaoClubes().stream()
				.flatMap(e -> e.getListEscalacaoJogadorPosicao().stream()).collect(Collectors.toList()));
	}

	private void jogarRodada(Rodada rodada, PartidaDadosSalvarDTO partidaDadosSalvarDTO, List<Classificacao> classificacao, Boolean agrupado) {

		for (PartidaResultado p : rodada.getPartidas()) {
			if (agrupado) {
				jogarPartidaHabilidadeGrupoService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaDadosSalvarDTO);
			} else {
				jogarPartidaHabilidadeService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaDadosSalvarDTO);
			}
		}
		
		ClassificacaoUtil.atualizarClassificacao(classificacao, rodada.getPartidas());
	}

	private void jogarRodada(RodadaEliminatoria rodada, PartidaDadosSalvarDTO partidaDadosSalvarDTO, Boolean agrupado) {

		for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
			if (agrupado) {
				jogarPartidaHabilidadeGrupoService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaDadosSalvarDTO);	
			} else {
				jogarPartidaHabilidadeService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaDadosSalvarDTO);
			}

			if (p.getProximaPartida() != null) {
				PromotorEliminatoria.promoverProximaPartidaEliminatoria(p);
			}
		}
	}

	private void jogarRodada(RodadaAmistosa rodada, PartidaDadosSalvarDTO partidaDadosSalvarDTO, Boolean agrupado) {
		for (PartidaAmistosaResultado p : rodada.getPartidas()) {
			if (agrupado) {
				jogarPartidaHabilidadeGrupoService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaDadosSalvarDTO);
			} else {
				jogarPartidaHabilidadeService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaDadosSalvarDTO);
			}
		}
	}
}

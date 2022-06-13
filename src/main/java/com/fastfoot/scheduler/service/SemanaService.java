package com.fastfoot.scheduler.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.Constantes;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.probability.service.CalcularProbabilidadeService;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.dto.SemanaDTO;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.CampeonatoEliminatorioRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoMistoRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoRepository;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.model.repository.GrupoCampeonatoRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.RodadaAmistoraRepository;
import com.fastfoot.scheduler.model.repository.RodadaEliminatoriaRepository;
import com.fastfoot.scheduler.model.repository.RodadaRepository;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;
import com.fastfoot.service.ParametroService;

@Service
public class SemanaService {

	@Autowired
	private TemporadaRepository temporadaRepository;

	@Autowired
	private SemanaRepository semanaRepository;

	@Autowired
	private RodadaRepository rodadaRepository;
	
	@Autowired
	private RodadaEliminatoriaRepository rodadaEliminatoriaRepository;

	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaRepository;

	@Autowired
	private CampeonatoEliminatorioRepository campeonatoEliminatorioRepository;
	
	@Autowired
	private CampeonatoRepository campeonatoRepository;
	
	@Autowired
	private CampeonatoMistoRepository campeonatoMistoRepository;

	@Autowired
	private GrupoCampeonatoRepository grupoCampeonatoRepository;
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;
	
	@Autowired
	private TemporadaService temporadaService;
	
	@Autowired
	private RodadaService rodadaService;

	@Autowired
	private ParametroService parametroService;
	
	@Autowired
	private CalcularProbabilidadeService calcularProbabilidadeService; 

	@Autowired
	private RodadaAmistoraRepository rodadaAmistoraRepository;

	/*public SemanaDTO proximaSemana() {
		Temporada temporada = temporadaRepository.findFirstByAtual(true).get();
		temporada.setSemanaAtual(temporada.getSemanaAtual() + 1);
		Semana semana = semanaRepository.findFirstByTemporadaAndNumero(temporada, temporada.getSemanaAtual()).get();

		List<Rodada> rodadas = rodadaRepository.findBySemana(semana);
		List<RodadaEliminatoria> rodadaEliminatorias = rodadaEliminatoriaRepository.findBySemana(semana);
		
		semana.setRodadas(rodadas);
		semana.setRodadasEliminatorias(rodadaEliminatorias);

		carregarPartidas(semana);
		jogarPartidas(semana);
		salvarPartidas(semana);
		
		if (semana.getRodadasJogavel().get(0).isUltimaRodadaPontosCorridos()) {
			carregarClassificacao(semana);
			classificarComDesempate(semana);
			salvarClassificacao(semana);
		}

		promover(semana);

		incrementarRodadaAtualCampeonato(rodadas, rodadaEliminatorias);
		
		if (semana.isUltimaSemana()) {
			temporadaService.classificarClubesTemporadaAtual();
		}

		return SemanaDTO.convertToDTO(semana);
	}*/
	
	public SemanaDTO proximaSemana() {
		Temporada temporada = temporadaRepository.findFirstByAtual(true).get();
		temporada.setSemanaAtual(temporada.getSemanaAtual() + 1);
		Semana semana = semanaRepository.findFirstByTemporadaAndNumero(temporada, temporada.getSemanaAtual()).get();

		List<Rodada> rodadas = rodadaRepository.findBySemana(semana);
		List<RodadaEliminatoria> rodadaEliminatorias = rodadaEliminatoriaRepository.findBySemana(semana);
		List<RodadaAmistosa> rodadasAmistosas = rodadaAmistoraRepository.findBySemana(semana);
		
		semana.setRodadas(rodadas);
		semana.setRodadasEliminatorias(rodadaEliminatorias);
		semana.setRodadasAmistosas(rodadasAmistosas);

		/*carregarPartidas(semana);
		jogarPartidas(semana);
		salvarPartidas(semana);
		
		if (semana.getRodadasJogavel().get(0).isUltimaRodadaPontosCorridos()) {
			carregarClassificacao(semana);
			classificarComDesempate(semana);
			salvarClassificacao(semana);
		}*/

		List<CompletableFuture<RodadaJogavel>> rodadasFuture = new ArrayList<CompletableFuture<RodadaJogavel>>();

		for (Rodada r : semana.getRodadas()) {
			rodadasFuture.add(rodadaService.executarRodada(r));
		}

		for (RodadaEliminatoria r : semana.getRodadasEliminatorias()) {
			rodadasFuture.add(rodadaService.executarRodada(r));
		}
		
		for (RodadaAmistosa r : semana.getRodadasAmistosas()) {
			rodadasFuture.add(rodadaService.executarRodada(r));
		}

		CompletableFuture.allOf(rodadasFuture.toArray(new CompletableFuture<?>[0])).join();

		promover(semana);

		incrementarRodadaAtualCampeonato(rodadas, rodadaEliminatorias);
		
		if (semana.getNumero() >= 22 && semana.getNumero() <= 24) {
			//calcularProbabilidades(semana, temporada);
		}

		if (semana.isUltimaSemana()) {
			temporadaService.classificarClubesTemporadaAtual();
		}

		return SemanaDTO.convertToDTO(semana);
	}

	private void calcularProbabilidades(Semana semana, Temporada temporada) {
		
		List<Campeonato> campeonatos = campeonatoRepository.findByTemporada(temporada);
		
		List<CompletableFuture<Boolean>> calculoProbabilidadesFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		for (Campeonato c : campeonatos) {
			calculoProbabilidadesFuture.add(calcularProbabilidadeService.calcularProbabilidadesCampeonato(semana, c));
		}

		CompletableFuture.allOf(calculoProbabilidadesFuture.toArray(new CompletableFuture<?>[0])).join();
	}

	/*private void carregarClassificacao(Semana semana) {
		if (semana.getRodadas() != null) {
			for (Rodada r : semana.getRodadas()) {
				if(r.getCampeonato() != null) {
					r.getCampeonato().setClassificacao(classificacaoRepository.findByCampeonato(r.getCampeonato()));
				} else if (r.getGrupoCampeonato() != null) {
					r.getGrupoCampeonato().setClassificacao(classificacaoRepository.findByGrupoCampeonato(r.getGrupoCampeonato()));
				}
			}
		}
	}*/

	/* *
	 * Classifica os times com Desempate. Para ser executado na ultima rodada. 
	 * @param semana
	 */
	/*private void classificarComDesempate(Semana semana) {
		if (semana.getRodadas() != null) {
			for (Rodada r : semana.getRodadas()) {
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
		}
	}*/

	/*private void salvarClassificacao(Semana semana) {
		if (semana.getRodadas() != null) {
			for (Rodada r : semana.getRodadas()) {
				if(r.getCampeonato() != null) {
					classificacaoRepository.saveAll(r.getCampeonato().getClassificacao());
				} else if (r.getGrupoCampeonato() != null) {
					classificacaoRepository.saveAll(r.getGrupoCampeonato().getClassificacao());
				}
			}
		}
	}*/

	private void incrementarRodadaAtualCampeonato(List<Rodada> rodadas, List<RodadaEliminatoria> rodadaEliminatorias) {
		Set<Campeonato> camps1 = new HashSet<Campeonato>();
		Set<CampeonatoEliminatorio> camps2 = new HashSet<CampeonatoEliminatorio>();
		Set<CampeonatoMisto> camps3 = new HashSet<CampeonatoMisto>();

		camps1.addAll(rodadas.stream().filter(r -> r.getCampeonato() != null).map(r -> r.getCampeonato()).collect(Collectors.toSet()));
		camps2.addAll(rodadaEliminatorias.stream().filter(r -> r.getCampeonatoEliminatorio() != null).map(r -> r.getCampeonatoEliminatorio()).collect(Collectors.toSet()));
		camps3.addAll(rodadas.stream().filter(r -> r.getGrupoCampeonato() != null).map(r -> r.getGrupoCampeonato().getCampeonato()).collect(Collectors.toSet()));
		camps3.addAll(rodadaEliminatorias.stream().filter(r -> r.getCampeonatoMisto() != null).map(r -> r.getCampeonatoMisto()).collect(Collectors.toSet()));

		for (Campeonato c : camps1) {
			c.setRodadaAtual(c.getRodadaAtual() + 1);
		}

		for (CampeonatoEliminatorio c : camps2) {
			c.setRodadaAtual(c.getRodadaAtual() + 1);
		}

		for (CampeonatoMisto c : camps3) {
			c.setRodadaAtual(c.getRodadaAtual() + 1);
		}

		campeonatoRepository.saveAllAndFlush(camps1);
		campeonatoEliminatorioRepository.saveAllAndFlush(camps2);
		campeonatoMistoRepository.saveAllAndFlush(camps3);
	}

	/*private void carregarPartidas(Semana semana) {

		if (semana.getRodadas() != null) {
			for (Rodada r : semana.getRodadas()) {
				r.setPartidas(partidaRepository.findByRodada(r));
				
				if(r.getCampeonato() != null) {
					r.getCampeonato().setClassificacao(classificacaoRepository.findByCampeonato(r.getCampeonato()));
				} else if (r.getGrupoCampeonato() != null) {
					r.getGrupoCampeonato().setClassificacao(classificacaoRepository.findByGrupoCampeonato(r.getGrupoCampeonato()));
				}
			}
		}
		if (semana.getRodadasEliminatorias() != null) {
			for (RodadaEliminatoria r : semana.getRodadasEliminatorias()) {
				r.setPartidas(partidaEliminatoriaRepository.findByRodada(r));
			}
		}
	}*/

	/*private void jogarPartidas(Semana semana) {
		if (semana.getRodadas() != null) {
			for (Rodada r : semana.getRodadas()) {
				if(r.getCampeonato() != null) {
					partidaResultadoService.jogarRodada(r, r.getCampeonato().getClassificacao());
					
					if (!r.isUltimaRodadaPontosCorridos()) {
						ClassificacaoUtil.ordernarClassificacao(r.getCampeonato().getClassificacao(), false);
					}
				} else if (r.getGrupoCampeonato() != null) {
					partidaResultadoService.jogarRodada(r, r.getGrupoCampeonato().getClassificacao());
					
					if (!r.isUltimaRodadaPontosCorridos()) {
						ClassificacaoUtil.ordernarClassificacao(r.getGrupoCampeonato().getClassificacao(), false);
					}
				}
			}
		}
		if (semana.getRodadasEliminatorias() != null) {
			for (RodadaEliminatoria r : semana.getRodadasEliminatorias()) {
				partidaResultadoService.jogarRodada(r);
			}
		}
	}*/

	private void promover(Semana semana) {
		if (semana.getNumero() == Constantes.SEMANA_PROMOCAO_CONTINENTAL) {

			String estrategiaPromotorCont = parametroService.getParametroString(ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL);

			RodadaEliminatoria rodadaInicial = null;
			List<PartidaEliminatoriaResultado> partidas = null;
			List<GrupoCampeonato> grupos = null;
			
			Set<CampeonatoMisto> campeonatosMisto = new HashSet<CampeonatoMisto>();
			
			for (Rodada r : semana.getRodadas()) {
				if (r.getGrupoCampeonato() != null) {//Continentais
					campeonatosMisto.add(r.getGrupoCampeonato().getCampeonato());
				}
			}

			for (CampeonatoMisto c : campeonatosMisto) {
				grupos = grupoCampeonatoRepository.findByCampeonato(c);
				for (GrupoCampeonato g : grupos) {
					g.setClassificacao(classificacaoRepository.findByGrupoCampeonato(g));
				}
				rodadaInicial = rodadaEliminatoriaRepository.findFirstByCampeonatoMistoAndNumero(c, Constantes.NRO_PARTIDAS_FASE_GRUPOS + 1).get();
				partidas = partidaEliminatoriaRepository.findByRodada(rodadaInicial);
				c.setGrupos(grupos);
				rodadaInicial.setPartidas(partidas);
				c.setPrimeiraRodadaEliminatoria(rodadaInicial);
			}
			
			getPromotorContinental(estrategiaPromotorCont).promover(campeonatosMisto);
			for (CampeonatoMisto c : campeonatosMisto) {
				partidaEliminatoriaRepository.saveAll(c.getPrimeiraRodadaEliminatoria().getPartidas());
			}
		}
		if (semana.getNumero() == Constantes.SEMANA_PROMOCAO_CNII) {

			String numeroRodadasCopaNacional = parametroService.getParametroString(ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL);
			Boolean jogarCopaNacII = parametroService.getParametroBoolean(ParametroConstantes.JOGAR_COPA_NACIONAL_II);
			Integer nroCompeticoes = parametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
			
			if (jogarCopaNacII) {
				List<CampeonatoEliminatorio> campeonatos = semana.getRodadasEliminatorias().stream()
						.filter(r -> r.getCampeonatoEliminatorio() != null
								&& NivelCampeonato.COPA_NACIONAL.equals(r.getCampeonatoEliminatorio().getNivelCampeonato()))
						.map(RodadaEliminatoria::getCampeonatoEliminatorio)
						.collect(Collectors.toList());
				
				RodadaEliminatoria rodadaCNII = null, rodada1Fase, rodada2Fase;
				
				for (CampeonatoEliminatorio c : campeonatos) {
					CampeonatoEliminatorio copaNacionalII = campeonatoEliminatorioRepository
							.findFirstByTemporadaAndLigaAndNivelCampeonato(c.getTemporada(), c.getLiga(), NivelCampeonato.COPA_NACIONAL_II).get();
					
					rodadaCNII = rodadaEliminatoriaRepository.findByCampeonatoEliminatorioAndNumero(copaNacionalII, 1).get(0);
					rodadaCNII.setPartidas(partidaEliminatoriaRepository.findByRodada(rodadaCNII));
					
					rodada1Fase = rodadaEliminatoriaRepository.findByCampeonatoEliminatorioAndNumero(c, 1).get(0);
					rodada1Fase.setPartidas(partidaEliminatoriaRepository.findByRodada(rodada1Fase));
	
					rodada2Fase = rodadaEliminatoriaRepository.findByCampeonatoEliminatorioAndNumero(c, 2).get(0);
					rodada2Fase.setPartidas(partidaEliminatoriaRepository.findByRodada(rodada2Fase));
	
					PromotorEliminatoria promotorEliminatoria = getPromotorEliminatoria(numeroRodadasCopaNacional, nroCompeticoes);
					promotorEliminatoria.classificarCopaNacionalII(rodadaCNII, rodada1Fase, rodada2Fase);
					
					partidaEliminatoriaRepository.saveAll(rodadaCNII.getPartidas());
				}
			}
		}

	}

	private PromotorContinental getPromotorContinental(String estrategia) {
		
		//SEGUNDO_MELHOR_GRUPO, MELHOR_ELIMINADO_CAMPEONATO_SUPERIOR
		PromotorContinental promotorContinental = null;

		if (ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_SEG.equals(estrategia)) {
			promotorContinental = new PromotorContinentalImplDoisPorGrupo();
		} else if (ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_ELI.equals(estrategia)) {	
			promotorContinental = new PromotorContinentalImplInterCampeonatos();
		}

		return promotorContinental;
	}

	private PromotorEliminatoria getPromotorEliminatoria(String numeroRodadasCopaNacional, Integer nroCompeticoes) {
		PromotorEliminatoria promotorEliminatoria = null;
		
		if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R.equals(numeroRodadasCopaNacional)) {
			if (nroCompeticoes == 3) {
				promotorEliminatoria = new PromotorEliminatoriaImplVinteEOitoClubes();
			} else if (nroCompeticoes == 2) {
				promotorEliminatoria = new PromotorEliminatoriaImplTrintaEDoisClubes();
			}
		} else if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_5R.equals(numeroRodadasCopaNacional)) {
			if (nroCompeticoes == 3) {
				promotorEliminatoria = new PromotorEliminatoriaImplVinteClubes();
			} else if (nroCompeticoes == 2) {
				promotorEliminatoria = new PromotorEliminatoriaImplVinteEQuatroClubes();
			}
		}
		
		return promotorEliminatoria;
	}

	/*private void salvarPartidas(Semana semana) {
		boolean salvarEstatisticas = false;
		if (semana.getRodadas() != null) {
			for (Rodada r : semana.getRodadas()) {
				partidaRepository.saveAll(r.getPartidas());
				//if (salvarEstatisticas) { salvarEstatisticas(r.getPartidas()); }
				if(r.getCampeonato() != null) {
					classificacaoRepository.saveAll(r.getCampeonato().getClassificacao());
				} else if (r.getGrupoCampeonato() != null) {
					classificacaoRepository.saveAll(r.getGrupoCampeonato().getClassificacao());
				}
			}
		}
		if (semana.getRodadasEliminatorias() != null) {
			for (RodadaEliminatoria r : semana.getRodadasEliminatorias()) {
				partidaEliminatoriaRepository.saveAll(r.getPartidas());//TODO: salvar proximaPartida
				//if (salvarEstatisticas) { salvarEstatisticas(r.getPartidas()); }
			}
		}
	}*/

}

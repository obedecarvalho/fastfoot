package com.fastfoot.scheduler.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.Constantes;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.dto.SemanaDTO;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.CampeonatoEliminatorioRepository;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.model.repository.GrupoCampeonatoRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;
import com.fastfoot.scheduler.model.repository.RodadaEliminatoriaRepository;
import com.fastfoot.scheduler.model.repository.RodadaRepository;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;
import com.fastfoot.scheduler.service.util.ClassificacaoUtil;
import com.fastfoot.scheduler.service.util.PromotorEliminatoriaUtil;

@Service
public class SemanaService {
	
	@Autowired
	private PartidaResultadoService partidaResultadoService;

	@Autowired
	private TemporadaRepository temporadaRepository;

	@Autowired
	private SemanaRepository semanaRepository;

	@Autowired
	private RodadaRepository rodadaRepository;
	
	@Autowired
	private RodadaEliminatoriaRepository rodadaEliminatoriaRepository;

	@Autowired
	private PartidaResultadoRepository partidaRepository;

	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaRepository;

	@Autowired
	private CampeonatoEliminatorioRepository campeonatoEliminatorioRepository;

	@Autowired
	private GrupoCampeonatoRepository grupoCampeonatoRepository;
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;
	
	@Autowired
	private TemporadaService temporadaService;

	public SemanaDTO proximaSemana() {
		//Temporada temporada = temporadaRepository.findAtual().get(0);
		Temporada temporada = temporadaRepository.findFirstByAtual(true).get();
		temporada.setSemanaAtual(temporada.getSemanaAtual() + 1);
		//Semana semana = semanaRepository.findFirstByTemporadaAndNumero(temporada, temporada.getSemanaAtual()).get(0);
		Semana semana = semanaRepository.findFirstByTemporadaAndNumero(temporada, temporada.getSemanaAtual()).get();

		List<Rodada> rodadas = rodadaRepository.findBySemana(semana);
		List<RodadaEliminatoria> rodadaEliminatorias = rodadaEliminatoriaRepository.findBySemana(semana);
		
		semana.setRodadas(rodadas);
		semana.setRodadasEliminatorias(rodadaEliminatorias);
		
		carregarPartidas(semana);
		jogarPartidas(semana);
		promover(semana);
		salvarPartidas(semana);
		
		if (semana.getNumero() == Constantes.NUM_SEMANAS) {
			temporadaService.classificarClubesTemporadaAtual();
		}

		return SemanaDTO.convertToDTO(semana);
	}
	
	private void carregarPartidas(Semana semana) {

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
	}

	private void jogarPartidas(Semana semana) {
		if (semana.getRodadas() != null) {
			for (Rodada r : semana.getRodadas()) {
				if(r.getCampeonato() != null) {
					partidaResultadoService.jogarRodada(r, r.getCampeonato().getClassificacao());
					
					if (r.getNumero() == Constantes.NRO_RODADAS_CAMP_NACIONAL) {
						List<PartidaResultado> partidas = partidaRepository.findByCampeonato(r.getCampeonato());
						ClassificacaoUtil.ordernarClassificacao(r.getCampeonato().getClassificacao(), partidas);
					} else {
						ClassificacaoUtil.ordernarClassificacao(r.getCampeonato().getClassificacao(), false);
					}
				} else if (r.getGrupoCampeonato() != null) {
					partidaResultadoService.jogarRodada(r, r.getGrupoCampeonato().getClassificacao());
					
					if (r.getNumero() == Constantes.NRO_PARTIDAS_FASE_GRUPOS) {
						List<PartidaResultado> partidas = partidaRepository.findByGrupoCampeonato(r.getGrupoCampeonato());
						ClassificacaoUtil.ordernarClassificacao(r.getGrupoCampeonato().getClassificacao(), partidas);
					} else {
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
	}

	private void promover(Semana semana) {
		if (semana.getNumero() == Constantes.SEMANA_PROMOCAO_CONTINENTAL) {

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
				rodadaInicial = rodadaEliminatoriaRepository.findByCampeonatoMistoAndNumero(c, Constantes.NRO_PARTIDAS_FASE_GRUPOS + 1).get(0);
				partidas = partidaEliminatoriaRepository.findByRodada(rodadaInicial);
				c.setGrupos(grupos);
				rodadaInicial.setPartidas(partidas);
				PromotorEliminatoriaUtil.promoverGruposParaRodadasEliminatorias(grupos, rodadaInicial);
				partidaEliminatoriaRepository.saveAll(rodadaInicial.getPartidas());
			}
		}
		if (semana.getNumero() == Constantes.SEMANA_PROMOCAO_CNII) {
			
			List<CampeonatoEliminatorio> campeonatos = semana.getRodadasEliminatorias().stream()
					.filter(r -> r.getCampeonatoEliminatorio() != null
							&& NivelCampeonato.COPA_NACIONAL.equals(r.getCampeonatoEliminatorio().getNivelCampeonato()))
					.map(RodadaEliminatoria::getCampeonatoEliminatorio)
					.collect(Collectors.toList());
			
			RodadaEliminatoria rodadaCNII = null, rodada1Fase, rodada2Fase;
			
			for (CampeonatoEliminatorio c : campeonatos) {
				CampeonatoEliminatorio copaNacionalII = campeonatoEliminatorioRepository
						.findByTemporadaAndLigaAndNivelCampeonato(c.getTemporada(), c.getLiga(), NivelCampeonato.COPA_NACIONAL_II).get(0);
				
				rodadaCNII = rodadaEliminatoriaRepository.findByCampeonatoEliminatorioAndNumero(copaNacionalII, 1).get(0);
				rodadaCNII.setPartidas(partidaEliminatoriaRepository.findByRodada(rodadaCNII));
				
				rodada1Fase = rodadaEliminatoriaRepository.findByCampeonatoEliminatorioAndNumero(c, 1).get(0);
				rodada1Fase.setPartidas(partidaEliminatoriaRepository.findByRodada(rodada1Fase));

				rodada2Fase = rodadaEliminatoriaRepository.findByCampeonatoEliminatorioAndNumero(c, 2).get(0);
				rodada2Fase.setPartidas(partidaEliminatoriaRepository.findByRodada(rodada2Fase));

				PromotorEliminatoriaUtil.classificarCopaNacionalII(rodadaCNII, rodada1Fase, rodada2Fase);
				
				partidaEliminatoriaRepository.saveAll(rodadaCNII.getPartidas());
			}
		}

	}

	private void salvarPartidas(Semana semana) {
		if (semana.getRodadas() != null) {
			for (Rodada r : semana.getRodadas()) {
				partidaRepository.saveAll(r.getPartidas());
				if(r.getCampeonato() != null) {
					classificacaoRepository.saveAll(r.getCampeonato().getClassificacao());
				} else if (r.getGrupoCampeonato() != null) {
					classificacaoRepository.saveAll(r.getGrupoCampeonato().getClassificacao());
				}
			}
		}
		if (semana.getRodadasEliminatorias() != null) {
			for (RodadaEliminatoria r : semana.getRodadasEliminatorias()) {
				partidaEliminatoriaRepository.saveAll(r.getPartidas());
			}
		}
	}
}

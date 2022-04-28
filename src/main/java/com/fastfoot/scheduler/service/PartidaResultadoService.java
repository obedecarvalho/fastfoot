package com.fastfoot.scheduler.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.match.service.PartidaService;
import com.fastfoot.model.entity.Clube;
import com.fastfoot.model.repository.ClubeRepository;
import com.fastfoot.scheduler.model.CampeonatoJogavel;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.dto.PartidaResultadoDTO;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.CampeonatoEliminatorioRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoMistoRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoRepository;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.model.repository.GrupoCampeonatoRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;
import com.fastfoot.scheduler.model.repository.RodadaEliminatoriaRepository;
import com.fastfoot.scheduler.model.repository.RodadaRepository;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;
import com.fastfoot.scheduler.service.util.ClassificacaoUtil;

@Service
public class PartidaResultadoService {

	@Autowired
	private CampeonatoRepository campeonatoRepository;

	@Autowired
	private CampeonatoMistoRepository campeonatoMistoRepository;

	@Autowired
	private CampeonatoEliminatorioRepository campeonatoEliminatorioRepository;

	@Autowired
	private PartidaService partidaService;

	@Autowired
	private RodadaRepository rodadaRepository;

	@Autowired
	private RodadaEliminatoriaRepository rodadaEliminatoriaRepository;

	@Autowired
	private PartidaResultadoRepository partidaResultadoRepository;

	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaResultadoRepository;

	@Autowired
	private GrupoCampeonatoRepository grupoCampeonatoRepository;

	@Autowired
	private ClassificacaoRepository classificacaoRepository;
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private SemanaRepository semanaRepository;
	
	@Autowired
	private TemporadaRepository temporadaRepository;

	private void jogarPartida(PartidaResultadoJogavel partida) {
		partidaService.jogar(partida);
	}

	/*private void jogarPartida(PartidaResultadoJogavel partida, Integer numeroJogadas) {
		
		int golMandante = 0, golVisitante = 0;
		
		for (int i = 0; i < numeroJogadas; i++) {
			ElementoRoleta c = RoletaUtil.executarN2(partida.getClubeMandante(), partida.getClubeVisitante());
			if (partida.getClubeMandante().equals(c)) {
				golMandante++;
			} else if (partida.getClubeVisitante().equals(c)) {
				golVisitante++;
			}
		}

		partida.setGolsMandante(golMandante);
		partida.setGolsVisitante(golVisitante);

	}*/

	public void jogarRodada(Rodada rodada, List<Classificacao> classificacao) {

		for (PartidaResultado p : rodada.getPartidas()) {
			//jogarPartida(p, Constantes.NRO_JOGADAS_PARTIDA);
			jogarPartida(p);
		}
		
		ClassificacaoUtil.atualizarClassificacao(classificacao, rodada.getPartidas());
	}

	public void jogarRodada(RodadaEliminatoria rodada) {

		for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
			//jogarPartida(p, Constantes.NRO_JOGADAS_ELIMINATORIA);
			jogarPartida(p);
			
			if (p.getProximaPartida() != null) {
				PromotorEliminatoria.promoverProximaPartidaEliminatoria(p);
			}
		}
	}

	public List<PartidaResultadoDTO> getPartidasPorCampeonato(Long idCampeonato, String tipoCampeonato) {
		Optional<? extends CampeonatoJogavel> campeonato = null;

		if (tipoCampeonato.equals("NACIONAL")) {
			campeonato = campeonatoRepository.findById(idCampeonato);			
		} else if (tipoCampeonato.equals("COPA-NACIONAL")) {
			campeonato = campeonatoEliminatorioRepository.findById(idCampeonato);
		} else if (tipoCampeonato.equals("CONTINENTAL")) {
			campeonato = campeonatoMistoRepository.findById(idCampeonato);
		}

		return campeonato.isPresent() ? getPartidasPorCampeonato(campeonato.get()) : null;
	}
	
	public List<PartidaResultadoDTO> getPartidasPorCampeonato(CampeonatoJogavel campeonato) {
		List<PartidaResultadoDTO> partidas = new ArrayList<PartidaResultadoDTO>();
		
		if (campeonato instanceof Campeonato) {
			((Campeonato) campeonato).setRodadas(rodadaRepository.findByCampeonato((Campeonato) campeonato));
			for (Rodada r : ((Campeonato) campeonato).getRodadas()) {
				r.setPartidas(partidaResultadoRepository.findByRodada(r));
				partidas.addAll(PartidaResultadoDTO.convertToDTO(r.getPartidas()));
			}
		} else if (campeonato instanceof CampeonatoEliminatorio) {
			((CampeonatoEliminatorio) campeonato).setRodadas(rodadaEliminatoriaRepository.findByCampeonatoEliminatorio((CampeonatoEliminatorio) campeonato));
			for (RodadaEliminatoria r : ((CampeonatoEliminatorio) campeonato).getRodadas()) {
				r.setPartidas(partidaEliminatoriaResultadoRepository.findByRodada(r));
				partidas.addAll(PartidaResultadoDTO.convertToDTO(r.getPartidas()));
			}
		} else if (campeonato instanceof CampeonatoMisto) {
			//Fase final
			((CampeonatoMisto) campeonato).setRodadasEliminatorias(rodadaEliminatoriaRepository.findByCampeonatoMisto((CampeonatoMisto) campeonato));
			for (RodadaEliminatoria r : ((CampeonatoMisto) campeonato).getRodadasEliminatorias()) {
				r.setPartidas(partidaEliminatoriaResultadoRepository.findByRodada(r));
				partidas.addAll(PartidaResultadoDTO.convertToDTO(r.getPartidas()));
			}

			//Grupos
			((CampeonatoMisto) campeonato).setGrupos(grupoCampeonatoRepository.findByCampeonato((CampeonatoMisto) campeonato));
			for (GrupoCampeonato gc : ((CampeonatoMisto) campeonato).getGrupos()) {
				gc.setClassificacao(classificacaoRepository.findByGrupoCampeonato(gc));
				gc.setRodadas(rodadaRepository.findByGrupoCampeonato(gc));
				for (Rodada r : ((GrupoCampeonato) gc).getRodadas()) {
					r.setPartidas(partidaResultadoRepository.findByRodada(r));
					partidas.addAll(PartidaResultadoDTO.convertToDTO(r.getPartidas()));
				}
			}
		}
		return partidas;
	}
	
	public List<PartidaResultadoDTO> getPartidasPorClube(Integer idClube) {
		Optional<Temporada> temporadaOpt = temporadaRepository.findFirstByAtual(true);
		if (temporadaOpt.isPresent()) {
			return getPartidasPorClube(idClube, temporadaOpt.get());
		}
		return null;
	}
	
	public List<PartidaResultadoDTO> getPartidasPorClube(Integer idClube, Temporada temporada) {
		List<PartidaResultadoDTO> partidas = new ArrayList<PartidaResultadoDTO>();
		Optional<Clube> c = clubeRepository.findById(idClube);
		if (c.isPresent()) {
			partidas.addAll(PartidaResultadoDTO.convertToDTO(partidaResultadoRepository.findByTemporadaAndClube(temporada, c.get())));
			partidas.addAll(PartidaResultadoDTO.convertToDTO(partidaEliminatoriaResultadoRepository.findByTemporadaAndClube(temporada, c.get())));
		}
		return partidas;
	}
	
	public List<PartidaResultadoDTO> getPartidasPorSemana(Integer numeroSemana) {
		Optional<Temporada> temporadaOpt = temporadaRepository.findFirstByAtual(true);
		if (temporadaOpt.isPresent()) {
			return getPartidasPorSemana(numeroSemana, temporadaOpt.get());
		}
		return null;
	}
	
	public List<PartidaResultadoDTO> getPartidasPorSemana(Integer numeroSemana, Temporada temporada) {
		List<PartidaResultadoDTO> partidas = new ArrayList<PartidaResultadoDTO>();
		Optional<Semana> s = semanaRepository.findFirstByTemporadaAndNumero(temporada, numeroSemana);
		if (s.isPresent()) {
			partidas.addAll(PartidaResultadoDTO.convertToDTO(partidaResultadoRepository.findByTemporadaAndSemana(temporada, s.get())));
			partidas.addAll(PartidaResultadoDTO.convertToDTO(partidaEliminatoriaResultadoRepository.findByTemporadaAndSemana(temporada, s.get())));
		}
		return partidas;
	}

}

package com.fastfoot.club.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.dto.ClubeTituloAnoDTO;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.repository.ClubeRankingRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.probability.service.util.ClubeRankingProbabilidadeUtil;
import com.fastfoot.scheduler.model.ClassificacaoContinental;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacional;
import com.fastfoot.scheduler.model.ClassificacaoNacional;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.CampeonatoEliminatorioRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoMistoRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.RodadaEliminatoriaRepository;
import com.fastfoot.scheduler.service.crud.TemporadaCRUDService;
import com.fastfoot.service.CarregarParametroService;
import com.fastfoot.service.LigaJogoCRUDService;

@Service
public class ConsultarClubeCampeaoService {
	
	//###	SERVICE	###
	
	@Autowired
	private TemporadaCRUDService temporadaCRUDService;
	
	@Autowired
	private CarregarParametroService carregarParametroService;
	
	@Autowired
	private LigaJogoCRUDService ligaJogoCRUDService;
	
	//###	REPOSITORY	###
	
	@Autowired
	private CampeonatoMistoRepository campeonatoMistoRepository;
	
	@Autowired
	private CampeonatoEliminatorioRepository campeonatoEliminatorioRepository;
	
	@Autowired
	private RodadaEliminatoriaRepository rodadaEliminatoriaRepository;
	
	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaResultadoRepository;
	
	@Autowired
	private ClubeRankingRepository clubeRankingRepository;

	public List<ClubeTituloAnoDTO> consultarClubeCampeaoByAno(Jogo jogo, Integer ano) {
		Temporada temporada = temporadaCRUDService.getTemporadaAtual(jogo);

		List<ClubeTituloAnoDTO> clubeTituloAnosList = new ArrayList<ClubeTituloAnoDTO>();

		if (temporada.getAno().equals(ano) && temporada.getSemanaAtual() < Constantes.NUM_SEMANAS) {

			if (temporada.getSemanaAtual() >= 16) {
				clubeTituloAnosList.addAll(getCampeoesCopaNacional(temporada));
			}

			if (temporada.getSemanaAtual() >= 22) {
				clubeTituloAnosList.addAll(getCampeoesContinentais(temporada));
			}

		} else {

			temporada = temporadaCRUDService.getTemporadaByAno(jogo, ano);

			if (temporada != null) {
				clubeTituloAnosList.addAll(getCampeoes(temporada));
			}
		}

		return clubeTituloAnosList;
	}

	private List<ClubeTituloAnoDTO> getCampeoesContinentais(Temporada t) {
		List<ClubeTituloAnoDTO> clubeTituloAnosList = new ArrayList<ClubeTituloAnoDTO>();

		// Campeoes
		List<CampeonatoMisto> continentais = campeonatoMistoRepository.findByTemporada(t);

		RodadaEliminatoria r = null;
		List<PartidaEliminatoriaResultado> p = null;

		for (CampeonatoMisto c : continentais) {
			r = rodadaEliminatoriaRepository.findFirstByCampeonatoMistoAndNumero(c, 6).get();
			p = partidaEliminatoriaResultadoRepository.findByRodada(r);
			clubeTituloAnosList.add(new ClubeTituloAnoDTO(p.get(0).getClubeVencedor(),
					c.getNivelCampeonato(), t.getAno()));
		}

		return clubeTituloAnosList;
	}
	
	private List<ClubeTituloAnoDTO> getCampeoesCopaNacional(Temporada t) {

		List<ClubeTituloAnoDTO> clubeTituloAnosList = new ArrayList<ClubeTituloAnoDTO>();

		//List<CampeonatoEliminatorio> copasNacionais = null;

		//RodadaEliminatoria r = null;
		//List<PartidaEliminatoriaResultado> p = null;

		//int numeroRodadas = carregarParametroService.getNumeroRodadasCopaNacional();

		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(t.getJogo());
		for (LigaJogo liga : ligaJogos) {

			/*copasNacionais = campeonatoEliminatorioRepository.findByTemporadaAndLiga(t, liga);

			for (CampeonatoEliminatorio c : copasNacionais) {

				r = rodadaEliminatoriaRepository.findFirstByCampeonatoEliminatorioAndNumero(c,
						c.getNivelCampeonato().isCopaNacional() ? numeroRodadas : 4).get();
				p = partidaEliminatoriaResultadoRepository.findByRodada(r);

				clubeTituloAnosList.add(new ClubeTituloAnoDTO(p.get(0).getClubeVencedor(),
						c.getNivelCampeonato(), t.getAno()));

			}*/
			
			clubeTituloAnosList.addAll(getCampeoesCopaNacional(t, liga));
		}

		return clubeTituloAnosList;
	}

	private List<ClubeTituloAnoDTO> getCampeoesCopaNacional(Temporada t, LigaJogo liga) {

		List<ClubeTituloAnoDTO> clubeTituloAnosList = new ArrayList<ClubeTituloAnoDTO>();

		List<CampeonatoEliminatorio> copasNacionais = campeonatoEliminatorioRepository.findByTemporadaAndLigaJogo(t, liga);

		RodadaEliminatoria r = null;
		List<PartidaEliminatoriaResultado> p = null;

		int numeroRodadas = carregarParametroService.getNumeroRodadasCopaNacional(t.getJogo());

		for (CampeonatoEliminatorio c : copasNacionais) {

			r = rodadaEliminatoriaRepository.findFirstByCampeonatoEliminatorioAndNumero(c,
					c.getNivelCampeonato().isCopaNacional() ? numeroRodadas : 4).get();
			p = partidaEliminatoriaResultadoRepository.findByRodada(r);

			clubeTituloAnosList.add(new ClubeTituloAnoDTO(p.get(0).getClubeVencedor(),
					c.getNivelCampeonato(), t.getAno()));

		}

		return clubeTituloAnosList;
	}

	private List<ClubeTituloAnoDTO> getCampeoes(Temporada t) {

		List<ClubeTituloAnoDTO> clubeTituloAnosList = new ArrayList<ClubeTituloAnoDTO>();

		List<ClubeRanking> rankings = clubeRankingRepository.findByTemporada(t);

		clubeTituloAnosList.addAll(ClubeTituloAnoDTO.convertToDTO(rankings.stream()
				.filter(r -> ClassificacaoContinental.C_CAMPEAO.equals(r.getClassificacaoContinental()))
				.map(r -> r.getClube()).collect(Collectors.toList()), NivelCampeonato.CONTINENTAL, t.getAno()));

		clubeTituloAnosList.addAll(ClubeTituloAnoDTO.convertToDTO(
				rankings.stream()
						.filter(r -> ClassificacaoContinental.CII_CAMPEAO.equals(r.getClassificacaoContinental()))
						.map(r -> r.getClube()).collect(Collectors.toList()),
				NivelCampeonato.CONTINENTAL_II, t.getAno()));

		clubeTituloAnosList.addAll(ClubeTituloAnoDTO.convertToDTO(
				rankings.stream()
						.filter(r -> ClassificacaoContinental.CIII_CAMPEAO.equals(r.getClassificacaoContinental()))
						.map(r -> r.getClube()).collect(Collectors.toList()),
				NivelCampeonato.CONTINENTAL_III, t.getAno()));

		clubeTituloAnosList
				.addAll(ClubeTituloAnoDTO.convertToDTO(
						rankings.stream().filter(r -> ClassificacaoNacional.N_1.equals(r.getClassificacaoNacional()))
								.map(r -> r.getClube()).collect(Collectors.toList()),
						NivelCampeonato.NACIONAL, t.getAno()));

		clubeTituloAnosList
				.addAll(ClubeTituloAnoDTO.convertToDTO(
						rankings.stream().filter(r -> ClassificacaoNacional.NII_1.equals(r.getClassificacaoNacional()))
								.map(r -> r.getClube()).collect(Collectors.toList()),
						NivelCampeonato.NACIONAL_II, t.getAno()));

		clubeTituloAnosList.addAll(ClubeTituloAnoDTO.convertToDTO(
				rankings.stream()
						.filter(r -> ClassificacaoCopaNacional.CN_CAMPEAO.equals(r.getClassificacaoCopaNacional()))
						.map(r -> r.getClube()).collect(Collectors.toList()),
				NivelCampeonato.COPA_NACIONAL, t.getAno()));

		clubeTituloAnosList.addAll(ClubeTituloAnoDTO.convertToDTO(
				rankings.stream()
						.filter(r -> ClassificacaoCopaNacional.CNII_CAMPEAO.equals(r.getClassificacaoCopaNacional()))
						.map(r -> r.getClube()).collect(Collectors.toList()),
				NivelCampeonato.COPA_NACIONAL_II, t.getAno()));

		return clubeTituloAnosList;
	}
	
	public Map<Integer, Clube> getCampeoes(Temporada t, LigaJogo liga) {
		
		Map<Integer, Clube> clubesCampeoes = new HashMap<Integer, Clube>();
		
		List<ClubeTituloAnoDTO> clubeTituloAnoDTOs = new ArrayList<ClubeTituloAnoDTO>();

		clubeTituloAnoDTOs.addAll(getCampeoesContinentais(t));
		clubeTituloAnoDTOs.addAll(getCampeoesCopaNacional(t, liga));
		
		Optional<ClubeTituloAnoDTO> clubeCampeao;
		
		clubeCampeao = clubeTituloAnoDTOs.stream()
				.filter(ct -> NivelCampeonato.CONTINENTAL.equals(ct.getNivelCampeonato())).findFirst();
		if (clubeCampeao.isPresent()) {
			clubesCampeoes.put(ClubeRankingProbabilidadeUtil.CAMP_CONT, clubeCampeao.get().getClube());
		}

		clubeCampeao = clubeTituloAnoDTOs.stream()
				.filter(ct -> NivelCampeonato.CONTINENTAL_II.equals(ct.getNivelCampeonato())).findFirst();
		if (clubeCampeao.isPresent()) {
			clubesCampeoes.put(ClubeRankingProbabilidadeUtil.CAMP_CONT_II, clubeCampeao.get().getClube());
		}

		clubeCampeao = clubeTituloAnoDTOs.stream()
				.filter(ct -> NivelCampeonato.CONTINENTAL_III.equals(ct.getNivelCampeonato())).findFirst();
		if (clubeCampeao.isPresent()) {
			clubesCampeoes.put(ClubeRankingProbabilidadeUtil.CAMP_CONT_III, clubeCampeao.get().getClube());
		}
		
		clubeCampeao = clubeTituloAnoDTOs.stream()
				.filter(ct -> NivelCampeonato.COPA_NACIONAL.equals(ct.getNivelCampeonato())).findFirst();
		if (clubeCampeao.isPresent()) {
			clubesCampeoes.put(ClubeRankingProbabilidadeUtil.CAMP_COPA_NAC, clubeCampeao.get().getClube());
		}
		
		clubeCampeao = clubeTituloAnoDTOs.stream()
				.filter(ct -> NivelCampeonato.COPA_NACIONAL_II.equals(ct.getNivelCampeonato())).findFirst();
		if (clubeCampeao.isPresent()) {
			clubesCampeoes.put(ClubeRankingProbabilidadeUtil.CAMP_COPA_NAC_II, clubeCampeao.get().getClube());
		}
		
		return clubesCampeoes;
	}
}

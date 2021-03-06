package com.fastfoot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.dto.ClubeTituloAnoDTO;
import com.fastfoot.club.model.dto.ClubeTituloRankingDTO;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.model.repository.ClubeTituloRankingRepository;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.ClassificacaoContinentalFinal;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacionalFinal;
import com.fastfoot.scheduler.model.ClassificacaoNacionalFinal;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.dto.ClubeDTO;
import com.fastfoot.scheduler.model.dto.ClubeRankingDTO;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.ClubeRanking;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.CampeonatoEliminatorioRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoMistoRepository;
import com.fastfoot.scheduler.model.repository.ClubeRankingRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.RodadaEliminatoriaRepository;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;
import com.fastfoot.scheduler.service.TemporadaService;

@Service
public class ClubeService {
	
	@Autowired
	private ClubeRepository clubeRepository;

	@Autowired
	private ClubeRankingRepository clubeRankingRepository;
	
	@Autowired
	private ClubeTituloRankingRepository clubeTituloRankingRepository;

	@Autowired
	private TemporadaService temporadaService;
	
	@Autowired
	private CampeonatoMistoRepository campeonatoMistoRepository;
	
	@Autowired
	private CampeonatoEliminatorioRepository campeonatoEliminatorioRepository;
	
	@Autowired
	private RodadaEliminatoriaRepository rodadaEliminatoriaRepository;

	@Autowired
	private ParametroService parametroService;

	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaResultadoRepository;

	@Autowired
	private TemporadaRepository temporadaRepository;

	public List<ClubeDTO> getClubesPorLiga(String liga) {//'GENEBE', 'SPAPOR', 'ITAFRA', 'ENGLND'
		List<Clube> clubes = clubeRepository.findByLiga(Liga.valueOf(liga));
		return ClubeDTO.convertToDTO(clubes);
	}
	
	public List<ClubeRankingDTO> getClubesRankings(String liga){//'GENEBE', 'SPAPOR', 'ITAFRA', 'ENGLND'
		Temporada temporada = temporadaService.getTemporadaAtual();
		return temporada != null ? ClubeRankingDTO.convertToDTO(clubeRankingRepository.findByLigaAndAno(Liga.valueOf(liga), temporada.getAno()-1)) : null;
	}

	public List<ClubeRankingDTO> getClubesRankings(String liga, Integer ano){//'GENEBE', 'SPAPOR', 'ITAFRA', 'ENGLND'
		return ClubeRankingDTO.convertToDTO(clubeRankingRepository.findByLigaAndAno(Liga.valueOf(liga), ano));
	}

	public List<Integer> getAnosClubeRanking(){
		return clubeRankingRepository.findAnosClubeRanking();
	}

	public List<ClubeTituloAnoDTO> getClubesCampeoesPorAno(Integer ano) {
		Temporada temporada = temporadaService.getTemporadaAtual();
		
		List<ClubeTituloAnoDTO> clubeTituloAnosList = new ArrayList<ClubeTituloAnoDTO>();
		
		if (temporada.getAno().equals(ano) && temporada.getSemanaAtual() < 25) {
			
			if (temporada.getSemanaAtual() >= 16) {
				clubeTituloAnosList.addAll(getCampeoesCopaNacional(temporada));
			}

			if (temporada.getSemanaAtual() >= 22) {
				clubeTituloAnosList.addAll(getCampeoesContinentais(temporada));
			}

		} else {
			
			Optional<Temporada> temporadaOpt = temporadaRepository.findFirstByAno(ano);
			
			if (temporadaOpt.isPresent()) {
				clubeTituloAnosList.addAll(getCampeoes(temporadaOpt.get()));
			}
		}

		return clubeTituloAnosList;
	}

	private List<ClubeTituloAnoDTO> getCampeoesContinentais(Temporada t) {
		List<ClubeTituloAnoDTO> clubeTituloAnosList = new ArrayList<ClubeTituloAnoDTO>();
		
		//Campeoes
		List<CampeonatoMisto> continentais = campeonatoMistoRepository.findByTemporada(t);
		
		RodadaEliminatoria r = null;
		List<PartidaEliminatoriaResultado> p = null;

		for (CampeonatoMisto c : continentais) {
			r = rodadaEliminatoriaRepository.findFirstByCampeonatoMistoAndNumero(c, 6).get();
			p = partidaEliminatoriaResultadoRepository.findByRodada(r);
			/*if (c.getNivelCampeonato().isContinental()) {
				clubesCampeoes.put(ClassificacaoContinentalFinal.C_CAMPEAO.name(), Arrays.asList(p.get(0).getClubeVencedor()));
			} else if (c.getNivelCampeonato().isContinentalII()) {
				clubesCampeoes.put(ClassificacaoContinentalFinal.CII_CAMPEAO.name(), Arrays.asList(p.get(0).getClubeVencedor()));
			} else if (c.getNivelCampeonato().isContinentalIII()) {
				clubesCampeoes.put(ClassificacaoContinentalFinal.CIII_CAMPEAO.name(), Arrays.asList(p.get(0).getClubeVencedor()));
			}*/
			clubeTituloAnosList.add(new ClubeTituloAnoDTO(p.get(0).getClubeVencedor().getNome(), c.getNivelCampeonato().name(), t.getAno()));
		}
		
		return clubeTituloAnosList;
	}

	private List<ClubeTituloAnoDTO> getCampeoes(Temporada t) {
		
		List<ClubeTituloAnoDTO> clubeTituloAnosList = new ArrayList<ClubeTituloAnoDTO>();

		List<ClubeRanking> rankings = clubeRankingRepository.findByTemporada(t);

		clubeTituloAnosList.addAll(ClubeTituloAnoDTO.convertToDTO(rankings.stream()
				.filter(r -> ClassificacaoContinentalFinal.C_CAMPEAO.equals(r.getClassificacaoContinental()))
				.map(r -> r.getClube()).collect(Collectors.toList()), NivelCampeonato.CONTINENTAL, t.getAno()));
		
		clubeTituloAnosList.addAll(ClubeTituloAnoDTO.convertToDTO(rankings.stream()
				.filter(r -> ClassificacaoContinentalFinal.CII_CAMPEAO.equals(r.getClassificacaoContinental()))
				.map(r -> r.getClube()).collect(Collectors.toList()), NivelCampeonato.CONTINENTAL_II, t.getAno()));
		
		clubeTituloAnosList.addAll(ClubeTituloAnoDTO.convertToDTO(rankings.stream()
				.filter(r -> ClassificacaoContinentalFinal.CIII_CAMPEAO.equals(r.getClassificacaoContinental()))
				.map(r -> r.getClube()).collect(Collectors.toList()), NivelCampeonato.CONTINENTAL_III, t.getAno()));
		
		clubeTituloAnosList.addAll(ClubeTituloAnoDTO.convertToDTO(rankings.stream().filter(r -> ClassificacaoNacionalFinal.N_1.equals(r.getClassificacaoNacional()))
				.map(r -> r.getClube()).collect(Collectors.toList()), NivelCampeonato.NACIONAL, t.getAno()));
		
		clubeTituloAnosList.addAll(ClubeTituloAnoDTO.convertToDTO(rankings.stream().filter(r -> ClassificacaoNacionalFinal.NII_1.equals(r.getClassificacaoNacional()))
				.map(r -> r.getClube()).collect(Collectors.toList()), NivelCampeonato.NACIONAL_II, t.getAno()));
		
		clubeTituloAnosList.addAll(ClubeTituloAnoDTO.convertToDTO(rankings.stream()
				.filter(r -> ClassificacaoCopaNacionalFinal.CN_CAMPEAO.equals(r.getClassificacaoCopaNacional()))
				.map(r -> r.getClube()).collect(Collectors.toList()), NivelCampeonato.COPA_NACIONAL, t.getAno()));
		
		clubeTituloAnosList.addAll(ClubeTituloAnoDTO.convertToDTO(rankings.stream()
				.filter(r -> ClassificacaoCopaNacionalFinal.CNII_CAMPEAO.equals(r.getClassificacaoCopaNacional()))
				.map(r -> r.getClube()).collect(Collectors.toList()), NivelCampeonato.COPA_NACIONAL_II, t.getAno()));

		return clubeTituloAnosList;
	}

	private List<ClubeTituloAnoDTO> getCampeoesCopaNacional(Temporada t) {

		List<ClubeTituloAnoDTO> clubeTituloAnosList = new ArrayList<ClubeTituloAnoDTO>();
		
		List<CampeonatoEliminatorio> copasNacionais = null;
		
		//List<Clube> clubes = null;
		
		RodadaEliminatoria r = null;
		List<PartidaEliminatoriaResultado> p = null;

		int numeroRodadas = parametroService.getNumeroRodadasCopaNacional();
		
		for (Liga liga : Liga.getAll()) {
		
			copasNacionais = campeonatoEliminatorioRepository.findByTemporadaAndLiga(t, liga);
	
			for (CampeonatoEliminatorio c : copasNacionais) {
	
				r = rodadaEliminatoriaRepository.findFirstByCampeonatoEliminatorioAndNumero(c,
						c.getNivelCampeonato().isCopaNacional() ? numeroRodadas : 4).get();
				p = partidaEliminatoriaResultadoRepository.findByRodada(r);
				
				/*clubes = clubesCampeoes.get(c.getNivelCampeonato().name());
				
				if (clubes == null) {
					clubes = new ArrayList<Clube>();
					
					//clubesCampeoes.put(c.getNivelCampeonato().name(), clubes);
					clubesCampeoes.put(ClassificacaoCopaNacionalFinal.getClassificacaoCampeao(c.getNivelCampeonato()).name(), clubes);
				}
				
				clubes.add(p.get(0).getClubeVencedor());*/
				
				clubeTituloAnosList.add(new ClubeTituloAnoDTO(p.get(0).getClubeVencedor().getNome(), c.getNivelCampeonato().name(), t.getAno()));
	
				/*if (c.getNivelCampeonato().isCopaNacional()) {
					clubesCampeoes.put(ClassificacaoCopaNacionalFinal.CN_CAMPEAO.name(), p.get(0).getClubeVencedor());
				} else if (c.getNivelCampeonato().isCopaNacionalII()) {
					clubesCampeoes.put(ClassificacaoCopaNacionalFinal.CNII_CAMPEAO.name(), p.get(0).getClubeVencedor());
				}*/
			}
		}
		
		return clubeTituloAnosList;
	}

	public List<ClubeTituloRankingDTO> getClubesTitulosRankings(String ligaStr){//'GENEBE', 'SPAPOR', 'ITAFRA', 'ENGLND'
		
		Liga liga = null;

		try {
			liga = Liga.valueOf(ligaStr);
		} catch (Exception e) {
			// TODO: handle exception
		}

		Temporada temporada = temporadaService.getTemporadaAtual();

		if (liga == null) {
			return temporada != null ? ClubeTituloRankingDTO.convertToDTO(clubeTituloRankingRepository.findAll()) : null;
		}

		return temporada != null ? ClubeTituloRankingDTO.convertToDTO(clubeTituloRankingRepository.findByLiga(liga)) : null;
	}
}

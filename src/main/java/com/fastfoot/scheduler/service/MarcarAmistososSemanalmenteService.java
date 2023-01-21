package com.fastfoot.scheduler.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.repository.PartidaAmistosaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;
import com.fastfoot.scheduler.model.repository.RodadaAmistoraRepository;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class MarcarAmistososSemanalmenteService {
	
	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaRepository;
	
	@Autowired
	private PartidaResultadoRepository partidaResultadoRepository;
	
	@Autowired
	private PartidaAmistosaResultadoRepository partidaAmistosaResultadoRepository;
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private RodadaAmistoraRepository rodadaAmistoraRepository;

	public void marcarAmistososSemanalmente(Semana semanaAmistoso) {
		
		//TODO: sortear mando de campo
		//TODO: dar um shuffle na lista OU fazer heuristica para evitar muitos amistosos entre os mesmos dois clubes 
		
		RodadaAmistosa rodadaAmistosa = new RodadaAmistosa(100 + semanaAmistoso.getNumero(), NivelCampeonato.AMISTOSO_INTERNACIONAL);
		RodadaAmistosa rodadaAmistosaNacional = new RodadaAmistosa(200 + semanaAmistoso.getNumero(), NivelCampeonato.AMISTOSO_NACIONAL);
		
		rodadaAmistosa.setPartidas(new ArrayList<PartidaAmistosaResultado>());
		rodadaAmistosaNacional.setPartidas(new ArrayList<PartidaAmistosaResultado>());
		rodadaAmistosa.setSemana(semanaAmistoso);
		rodadaAmistosaNacional.setSemana(semanaAmistoso);

		List<Clube> clubes = clubeRepository.findAll();

		List<PartidaResultado> partidas = partidaResultadoRepository.findBySemana(semanaAmistoso);

		List<PartidaAmistosaResultado> partidasAmistosas = partidaAmistosaResultadoRepository
				.findBySemana(semanaAmistoso);

		List<PartidaEliminatoriaResultado> partidasEliminatorias = partidaEliminatoriaRepository
				.findBySemana(semanaAmistoso);

		Set<Clube> clubesComJogos = new HashSet<Clube>();

		clubesComJogos.addAll(partidas.stream().filter(p -> p.getClubeMandante() != null).map(p -> p.getClubeMandante())
				.collect(Collectors.toSet()));
		clubesComJogos.addAll(partidas.stream().filter(p -> p.getClubeVisitante() != null)
				.map(p -> p.getClubeVisitante()).collect(Collectors.toSet()));

		clubesComJogos.addAll(partidasAmistosas.stream().filter(p -> p.getClubeMandante() != null)
				.map(p -> p.getClubeMandante()).collect(Collectors.toSet()));
		clubesComJogos.addAll(partidasAmistosas.stream().filter(p -> p.getClubeVisitante() != null)
				.map(p -> p.getClubeVisitante()).collect(Collectors.toSet()));

		clubesComJogos.addAll(partidasEliminatorias.stream().filter(p -> p.getClubeMandante() != null)
				.map(p -> p.getClubeMandante()).collect(Collectors.toSet()));
		clubesComJogos.addAll(partidasEliminatorias.stream().filter(p -> p.getClubeVisitante() != null)
				.map(p -> p.getClubeVisitante()).collect(Collectors.toSet()));

		clubes.removeAll(clubesComJogos);
		
		Map<Liga, List<Clube>> clubesLiga = clubes.stream().collect(Collectors.groupingBy(Clube::getLiga));
		
		/*System.err.println(semanaAmistoso.getNumero() + ":" + clubes.size());
		System.err.println("\tG:" + clubesLiga.get(Liga.GENEBE).size());
		System.err.println("\tE:" + clubesLiga.get(Liga.ENGLND).size());
		System.err.println("\tS:" + clubesLiga.get(Liga.SPAPOR).size());
		System.err.println("\tI:" + clubesLiga.get(Liga.ITAFRA).size());*/
		
		criarPartidas(clubesLiga, rodadaAmistosa, rodadaAmistosaNacional, clubes.size());
		
		//System.err.println(rodadaAmistosa.getPartidas());

		if (!ValidatorUtil.isEmpty(rodadaAmistosa.getPartidas())) {
			rodadaAmistoraRepository.save(rodadaAmistosa);
			partidaAmistosaResultadoRepository.saveAll(rodadaAmistosa.getPartidas());
		}
		
		if (!ValidatorUtil.isEmpty(rodadaAmistosaNacional.getPartidas())) {
			rodadaAmistoraRepository.save(rodadaAmistosaNacional);
			partidaAmistosaResultadoRepository.saveAll(rodadaAmistosaNacional.getPartidas());
		}
	}
	
	private void criarPartidas(Map<Liga, List<Clube>> clubesLiga, RodadaAmistosa rodadaAmistosa, RodadaAmistosa rodadaAmistosaNacional, int totalClubes) {
		
		List<Clube> clubesA = null;
		List<Clube> clubesB = null;
		Liga ligaA = null;
		Liga ligaB = null;
		
		while (totalClubes > 0) {
			
			for (Liga l : Liga.getAll()) {
				if (clubesA == null || clubesLiga.get(l).size() > clubesA.size()) {
					clubesA = clubesLiga.get(l);
					ligaA = l;
				}
			}
			
			for (Liga l : Liga.getAll()) {
				if (!ligaA.equals(l) && (clubesB == null || clubesLiga.get(l).size() > clubesB.size())) {
					clubesB = clubesLiga.get(l);
					ligaB = l;
				}
			}
			
			if (clubesB.size() > 0) {

				criarPartidas(rodadaAmistosa, clubesA, clubesB);
				
				totalClubes -= (clubesB.size() * 2);
				
				clubesLiga.get(ligaA).removeAll(clubesA.subList(0, clubesB.size()));
				clubesLiga.get(ligaB).removeAll(clubesB);

			} else if (clubesA.size() > 0 && clubesB.size() == 0) {

				criarAmistososNacionais(rodadaAmistosaNacional, clubesA);
				totalClubes = 0;//encerra loop

			}

			clubesA = null;
			clubesB = null;
			ligaA = null;
			ligaB = null;

		}
		
	}
	
	private void criarPartidas(RodadaAmistosa rodadaAmistosa, List<Clube> clubesA, List<Clube> clubesB) {

		//if (clubesB.size() > clubesA.size()) throw new RuntimeException("Comportamento Inesperado");

		PartidaAmistosaResultado partida;

		for (int i = 0; i < clubesB.size(); i++) {
			partida = new PartidaAmistosaResultado();
			partida.setClubeMandante(clubesA.get(i));
			partida.setClubeVisitante(clubesB.get(i));
			partida.setRodada(rodadaAmistosa);
			rodadaAmistosa.getPartidas().add(partida);
		}
		
	}
	
	private void criarAmistososNacionais(RodadaAmistosa rodadaAmistosa, List<Clube> clubesA) {

		PartidaAmistosaResultado partida;

		int qtdePartidas = clubesA.size() / 2;

		for (int i = 0; i < qtdePartidas; i++) {
			partida = new PartidaAmistosaResultado();
			partida.setClubeMandante(clubesA.get(2 * i));
			partida.setClubeVisitante(clubesA.get(2 * i + 1));
			partida.setRodada(rodadaAmistosa);
			rodadaAmistosa.getPartidas().add(partida);
		}

	}
}

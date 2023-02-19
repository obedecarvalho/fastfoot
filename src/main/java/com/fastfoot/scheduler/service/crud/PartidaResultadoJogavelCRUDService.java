package com.fastfoot.scheduler.service.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.CampeonatoJogavel;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.PartidaAmistosaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;

@Service
public class PartidaResultadoJogavelCRUDService {

	@Autowired
	private PartidaResultadoRepository partidaResultadoRepository;

	@Autowired
	private PartidaAmistosaResultadoRepository partidaAmistosaResultadoRepository;

	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaResultadoRepository;
	
	@Autowired
	private TemporadaCRUDService temporadaCRUDService;
	
	@Autowired
	private SemanaCRUDService semanaCRUDService;
	
	@Autowired
	private CampeonatoJogavelCRUDService campeonatoJogavelCRUDService;

	public List<PartidaResultadoJogavel> getAll() {

		List<PartidaResultadoJogavel> partidas = new ArrayList<PartidaResultadoJogavel>();

		partidas.addAll(partidaResultadoRepository.findAll());
		partidas.addAll(partidaEliminatoriaResultadoRepository.findAll());
		partidas.addAll(partidaAmistosaResultadoRepository.findAll());

		return partidas;
	}
	
	public List<PartidaResultadoJogavel> getByIdCampeonato(Long idCampeonato) {
		return getByCampeonato(campeonatoJogavelCRUDService.getById(idCampeonato));
	}

	public List<PartidaResultadoJogavel> getByCampeonato(CampeonatoJogavel campeonato) {

		List<PartidaResultadoJogavel> partidas = new ArrayList<PartidaResultadoJogavel>();

		if (campeonato instanceof Campeonato) {
			partidas.addAll(partidaResultadoRepository.findByCampeonato((Campeonato) campeonato));
		} else if (campeonato instanceof CampeonatoMisto) {
			partidas.addAll(partidaResultadoRepository.findByCampeonato((CampeonatoMisto) campeonato));
			partidas.addAll(partidaEliminatoriaResultadoRepository.findByCampeonato((CampeonatoMisto) campeonato));
		} else if (campeonato instanceof CampeonatoEliminatorio) {
			partidas.addAll(partidaEliminatoriaResultadoRepository.findByCampeonato((CampeonatoEliminatorio) campeonato));
		}

		return partidas;
	}

	public List<PartidaResultadoJogavel> getByClube(Clube clube) {
		List<PartidaResultadoJogavel> partidas = new ArrayList<PartidaResultadoJogavel>();

		partidas.addAll(partidaResultadoRepository.findByClube(clube));
		partidas.addAll(partidaEliminatoriaResultadoRepository.findByClube(clube));
		partidas.addAll(partidaAmistosaResultadoRepository.findByClube(clube));

		return partidas;
	}
	
	public List<PartidaResultadoJogavel> getByClubeAndTemporadaAtual(Clube clube) {
		Temporada temporada = temporadaCRUDService.getTemporadaAtual();
		
		List<PartidaResultadoJogavel> partidas = new ArrayList<PartidaResultadoJogavel>();

		partidas.addAll(partidaResultadoRepository.findByTemporadaAndClube(temporada, clube));
		partidas.addAll(partidaEliminatoriaResultadoRepository.findByTemporadaAndClube(temporada, clube));
		partidas.addAll(partidaAmistosaResultadoRepository.findByTemporadaAndClube(temporada, clube));

		return partidas;
	}
	
	public List<PartidaResultadoJogavel> getByNumeroSemanaTemporadaAtual(Integer numeroSemana) {
		return getBySemana(semanaCRUDService.getByNumeroSemanaTemporadaAtual(numeroSemana));
	}

	public List<PartidaResultadoJogavel> getBySemana(Semana semana) {
		List<PartidaResultadoJogavel> partidas = new ArrayList<PartidaResultadoJogavel>();

		partidas.addAll(partidaResultadoRepository.findBySemana(semana));
		partidas.addAll(partidaEliminatoriaResultadoRepository.findBySemana(semana));
		partidas.addAll(partidaAmistosaResultadoRepository.findBySemana(semana));

		return partidas;
	}

	public PartidaResultadoJogavel getById(Long id) {

		Optional<? extends PartidaResultadoJogavel> partidaOpt = partidaResultadoRepository.findById(id);
		if (partidaOpt.isPresent()) {
			return partidaOpt.get();
		}

		partidaOpt = partidaEliminatoriaResultadoRepository.findById(id);
		if (partidaOpt.isPresent()) {
			return partidaOpt.get();
		}

		partidaOpt = partidaAmistosaResultadoRepository.findById(id);
		if (partidaOpt.isPresent()) {
			return partidaOpt.get();
		}

		return null;
	}

}

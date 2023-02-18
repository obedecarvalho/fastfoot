package com.fastfoot.scheduler.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.CampeonatoEliminatorioRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoMistoRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.service.CRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class PartidaEliminatoriaCRUDService implements CRUDService<PartidaEliminatoriaResultado, Long>{

	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaResultadoRepository;
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private SemanaRepository semanaRepository;
	
	@Autowired
	private CampeonatoMistoRepository campeonatoMistoRepository;
	
	@Autowired
	private CampeonatoEliminatorioRepository campeonatoEliminatorioRepository;
	
	@Autowired
	private TemporadaCRUDService temporadaService;
	
	@Override
	public List<PartidaEliminatoriaResultado> getAll() {
		return partidaEliminatoriaResultadoRepository.findAll();
	}

	@Override
	public PartidaEliminatoriaResultado getById(Long id) {
		Optional<PartidaEliminatoriaResultado> partidaOpt = partidaEliminatoriaResultadoRepository.findById(id);
		if (partidaOpt.isPresent()) {
			return partidaOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		partidaEliminatoriaResultadoRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		partidaEliminatoriaResultadoRepository.deleteAll();
	}

	@Override
	public PartidaEliminatoriaResultado create(PartidaEliminatoriaResultado t) {
		return partidaEliminatoriaResultadoRepository.save(t);
	}

	@Override
	public PartidaEliminatoriaResultado update(PartidaEliminatoriaResultado t) {
		return partidaEliminatoriaResultadoRepository.save(t);
	}

	public List<PartidaEliminatoriaResultado> getByIdClube(Integer idClube){
		
		Optional<Clube> clubeOpt = clubeRepository.findById(idClube);
		
		if (!clubeOpt.isPresent()) {
			return null;
		}
		
		List<PartidaEliminatoriaResultado> partidas = partidaEliminatoriaResultadoRepository
				.findByTemporadaAndClube(temporadaService.getTemporadaAtual(), clubeOpt.get());
		
		return partidas;
	}

	public List<PartidaEliminatoriaResultado> getByIdCampeonato(Long idCampeonato) {

		Optional<CampeonatoEliminatorio> campeonatoOpt = campeonatoEliminatorioRepository.findById(idCampeonato);

		Optional<CampeonatoMisto> campeonatoMistoOpt = null;

		List<PartidaEliminatoriaResultado> partidas = null;

		if (!campeonatoOpt.isPresent()) {
			campeonatoMistoOpt = campeonatoMistoRepository.findById(idCampeonato);
			if (campeonatoMistoOpt.isPresent()) {
				partidas = partidaEliminatoriaResultadoRepository.findByCampeonato(campeonatoMistoOpt.get());
			}
		} else {
			partidas = partidaEliminatoriaResultadoRepository.findByCampeonato(campeonatoOpt.get());
		}

		return partidas;
	}
	
	public List<PartidaEliminatoriaResultado> getByNumeroSemana(Integer numeroSemana){
		
		Temporada temporadaAtual = temporadaService.getTemporadaAtual();
		
		Optional<Semana> semanaOpt = semanaRepository.findFirstByTemporadaAndNumero(temporadaAtual, numeroSemana);
		
		if (!semanaOpt.isPresent()) {
			return null;
		}
		
		List<PartidaEliminatoriaResultado> partidas = partidaEliminatoriaResultadoRepository
				.findBySemana(semanaOpt.get());
		
		return partidas;
	}
	
	public List<PartidaEliminatoriaResultado> getByIdClubeIdCampeonatoNumeroSemana(Integer idClube, Long idCampeonato,
			Integer numeroSemana) {

		if (!ValidatorUtil.isEmpty(idClube) && !ValidatorUtil.isEmpty(idCampeonato)
				&& !ValidatorUtil.isEmpty(numeroSemana)) {
			// TODO
		} else if (!ValidatorUtil.isEmpty(idClube)) {
			return getByIdClube(idClube);
		} else if (!ValidatorUtil.isEmpty(idCampeonato)) {
			return getByIdCampeonato(idCampeonato);
		} else if (!ValidatorUtil.isEmpty(numeroSemana)) {
			return getByNumeroSemana(numeroSemana);
		}

		return null;
	}
}

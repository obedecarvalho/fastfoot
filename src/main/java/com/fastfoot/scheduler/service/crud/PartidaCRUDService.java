package com.fastfoot.scheduler.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.CampeonatoMistoRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.scheduler.service.TemporadaService;
import com.fastfoot.service.CRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class PartidaCRUDService implements CRUDService<PartidaResultado, Long>{
	
	@Autowired
	private PartidaResultadoRepository partidaResultadoRepository;
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private SemanaRepository semanaRepository;
	
	@Autowired
	private CampeonatoRepository campeonatoRepository;
	
	@Autowired
	private CampeonatoMistoRepository campeonatoMistoRepository;
	
	@Autowired
	private TemporadaService temporadaService;

	@Override
	public List<PartidaResultado> getAll() {
		return partidaResultadoRepository.findAll();
	}

	@Override
	public PartidaResultado getById(Long id) {
		Optional<PartidaResultado> partidaOpt = partidaResultadoRepository.findById(id);
		if (partidaOpt.isPresent()) {
			return partidaOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		partidaResultadoRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		partidaResultadoRepository.deleteAll();
	}

	@Override
	public PartidaResultado create(PartidaResultado t) {
		return partidaResultadoRepository.save(t);
	}

	@Override
	public PartidaResultado update(PartidaResultado t) {
		return partidaResultadoRepository.save(t);
	}
	
	public List<PartidaResultado> getByIdClube(Integer idClube){
		
		Optional<Clube> clubeOpt = clubeRepository.findById(idClube);
		
		if (!clubeOpt.isPresent()) {
			return null;
		}
		
		List<PartidaResultado> partidas = partidaResultadoRepository
				.findByTemporadaAndClube(temporadaService.getTemporadaAtual(), clubeOpt.get());
		
		return partidas;
	}

	public List<PartidaResultado> getByIdCampeonato(Long idCampeonato) {

		Optional<Campeonato> campeonatoOpt = campeonatoRepository.findById(idCampeonato);

		Optional<CampeonatoMisto> campeonatoMistoOpt = null;

		List<PartidaResultado> partidas = null;

		if (!campeonatoOpt.isPresent()) {
			campeonatoMistoOpt = campeonatoMistoRepository.findById(idCampeonato);
			if (campeonatoMistoOpt.isPresent()) {
				partidas = partidaResultadoRepository.findByCampeonato(campeonatoMistoOpt.get());
			}
		} else {
			partidas = partidaResultadoRepository.findByCampeonato(campeonatoOpt.get());
		}

		return partidas;
	}
	
	public List<PartidaResultado> getByNumeroSemana(Integer numeroSemana){
		
		Temporada temporadaAtual = temporadaService.getTemporadaAtual();
		
		Optional<Semana> semanaOpt = semanaRepository.findFirstByTemporadaAndNumero(temporadaAtual, numeroSemana);
		
		if (!semanaOpt.isPresent()) {
			return null;
		}
		
		List<PartidaResultado> partidas = partidaResultadoRepository.findBySemana(semanaOpt.get());
		
		return partidas;
	}
	
	public List<PartidaResultado> getByIdClubeIdCampeonatoNumeroSemana(Integer idClube, Long idCampeonato,
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

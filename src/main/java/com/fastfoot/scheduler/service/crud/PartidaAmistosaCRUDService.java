package com.fastfoot.scheduler.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.PartidaAmistosaResultadoRepository;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.scheduler.service.TemporadaService;
import com.fastfoot.service.CRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class PartidaAmistosaCRUDService implements CRUDService<PartidaAmistosaResultado, Long> {

	@Autowired
	private PartidaAmistosaResultadoRepository partidaAmistosaResultadoRepository;
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private SemanaRepository semanaRepository;
	
	@Autowired
	private TemporadaService temporadaService;

	@Override
	public List<PartidaAmistosaResultado> getAll() {
		return partidaAmistosaResultadoRepository.findAll();
	}

	@Override
	public PartidaAmistosaResultado getById(Long id) {
		Optional<PartidaAmistosaResultado> partidaOpt = partidaAmistosaResultadoRepository.findById(id);
		if (partidaOpt.isPresent()) {
			return partidaOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		partidaAmistosaResultadoRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		partidaAmistosaResultadoRepository.deleteAll();
	}

	@Override
	public PartidaAmistosaResultado create(PartidaAmistosaResultado t) {
		return partidaAmistosaResultadoRepository.save(t);
	}

	@Override
	public PartidaAmistosaResultado update(PartidaAmistosaResultado t) {
		return partidaAmistosaResultadoRepository.save(t);
	}
	
	public List<PartidaAmistosaResultado> getByIdClube(Integer idClube) {

		Optional<Clube> clubeOpt = clubeRepository.findById(idClube);

		if (!clubeOpt.isPresent()) {
			return null;
		}

		List<PartidaAmistosaResultado> partidas = partidaAmistosaResultadoRepository
				.findByTemporadaAndClube(temporadaService.getTemporadaAtual(), clubeOpt.get());

		return partidas;
	}

	public List<PartidaAmistosaResultado> getByNumeroSemana(Integer numeroSemana) {

		Temporada temporadaAtual = temporadaService.getTemporadaAtual();

		Optional<Semana> semanaOpt = semanaRepository.findFirstByTemporadaAndNumero(temporadaAtual, numeroSemana);

		if (!semanaOpt.isPresent()) {
			return null;
		}

		List<PartidaAmistosaResultado> partidas = partidaAmistosaResultadoRepository.findBySemana(semanaOpt.get());

		return partidas;
	}
	
	public List<PartidaAmistosaResultado> getByIdClubeNumeroSemana(Integer idClube, Integer numeroSemana) {

		if (!ValidatorUtil.isEmpty(idClube) && !ValidatorUtil.isEmpty(numeroSemana)) {
			// TODO
		} else if (!ValidatorUtil.isEmpty(idClube)) {
			return getByIdClube(idClube);
		} else if (!ValidatorUtil.isEmpty(numeroSemana)) {
			return getByNumeroSemana(numeroSemana);
		}

		return null;
	}
}

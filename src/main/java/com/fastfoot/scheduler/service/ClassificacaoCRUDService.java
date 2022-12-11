package com.fastfoot.scheduler.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.repository.CampeonatoMistoRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoRepository;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.service.CRUDService;

@Service
public class ClassificacaoCRUDService implements CRUDService<Classificacao, Long> {
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;
	
	@Autowired
	private CampeonatoRepository campeonatoRepository;

	@Autowired
	private CampeonatoMistoRepository campeonatoMistoRepository;

	@Override
	public List<Classificacao> getAll() {
		return classificacaoRepository.findAll();
	}

	@Override
	public Classificacao getById(Long id) {
		Optional<Classificacao> classificacaoOpt = classificacaoRepository.findById(id);
		if (classificacaoOpt.isPresent()) {
			return classificacaoOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		classificacaoRepository.deleteById(id);
		
	}

	@Override
	public void deleteAll() {
		classificacaoRepository.deleteAll();
	}

	@Override
	public Classificacao create(Classificacao t) {
		return classificacaoRepository.save(t);
	}

	@Override
	public Classificacao update(Classificacao t) {
		return classificacaoRepository.save(t);
	}

	public List<Classificacao> getByIdCampeonato(Long idCampeonato){
		
		List<Classificacao> classificacoes = null;
		
		Optional<Campeonato> campeonatoOpt = campeonatoRepository.findById(idCampeonato);
		
		if (campeonatoOpt.isPresent()) {
			classificacoes = classificacaoRepository.findByCampeonatoOrderByPosicao(campeonatoOpt.get());
		} else {
			Optional<CampeonatoMisto> campeonatoMistoOpt = campeonatoMistoRepository.findById(idCampeonato);
			if (campeonatoMistoOpt.isPresent()) {
				classificacoes = classificacaoRepository.findByCampeonatoMistoOrderByPosicao(campeonatoMistoOpt.get());
			}
		}
		
		return classificacoes;
	}
}

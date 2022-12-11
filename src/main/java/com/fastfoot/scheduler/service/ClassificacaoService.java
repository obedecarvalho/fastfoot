package com.fastfoot.scheduler.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.scheduler.model.CampeonatoJogavel;
import com.fastfoot.scheduler.model.dto.ClassificacaoDTO;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.repository.CampeonatoMistoRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoRepository;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;

@Service
public class ClassificacaoService {
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;

	@Autowired
	private CampeonatoRepository campeonatoRepository;

	@Autowired
	private CampeonatoMistoRepository campeonatoMistoRepository;

	@Deprecated
	public List<ClassificacaoDTO> getClassificacaoCampeonato(Long idCampeonato, String tipoCampeonato) {
		Optional<? extends CampeonatoJogavel> campeonato = null;

		if (tipoCampeonato.equals("NACIONAL")) {
			campeonato = campeonatoRepository.findById(idCampeonato);			
		} else if (tipoCampeonato.equals("CONTINENTAL")) {
			campeonato = campeonatoMistoRepository.findById(idCampeonato);
		}

		return campeonato.isPresent() ? getClassificacaoCampeonato(campeonato.get()) : null;
	}

	@Deprecated
	public List<ClassificacaoDTO> getClassificacaoCampeonato(CampeonatoJogavel campeonato) {
		List<ClassificacaoDTO> classificacao = new ArrayList<ClassificacaoDTO>();
		
		if (campeonato instanceof Campeonato) {
			classificacao.addAll(ClassificacaoDTO.convertToDTO(classificacaoRepository.findByCampeonatoOrderByPosicao((Campeonato) campeonato)));
		} else if (campeonato instanceof CampeonatoMisto){
			classificacao.addAll(ClassificacaoDTO.convertToDTO(classificacaoRepository.findByCampeonatoMistoOrderByPosicao((CampeonatoMisto) campeonato)));
		}
		
		return classificacao;
	}
}

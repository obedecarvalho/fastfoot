package com.fastfoot.probability.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.probability.model.dto.ClubeProbabilidadeDTO;
import com.fastfoot.probability.model.entity.ClubeProbabilidade;
import com.fastfoot.probability.model.repository.ClubeProbabilidadeRepository;
import com.fastfoot.scheduler.model.dto.ClassificacaoDTO;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.CampeonatoRepository;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.scheduler.service.TemporadaService;

@Service
public class ClubeProbabilidadeService {

	@Autowired
	private ClubeProbabilidadeRepository clubeProbabilidadeRepository;

	/*@Autowired
	private ClubeProbabilidadePosicaoRepository clubeProbabilidadePosicaoRepository;*/
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;

	@Autowired
	private CampeonatoRepository campeonatoRepository;
	
	@Autowired
	private TemporadaService temporadaService;

	@Autowired
	private SemanaRepository semanaRepository;
	
	public List<ClubeProbabilidadeDTO> getProbabilidadePorCampeonato(Long idCampeonato) {
		Optional<Campeonato> campeonatoOpt = campeonatoRepository.findById(idCampeonato);
		
		if (!campeonatoOpt.isPresent()) {
			return null; //TODO
		}
		
		Temporada t = temporadaService.getTemporadaAtual();
		
		Semana s = semanaRepository.findFirstByTemporadaAndNumero(t, t.getSemanaAtual()).get();
		
		List<ClubeProbabilidade> clubesProbabilidades = clubeProbabilidadeRepository.findByCampeonatoAndSemana(campeonatoOpt.get(), s);
		
		List<ClubeProbabilidadeDTO> dtos = new ArrayList<ClubeProbabilidadeDTO>();
		
		List<Classificacao> classificacao = classificacaoRepository.findByCampeonato(campeonatoOpt.get());
		
		Map<Clube, Classificacao> clubeClassificacao = classificacao.stream()
				.collect(Collectors.toMap(Classificacao::getClube, Function.identity()));
		
		for (ClubeProbabilidade cp : clubesProbabilidades) {
			ClubeProbabilidadeDTO dto = ClubeProbabilidadeDTO.convertToDTO(cp);
			
			dto.setClassificacao(ClassificacaoDTO.convertToDTO(clubeClassificacao.get(cp.getClube())));
			
			dtos.add(dto);
		}

		Collections.sort(dtos, new Comparator<ClubeProbabilidadeDTO>() {

			@Override
			public int compare(ClubeProbabilidadeDTO o1, ClubeProbabilidadeDTO o2) {
				return o1.getClassificacao().getPosicao().compareTo(o2.getClassificacao().getPosicao());
			}
		});

		return dtos;
	}
}

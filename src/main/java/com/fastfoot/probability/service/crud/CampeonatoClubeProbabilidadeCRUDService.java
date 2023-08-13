package com.fastfoot.probability.service.crud;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.probability.model.entity.CampeonatoClubeProbabilidade;
import com.fastfoot.probability.model.repository.CampeonatoClubeProbabilidadeRepository;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.service.crud.SemanaCRUDService;
import com.fastfoot.service.CRUDService;

@Service
public class CampeonatoClubeProbabilidadeCRUDService implements CRUDService<CampeonatoClubeProbabilidade, Long> {

	@Autowired
	private CampeonatoClubeProbabilidadeRepository clubeProbabilidadeRepository;
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;
	
	@Autowired
	private SemanaCRUDService semanaCRUDService;

	@Override
	public List<CampeonatoClubeProbabilidade> getAll() {
		return clubeProbabilidadeRepository.findAll();
	}

	@Override
	public CampeonatoClubeProbabilidade getById(Long id) {
		Optional<CampeonatoClubeProbabilidade> clubeOpt = clubeProbabilidadeRepository.findById(id);
		if (clubeOpt.isPresent()) {
			return clubeOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		clubeProbabilidadeRepository.deleteById(id);		
	}

	@Override
	public void deleteAll() {
		clubeProbabilidadeRepository.deleteAll();
	}

	@Override
	public CampeonatoClubeProbabilidade create(CampeonatoClubeProbabilidade t) {
		return clubeProbabilidadeRepository.save(t);
	}

	@Override
	public CampeonatoClubeProbabilidade update(CampeonatoClubeProbabilidade t) {
		return clubeProbabilidadeRepository.save(t);
	}
	
	public List<CampeonatoClubeProbabilidade> getByCampeonato(Campeonato campeonato){
		return clubeProbabilidadeRepository.findByCampeonato(campeonato);
	}

	public List<CampeonatoClubeProbabilidade> getByCampeonatoAndSemana(Campeonato campeonato, Semana semana){
		return clubeProbabilidadeRepository.findByCampeonatoAndSemana(campeonato, semana);
	}
	
	public List<CampeonatoClubeProbabilidade> getByCampeonatoRodadaAtual(Campeonato campeonato){
		return clubeProbabilidadeRepository.findByCampeonatoRodadaAtual(campeonato.getId());
	}
	
	public List<CampeonatoClubeProbabilidade> getByCampeonatoAndRodadaAtualComClassificacao(Campeonato campeonato){
		
		Semana semanaAtual = semanaCRUDService.getSemanaAtual();

		List<CampeonatoClubeProbabilidade> probabilidades;

		/*
		 * Ao fim da semana 22 as probabilidade são recalculadas para compreender probabilidade de classificação
		 * continental e copa nacional, mesmo não tendo jogos na campeonato nacional.
		 */
		if (semanaAtual.getNumero() == 22) {
			probabilidades = getByCampeonatoAndSemana(campeonato, semanaAtual);
		} else {
			probabilidades = getByCampeonatoRodadaAtual(campeonato);
		}
		
		List<Classificacao> classificacoes = classificacaoRepository.findByCampeonato(campeonato);
		
		Map<Clube, Classificacao> clubeClassificacao = classificacoes.stream()
				.collect(Collectors.toMap(Classificacao::getClube, Function.identity()));

		probabilidades.stream().forEach(p -> p.setClassificacao(clubeClassificacao.get(p.getClube())));

		return probabilidades;
	}
}

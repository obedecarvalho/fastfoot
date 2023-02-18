package com.fastfoot.probability.service.crud;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.probability.model.entity.ClubeProbabilidade;
import com.fastfoot.probability.model.repository.ClubeProbabilidadeRepository;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.service.crud.SemanaCRUDService;
import com.fastfoot.service.CRUDService;

@Service
public class ClubeProbabilidadeCRUDService implements CRUDService<ClubeProbabilidade, Long> {

	@Autowired
	private ClubeProbabilidadeRepository clubeProbabilidadeRepository;
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;
	
	@Autowired
	private SemanaCRUDService semanaCRUDService;

	@Override
	public List<ClubeProbabilidade> getAll() {
		return clubeProbabilidadeRepository.findAll();
	}

	@Override
	public ClubeProbabilidade getById(Long id) {
		Optional<ClubeProbabilidade> clubeOpt = clubeProbabilidadeRepository.findById(id);
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
	public ClubeProbabilidade create(ClubeProbabilidade t) {
		return clubeProbabilidadeRepository.save(t);
	}

	@Override
	public ClubeProbabilidade update(ClubeProbabilidade t) {
		return clubeProbabilidadeRepository.save(t);
	}
	
	public List<ClubeProbabilidade> getByCampeonato(Campeonato campeonato){
		return clubeProbabilidadeRepository.findByCampeonato(campeonato);
	}

	public List<ClubeProbabilidade> getByCampeonatoAndSemana(Campeonato campeonato, Semana semana){
		return clubeProbabilidadeRepository.findByCampeonatoAndSemana(campeonato, semana);
	}
	
	public List<ClubeProbabilidade> getByCampeonatoAndSemanaAtual(Campeonato campeonato){
		return getByCampeonatoAndSemana(campeonato, semanaCRUDService.getSemanaAtual());
	}
	
	public List<ClubeProbabilidade> getByCampeonatoAndSemanaAtualComClassificacao(Campeonato campeonato){
		
		List<ClubeProbabilidade> probabilidades = getByCampeonatoAndSemana(campeonato, semanaCRUDService.getSemanaAtual());
		
		List<Classificacao> classificacoes = classificacaoRepository.findByCampeonato(campeonato);
		
		Map<Clube, Classificacao> clubeClassificacao = classificacoes.stream()
				.collect(Collectors.toMap(Classificacao::getClube, Function.identity()));

		probabilidades.stream().forEach(p -> p.setClassificacao(clubeClassificacao.get(p.getClube())));

		return probabilidades;
	}
}

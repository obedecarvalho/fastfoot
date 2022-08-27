package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.HabilidadeEstatisticaPercentil;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatisticaGrupo;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.HabilidadeValorEstatisticaGrupoRepository;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;

@Service
public class AtualizarPassoDesenvolvimentoJogadorService {
	
	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;

	/*@Autowired
	private AgruparHabilidadeValorEstatisticaService agruparHabilidadeValorEstatisticaService;*/

	/*@Autowired
	private TemporadaRepository temporadaRepository;*/
	
	/*@Autowired
	private HabilidadeValorEstatisticaGrupoRepository habilidadeValorEstatisticaGrupoRepository;*/

	//private static final Random R = new Random();
	
	/*@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> ajustarPassoDesenvolvimento(List<Jogador> jogadores) {
		
		for (Jogador j : jogadores) {
			j.setIdade(j.getIdade() + 1);
			if (j.getIdade() < JogadorFactory.IDADE_MAX) {
				ajustarPassoDesenvolvimento(j);
			}
		}
		
		jogadorRepository.saveAll(jogadores);
		for (Jogador jog : jogadores) {
			habilidadeValorRepository.saveAll(jog.getHabilidades());
		}
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}*/

	@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> ajustarPassoDesenvolvimento(List<Jogador> jogadores) {
		
		//
		//TODO: mover para CriarCalendarioTemporadaService.atualizarPassoDesenvolvimentoJogador()
		/*Temporada temporada = null;
		temporada = temporadaRepository.findFirstByOrderByAnoDesc().get();*/

		//HabilidadeEstatisticaPercentil hep = agruparHabilidadeValorEstatisticaService.getPercentilHabilidadeValor(temporada);
		/*List<HabilidadeValorEstatisticaGrupo> estatisticasGrupo = habilidadeValorEstatisticaGrupoRepository
				.findByTemporada(temporada);*/
		
		/*Map<HabilidadeValor, HabilidadeValorEstatisticaGrupo> estatisticasGrupoMap = estatisticasGrupo.stream()
				.collect(Collectors.toMap(HabilidadeValorEstatisticaGrupo::getHabilidadeValor, Function.identity()));*/
		
		//
		
		for (Jogador j : jogadores) {
			j.setIdade(j.getIdade() + 1);
			if (j.getIdade() < JogadorFactory.IDADE_MAX) {
				JogadorFactory.getInstance().ajustarPassoDesenvolvimento(j);
				//JogadorFactory.getInstance().ajustarPassoDesenvolvimento(j, hep, estatisticasGrupoMap);
			}
		}
		
		List<HabilidadeValor> habilidadeValores = new ArrayList<HabilidadeValor>();
		for (Jogador jog : jogadores) {
			habilidadeValores.addAll(jog.getHabilidades());
		}
		
		jogadorRepository.saveAll(jogadores);
		habilidadeValorRepository.saveAll(habilidadeValores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> ajustarPassoDesenvolvimento(List<Jogador> jogadores,
			HabilidadeEstatisticaPercentil hep,
			Map<HabilidadeValor, HabilidadeValorEstatisticaGrupo> estatisticasGrupoMap) {
		
		//
		//TODO: mover para CriarCalendarioTemporadaService.atualizarPassoDesenvolvimentoJogador()
		/*Temporada temporada = null;
		temporada = temporadaRepository.findFirstByOrderByAnoDesc().get();*/

		//HabilidadeEstatisticaPercentil hep = agruparHabilidadeValorEstatisticaService.getPercentilHabilidadeValor(temporada);
		/*List<HabilidadeValorEstatisticaGrupo> estatisticasGrupo = habilidadeValorEstatisticaGrupoRepository
				.findByTemporada(temporada);*/
		
		/*Map<HabilidadeValor, HabilidadeValorEstatisticaGrupo> estatisticasGrupoMap = estatisticasGrupo.stream()
				.collect(Collectors.toMap(HabilidadeValorEstatisticaGrupo::getHabilidadeValor, Function.identity()));*/
		
		//
		
		for (Jogador j : jogadores) {
			j.setIdade(j.getIdade() + 1);
			if (j.getIdade() < JogadorFactory.IDADE_MAX) {
				//JogadorFactory.getInstance().ajustarPassoDesenvolvimento(j);
				JogadorFactory.getInstance().ajustarPassoDesenvolvimento(j, hep, estatisticasGrupoMap);
			}
		}
		
		List<HabilidadeValor> habilidadeValores = new ArrayList<HabilidadeValor>();
		for (Jogador jog : jogadores) {
			habilidadeValores.addAll(jog.getHabilidades());
		}
		
		jogadorRepository.saveAll(jogadores);
		habilidadeValorRepository.saveAll(habilidadeValores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	/*private void ajustarPassoDesenvolvimento(Jogador j) {//Passo desenvolvimento REGULAR

		//Double ajusteForca = JogadorFactory.getAjusteForca(j.getIdade());
		Double ajusteForcaProx = JogadorFactory.getAjusteForca(j.getIdade() + 1);
		//Integer potencialSorteado = null;
		//Double forca = null;
		Double passoProx = null;
		
		for (HabilidadeValor hv : j.getHabilidades()) {
			//potencialSorteado = hv.getPotencialDesenvolvimento();
			//forca = hv.getPotencialDesenvolvimento() * ajusteForca;
			passoProx = ((hv.getPotencialDesenvolvimento() * ajusteForcaProx) - hv.getValorTotal()) / JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR;
			hv.setPassoDesenvolvimento(passoProx);
		}
	}*/

	/*private void ajustarPassoDesenvolvimento(Jogador j) {

		Double ajusteForcaProx = JogadorFactory.getAjusteForca(j.getIdade() + 1);
		Double passoProx = null;
		
		Double variacao = null;
		List<Double> valorHabilidadesEspecificasPotEfetiva = new ArrayList<Double>();
		
		for (HabilidadeValor hv : j.getHabilidades()) {
			//passoProx = ((hv.getPotencialDesenvolvimento() * ajusteForcaProx) - forca) / JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR;
			passoProx = ((hv.getPotencialDesenvolvimentoEfetivo() * ajusteForcaProx) - hv.getValorTotal()) / JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR;
			variacao = gerarVariacaoPassoDesenvolvimento(passoProx, j.getIdade());
			hv.setPassoDesenvolvimento(passoProx + variacao);
			hv.setPotencialDesenvolvimentoEfetivo(hv.getPotencialDesenvolvimentoEfetivo() + variacao * JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR);
			if (hv.getHabilidadeEspecifica()) {
				valorHabilidadesEspecificasPotEfetiva.add(hv.getPotencialDesenvolvimentoEfetivo() + variacao * JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR);
			}
		}
		
		j.setForcaGeralPotencialEfetiva(
				(valorHabilidadesEspecificasPotEfetiva.stream().mapToDouble(v -> v).average().getAsDouble()));
	}*/
	
	/*private Double gerarVariacaoPassoDesenvolvimento(Double passo, Integer idade) {
		Double stdev = JogadorFactory.getPercVariacaoPassoDesenvolvimento(idade) * passo;
		return R.nextGaussian() * stdev;
	}*/
}

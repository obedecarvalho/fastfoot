package com.fastfoot.player.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.util.JogadorCalcularForcaUtil;

@Service
public class DesenvolverJogadorService {

	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private CalcularHabilidadeGrupoValorService calcularHabilidadeGrupoValorService;
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> desenvolverJogador(List<Jogador> jogadores) {
		
		for (Jogador jogador : jogadores) {
			desenvolverJogador(jogador);
		}
		
		calcularHabilidadeGrupoValorService.calcularHabilidadeGrupoValor(jogadores);
		
		habilidadeValorRepository.saveAll(jogadores.stream().flatMap(j -> j.getHabilidades().stream()).collect(Collectors.toList()));
		jogadorRepository.saveAll(jogadores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	private void desenvolverJogador(Jogador jogador) {

		Double newValorTotal = null;
		for (HabilidadeValor hv : jogador.getHabilidades()) {
			newValorTotal = hv.getValorTotal() + hv.getPassoDesenvolvimento();
	
			hv.setValor(newValorTotal.intValue());
			hv.setValorDecimal(newValorTotal);
		}
		
		JogadorCalcularForcaUtil.calcularForcaGeral(jogador);
	}

	/*@Async("defaultExecutor")
	public CompletableFuture<Boolean> desenvolverGrupo(List<GrupoDesenvolvimentoJogador> grupoDesenvolvimento) {
		
		//Jogador j = null;

		for (GrupoDesenvolvimentoJogador gdj : grupoDesenvolvimento) {
			//j = jogadorRepository.findByJogadorFetchHabilidades(gdj.getJogador()).get(0);
			gdj.setQtdeExecAno((gdj.getQtdeExecAno() + 1));
			gdj.setQtdeExec((gdj.getQtdeExec() + 1));
			desenvolverJogador(gdj, gdj.getJogador());
			//gdj.setJogador(j);//
			
			if (gdj.getQtdeExecAno().equals(JogadorFactory.QTDE_DESENVOLVIMENTO_ANO_JOGADOR.intValue())) {
				gdj.setQtdeExecAno(0);
			}

		}

		List<Jogador> jogadores = null;
		
		jogadores = grupoDesenvolvimento.stream().map(gd -> gd.getJogador()).collect(Collectors.toList());
		jogadorRepository.saveAll(jogadores);
		for (Jogador jog : jogadores) {
			habilidadeValorRepository.saveAll(jog.getHabilidades());
		}
		
		grupoDesenvolvimentoJogadorRepository.saveAll(grupoDesenvolvimento);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	/*public void desenvolverCelula(CelulaDesenvolvimento celulaDesenvolvimento) {
		habilidadeValorRepository.desenvolverHabilidadesByCelulaDesenvolvimento(celulaDesenvolvimento.ordinal());
	}*/
	
	/*private Integer getPesoPassoDesenvolvimento() {
		/*
		 * 90% do passo + 20% de uso das habilidades?
		 * ir variando por idade?
		 * /
		return 1;
	}*/

	/*private void desenvolverJogador(GrupoDesenvolvimentoJogador grupoDesenvolvimentoJogador, Jogador j) {
		//Caso for usar so passo, pode ser substuido por sql UPDATE tab SET valor = valor + passo ....
		Double newValorTotal = null;
		//Double newValorDecimal = null;
		for (HabilidadeValor hv : j.getHabilidades()) {
			//newValorTotal = hv.getValorTotal() + (hv.getPassoDesenvolvimento() * getPesoPassoDesenvolvimento());
			newValorTotal = hv.getValorTotal() + hv.getPassoDesenvolvimento();
			
			//newValorDecimal = newValorTotal - newValorTotal.intValue();
			
			hv.setValor(newValorTotal.intValue());
			hv.setValorDecimal(newValorTotal);
		}
		
		JogadorCalcularForcaUtil.calcularForcaGeral(j);
		
		/*if (grupoDesenvolvimentoJogador.getQtdeExecAno().equals(JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR.intValue())) {
			j.setIdade(j.getIdade() + 1);
			ajustarPassoDesenvolvimentoProximoAno(j);
		}* /
		
		//habilidadeValorRepository.saveAll(j.getHabilidades());
		//jogadorRepository.save(j);
		//grupoDesenvolvimentoJogadorRepository.save(grupoDesenvolvimentoJogador);
	}

	/*private void ajustarPassoDesenvolvimentoProximoAno(Jogador j) {

		Double ajusteForca = JogadorFactory.getAjusteForca(j.getIdade());
		Double ajusteForcaProx = JogadorFactory.getAjusteForca(j.getIdade() + 1);
		Integer potencialSorteado = null;
		Double forca = null;
		Double passoProx = null;
		
		for (HabilidadeValor hv : j.getHabilidades()) {
			potencialSorteado = hv.getPotencialDesenvolvimento();
			forca = potencialSorteado * ajusteForca;
			passoProx = ((potencialSorteado * ajusteForcaProx) - forca) / JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR;			
			hv.setPassoDesenvolvimentoAno(passoProx);
		}
	}*/
}

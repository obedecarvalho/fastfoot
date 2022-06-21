package com.fastfoot.player.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.entity.GrupoDesenvolvimentoJogador;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.GrupoDesenvolvimentoJogadorRepository;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.util.JogadorCalcularForcaUtil;

@Service
public class DesenvolverJogadorService {

	@Autowired
	private GrupoDesenvolvimentoJogadorRepository grupoDesenvolvimentoJogadorRepository;
	
	/*@Autowired
	private GrupoDesenvolvimentoRepository desenvolvimentoRepository;*/
	
	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;

	/*@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> desenvolverGrupo(GrupoDesenvolvimento grupoDesenvolvimento) {
		/*List<GrupoDesenvolvimentoJogador> grupoJogador = grupoDesenvolvimentoJogadorRepository
				.findByGrupoDesenvolvimentoAndAtivoFetchJogadorHabilidade(grupoDesenvolvimento, true);* /
		
		Jogador j = null;
		
		grupoDesenvolvimento.setQtdeExec(grupoDesenvolvimento.getQtdeExec() + 1);

		for (GrupoDesenvolvimentoJogador gdj : grupoDesenvolvimento.getGrupoJogadores()) {
			j = jogadorRepository.findByJogadorFetchHabilidades(gdj.getJogador()).get(0);
			desenvolverJogador(grupoDesenvolvimento, gdj, j);
		}
		
		if (grupoDesenvolvimento.getQtdeExec().equals(JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR.intValue())) {
			grupoDesenvolvimento.setQtdeExec(0);
		}

		desenvolvimentoRepository.save(grupoDesenvolvimento);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}*/
	
	@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> desenvolverJogadores(List<Jogador> jogadores) {
		
		//Jogador j = null;
		
		//grupoDesenvolvimento.setQtdeExec(grupoDesenvolvimento.getQtdeExec() + 1);

		//for (GrupoDesenvolvimentoJogador gdj : grupoDesenvolvimento) {
		for (Jogador j : jogadores) {
			//j = jogadorRepository.findByJogadorFetchHabilidades(gdj.getJogador()).get(0);
			
			j.getGrupoDesenvolvimentoJogador().setQtdeExecAno((j.getGrupoDesenvolvimentoJogador().getQtdeExecAno() + 1));
			desenvolverJogador(j.getGrupoDesenvolvimentoJogador(), j);
			//gdj.setJogador(j);//
			
			if (j.getGrupoDesenvolvimentoJogador().getQtdeExecAno().equals(JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR.intValue())) {
				j.getGrupoDesenvolvimentoJogador().setQtdeExecAno(0);
			}
			
			//grupoDesenvolvimentoJogadorRepository.save(gdj);
		}

		//
		//List<Jogador> jogadores = null;
		List<GrupoDesenvolvimentoJogador> grupoDesenvolvimento = jogadores.stream().map(j -> j.getGrupoDesenvolvimentoJogador()).collect(Collectors.toList());
		
		//jogadores = grupoDesenvolvimento.stream().map(gd -> gd.getJogador()).collect(Collectors.toList());
		jogadorRepository.saveAll(jogadores);
		for (Jogador jog : jogadores) {
			habilidadeValorRepository.saveAll(jog.getHabilidades());
		}
		
		grupoDesenvolvimentoJogadorRepository.saveAll(grupoDesenvolvimento);
		//

		//desenvolvimentoRepository.save(grupoDesenvolvimento);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> desenvolverGrupo(List<GrupoDesenvolvimentoJogador> grupoDesenvolvimento) {
		
		Jogador j = null;
		
		//grupoDesenvolvimento.setQtdeExec(grupoDesenvolvimento.getQtdeExec() + 1);

		for (GrupoDesenvolvimentoJogador gdj : grupoDesenvolvimento) {
			j = jogadorRepository.findByJogadorFetchHabilidades(gdj.getJogador()).get(0);
			gdj.setQtdeExecAno((gdj.getQtdeExecAno() + 1));
			desenvolverJogador(gdj, j);
			gdj.setJogador(j);//
			
			if (gdj.getQtdeExecAno().equals(JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR.intValue())) {
				gdj.setQtdeExecAno(0);
			}
			
			//grupoDesenvolvimentoJogadorRepository.save(gdj);
		}

		//
		List<Jogador> jogadores = null;
		
		jogadores = grupoDesenvolvimento.stream().map(gd -> gd.getJogador()).collect(Collectors.toList());
		jogadorRepository.saveAll(jogadores);
		for (Jogador jog : jogadores) {
			habilidadeValorRepository.saveAll(jog.getHabilidades());
		}
		
		grupoDesenvolvimentoJogadorRepository.saveAll(grupoDesenvolvimento);
		//

		//desenvolvimentoRepository.save(grupoDesenvolvimento);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	private void desenvolverJogador(GrupoDesenvolvimentoJogador grupoDesenvolvimentoJogador, Jogador j) {
		//Caso for usar so passo, pode ser substuido por sql UPDATE tab SET valor = valor + passo ....
		Double newValorTotal = null;
		Double newValorDecimal = null;
		for (HabilidadeValor hv : j.getHabilidades()) {
			newValorTotal = hv.getValorTotal() + hv.getPassoDesenvolvimentoAno();
			
			newValorDecimal = newValorTotal - newValorTotal.intValue();
			
			hv.setValor(newValorTotal.intValue());
			hv.setValorDecimal(newValorDecimal);
		}
		
		JogadorCalcularForcaUtil.calcularForcaGeral(j);
		
		if (grupoDesenvolvimentoJogador.getQtdeExecAno().equals(JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR.intValue())) {
			j.setIdade(j.getIdade() + 1);
			/*if (JogadorFactory.IDADE_MAX.equals(j.getIdade())) {
				//aposentar
				j.setAposentado(true);
				grupoDesenvolvimentoJogador.setAtivo(false);
				
				criarNovoJogadorSubsAposentado(j.getClube(), j.getPosicao(), j.getNumero(), grupoDesenvolvimentoJogador.getCelulaDesenvolvimento());
			} else {*/
			ajustarPassoDesenvolvimentoProximoAno(j);
			//}
		}
		
		//habilidadeValorRepository.saveAll(j.getHabilidades());
		//jogadorRepository.save(j);
		//grupoDesenvolvimentoJogadorRepository.save(grupoDesenvolvimentoJogador);
	}

	/*private void criarNovoJogadorSubsAposentado(Clube clube, Posicao posicao, Integer numero, CelulaDesenvolvimento celulaDesenvolvimento) {

		Jogador novoJogador = JogadorFactory.gerarJogador(clube, posicao, numero, JogadorFactory.IDADE_MIN);
		
		GrupoDesenvolvimentoJogador grupoDesenvolvimentoJogador = new GrupoDesenvolvimentoJogador(celulaDesenvolvimento, novoJogador, true);

		jogadorRepository.save(novoJogador);
		habilidadeValorRepository.saveAll(novoJogador.getHabilidades());
		grupoDesenvolvimentoJogadorRepository.save(grupoDesenvolvimentoJogador);
	}*/
	
	private void ajustarPassoDesenvolvimentoProximoAno(Jogador j) {

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
	}
}

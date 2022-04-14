package com.fastfoot.scheduler.model.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;

public class ClassificacaoFactory {

	public static List<Classificacao> gerarClassificacaoInicial(List<Clube> clubes, Campeonato campeonato, GrupoCampeonato grupoCampeonato) {
		Classificacao c = null;
		List<Classificacao> classificacao = new ArrayList<Classificacao>();
		for (Iterator<Clube> iterator = clubes.iterator(); iterator.hasNext();) {
			c = new Classificacao((Clube) iterator.next(), campeonato, grupoCampeonato);
			c.setPontos(0);
			c.setVitorias(0);
			c.setGolsPro(0);
			c.setSaldoGols(0);
			c.setPosicao(1);
			c.setNumJogos(0);
			classificacao.add(c);
		}
		return classificacao;
	}
}

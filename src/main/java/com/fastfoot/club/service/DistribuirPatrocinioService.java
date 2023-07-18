package com.fastfoot.club.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceira;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;

@Service
public class DistribuirPatrocinioService {
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

	public void distribuirPatrocinio(Temporada temporada) {
		List<Clube> clubes = clubeRepository.findAll();
		
		List<MovimentacaoFinanceira> entradas = new ArrayList<MovimentacaoFinanceira>();
		
		for (Clube c : clubes) {
			entradas.add(criarMovimentacaoFinanceira(c,
					temporada.getSemanas().stream().filter(s -> s.getNumero() == 1).findFirst().get(),
					c.getNivelNacional().getCaixaInicial() * 0.5, "Patrocínio"));
		}
		
		movimentacaoFinanceiraRepository.saveAll(entradas);
	}
	
	private MovimentacaoFinanceira criarMovimentacaoFinanceira(Clube clube, Semana semana,
			Double valorMovimentacao, String descricao) {
		
		MovimentacaoFinanceira saida = new MovimentacaoFinanceira();
		
		saida.setClube(clube);
		saida.setSemana(semana);
		saida.setTipoMovimentacao(TipoMovimentacaoFinanceira.ENTRADA_PATROCINIO);
		saida.setValorMovimentacao(valorMovimentacao);
		saida.setDescricao(descricao);

		return saida;
	}
}

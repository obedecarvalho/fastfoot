package com.fastfoot.financial.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceira;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.service.util.DatabaseUtil;
import com.fastfoot.transfer.model.ClubeSaldo;

@Service
public class CalcularJurosBancariosService {
	
	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;
	
	public void calcularJurosBancarios(Semana semana) {
		List<Map<String, Object>> saldoClubes = movimentacaoFinanceiraRepository.findSaldoPorClube();
		ClubeSaldo clubeSaldo = null;
		
		List<ClubeSaldo> clubeSaldos = new ArrayList<ClubeSaldo>();
		
		for (Map<String, Object> sc : saldoClubes) {
			clubeSaldo = new ClubeSaldo();
			clubeSaldo.setClube(new Clube((Integer) sc.get("id_clube")));
			clubeSaldo.setSaldo(DatabaseUtil.getValueDecimal(sc.get("saldo")));
			clubeSaldos.add(clubeSaldo);
		}
		
		clubeSaldos = clubeSaldos.stream().filter(cs -> cs.getSaldo() < 0).collect(Collectors.toList());
		
		List<MovimentacaoFinanceira> movimentacoesFinanceiras = new ArrayList<MovimentacaoFinanceira>();
		MovimentacaoFinanceira movimentacaoFinanceira;
		
		double porcentagem = Constantes.PORC_JUROS_BANCARIOS_SEMANAL * 100d;
		
		for (ClubeSaldo cs : clubeSaldos) {
			movimentacaoFinanceira = new MovimentacaoFinanceira();
			
			movimentacaoFinanceira.setClube(cs.getClube());
			movimentacaoFinanceira.setSemana(semana);
			movimentacaoFinanceira.setTipoMovimentacao(TipoMovimentacaoFinanceira.SAIDA_JUROS_BANCARIOS);
			movimentacaoFinanceira.setValorMovimentacao(
					Math.round(cs.getSaldo() * porcentagem) / 100d);//Arredondar
			movimentacaoFinanceira.setDescricao("Juros bancários");
			
			movimentacoesFinanceiras.add(movimentacaoFinanceira);
		}
		
		//System.err.println("\tMF: " + movimentacoesFinanceiras.size());
		
		movimentacaoFinanceiraRepository.saveAll(movimentacoesFinanceiras);
	}

}

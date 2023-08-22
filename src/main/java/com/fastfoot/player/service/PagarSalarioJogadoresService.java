package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceira;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.repository.ContratoRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.service.util.DatabaseUtil;

@Service
public class PagarSalarioJogadoresService {

	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

	@Autowired
	private ContratoRepository contratoRepository;

	public void pagarSalarioJogadores(Semana semana) {
		List<Map<String, Object>> valorTransferenciaClubes = jogadorRepository.findValorTransferenciaPorClube();

		List<MovimentacaoFinanceira> saidas = new ArrayList<MovimentacaoFinanceira>();
		
		double valorSalario, porcentagemSalario = Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL * 100;

		for (Map<String, Object> vtc : valorTransferenciaClubes) {
			
			valorSalario = Math.round((DatabaseUtil.getValueDecimal(vtc.get("valor_transferencia"))) * porcentagemSalario) / 100d;//Arredondar
			
			saidas.add(criarMovimentacaoFinanceira(new Clube((Integer) vtc.get("id_clube")), semana, valorSalario,
					String.format("Salários (%d)", semana.getNumero())));
		}

		movimentacaoFinanceiraRepository.saveAll(saidas);
	}

	public void pagarSalarioJogadoresPorContrato(Semana semana) {
		List<Map<String, Object>> valorSalariosClubes = contratoRepository.findValorTotalSalariosPorClube();

		List<MovimentacaoFinanceira> saidas = new ArrayList<MovimentacaoFinanceira>();
		
		double valorSalario;

		for (Map<String, Object> vtc : valorSalariosClubes) {
			
			valorSalario = DatabaseUtil.getValueDecimal(vtc.get("total_salarios")); 
					//Math.round(((Double) vtc.get("valor_transferencia")) * porcentagemSalario) / 100d;//Arredondar
			
			saidas.add(criarMovimentacaoFinanceira(new Clube((Integer) vtc.get("id_clube")), semana, valorSalario,
					String.format("Salários (%d)", semana.getNumero())));
		}

		movimentacaoFinanceiraRepository.saveAll(saidas);
	}
	
	private MovimentacaoFinanceira criarMovimentacaoFinanceira(Clube clube, Semana semana,
			Double valorMovimentacao, String descricao) {
		
		MovimentacaoFinanceira saida = new MovimentacaoFinanceira();
		
		saida.setClube(clube);
		saida.setSemana(semana);
		saida.setTipoMovimentacao(TipoMovimentacaoFinanceira.SAIDA_SALARIO_JOGADOR);
		saida.setValorMovimentacao(-1 * valorMovimentacao);
		saida.setDescricao(descricao);

		return saida;
	}
}

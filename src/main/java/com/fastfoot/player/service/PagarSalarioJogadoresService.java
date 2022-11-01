package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceiraSaida;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceiraSaida;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraSaidaRepository;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Semana;

@Service
public class PagarSalarioJogadoresService {
	
	private static final Double PORC_VALOR_JOG_SALARIO_SEMANAL = 0.004;
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private MovimentacaoFinanceiraSaidaRepository movimentacaoFinanceiraSaidaRepository;

	public void pagarSalarioJogadores(Semana semana) {
		List<Map<String, Object>> valorTransferenciaClubes = jogadorRepository.findValorTransferenciaPorClube();

		List<MovimentacaoFinanceiraSaida> saidas = new ArrayList<MovimentacaoFinanceiraSaida>();
		
		double valorSalario, porcentagemSalario = PORC_VALOR_JOG_SALARIO_SEMANAL * 100;

		for (Map<String, Object> vtc : valorTransferenciaClubes) {
			
			valorSalario = Math.round(((Double) vtc.get("valor_transferencia")) * porcentagemSalario) / 100d;//Arredondar
			
			saidas.add(criarMovimentacaoFinanceira(new Clube((Integer) vtc.get("id_clube")), semana, valorSalario,
					String.format("Salários (%d)", semana.getNumero())));
		}

		movimentacaoFinanceiraSaidaRepository.saveAll(saidas);
	}

	public void pagarSalarioJogadores(Semana semana, boolean old) {
		List<Jogador> jogadores = jogadorRepository.findByStatusJogador(StatusJogador.ATIVO);

		List<Jogador> jogadoresClube = null;

		Double valorTransferenciaClube;

		Map<Clube, List<Jogador>> jogadoresClubeMap = jogadores.stream()
				.collect(Collectors.groupingBy(Jogador::getClube));

		List<MovimentacaoFinanceiraSaida> saidas = new ArrayList<MovimentacaoFinanceiraSaida>();

		for (Clube c : jogadoresClubeMap.keySet()) {

			jogadoresClube = jogadoresClubeMap.get(c);

			valorTransferenciaClube = jogadoresClube.stream().mapToDouble(j -> j.getValorTransferencia()).sum();

			saidas.add(criarMovimentacaoFinanceira(c, semana, valorTransferenciaClube * PORC_VALOR_JOG_SALARIO_SEMANAL,
					String.format("Salários (%d)", semana.getNumero())));
		}

		movimentacaoFinanceiraSaidaRepository.saveAll(saidas);
	}
	
	private MovimentacaoFinanceiraSaida criarMovimentacaoFinanceira(Clube clube, Semana semana,
			Double valorMovimentacao, String descricao) {
		
		MovimentacaoFinanceiraSaida saida = new MovimentacaoFinanceiraSaida();
		
		saida.setClube(clube);
		saida.setSemana(semana);
		saida.setTipoMovimentacao(TipoMovimentacaoFinanceiraSaida.SALARIO_JOGADOR);
		saida.setValorMovimentacao(valorMovimentacao);
		saida.setDescricao(descricao);

		return saida;
	}
}

package com.fastfoot.player.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fastfoot.player.model.HabilidadeJogavel;
import com.fastfoot.player.model.HabilidadeValorJogavel;
import com.fastfoot.player.model.ModoDesenvolvimentoJogador;
import com.fastfoot.player.model.factory.JogadorFactory;

public abstract class CalcularValorTransferenciaJogadorService {
	//@see CalcularSalarioContratoService

	public static final Integer FORCA_N_POWER = 3;

	/* 
	 * Calculado como: Math.pow(1 + TAXA_DESCONTO, i - jogador.getIdade())
	 */
	/*public static final Double[] TAXA_DESCONTO_TEMPO = new Double[] { 1.0, 1.25, 1.5625, 1.953125, 2.44140625,
			3.051757813, 3.814697266, 4.768371582, 5.960464478, 7.450580597, 9.313225746, 11.64153218, 14.55191523,
			18.18989404, 22.73736754, 28.42170943, 35.52713679, 44.40892099, 55.51115123, 69.38893904, 86.7361738,
			108.4202172 };*///0.25
	public static final Double[] TAXA_DESCONTO_TEMPO = new Double[] { 1.0, 1.18, 1.3924, 1.643032, 1.93877776,
			2.287757757, 2.699554153, 3.185473901, 3.758859203, 4.435453859, 5.233835554, 6.175925953, 7.287592625,
			8.599359298, 10.14724397, 11.97374789, 14.12902251, 16.67224656, 19.67325094, 23.21443611, 27.3930346,
			32.32378083 };//0.18

	protected Double calcularValorTransferencia(
			List<? extends HabilidadeValorJogavel> habilidadesValorJogavel,
			Integer idade, ModoDesenvolvimentoJogador modoDesenvolvimentoJogador,
			Map<? extends HabilidadeJogavel, Double> pesoHabilidadeJogavel, Integer peso) {
		
		Double pesoHabilidade = null;
		
		Map<HabilidadeValorJogavel, Double> habilidadeValorPeso = new HashMap<HabilidadeValorJogavel, Double>();
		
		for (HabilidadeValorJogavel habilidadeValor : habilidadesValorJogavel) {
			
			pesoHabilidade = pesoHabilidadeJogavel.get(habilidadeValor.getHabilidadeJogavel());
			
			habilidadeValorPeso.put(habilidadeValor, pesoHabilidade * habilidadeValor.getPotencialDesenvolvimento());
		}

		Double valor = 0d;

		Double habilidadeComPeso = null;

		for (int i = idade; i < JogadorFactory.IDADE_MAX; i++) {

			double ajuste = modoDesenvolvimentoJogador.getValorAjuste()[i - JogadorFactory.IDADE_MIN];

			for (HabilidadeValorJogavel habilidadeValor : habilidadesValorJogavel) {

				habilidadeComPeso = habilidadeValorPeso.get(habilidadeValor);
				
				double valorAj = Math.pow((ajuste * habilidadeComPeso), FORCA_N_POWER)
						/ TAXA_DESCONTO_TEMPO[i - idade];

				valor += valorAj;
			}

		}

		//Aproveitando para arredondar também
		return Math.round(valor * peso) / 100d;

	}
}

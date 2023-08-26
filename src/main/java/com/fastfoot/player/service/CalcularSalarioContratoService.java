package com.fastfoot.player.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.HabilidadeJogavel;
import com.fastfoot.player.model.HabilidadeValorJogavel;
import com.fastfoot.player.model.ModoDesenvolvimentoJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;

@Service
public class CalcularSalarioContratoService {
	//@see CalcularValorTransferenciaJogadorService
	
	public Double calcularSalarioContrato(Jogador jogador, Integer numeroTemporadasDuracao) {

		Double valor = 0d;
		
		Integer idade = jogador.getIdade();
		Double forcaGeralPotencial = jogador.getForcaGeralPotencial();
		ModoDesenvolvimentoJogador modoDesenvolvimentoJogador = jogador.getModoDesenvolvimentoJogador();
		
		Double[] salarios = new Double[numeroTemporadasDuracao];
		Double salario = 0.0;
		
		//Tem que multiplicar o valor por
		//	FORCA_N_POWER == 2: 1000
		//	FORCA_N_POWER == 3: 10
		double porcentagemSalarioSemanal = 10 * Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL;
		
		for (int j = 0; j < numeroTemporadasDuracao; j++) {

			for (int i = idade; i < JogadorFactory.IDADE_MAX; i++) {

				double ajuste = modoDesenvolvimentoJogador.getValorAjuste()[i - JogadorFactory.IDADE_MIN];

				double valorAj = Math.pow((ajuste * forcaGeralPotencial),
						CalcularValorTransferenciaJogadorService.FORCA_N_POWER)
						/ CalcularValorTransferenciaJogadorService.TAXA_DESCONTO_TEMPO[i - idade];

				valor += valorAj;
			}

			//Tem que multiplicar o valor por
			//	FORCA_N_POWER == 2: 1000
			//	FORCA_N_POWER == 3: 10
			//salarios[j] = valor * 10 * Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL;
			salarios[j] = valor * porcentagemSalarioSemanal;
			salario += valor * porcentagemSalarioSemanal;

			idade++;
			valor = 0d;

		}

		return Math.round((salario / numeroTemporadasDuracao) * 100) / 100d;//Arredondar

	}
	
	public Double calcularSalarioContratoPorHabilidade(Jogador jogador, Integer numeroTemporadasDuracao) {
		return calcularSalarioContratoPorHabilidadeValorJogavel(jogador, numeroTemporadasDuracao,
				jogador.getHabilidadesValor(),
				CalcularValorTransferenciaJogadorPorHabilidadeService.VALOR_TRANSFERENCIA_HABILIDADE, 100);
	}
	
	/*public Double calcularSalarioContratoPorHabilidade(Jogador jogador, Integer numeroTemporadasDuracao) {

		Double pesoHabilidade = null;
		
		Map<HabilidadeValor, Double> habilidadeValorPeso = new HashMap<HabilidadeValor, Double>();
		
		for (HabilidadeValor habilidadeValor : jogador.getHabilidadesValor()) {
			
			pesoHabilidade = CalcularValorTransferenciaJogadorPorHabilidadeService.VALOR_TRANSFERENCIA_HABILIDADE.get(habilidadeValor.getHabilidade()).getPeso();
			
			habilidadeValorPeso.put(habilidadeValor, pesoHabilidade * habilidadeValor.getPotencialDesenvolvimento());
		}

		Double valor = 0d;

		Double habilidadeComPeso = null;
		
		Double[] salarios = new Double[numeroTemporadasDuracao];
		
		Double salario = 0.0;
		
		Integer idade = jogador.getIdade();
		
		ModoDesenvolvimentoJogador modoDesenvolvimentoJogador = jogador.getModoDesenvolvimentoJogador();

		for (int j = 0; j < numeroTemporadasDuracao; j++) {

			for (int i = idade; i < JogadorFactory.IDADE_MAX; i++) {
	
				double ajuste = modoDesenvolvimentoJogador.getValorAjuste()[i - JogadorFactory.IDADE_MIN];
	
				for (HabilidadeValor habilidadeValor : jogador.getHabilidadesValor()) {
	
					habilidadeComPeso = habilidadeValorPeso.get(habilidadeValor);
	
					double valorAj = Math.pow((ajuste * habilidadeComPeso),
							CalcularValorTransferenciaJogadorService.FORCA_N_POWER)
							/ CalcularValorTransferenciaJogadorService.TAXA_DESCONTO_TEMPO[i - idade];
	
					valor += valorAj;
				}
	
			}

			salarios[j] = valor * Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL;
			salario += valor * Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL;

			idade++;
			valor = 0d;

		}

		return Math.round((salario / numeroTemporadasDuracao) * 100) / 100d;//Arredondar
	}*/
	
	public Double calcularSalarioContratoPorHabilidadeGrupo(Jogador jogador, Integer numeroTemporadasDuracao) {
		return calcularSalarioContratoPorHabilidadeValorJogavel(jogador, numeroTemporadasDuracao,
				jogador.getHabilidadesGrupoValor(),
				CalcularValorTransferenciaJogadorPorHabilidadeGrupoService.VALOR_TRANSFERENCIA_HABILIDADE, 300);
	}
	
	/*public Double calcularSalarioContratoPorHabilidadeGrupo(Jogador jogador, Integer numeroTemporadasDuracao) {

		Double pesoHabilidade = null;
		
		Map<HabilidadeGrupoValor, Double> habilidadeValorPeso = new HashMap<HabilidadeGrupoValor, Double>();
		
		for (HabilidadeGrupoValor habilidadeValor : jogador.getHabilidadesGrupoValor()) {
			
			pesoHabilidade = CalcularValorTransferenciaJogadorPorHabilidadeGrupoService.VALOR_TRANSFERENCIA_HABILIDADE.get(habilidadeValor.getHabilidadeGrupo()).getPeso();
			
			habilidadeValorPeso.put(habilidadeValor, pesoHabilidade * habilidadeValor.getPotencialDesenvolvimento());
		}

		Double valor = 0d;

		Double habilidadeComPeso = null;
		
		Double[] salarios = new Double[numeroTemporadasDuracao];
		
		Double salario = 0.0;
		
		Integer idade = jogador.getIdade();
		
		ModoDesenvolvimentoJogador modoDesenvolvimentoJogador = jogador.getModoDesenvolvimentoJogador();

		for (int j = 0; j < numeroTemporadasDuracao; j++) {

			for (int i = idade; i < JogadorFactory.IDADE_MAX; i++) {
	
				double ajuste = modoDesenvolvimentoJogador.getValorAjuste()[i - JogadorFactory.IDADE_MIN];
	
				for (HabilidadeGrupoValor habilidadeValor : jogador.getHabilidadesGrupoValor()) {
	
					habilidadeComPeso = habilidadeValorPeso.get(habilidadeValor);
	
					double valorAj = Math.pow((ajuste * habilidadeComPeso),
							CalcularValorTransferenciaJogadorService.FORCA_N_POWER)
							/ CalcularValorTransferenciaJogadorService.TAXA_DESCONTO_TEMPO[i - idade];
	
					valor += valorAj;
				}
	
			}

			salarios[j] = valor * Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL;
			salario += valor * Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL;

			idade++;
			valor = 0d;

		}

		return Math.round((salario / numeroTemporadasDuracao) * 300) / 100d;//Arredondar
	}*/

	private Double calcularSalarioContratoPorHabilidadeValorJogavel(Jogador jogador, Integer numeroTemporadasDuracao, 
			List<? extends HabilidadeValorJogavel> habilidadesValorJogavel, Map<? extends HabilidadeJogavel, Double> pesoHabilidadeJogavel, Integer peso) {

		Double pesoHabilidade = null;
		
		Map<HabilidadeValorJogavel, Double> habilidadeValorPeso = new HashMap<HabilidadeValorJogavel, Double>();
		
		for (HabilidadeValorJogavel habilidadeValor : habilidadesValorJogavel) {
			
			pesoHabilidade = pesoHabilidadeJogavel.get(habilidadeValor.getHabilidadeJogavel());
			
			habilidadeValorPeso.put(habilidadeValor, pesoHabilidade * habilidadeValor.getPotencialDesenvolvimento());
		}

		Double valor = 0d;

		Double habilidadeComPeso = null;
		
		Double[] salarios = new Double[numeroTemporadasDuracao];
		
		Double salario = 0.0;
		
		Integer idade = jogador.getIdade();
		
		ModoDesenvolvimentoJogador modoDesenvolvimentoJogador = jogador.getModoDesenvolvimentoJogador();

		for (int j = 0; j < numeroTemporadasDuracao; j++) {

			for (int i = idade; i < JogadorFactory.IDADE_MAX; i++) {
	
				double ajuste = modoDesenvolvimentoJogador.getValorAjuste()[i - JogadorFactory.IDADE_MIN];
	
				for (HabilidadeValorJogavel habilidadeValor : habilidadesValorJogavel) {
	
					habilidadeComPeso = habilidadeValorPeso.get(habilidadeValor);
	
					double valorAj = Math.pow((ajuste * habilidadeComPeso),
							CalcularValorTransferenciaJogadorService.FORCA_N_POWER)
							/ CalcularValorTransferenciaJogadorService.TAXA_DESCONTO_TEMPO[i - idade];
	
					valor += valorAj;
				}
	
			}

			salarios[j] = valor * Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL;
			salario += valor * Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL;

			idade++;
			valor = 0d;

		}

		return Math.round((salario / numeroTemporadasDuracao) * peso) / 100d;//Arredondar
	}
}

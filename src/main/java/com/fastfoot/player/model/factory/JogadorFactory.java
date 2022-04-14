package com.fastfoot.player.model.factory;

import java.util.Random;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.service.util.NomeUtil;

public class JogadorFactory {
	
	private static final Random R = new Random();
	
	private static final Double STDEV = 3.5; 
	
	private static final Double PESO_HABILIDADE_COMUM = 0.8;
	
	private static final Double PESO_HABILIDADE_ESPECIFICO = 1.0;
	
	private static final Double PESO_HABILIDADE_OUTROS = 0.4;
	
	private static final Integer VALOR_HABILIDADE_MAX = 100;
	
	private static final Integer VALOR_HABILIDADE_MIN = 10;
	
	public static Jogador gerarJogador(Clube clube, Posicao posicao, Integer numero) {
		Jogador jogador = null;
		switch (posicao) {
		case GOLEIRO:
			jogador = gerarGoleiro(clube, numero);
			break;
		case ZAGUEIRO:
			jogador = gerarZagueiro(clube, numero);		
			break;
		case LATERAL:
			jogador = gerarLateral(clube, numero);
			break;
		case VOLANTE:
			jogador = gerarVolante(clube, numero);
			break;
		case MEIA_LATERAL:
			jogador = gerarMeiaLateral(clube, numero);
			break;
		case MEIA:
			jogador = gerarMeia(clube, numero);
			break;
		case ATACANTE:
			jogador = gerarAtacante(clube, numero);
			break;
		default:
			break;
		}
		return jogador;
	}

	public static Jogador gerarGoleiro(Clube clube, Integer numero) {
		Jogador j = gerarGoleiro(clube.getOverhall(), numero);
		j.setClube(clube);
		return j;
	}

	public static Jogador gerarGoleiro(Integer overhall, Integer numero) {
		Jogador j = new Jogador();
		
		j.setPosicao(Posicao.GOLEIRO);
		j.setNumero(numero);
		j.setNome(NomeUtil.getNome());
		
		j.setValorPasse(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorFinalizacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorCruzamento(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorArmacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorCabeceio(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorMarcacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorDesarme(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorInterceptacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorVelocidade(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorDible(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorForca(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorPosicionamento(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorDominio(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorReflexo(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorJogoAereo(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		
		return j;
	}
	
	public static Jogador gerarZagueiro(Clube clube, Integer numero) {
		Jogador j = gerarZagueiro(clube.getOverhall(), numero);
		j.setClube(clube);
		return j;
	}

	public static Jogador gerarZagueiro(Integer overhall, Integer numero) {
		Jogador j = new Jogador();
		
		j.setPosicao(Posicao.ZAGUEIRO);
		j.setNumero(numero);
		j.setNome(NomeUtil.getNome());
		
		j.setValorPasse(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorFinalizacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorCruzamento(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorArmacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorCabeceio(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorMarcacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorDesarme(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorInterceptacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorVelocidade(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorDible(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorForca(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorPosicionamento(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorDominio(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorReflexo(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorJogoAereo(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		
		return j;
	}
	
	public static Jogador gerarLateral(Clube clube, Integer numero) {
		Jogador j = gerarLateral(clube.getOverhall(), numero);
		j.setClube(clube);
		return j;
	}

	public static Jogador gerarLateral(Integer overhall, Integer numero) {
		Jogador j = new Jogador();
		
		j.setPosicao(Posicao.LATERAL);
		j.setNumero(numero);
		j.setNome(NomeUtil.getNome());
		
		j.setValorPasse(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorFinalizacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorCruzamento(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorArmacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorCabeceio(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorMarcacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorDesarme(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorInterceptacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorVelocidade(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorDible(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorForca(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorPosicionamento(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorDominio(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorReflexo(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorJogoAereo(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		
		return j;
	}
	
	public static Jogador gerarVolante(Clube clube, Integer numero) {
		Jogador j = gerarVolante(clube.getOverhall(), numero);
		j.setClube(clube);
		return j;
	}

	public static Jogador gerarVolante(Integer overhall, Integer numero) {
		Jogador j = new Jogador();
		
		j.setPosicao(Posicao.VOLANTE);
		j.setNumero(numero);
		j.setNome(NomeUtil.getNome());
		
		j.setValorPasse(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorFinalizacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorCruzamento(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorArmacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorCabeceio(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorMarcacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorDesarme(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorInterceptacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorVelocidade(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorDible(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorForca(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorPosicionamento(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorDominio(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorReflexo(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorJogoAereo(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		
		return j;
	}
	
	public static Jogador gerarMeiaLateral(Clube clube, Integer numero) {
		Jogador j = gerarMeiaLateral(clube.getOverhall(), numero);
		j.setClube(clube);
		return j;
	}

	public static Jogador gerarMeiaLateral(Integer overhall, Integer numero) {
		Jogador j = new Jogador();
		
		j.setPosicao(Posicao.MEIA_LATERAL);
		j.setNumero(numero);
		j.setNome(NomeUtil.getNome());
		
		j.setValorPasse(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorFinalizacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorCruzamento(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorArmacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorCabeceio(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorMarcacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorDesarme(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorInterceptacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorVelocidade(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorDible(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorForca(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorPosicionamento(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorDominio(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorReflexo(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorJogoAereo(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		
		return j;
	}
	
	public static Jogador gerarMeia(Clube clube, Integer numero) {
		Jogador j = gerarMeia(clube.getOverhall(), numero);
		j.setClube(clube);
		return j;
	}

	public static Jogador gerarMeia(Integer overhall, Integer numero) {
		Jogador j = new Jogador();
		
		j.setPosicao(Posicao.MEIA);
		j.setNumero(numero);
		j.setNome(NomeUtil.getNome());
		
		j.setValorPasse(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorFinalizacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorCruzamento(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorArmacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorCabeceio(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorMarcacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorDesarme(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorInterceptacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorVelocidade(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorDible(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorForca(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorPosicionamento(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorDominio(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorReflexo(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorJogoAereo(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		
		return j;
	}
	
	public static Jogador gerarAtacante(Clube clube, Integer numero) {
		Jogador j = gerarAtacante(clube.getOverhall(), numero);
		j.setClube(clube);
		return j;
	}

	public static Jogador gerarAtacante(Integer overhall, Integer numero) {
		Jogador j = new Jogador();
		
		j.setPosicao(Posicao.ATACANTE);
		j.setNumero(numero);
		j.setNome(NomeUtil.getNome());
		
		j.setValorPasse(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorFinalizacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorCruzamento(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorArmacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorCabeceio(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorMarcacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorDesarme(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorInterceptacao(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorVelocidade(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorDible(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorForca(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorPosicionamento(gerarValorHabilidade(overhall, PESO_HABILIDADE_ESPECIFICO));
		j.setValorDominio(gerarValorHabilidade(overhall, PESO_HABILIDADE_COMUM));
		j.setValorReflexo(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		j.setValorJogoAereo(gerarValorHabilidade(overhall, PESO_HABILIDADE_OUTROS));
		
		return j;
	}

	private static Integer gerarValorHabilidade(Integer media, Double peso) {
		return Long.valueOf(Math.max(Math.min(Math.round(peso * (R.nextGaussian() * STDEV + media)), VALOR_HABILIDADE_MAX), VALOR_HABILIDADE_MIN)).intValue();
	}
}

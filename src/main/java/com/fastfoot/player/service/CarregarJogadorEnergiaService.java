package com.fastfoot.player.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEnergia;
import com.fastfoot.player.model.repository.JogadorEnergiaRepository;
import com.fastfoot.service.util.DatabaseUtil;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class CarregarJogadorEnergiaService {
	
	@Autowired
	private JogadorEnergiaRepository jogadorEnergiaRepository;

	public void carregarJogadorEnergia(Jogo jogo, List<Jogador> jogadores) {
		List<Map<String, Object>> jogEnergia = jogadorEnergiaRepository.findEnergiaJogador(jogo.getId());

		carregarJogadorEnergia(jogadores, jogEnergia);
	}

	public void carregarJogadorEnergia(Clube clube, List<Jogador> jogadores) {
		List<Map<String, Object>> jogEnergia = jogadorEnergiaRepository.findEnergiaJogadorByIdClube(clube.getId());

		carregarJogadorEnergia(jogadores, jogEnergia);
	}

	private void carregarJogadorEnergia(List<Jogador> jogadores, List<Map<String, Object>> jogEnergia) {

		Map<Jogador, Map<String, Object>> x = jogEnergia.stream()
				.collect(Collectors.toMap(ej -> new Jogador(((BigInteger) ej.get("id_jogador")).longValue()), Function.identity()));

		JogadorEnergia je = null;
		Map<String, Object> jes = null;
		Integer energia = null;

		for (Jogador j : jogadores) {
			je = new JogadorEnergia();
			je.setJogador(j);
			je.setEnergia(0);//Variacao de energia

			jes = x.get(j);
			if (!ValidatorUtil.isEmpty(jes)) {
				energia = DatabaseUtil.getValueInteger(jes.get("energia"));
				if (energia > Constantes.ENERGIA_INICIAL) {
					je.setEnergiaInicial(Constantes.ENERGIA_INICIAL);
				} else {
					je.setEnergiaInicial(energia);
				}
			} else {
				je.setEnergiaInicial(Constantes.ENERGIA_INICIAL);
			}

			j.setJogadorEnergia(je);
		}
	}
}

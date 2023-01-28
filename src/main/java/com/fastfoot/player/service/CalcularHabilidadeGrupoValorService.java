package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.HabilidadeGrupoValorRepository;

@Service
public class CalcularHabilidadeGrupoValorService {
	
	@Autowired
	private HabilidadeGrupoValorRepository habilidadeGrupoValorRepository;
	
	public void calcularHabilidadeGrupoValor(List<Jogador> jogadores) {
		
		List<HabilidadeGrupoValor> habilidadeGrupoValores = new ArrayList<HabilidadeGrupoValor>();
		
		for (Jogador jogador : jogadores) {
			calcularHabilidadeGrupoValor(jogador, habilidadeGrupoValores);
		}
		
		habilidadeGrupoValorRepository.saveAll(habilidadeGrupoValores);
	}

	public void calcularHabilidadeGrupoValor(Jogador jogador, List<HabilidadeGrupoValor> habilidadeGrupoValores) {

		List<HabilidadeValor> habilidadeValores;

		HabilidadeGrupoValor habilidadeGrupoValor;
		
		for (HabilidadeGrupo habilidadeGrupo : HabilidadeGrupo.values()) {
			
			habilidadeValores = jogador.getHabilidadeValorByHabilidade(Arrays.asList(habilidadeGrupo.getHabilidades()));

			habilidadeGrupoValor = new HabilidadeGrupoValor();
			
			habilidadeGrupoValor.setJogador(jogador);
			habilidadeGrupoValor.setHabilidadeGrupo(habilidadeGrupo);
			habilidadeGrupoValor.setValor(habilidadeValores.stream().mapToDouble(HabilidadeValor::getValorTotal).average().getAsDouble());
			
			habilidadeGrupoValores.add(habilidadeGrupoValor);

		}

		//System.err.println(habilidadeGrupoValores);
	}
}

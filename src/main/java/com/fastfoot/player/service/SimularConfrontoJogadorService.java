package com.fastfoot.player.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaImpl;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.service.util.ElementoRoleta;
import com.fastfoot.service.util.RoletaUtil;

@Service
public class SimularConfrontoJogadorService {
	
	private static final Integer NUM_SIMULACOES = 100;

	public void simularConfrontoJogador(Jogador mandante, Jogador visitante) {
		
		HabilidadeValor habilidadeValorAcao = null;
		HabilidadeValor habilidadeValorReacao = null;
		Boolean jogadorAcaoVenceu = null;
		
		Esquema esquema = new EsquemaImpl();//TODO
		
		
		for (int i = 0; i < NUM_SIMULACOES; i++) {
			habilidadeValorAcao = (HabilidadeValor) RoletaUtil
					.executarN((List<? extends ElementoRoleta>) mandante.getHabilidadesAcaoMeioFimValor());
			
			habilidadeValorReacao = (HabilidadeValor) RoletaUtil.executarN((List<? extends ElementoRoleta>) visitante
					.getHabilidades(habilidadeValorAcao.getHabilidadeAcao().getPossiveisReacoes()));
			
			jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedorN(habilidadeValorAcao, habilidadeValorReacao);
			
			if (jogadorAcaoVenceu) {
				
				if (habilidadeValorAcao.getHabilidadeAcao().isAcaoMeio() || habilidadeValorAcao.getHabilidadeAcao().isAcaoInicioMeio()) {
					
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil
							.executarN((List<? extends ElementoRoleta>) mandante.getHabilidadesAcaoFimValor());
					
					if (habilidadeValorAcao.getHabilidadeAcao().isExigeGoleiro()) {
						
					} else {
						
					}
					
				}
				
			}
		}
		
	}
}

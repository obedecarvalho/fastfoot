package com.fastfoot.scheduler.service.util;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Constantes;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;

public abstract class PromotorContinental {
	
	/**
	 * Comparator para classificação atualizada e que já houve desempate
	 */
	protected static final Comparator<Classificacao> COMPARATOR = new Comparator<Classificacao>() {
		
		@Override
		public int compare(Classificacao c0, Classificacao c1) {
			return c0.getPosicao().compareTo(c1.getPosicao());
		}
	};

	//public void promover(CampeonatoMisto campeonato);

	public abstract void promover(Set<CampeonatoMisto> campeonatos);
	
	protected void promover(CampeonatoMisto campeonato) {

		Clube primeiroColocado, segundoColocado; 

		for (int i = 0; i < Constantes.NRO_GRUPOS_CONT; i++) {
			int j = (i + (Constantes.NRO_GRUPOS_CONT/2))%Constantes.NRO_GRUPOS_CONT;
			GrupoCampeonato gc = campeonato.getGrupos().get(i);

			primeiroColocado = gc.getClassificacao().stream().filter(c -> c.getPosicao() == 1).findFirst().get().getClube();
			segundoColocado = gc.getClassificacao().stream().filter(c -> c.getPosicao() == 2).findFirst().get().getClube();

			campeonato.getPrimeiraRodadaEliminatoria().getPartidas().get(i).setClubeMandante(primeiroColocado);
			campeonato.getPrimeiraRodadaEliminatoria().getPartidas().get(j).setClubeVisitante(segundoColocado);
		}
	}
	
	protected void promoverUnicoGrupo(CampeonatoMisto campeonato) {
		
		if (campeonato.getGrupos().size() != 1) {
			//throw Numero de grupos diferente do esperado.
		}
		
		int numeroClubesClassificadosProxFase = campeonato.getPrimeiraRodadaEliminatoria().getPartidas().size() * 2;
		
		List<Clube> clubes = campeonato.getGrupos().get(0).getClassificacao().stream()
				.filter(c -> c.getPosicao() <= numeroClubesClassificadosProxFase).sorted(COMPARATOR).map(c -> c.getClube())
				.collect(Collectors.toList());
		
		
		for (int i = 0; i < campeonato.getPrimeiraRodadaEliminatoria().getPartidas().size(); i++) {
			campeonato.getPrimeiraRodadaEliminatoria().getPartidas().get(i).setClubeMandante(clubes.get(i));
			campeonato.getPrimeiraRodadaEliminatoria().getPartidas().get(i)
					.setClubeVisitante(clubes.get(numeroClubesClassificadosProxFase - i - 1));
		}
		
	}
}

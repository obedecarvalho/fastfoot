package com.fastfoot.scheduler.service.util;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Constantes;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;

public class PromotorContinentalImplInterCampeonatosFaseGrupo extends PromotorContinental {

	/*@Override
	public void promover(CampeonatoMisto campeonato) {

		Clube primeiroColocado, segundoColocado; 

		for (int i = 0; i < Constantes.NRO_GRUPOS_CONT; i++) {
			int j = (i + (Constantes.NRO_GRUPOS_CONT/2))%Constantes.NRO_GRUPOS_CONT;
			GrupoCampeonato gc = campeonato.getGrupos().get(i);

			primeiroColocado = gc.getClassificacao().stream().filter(c -> c.getPosicao() == 1).findFirst().get().getClube();
			segundoColocado = gc.getClassificacao().stream().filter(c -> c.getPosicao() == 2).findFirst().get().getClube();

			campeonato.getPrimeiraRodadaEliminatoria().getPartidas().get(i).setClubeMandante(primeiroColocado);
			campeonato.getPrimeiraRodadaEliminatoria().getPartidas().get(j).setClubeVisitante(segundoColocado);
		}
	}*/

	private void promover(CampeonatoMisto campeonato, CampeonatoMisto campeonatoSuperior, Integer posProxColocado) {

		Clube primeiroColocado, proximoColocado;
		
		int numGrupos = campeonato.getGrupos().size();

		for (int i = 0; i < Constantes.NRO_GRUPOS_CONT; i++) {
			int posG = i % numGrupos;
			//int posClas = (i / numGrupos) + 1;
			
			GrupoCampeonato gc = campeonato.getGrupos().get(posG);
			GrupoCampeonato gcSup = campeonatoSuperior.getGrupos().get(i);

			//primeiroColocado = gc.getClassificacao().stream().filter(c -> c.getPosicao() == posClas).findFirst().get().getClube();	
			if (numGrupos == Constantes.NRO_GRUPOS_CONT || i == posG) {
				primeiroColocado = gc.getClassificacao().stream().filter(c -> c.getPosicao() == 1).findFirst().get().getClube();
			} else {
				primeiroColocado = gc.getClassificacao().stream().filter(c -> c.getPosicao() == 2).findFirst().get().getClube();
			}
			proximoColocado = gcSup.getClassificacao().stream().filter(c -> c.getPosicao() == posProxColocado).findFirst().get().getClube();
			
			campeonato.getPrimeiraRodadaEliminatoria().getPartidas().get(i).setClubeMandante(primeiroColocado);

			campeonato.getPrimeiraRodadaEliminatoria().getPartidas().get(i).setClubeVisitante(proximoColocado);
		}

	}

	@Override
	public void promover(Set<CampeonatoMisto> campeonatos) {
		Optional<CampeonatoMisto> c1 = campeonatos.stream()
				.filter(c -> NivelCampeonato.CONTINENTAL.equals(c.getNivelCampeonato())).findFirst();

		Optional<CampeonatoMisto> c2 = campeonatos.stream()
				.filter(c -> NivelCampeonato.CONTINENTAL_II.equals(c.getNivelCampeonato())).findFirst();

		Optional<CampeonatoMisto> c3 = campeonatos.stream()
				.filter(c -> NivelCampeonato.CONTINENTAL_III.equals(c.getNivelCampeonato())).findFirst();
		
		List<CampeonatoMisto> outrosCont = campeonatos.stream()
				.filter(c -> NivelCampeonato.CONTINENTAL_OUTROS.equals(c.getNivelCampeonato()))
				.collect(Collectors.toList());
		
		CampeonatoMisto ultimo = null;
		
		if (c1.isPresent()) {
			promover(c1.get());
		}
		
		if (c1.isPresent() && c2.isPresent()) {
			promover(c2.get(), c1.get(), 3);
		}
		
		if (c2.isPresent() && c3.isPresent()) {
			promover(c3.get(), c2.get(), 2);
			ultimo = c3.get();
		}

		for (int i = 0; i < outrosCont.size(); i++) {
			promover(outrosCont.get(i), ultimo, 2);
			ultimo = outrosCont.get(i);
		}
	}
}

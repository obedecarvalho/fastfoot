package com.fastfoot.scheduler.service.util;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;

public class PromotorContinentalImplInterCampeonatosFaseLiga extends PromotorContinental {

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
			promoverUnicoGrupo(c1.get());
		}
		
		if (c1.isPresent() && c2.isPresent()) {
			promover(c2.get(), c1.get(), 9);
		}
		
		if (c2.isPresent() && c3.isPresent()) {
			promover(c3.get(), c2.get(), 5);
			ultimo = c3.get();
		}

		for (int i = 0; i < outrosCont.size(); i++) {
			promover(outrosCont.get(i), ultimo, 5);
			ultimo = outrosCont.get(i);
		}
	}
	
	private void promover(CampeonatoMisto campeonato, CampeonatoMisto campeonatoSuperior, Integer posProxColocado) {
		
		if (campeonato.getGrupos().size() != 1) {
			//throw Numero de grupos diferente do esperado.
		}
		
		int numeroClubesClassificadosProxFasePorCampeonato = campeonato.getPrimeiraRodadaEliminatoria().getPartidas().size();
		
		List<Clube> clubes = campeonato.getGrupos().get(0).getClassificacao().stream()
				.filter(c -> c.getPosicao() <= numeroClubesClassificadosProxFasePorCampeonato).sorted(COMPARATOR)
				.map(c -> c.getClube()).collect(Collectors.toList());

		List<Clube> clubesCampSuperior = campeonatoSuperior.getGrupos().get(0).getClassificacao().stream()
				.filter(c -> c.getPosicao() >= posProxColocado
						&& c.getPosicao() < (numeroClubesClassificadosProxFasePorCampeonato + posProxColocado))
				.sorted(COMPARATOR).map(c -> c.getClube()).collect(Collectors.toList());
		
		
		for (int i = 0; i < numeroClubesClassificadosProxFasePorCampeonato; i++) {
			campeonato.getPrimeiraRodadaEliminatoria().getPartidas().get(i).setClubeMandante(clubes.get(i));
			campeonato.getPrimeiraRodadaEliminatoria().getPartidas().get(i)
					.setClubeVisitante(clubesCampSuperior.get(numeroClubesClassificadosProxFasePorCampeonato - i - 1));
		}
		
	}

}

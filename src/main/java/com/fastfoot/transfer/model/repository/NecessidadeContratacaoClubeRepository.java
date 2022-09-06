package com.fastfoot.transfer.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.transfer.model.NivelAdequacao;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;

@Repository
public interface NecessidadeContratacaoClubeRepository extends JpaRepository<NecessidadeContratacaoClube, Long> {

	public List<NecessidadeContratacaoClube> findByClubeAndTemporadaAndNecessidadeSatisfeita(Clube clube,
			Temporada temporada, Boolean necessidadeSatisfeita);
	
	public List<NecessidadeContratacaoClube> findByClubeAndTemporadaAndNivelAdequacaoMaxAndNivelAdequacaoMinAndNecessidadeSatisfeita(
			Clube clube, Temporada temporada, NivelAdequacao nivelAdequacaoMax, NivelAdequacao nivelAdequacaoMin,
			Boolean necessidadeSatisfeita);

	public List<NecessidadeContratacaoClube> findByTemporadaAndNecessidadeSatisfeita(Temporada temporada,
			Boolean necessidadeSatisfeita);
	
	//public List<NecessidadeContratacaoClube> findByClube(Clube clube);

}

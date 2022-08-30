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

	public List<NecessidadeContratacaoClube> findByClubeAndTemporada(Clube clube, Temporada temporada);
	
	public List<NecessidadeContratacaoClube> findByClubeAndTemporadaAndNivelAdequacaoMax(Clube clube, Temporada temporada, NivelAdequacao nivelAdequacaoMax);

	public List<NecessidadeContratacaoClube> findByTemporada(Temporada temporada);
	
	public List<NecessidadeContratacaoClube> findByClube(Clube clube);

}

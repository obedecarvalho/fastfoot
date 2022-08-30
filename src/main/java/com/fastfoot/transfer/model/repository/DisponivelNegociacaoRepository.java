package com.fastfoot.transfer.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.transfer.model.entity.DisponivelNegociacao;

@Repository
public interface DisponivelNegociacaoRepository extends JpaRepository<DisponivelNegociacao, Long> {

	public List<DisponivelNegociacao> findByClubeAndTemporada(Clube clube, Temporada temporada);
	
	public List<DisponivelNegociacao> findByClube(Clube clube);
	
	public List<DisponivelNegociacao> findByTemporada(Temporada temporada);
	
	public List<DisponivelNegociacao> findByJogador(Jogador jogador);

}

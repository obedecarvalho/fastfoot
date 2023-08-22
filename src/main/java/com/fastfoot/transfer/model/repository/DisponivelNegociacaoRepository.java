package com.fastfoot.transfer.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.transfer.model.entity.DisponivelNegociacao;

@Repository
public interface DisponivelNegociacaoRepository extends JpaRepository<DisponivelNegociacao, Long> {

	public List<DisponivelNegociacao> findByClubeAndTemporadaAndAtivo(Clube clube, Temporada temporada, Boolean ativo);
	
	public List<DisponivelNegociacao> findByTemporadaAndAtivo(Temporada temporada, Boolean ativo);
	
	public List<DisponivelNegociacao> findByTemporadaAndJogadorAndAtivo(Temporada temporada, Jogador jogador, Boolean ativo);
	
	public Optional<DisponivelNegociacao> findFirstByTemporadaAndJogadorAndAtivo(Temporada temporada, Jogador jogador, Boolean ativo);
	
}

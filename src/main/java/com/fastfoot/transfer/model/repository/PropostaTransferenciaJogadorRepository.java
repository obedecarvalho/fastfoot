package com.fastfoot.transfer.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;

@Repository
public interface PropostaTransferenciaJogadorRepository extends JpaRepository<PropostaTransferenciaJogador, Long> {

	//public List<PropostaTransferenciaJogador> findByJogador(Jogador jogador);
	
	//public List<PropostaTransferenciaJogador> findByClubeOrigem(Clube clube);
	
	//public List<PropostaTransferenciaJogador> findByClubeDestino(Clube clube);
	
	public List<PropostaTransferenciaJogador> findByTemporada(Temporada temporada);
	
	public List<PropostaTransferenciaJogador> findByTemporadaAndClubeOrigem(Temporada temporada, Clube clubeOrigem);
	
	public List<PropostaTransferenciaJogador> findByTemporadaAndClubeDestino(Temporada temporada, Clube clubeDestino);
	
	@Query(" SELECT ptj FROM PropostaTransferenciaJogador ptj WHERE ptj.temporada = :temporada AND ptj.propostaAceita IS NULL ")
	public List<PropostaTransferenciaJogador> findByTemporadaAndPropostaAceitaIsNull(@Param("temporada") Temporada temporada);
	
	public List<PropostaTransferenciaJogador> findByTemporadaAndPropostaAceita(Temporada temporada, Boolean propostaAceita);
}

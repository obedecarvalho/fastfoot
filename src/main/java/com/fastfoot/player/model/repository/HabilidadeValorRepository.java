package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.player.model.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;

@Repository
public interface HabilidadeValorRepository extends JpaRepository<HabilidadeValor, Long> {

	public List<HabilidadeValor> findByJogador(Jogador jogador);

}

package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastfoot.player.model.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;

public interface HabilidadeValorRepository extends JpaRepository<HabilidadeValor, Long> {

	public List<HabilidadeValor> findByJogador(Jogador jogador);

}

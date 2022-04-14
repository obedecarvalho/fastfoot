package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;

public interface JogadorRepository extends JpaRepository<Jogador, Long>{

	public List<Jogador> findByClube(Clube clube);
}

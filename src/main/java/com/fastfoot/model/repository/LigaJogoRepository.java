package com.fastfoot.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.model.Liga;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;

@Repository
public interface LigaJogoRepository extends JpaRepository<LigaJogo, Long> {

	public List<LigaJogo> findByJogo(Jogo jogo);
	
	public List<LigaJogo> findByJogoAndLiga(Jogo jogo, Liga liga);
	
	public Optional<LigaJogo> findFirstByJogoAndLiga(Jogo jogo, Liga liga);
}

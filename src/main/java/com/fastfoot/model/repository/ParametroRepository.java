package com.fastfoot.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.Parametro;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Long>{
	
	public Optional<Parametro> findFirstByNomeAndJogo(String nome, Jogo jogo);

	public List<Parametro> findByJogo(Jogo jogo);

}

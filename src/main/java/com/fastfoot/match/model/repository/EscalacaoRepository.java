package com.fastfoot.match.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.Escalacao;

public interface EscalacaoRepository extends JpaRepository<Escalacao, Long>{
	
	public List<Escalacao> findByClube(Clube clube);

}

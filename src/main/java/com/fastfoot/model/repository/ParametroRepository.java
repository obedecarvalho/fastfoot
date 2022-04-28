package com.fastfoot.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.model.entity.Parametro;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Integer>{
	
	public Optional<Parametro> findFirstByNome(String nome);

	public List<Parametro> findByNome(String nome);

}

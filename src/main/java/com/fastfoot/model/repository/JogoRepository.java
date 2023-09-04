package com.fastfoot.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.model.entity.Jogo;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Long> {

}

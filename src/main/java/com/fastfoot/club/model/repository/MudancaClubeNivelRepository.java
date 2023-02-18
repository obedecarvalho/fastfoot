package com.fastfoot.club.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.MudancaClubeNivel;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface MudancaClubeNivelRepository extends JpaRepository<MudancaClubeNivel, Long> {

	public List<MudancaClubeNivel> findByClube(Clube clube);

	public List<MudancaClubeNivel> findByTemporada(Temporada temporada);

}

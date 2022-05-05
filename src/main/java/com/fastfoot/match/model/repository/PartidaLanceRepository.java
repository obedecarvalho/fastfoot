package com.fastfoot.match.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;

@Repository
public interface PartidaLanceRepository extends JpaRepository<PartidaLance, Long> {

	public List<PartidaLance> findByJogador(Jogador jogador);
	
	public List<PartidaLance> findByPartidaAmistosaResultado(PartidaAmistosaResultado partidaAmistosaResultado);
	
	public List<PartidaLance> findByPartidaEliminatoriaResultado(PartidaEliminatoriaResultado partidaEliminatoriaResultado);
	
	public List<PartidaLance> findByPartidaResultado(PartidaResultado partidaResultado);

}

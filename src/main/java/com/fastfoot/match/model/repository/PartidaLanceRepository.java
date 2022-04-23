package com.fastfoot.match.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.match.model.entity.PartidaResumo;
import com.fastfoot.player.model.entity.Jogador;

public interface PartidaLanceRepository extends JpaRepository<PartidaLance, Long> {
	
	public List<PartidaLance> findByJogador(Jogador jogador);
	
	public List<PartidaLance> findByPartidaResumo(PartidaResumo partidaResumo);

}

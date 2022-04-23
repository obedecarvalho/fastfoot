package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.match.model.entity.PartidaResumo;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorGrupoEstatisticas;

public interface JogadorGrupoEstatisticasRepository extends JpaRepository<JogadorGrupoEstatisticas, Long>{

	public List<PartidaLance> findByJogador(Jogador jogador);
	
	public List<PartidaLance> findByPartidaResumo(PartidaResumo PartidaResumo);

}

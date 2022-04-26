package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.match.model.entity.PartidaResumo;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorGrupoEstatisticas;

@Repository
public interface JogadorGrupoEstatisticasRepository extends JpaRepository<JogadorGrupoEstatisticas, Long>{

	public List<PartidaLance> findByJogador(Jogador jogador);
	
	public List<PartidaLance> findByPartidaResumo(PartidaResumo PartidaResumo);

}

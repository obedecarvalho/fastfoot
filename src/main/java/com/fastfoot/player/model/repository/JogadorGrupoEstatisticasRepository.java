package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticas;
import com.fastfoot.player.model.entity.JogadorGrupoEstatisticas;

public interface JogadorGrupoEstatisticasRepository extends JpaRepository<JogadorGrupoEstatisticas, Long>{

	public List<JogadorEstatisticas> findByJogador(Jogador jogador);
	
	public List<JogadorEstatisticas> findByPartidaEstatisticas(PartidaEstatisticas partidaEstatisticas);

}

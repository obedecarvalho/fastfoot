package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.player.model.HabilidadeAcao;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticas;

public interface JogadorEstatisticasRepository extends JpaRepository<JogadorEstatisticas, Long> {

	/*public List<JogadorEstatisticas> findByHabilidadeUsada(Habilidade habilidadeUsada);*/
	
	public List<JogadorEstatisticas> findByJogador(Jogador jogador);
	
	public List<JogadorEstatisticas> findByPartidaEstatisticas(PartidaEstatisticas partidaEstatisticas);

}

package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.Jogador;

@Repository
public interface HabilidadeGrupoValorRepository extends JpaRepository<HabilidadeGrupoValor, Long> {

	public List<HabilidadeGrupoValor> findByJogador(Jogador jogador);

	public List<HabilidadeGrupoValor> findByHabilidadeGrupo(HabilidadeGrupo habilidadeGrupo);

}

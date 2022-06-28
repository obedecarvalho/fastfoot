package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;
import com.fastfoot.player.model.entity.HabilidadeValorEstatisticaGrupo;

@Repository
public interface HabilidadeValorEstatisticaGrupoRepository extends JpaRepository<HabilidadeValorEstatisticaGrupo, Long> {
	
	public List<HabilidadeValorEstatistica> findByHabilidadeValor(HabilidadeValor habilidadeValor);

}

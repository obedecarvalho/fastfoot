package com.fastfoot.match.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastfoot.match.model.entity.Escalacao;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;

public interface EscalacaoJogadorPosicaoRepository extends JpaRepository<EscalacaoJogadorPosicao, Long>{

	public List<EscalacaoJogadorPosicao> findByEscalacao(Escalacao escalacao);

}

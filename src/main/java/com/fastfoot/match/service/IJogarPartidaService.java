package com.fastfoot.match.service;

import com.fastfoot.match.model.PartidaJogadorEstatisticaDTO;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

public interface IJogarPartidaService {

	public void jogar(PartidaResultadoJogavel partidaResultado,
			PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO);

	public void jogar(PartidaResultadoJogavel partidaResultado, EscalacaoClube escalacaoMandante,
			EscalacaoClube escalacaoVisitante, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO);

}

package com.fastfoot.player.model;

import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.service.util.ElementoRoleta;

public interface HabilidadeValorJogavel extends ElementoRoleta {

	public HabilidadeAcaoJogavel getHabilidadeAcaoJogavel();
	
	public HabilidadeJogavel getHabilidadeJogavel();
	
	public HabilidadeValorJogavelEstatistica getHabilidadeValorJogavelEstatistica();
	
	public void calcularValorN();
	
	public Jogador getJogador();
	
	public Double getPotencialDesenvolvimento();
}

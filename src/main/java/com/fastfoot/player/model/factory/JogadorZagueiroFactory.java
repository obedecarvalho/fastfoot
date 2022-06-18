package com.fastfoot.player.model.factory;

import java.util.ArrayList;
import java.util.List;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.service.util.NomeUtil;

public class JogadorZagueiroFactory extends JogadorFactory {

	protected static final List<Habilidade> HABILIDADES_ESPECIFICAS;
	
	protected static final List<Habilidade> HABILIDADES_ESPECIFICAS_ELETIVAS;
	
	protected static final List<Habilidade> HABILIDADES_COMUM;
	
	protected static final List<Habilidade> HABILIDADES_OUTROS;

	protected static final Integer NUM_HAB_ESP_ELETIVAS = 2;
	
	static {
		HABILIDADES_ESPECIFICAS = new ArrayList<Habilidade>();
		HABILIDADES_ESPECIFICAS_ELETIVAS = new ArrayList<Habilidade>();
		HABILIDADES_COMUM = new ArrayList<Habilidade>();
		HABILIDADES_OUTROS = new ArrayList<Habilidade>();
		
		HABILIDADES_COMUM.add(Habilidade.PASSE);
		
		HABILIDADES_OUTROS.add(Habilidade.FINALIZACAO);
		HABILIDADES_OUTROS.add(Habilidade.CRUZAMENTO);
		HABILIDADES_OUTROS.add(Habilidade.ARMACAO);
		
		HABILIDADES_ESPECIFICAS_ELETIVAS.add(Habilidade.CABECEIO);
		
		HABILIDADES_ESPECIFICAS.add(Habilidade.MARCACAO);
		HABILIDADES_ESPECIFICAS.add(Habilidade.DESARME);
		HABILIDADES_ESPECIFICAS.add(Habilidade.INTERCEPTACAO);
		
		HABILIDADES_ESPECIFICAS_ELETIVAS.add(Habilidade.VELOCIDADE);
		HABILIDADES_OUTROS.add(Habilidade.DIBLE);
		HABILIDADES_ESPECIFICAS_ELETIVAS.add(Habilidade.FORCA);
		
		HABILIDADES_OUTROS.add(Habilidade.POSICIONAMENTO);
		HABILIDADES_COMUM.add(Habilidade.DOMINIO);
		
		HABILIDADES_OUTROS.add(Habilidade.REFLEXO);
		HABILIDADES_OUTROS.add(Habilidade.JOGO_AEREO);
	}

	@Override
	public List<Habilidade> getHabilidadesEspecificas() {
		return HABILIDADES_ESPECIFICAS;
	}

	@Override
	public List<Habilidade> getHabilidadesEspecificasEletivas() {
		return HABILIDADES_ESPECIFICAS_ELETIVAS;
	}

	@Override
	public List<Habilidade> getHabilidadesComum() {
		return HABILIDADES_COMUM;
	}

	@Override
	public List<Habilidade> getHabilidadesOutros() {
		return HABILIDADES_OUTROS;
	}

	@Override
	public Integer getNumHabEspEletivas() {
		return NUM_HAB_ESP_ELETIVAS;
	}
	
	private static JogadorZagueiroFactory INSTANCE;
	
	protected JogadorZagueiroFactory() {

	}
	
	public static JogadorZagueiroFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new JogadorZagueiroFactory();
		}
		return INSTANCE;
	}
	
	@Override
	public Jogador gerarJogador(Clube clube, Integer numero) {
		return gerarJogador(clube, numero, null);
	}

	@Override
	public Jogador gerarJogador(Clube clube, Integer numero, Boolean titular) {
		Jogador jogador = new Jogador();

		jogador.setNumero(numero);
		jogador.setNome(NomeUtil.sortearNome());
		jogador.setClube(clube);
		jogador.setPosicao(Posicao.ZAGUEIRO);
		jogador.setIdade(sortearIdade(titular));

		sortearHabilidadeValor(jogador, clube.getForcaGeral());
		
		return jogador;
	}

}

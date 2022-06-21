package com.fastfoot.player.model.factory;

import java.util.ArrayList;
import java.util.List;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.service.util.NomeUtil;

public class JogadorGoleiroFactory extends JogadorFactory {

	protected static final List<Habilidade> HABILIDADES_ESPECIFICAS;
	
	protected static final List<Habilidade> HABILIDADES_ESPECIFICAS_ELETIVAS;
	
	protected static final List<Habilidade> HABILIDADES_COMUM;
	
	protected static final List<Habilidade> HABILIDADES_OUTROS;

	protected static final Integer NUM_HAB_ESP_ELETIVAS = 0;
	
	static {
		HABILIDADES_ESPECIFICAS = new ArrayList<Habilidade>();
		HABILIDADES_ESPECIFICAS_ELETIVAS = new ArrayList<Habilidade>();
		HABILIDADES_COMUM = new ArrayList<Habilidade>();
		HABILIDADES_OUTROS = new ArrayList<Habilidade>();
		
		HABILIDADES_OUTROS.add(Habilidade.PASSE);
		
		HABILIDADES_OUTROS.add(Habilidade.FINALIZACAO);
		HABILIDADES_OUTROS.add(Habilidade.CRUZAMENTO);
		HABILIDADES_OUTROS.add(Habilidade.ARMACAO);
		
		HABILIDADES_OUTROS.add(Habilidade.CABECEIO);
		
		HABILIDADES_OUTROS.add(Habilidade.MARCACAO);
		HABILIDADES_OUTROS.add(Habilidade.DESARME);
		HABILIDADES_OUTROS.add(Habilidade.INTERCEPTACAO);
		
		HABILIDADES_OUTROS.add(Habilidade.VELOCIDADE);
		HABILIDADES_OUTROS.add(Habilidade.DIBLE);
		HABILIDADES_OUTROS.add(Habilidade.FORCA);
		
		HABILIDADES_OUTROS.add(Habilidade.POSICIONAMENTO);
		HABILIDADES_OUTROS.add(Habilidade.DOMINIO);
		
		HABILIDADES_ESPECIFICAS.add(Habilidade.REFLEXO);
		HABILIDADES_ESPECIFICAS.add(Habilidade.JOGO_AEREO);
	}

	public List<Habilidade> getHabilidadesEspecificas() {
		return HABILIDADES_ESPECIFICAS;
	}

	public List<Habilidade> getHabilidadesEspecificasEletivas() {
		return HABILIDADES_ESPECIFICAS_ELETIVAS;
	}

	public List<Habilidade> getHabilidadesComum() {
		return HABILIDADES_COMUM;
	}

	public List<Habilidade> getHabilidadesOutros() {
		return HABILIDADES_OUTROS;
	}

	public Integer getNumHabEspEletivas() {
		return NUM_HAB_ESP_ELETIVAS;
	}
	
	private static JogadorGoleiroFactory INSTANCE;
	
	protected JogadorGoleiroFactory() {

	}
	
	public static JogadorGoleiroFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new JogadorGoleiroFactory();
		}
		return INSTANCE;
	}
	
	/*@Override
	public Jogador gerarJogador(Clube clube, Integer numero) {
		return gerarJogador(clube, numero, null);
	}*/

	@Override
	public Jogador gerarJogador(Clube clube, Integer numero, Boolean titular) {
		Jogador jogador = new Jogador();

		jogador.setNumero(numero);
		jogador.setNome(NomeUtil.sortearNome());
		jogador.setClube(clube);
		jogador.setPosicao(Posicao.GOLEIRO);
		jogador.setIdade(sortearIdade(titular));
		jogador.setAposentado(false);

		sortearHabilidadeValor(jogador, clube.getForcaGeral());
		
		return jogador;
	}
	
	@Override
	public Jogador gerarJogador(Clube clube, Integer numero, Integer idade) {
		Jogador jogador = new Jogador();

		jogador.setNumero(numero);
		jogador.setNome(NomeUtil.sortearNome());
		jogador.setClube(clube);
		jogador.setPosicao(Posicao.GOLEIRO);
		jogador.setIdade(idade);
		jogador.setAposentado(false);

		sortearHabilidadeValor(jogador, clube.getForcaGeral());
		
		return jogador;
	}
}

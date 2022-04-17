package com.fastfoot.match.model;

import java.util.List;

import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;

public class EsquemaImpl implements Esquema {//Esquema tatico 4-1-3-2 (2-3-3-2)

	private List<EsquemaPosicao> posicoes;

	private EsquemaPosicao posicaoAtual;

	private Boolean posseBolaMandante;

	private EsquemaPosicao goleiroMandante;

	private EsquemaPosicao goleiroVisitante;

	public List<EsquemaPosicao> getPosicoes() {
		return posicoes;
	}

	public void setPosicoes(List<EsquemaPosicao> posicoes) {
		this.posicoes = posicoes;
	}

	public EsquemaPosicao getPosicaoAtual() {
		return posicaoAtual;
	}

	public void setPosicaoAtual(EsquemaPosicao posicaoAtual) {
		this.posicaoAtual = posicaoAtual;
	}

	public Boolean getPosseBolaMandante() {
		return posseBolaMandante;
	}

	public void setPosseBolaMandante(Boolean posseBolaMandante) {
		this.posseBolaMandante = posseBolaMandante;
	}
	
	public EsquemaPosicao getGoleiroMandante() {
		return goleiroMandante;
	}

	public void setGoleiroMandante(EsquemaPosicao goleiroMandante) {
		this.goleiroMandante = goleiroMandante;
	}

	public EsquemaPosicao getGoleiroVisitante() {
		return goleiroVisitante;
	}

	public void setGoleiroVisitante(EsquemaPosicao goleiroVisitante) {
		this.goleiroVisitante = goleiroVisitante;
	}
	
	//###	METODOS AUXILIARES	###

	@Override
	public List<HabilidadeValor> getHabilidadeValorAcaoMeioFimJogadorPosicaoAtualPosse() {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadeAcaoMeioFimValor();
		} else {
			return posicaoAtual.getVisitante().getHabilidadeAcaoMeioFimValor();
		}
	}

	@Override
	public List<HabilidadeValor> getHabilidadeValorJogadorPosicaoAtualPosse() {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadeValor();
		} else {
			return posicaoAtual.getVisitante().getHabilidadeValor();
		}
	}

	@Override
	public List<HabilidadeValor> getHabilidadeValorAcaoFimJogadorPosicaoAtualPosse() {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadeAcaoFimValor();
		} else {
			return posicaoAtual.getVisitante().getHabilidadeAcaoFimValor();
		}
	}

	@Override
	public List<HabilidadeValor> getHabilidadeValorJogadorPosicaoAtualSemPosse(List<Habilidade> habilidades) {
		if (!posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadeValor(habilidades);
		} else {
			return posicaoAtual.getVisitante().getHabilidadeValor(habilidades);
		}
	}

	@Override
	public void inverterPosse() {
		posseBolaMandante = !posseBolaMandante;
	}

	@Override
	public List<EsquemaTransicao> getTransicoesPosse() {
		if (posseBolaMandante) {
			return posicaoAtual.getTransicoesMandante();
		} else {
			return posicaoAtual.getTransicoesVisitante();
		}
	}

	@Override
	public List<HabilidadeValor> getHabilidadeValor(List<Habilidade> habilidades) {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadeValor(habilidades);
		} else {
			return posicaoAtual.getVisitante().getHabilidadeValor(habilidades);
		}
	}

	@Override
	public EsquemaPosicao getGoleiroPosse() {
		if (posseBolaMandante) {
			return getGoleiroMandante();
		} else {
			return getGoleiroVisitante();
		}
	}

	@Override
	public EsquemaPosicao getGoleiroSemPosse() {
		if (!posseBolaMandante) {
			return getGoleiroMandante();
		} else {
			return getGoleiroVisitante();
		}
	}
	
	@Override
	public Jogador getJogadorPosse() {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante();
		} else {
			return posicaoAtual.getVisitante();
		}
	}
	
	@Override
	public Jogador getJogadorSemPosse() {
		if (!posseBolaMandante) {
			return posicaoAtual.getMandante();
		} else {
			return posicaoAtual.getVisitante();
		}
	}
}

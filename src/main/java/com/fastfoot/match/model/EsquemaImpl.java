package com.fastfoot.match.model;

import java.util.List;

import com.fastfoot.player.model.HabilidadeAcao;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;

public class EsquemaImpl implements Esquema {

	private List<EsquemaPosicao> posicoes;

	private EsquemaPosicao posicaoAtual;

	private Boolean posseBolaMandante;

	private EsquemaPosicao goleiroMandante;

	private EsquemaPosicao goleiroVisitante;

	@Override
	public List<EsquemaPosicao> getPosicoes() {
		return posicoes;
	}

	public void setPosicoes(List<EsquemaPosicao> posicoes) {
		this.posicoes = posicoes;
	}

	@Override
	public EsquemaPosicao getPosicaoAtual() {
		return posicaoAtual;
	}

	@Override
	public void setPosicaoAtual(EsquemaPosicao posicaoAtual) {
		this.posicaoAtual = posicaoAtual;
	}

	@Override
	public Boolean getPosseBolaMandante() {
		return posseBolaMandante;
	}

	public void setPosseBolaMandante(Boolean posseBolaMandante) {
		this.posseBolaMandante = posseBolaMandante;
	}
	
	@Override
	public EsquemaPosicao getGoleiroMandante() {
		return goleiroMandante;
	}

	public void setGoleiroMandante(EsquemaPosicao goleiroMandante) {
		this.goleiroMandante = goleiroMandante;
	}

	@Override
	public EsquemaPosicao getGoleiroVisitante() {
		return goleiroVisitante;
	}

	public void setGoleiroVisitante(EsquemaPosicao goleiroVisitante) {
		this.goleiroVisitante = goleiroVisitante;
	}

	@Override
	public List<HabilidadeValor> getHabilidadesAcaoMeioFimJogadorPosicaoAtualPosse() {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadesAcaoMeioFimValor();
		} else {
			return posicaoAtual.getVisitante().getHabilidadesAcaoMeioFimValor();
		}
	}

	@Override
	public List<HabilidadeValor> getHabilidadesJogadorPosicaoAtualPosse() {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidades();
		} else {
			return posicaoAtual.getVisitante().getHabilidades();
		}
	}

	@Override
	public List<HabilidadeValor> getHabilidadesAcaoFimJogadorPosicaoAtualPosse() {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadesAcaoFimValor();
		} else {
			return posicaoAtual.getVisitante().getHabilidadesAcaoFimValor();
		}
	}

	@Override
	public List<HabilidadeValor> getHabilidadesJogadorPosicaoAtualSemPosse(List<HabilidadeAcao> habilidades) {
		if (!posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidades(habilidades);
		} else {
			return posicaoAtual.getVisitante().getHabilidades(habilidades);
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
	public List<HabilidadeValor> getHabilidades(List<HabilidadeAcao> habilidades) {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidades(habilidades);
		} else {
			return posicaoAtual.getVisitante().getHabilidades(habilidades);
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
	
	@Override
	public Double getProbabilidadeArremateForaPosicaoPosse() {
		return getPosicaoAtual().getProbabilidadeArremateFora(getPosseBolaMandante());
	}
}

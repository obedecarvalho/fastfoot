package com.fastfoot.match.model;

import java.util.List;

import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.player.model.HabilidadeAcao;
import com.fastfoot.player.model.HabilidadeAcaoJogavel;
import com.fastfoot.player.model.HabilidadeGrupoAcao;
import com.fastfoot.player.model.HabilidadeValorJogavel;
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;

public class EsquemaImpl implements Esquema {

	private List<EsquemaPosicao> posicoes;

	private EsquemaPosicao posicaoAtual;

	private Boolean posseBolaMandante;

	private EsquemaPosicao goleiroMandante;

	private EsquemaPosicao goleiroVisitante;
	
	private EscalacaoClube escalacaoClubeMandante;
	
	private EscalacaoClube escalacaoClubeVisitante;

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

	//@Override
	public List<HabilidadeValor> getHabilidadesValorAcaoMeioFimJogadorPosicaoAtualPosse() {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadesValorAcaoMeioFim();
		} else {
			return posicaoAtual.getVisitante().getHabilidadesValorAcaoMeioFim();
		}
	}

	//@Override
	public List<HabilidadeGrupoValor> getHabilidadesGrupoValorAcaoMeioFimJogadorPosicaoAtualPosse() {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadesGrupoValorAcaoMeioFim();
		} else {
			return posicaoAtual.getVisitante().getHabilidadesGrupoValorAcaoMeioFim();
		}
	}
	
	@Override
	public List<? extends HabilidadeValorJogavel> getHabilidadesValorJogavelAcaoMeioFimJogadorPosicaoAtualPosse(
			Boolean agrupado) {
		if (agrupado) {
			return getHabilidadesGrupoValorAcaoMeioFimJogadorPosicaoAtualPosse();
		} else {
			return getHabilidadesValorAcaoMeioFimJogadorPosicaoAtualPosse();
		}
	}

	//@Override
	public List<HabilidadeValor> getHabilidadesValorJogadorPosicaoAtualPosse() {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadesValor();
		} else {
			return posicaoAtual.getVisitante().getHabilidadesValor();
		}
	}

	//@Override
	public List<HabilidadeGrupoValor> getHabilidadesGrupoValorJogadorPosicaoAtualPosse() {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadesGrupoValor();
		} else {
			return posicaoAtual.getVisitante().getHabilidadesGrupoValor();
		}
	}

	@Override
	public List<? extends HabilidadeValorJogavel> getHabilidadesValorJogavelJogadorPosicaoAtualPosse(Boolean agrupado) {
		if (agrupado) {
			return getHabilidadesGrupoValorJogadorPosicaoAtualPosse();
		} else {
			return getHabilidadesValorJogadorPosicaoAtualPosse();
		}
	}

	//@Override
	public List<HabilidadeValor> getHabilidadesValorAcaoFimJogadorPosicaoAtualPosse() {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadesValorAcaoFim();
		} else {
			return posicaoAtual.getVisitante().getHabilidadesValorAcaoFim();
		}
	}

	//@Override
	public List<HabilidadeGrupoValor> getHabilidadesGrupoValorAcaoFimJogadorPosicaoAtualPosse() {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadesGrupoValorAcaoFim();
		} else {
			return posicaoAtual.getVisitante().getHabilidadesGrupoValorAcaoFim();
		}
	}
	
	@Override
	public List<? extends HabilidadeValorJogavel> getHabilidadesValorJogavelAcaoFimJogadorPosicaoAtualPosse(Boolean agrupado) {
		if (agrupado) {
			return getHabilidadesGrupoValorAcaoFimJogadorPosicaoAtualPosse();
		} else {
			return getHabilidadesValorAcaoFimJogadorPosicaoAtualPosse();
		}
	}

	//@Override
	public List<HabilidadeValor> getHabilidadesValorJogadorPosicaoAtualSemPosse(List<HabilidadeAcao> habilidades) {
		if (!posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadesValor(habilidades);
		} else {
			return posicaoAtual.getVisitante().getHabilidadesValor(habilidades);
		}
	}

	//@Override
	public List<HabilidadeGrupoValor> getHabilidadesGrupoValorJogadorPosicaoAtualSemPosse(List<HabilidadeGrupoAcao> habilidades) {
		if (!posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadesGrupoValor(habilidades);
		} else {
			return posicaoAtual.getVisitante().getHabilidadesGrupoValor(habilidades);
		}
	}
	
	@Override
	public List<? extends HabilidadeValorJogavel> getHabilidadesValorJogavelJogadorPosicaoAtualSemPosse(Boolean agrupado,
			List<? extends HabilidadeAcaoJogavel> habilidades) {
		/*if (agrupado) {
			return getHabilidadesGrupoValorJogadorPosicaoAtualSemPosse((List<HabilidadeGrupoAcao>) habilidades);
		} else {
			return getHabilidadesValorJogadorPosicaoAtualSemPosse((List<HabilidadeAcao>) habilidades);
		}*/
		if (!posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadesValorJogavel(agrupado, habilidades);
		} else {
			return posicaoAtual.getVisitante().getHabilidadesValorJogavel(agrupado, habilidades);
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

	//@Override
	public List<HabilidadeValor> getHabilidadesValor(List<HabilidadeAcao> habilidades) {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadesValor(habilidades);
		} else {
			return posicaoAtual.getVisitante().getHabilidadesValor(habilidades);
		}
	}

	//@Override
	public List<HabilidadeGrupoValor> getHabilidadesGrupoValor(List<HabilidadeGrupoAcao> habilidades) {
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadesGrupoValor(habilidades);
		} else {
			return posicaoAtual.getVisitante().getHabilidadesGrupoValor(habilidades);
		}
	}
	
	@Override
	public List<? extends HabilidadeValorJogavel> getHabilidadesValorJogavel(Boolean agrupado,
			List<? extends HabilidadeAcaoJogavel> habilidades) {
		/*if (agrupado) {
			return getHabilidadesGrupoValor((List<HabilidadeGrupoAcao>) habilidades);
		} else {
			return getHabilidadesValor((List<HabilidadeAcao>) habilidades);
		}*/
		if (posseBolaMandante) {
			return posicaoAtual.getMandante().getHabilidadesValorJogavel(agrupado, habilidades);
		} else {
			return posicaoAtual.getVisitante().getHabilidadesValorJogavel(agrupado, habilidades);
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

	@Override
	public EscalacaoClube getEscalacaoClubeMandante() {
		return escalacaoClubeMandante;
	}

	public void setEscalacaoClubeMandante(EscalacaoClube escalacaoClubeMandante) {
		this.escalacaoClubeMandante = escalacaoClubeMandante;
	}

	@Override
	public EscalacaoClube getEscalacaoClubeVisitante() {
		return escalacaoClubeVisitante;
	}

	public void setEscalacaoClubeVisitante(EscalacaoClube escalacaoClubeVisitante) {
		this.escalacaoClubeVisitante = escalacaoClubeVisitante;
	}
}

package com.fastfoot.match.model;

import java.util.ArrayList;
import java.util.List;

import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.match.model.entity.PartidaTorcida;
import com.fastfoot.player.model.entity.HabilidadeGrupoValorEstatistica;
import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;
import com.fastfoot.player.model.entity.JogadorEnergia;
import com.fastfoot.player.model.entity.JogadorEstatisticasSemana;

public class PartidaDadosSalvarDTO {

	private List<HabilidadeValorEstatistica> habilidadeValorEstatistica;

	private List<JogadorEstatisticasSemana> jogadorEstatisticasSemana;
	
	private List<PartidaLance> partidaLances;
	
	private List<JogadorEnergia> jogadorEnergias;

	private List<HabilidadeGrupoValorEstatistica> habilidadeGrupoValorEstatisticas;
	
	private List<MovimentacaoFinanceira> movimentacaoFinanceira;
	
	private List<PartidaTorcida> partidaTorcidaList;
	
	private List<EscalacaoClube> escalacaoClubes;

	public PartidaDadosSalvarDTO() {
		this.habilidadeValorEstatistica = new ArrayList<HabilidadeValorEstatistica>();
		this.jogadorEstatisticasSemana = new ArrayList<JogadorEstatisticasSemana>();
		this.partidaLances = new ArrayList<PartidaLance>();
		this.jogadorEnergias = new ArrayList<JogadorEnergia>();
		this.habilidadeGrupoValorEstatisticas = new ArrayList<HabilidadeGrupoValorEstatistica>();
		this.movimentacaoFinanceira = new ArrayList<MovimentacaoFinanceira>();
		this.partidaTorcidaList = new ArrayList<PartidaTorcida>();
		this.escalacaoClubes = new ArrayList<EscalacaoClube>();
	}

	public List<HabilidadeValorEstatistica> getHabilidadeValorEstatistica() {
		return habilidadeValorEstatistica;
	}
	
	public void adicionarHabilidadeValorEstatistica(List<HabilidadeValorEstatistica> habilidadeValorEstatistica) {
		this.habilidadeValorEstatistica.addAll(habilidadeValorEstatistica);
	}

	public List<JogadorEstatisticasSemana> getJogadorEstatisticasSemana() {
		return jogadorEstatisticasSemana;
	}

	public void adicionarJogadorEstatisticaSemana(List<JogadorEstatisticasSemana> jogadorEstatisticaSemana) {
		this.jogadorEstatisticasSemana.addAll(jogadorEstatisticaSemana);
	}

	public List<PartidaLance> getPartidaLances() {
		return partidaLances;
	}

	public void adicionarPartidaLance(List<PartidaLance> partidaLances) {
		this.partidaLances.addAll(partidaLances);
	}

	public List<JogadorEnergia> getJogadorEnergias() {
		return jogadorEnergias;
	}

	public void adicionarJogadorEnergias(List<JogadorEnergia> jogadorEstatisticaSemana) {
		this.jogadorEnergias.addAll(jogadorEstatisticaSemana);
	}

	public List<HabilidadeGrupoValorEstatistica> getHabilidadeGrupoValorEstatisticas() {
		return habilidadeGrupoValorEstatisticas;
	}

	public void adicionarHabilidadeGrupoValorEstatistica(List<HabilidadeGrupoValorEstatistica> habilidadeGrupoValorEstatisticas) {
		this.habilidadeGrupoValorEstatisticas.addAll(habilidadeGrupoValorEstatisticas);
	}

	public List<MovimentacaoFinanceira> getMovimentacaoFinanceira() {
		return movimentacaoFinanceira;
	}

	public List<PartidaTorcida> getPartidaTorcidaList() {
		return partidaTorcidaList;
	}
	
	public void addMovimentacaoFinanceira(MovimentacaoFinanceira mfe) {
		this.movimentacaoFinanceira.add(mfe);
	}

	public void addPartidaTorcida(PartidaTorcida partidaTorcida) {
		this.partidaTorcidaList.add(partidaTorcida);
	}

	public List<EscalacaoClube> getEscalacaoClubes() {
		return escalacaoClubes;
	}

	public void addEscalacaoClube(EscalacaoClube escalacaoClube) {
		this.escalacaoClubes.add(escalacaoClube);
	}
}

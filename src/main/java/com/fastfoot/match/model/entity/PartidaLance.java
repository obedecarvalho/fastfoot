package com.fastfoot.match.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;

@Entity
public class PartidaLance {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaLanceSequence")	
	@SequenceGenerator(name = "partidaLanceSequence", sequenceName = "partida_lance_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;

	private Habilidade habilidadeUsada;

	private HabilidadeGrupo habilidadeGrupoUsada;
	
	private Boolean vencedor;

	private Integer ordem;

	private Boolean acao;
	
	private Integer minuto;

	@ManyToOne
	@JoinColumn(name = "id_partida_resultado")
	private PartidaResultado partidaResultado;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_amistosa_resultado")
	private PartidaAmistosaResultado partidaAmistosaResultado;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_eliminatoria_resultado")
	private PartidaEliminatoriaResultado partidaEliminatoriaResultado;
	
	public PartidaLance() {

	}

	public PartidaLance(Jogador jogador, Habilidade habilidadeUsada, Boolean vencedor, Integer ordem, Boolean acao) {
		this.jogador = jogador;
		this.habilidadeUsada = habilidadeUsada;
		this.vencedor = vencedor;
		this.ordem = ordem;
		this.acao = acao;
	}
	
	public PartidaLance(Jogador jogador, HabilidadeGrupo habilidadeGrupoUsada, Boolean vencedor, Integer ordem, Boolean acao) {
		this.jogador = jogador;
		this.habilidadeGrupoUsada = habilidadeGrupoUsada;
		this.vencedor = vencedor;
		this.ordem = ordem;
		this.acao = acao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public Habilidade getHabilidadeUsada() {
		return habilidadeUsada;
	}

	public void setHabilidadeUsada(Habilidade habilidadeUsada) {
		this.habilidadeUsada = habilidadeUsada;
	}

	public Boolean getVencedor() {
		return vencedor;
	}

	public void setVencedor(Boolean vencedor) {
		this.vencedor = vencedor;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Boolean getAcao() {
		return acao;
	}

	public void setAcao(Boolean acao) {
		this.acao = acao;
	}

	public PartidaResultado getPartidaResultado() {
		return partidaResultado;
	}

	public void setPartidaResultado(PartidaResultado partidaResultado) {
		this.partidaResultado = partidaResultado;
	}

	public PartidaAmistosaResultado getPartidaAmistosaResultado() {
		return partidaAmistosaResultado;
	}

	public void setPartidaAmistosaResultado(PartidaAmistosaResultado partidaAmistosaResultado) {
		this.partidaAmistosaResultado = partidaAmistosaResultado;
	}

	public PartidaEliminatoriaResultado getPartidaEliminatoriaResultado() {
		return partidaEliminatoriaResultado;
	}

	public void setPartidaEliminatoriaResultado(PartidaEliminatoriaResultado partidaEliminatoriaResultado) {
		this.partidaEliminatoriaResultado = partidaEliminatoriaResultado;
	}

	public Integer getMinuto() {
		return minuto;
	}

	public void setMinuto(Integer minuto) {
		this.minuto = minuto;
	}

	public HabilidadeGrupo getHabilidadeGrupoUsada() {
		return habilidadeGrupoUsada;
	}

	public void setHabilidadeGrupoUsada(HabilidadeGrupo habilidadeGrupoUsada) {
		this.habilidadeGrupoUsada = habilidadeGrupoUsada;
	}

	@Override
	public String toString() {
		return "PartidaLance [jogador=" + jogador + ", habilidadeUsada="
				+ (habilidadeUsada != null ? habilidadeUsada : habilidadeGrupoUsada) + ", vencedor=" + vencedor
				+ ", ordem=" + ordem + ", acao=" + acao + "]";
	}

}

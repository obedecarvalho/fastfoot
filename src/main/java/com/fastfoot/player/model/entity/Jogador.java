package com.fastfoot.player.model.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeAcao;
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.ModoDesenvolvimentoJogador;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.PosicaoAttributeConverter;
import com.fastfoot.player.model.StatusJogador;

@Entity
@Table(indexes = { @Index(columnList = "id_clube, statusJogador") })
public class Jogador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jogadorSequence")	
	@SequenceGenerator(name = "jogadorSequence", sequenceName = "jogador_seq")
	private Long id;

	private String nome;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	@Convert(converter = PosicaoAttributeConverter.class)
	private Posicao posicao;
	
	private Integer numero;

	private Integer idade;

	private Integer forcaGeral;
	
	private Double forcaGeralPotencial;
	
	private StatusJogador statusJogador;
	
	private Double valorTransferencia;

	@JsonIgnore
	@OneToMany(mappedBy = "jogador", fetch = FetchType.LAZY)
	private List<HabilidadeValor> habilidades;
	
	@JsonIgnore
	@OneToMany(mappedBy = "jogador", fetch = FetchType.LAZY)
	private List<HabilidadeGrupoValor> habilidadesGrupo;
	
	/*@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_jogador_detalhe")
	private JogadorDetalhe jogadorDetalhe;*/
	
	private ModoDesenvolvimentoJogador modoDesenvolvimentoJogador;
	
	//###	TRANSIENT	###
	
	@Transient
	private JogadorEstatisticasTemporada estatisticasTemporadaAtual;
	
	@Transient
	private JogadorEstatisticasTemporada estatisticasAmistososTemporadaAtual;
	
	/*@ManyToOne
	@JoinColumn(name = "id_contrato_atual")*/
	@Transient
	private Contrato contratoAtual;

	@JsonIgnore
	@Transient
	private List<HabilidadeValor> habilidadesAcaoFim;

	@JsonIgnore
	@Transient
	private List<HabilidadeValor> habilidadesAcaoMeioFim;

	@Transient
	private JogadorEstatisticasSemana jogadorEstatisticasSemana;

	@Transient
	private JogadorEnergia jogadorEnergia;

	public Jogador() {
		
	}
	
	public Jogador(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Posicao getPosicao() {
		return posicao;
	}

	public void setPosicao(Posicao posicao) {
		this.posicao = posicao;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public List<HabilidadeValor> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(List<HabilidadeValor> habilidadeValor) {
		this.habilidades = habilidadeValor;
	}

	public Integer getForcaGeral() {
		return forcaGeral;
	}

	public void setForcaGeral(Integer forcaGeral) {
		this.forcaGeral = forcaGeral;
	}

	public Double getValorTransferencia() {
		return valorTransferencia;
	}

	public void setValorTransferencia(Double valorTransferencia) {
		this.valorTransferencia = valorTransferencia;
	}

	public StatusJogador getStatusJogador() {
		return statusJogador;
	}

	public void setStatusJogador(StatusJogador statusJogador) {
		this.statusJogador = statusJogador;
	}
	
	public boolean isJogadorAtivo() {
		return StatusJogador.ATIVO.equals(this.statusJogador);
	}

	public Double getForcaGeralPotencial() {
		return forcaGeralPotencial;
	}

	public void setForcaGeralPotencial(Double forcaGeralPotencial) {
		this.forcaGeralPotencial = forcaGeralPotencial;
	}

	/*public JogadorDetalhe getJogadorDetalhe() {
		return jogadorDetalhe;
	}

	public void setJogadorDetalhe(JogadorDetalhe jogadorDetalhe) {
		this.jogadorDetalhe = jogadorDetalhe;
	}*/

	public JogadorEnergia getJogadorEnergia() {
		return jogadorEnergia;
	}

	public void setJogadorEnergia(JogadorEnergia jogadorEnergia) {
		this.jogadorEnergia = jogadorEnergia;
	}

	@JsonIgnore
	@Transient
	public JogadorEstatisticasSemana getJogadorEstatisticasSemana() {
		return jogadorEstatisticasSemana;
	}

	public void setJogadorEstatisticasSemana(JogadorEstatisticasSemana jogadorEstatisticasSemana) {
		this.jogadorEstatisticasSemana = jogadorEstatisticasSemana;
	}

	public List<HabilidadeGrupoValor> getHabilidadesGrupo() {
		return habilidadesGrupo;
	}

	public void setHabilidadesGrupo(List<HabilidadeGrupoValor> habilidadesGrupo) {
		this.habilidadesGrupo = habilidadesGrupo;
	}

	public Contrato getContratoAtual() {
		return contratoAtual;
	}

	public void setContratoAtual(Contrato contratoAtual) {
		this.contratoAtual = contratoAtual;
	}

	public ModoDesenvolvimentoJogador getModoDesenvolvimentoJogador() {
		return modoDesenvolvimentoJogador;
	}

	public void setModoDesenvolvimentoJogador(ModoDesenvolvimentoJogador modoDesenvolvimentoJogador) {
		this.modoDesenvolvimentoJogador = modoDesenvolvimentoJogador;
	}

	public JogadorEstatisticasTemporada getEstatisticasTemporadaAtual() {
		return estatisticasTemporadaAtual;
	}

	public void setEstatisticasTemporadaAtual(JogadorEstatisticasTemporada estatisticasTemporadaAtual) {
		this.estatisticasTemporadaAtual = estatisticasTemporadaAtual;
	}

	public JogadorEstatisticasTemporada getEstatisticasAmistososTemporadaAtual() {
		return estatisticasAmistososTemporadaAtual;
	}

	public void setEstatisticasAmistososTemporadaAtual(JogadorEstatisticasTemporada estatisticasAmistososTemporadaAtual) {
		this.estatisticasAmistososTemporadaAtual = estatisticasAmistososTemporadaAtual;
	}

	@JsonIgnore
	public Double getNivelDesenvolvimento() {
		return getForcaGeral() / getForcaGeralPotencial();
	}

	@Override
	public String toString() {
		return "Jogador [posicao=" + posicao + ", numero=" + numero + "]";
	}
	
	@JsonIgnore
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jogador other = (Jogador) obj;
		return Objects.equals(id, other.id);
	}
	
	//###	METODOS AUXILIARES	###

	@JsonIgnore
	public void addHabilidade(HabilidadeValor habilidadeValor) {
		if (getHabilidades() == null) {
			setHabilidades(new ArrayList<HabilidadeValor>());
		}
		getHabilidades().add(habilidadeValor);
	}

	@JsonIgnore
	public List<HabilidadeValor> getHabilidades(List<HabilidadeAcao> habilidades) {
		return getHabilidades().stream().filter(hv -> habilidades.contains(hv.getHabilidadeAcao())).collect(Collectors.toList());
	}
	
	@JsonIgnore
	public List<HabilidadeValor> getHabilidadeValorByHabilidade(List<Habilidade> habilidades) {
		return getHabilidades().stream().filter(hv -> habilidades.contains(hv.getHabilidade())).collect(Collectors.toList());
	}
	
	@JsonIgnore
	public HabilidadeValor getHabilidadeValorByHabilidade(Habilidade habilidade) {
		return getHabilidades().stream().filter(hv -> habilidade.equals(hv.getHabilidade())).findFirst().get();
	}
	
	@JsonIgnore
	public HabilidadeGrupoValor getHabilidadeGrupoValorByHabilidade(HabilidadeGrupo habilidade) {
		return getHabilidadesGrupo().stream().filter(hv -> habilidade.equals(hv.getHabilidadeGrupo())).findFirst().get();
	}

	@JsonIgnore
	public List<HabilidadeValor> getHabilidadesAcaoFimValor() {
		if (habilidadesAcaoFim == null) {
			habilidadesAcaoFim = getHabilidades(Arrays.asList(HabilidadeAcao.PASSE, HabilidadeAcao.FINALIZACAO,
					HabilidadeAcao.CRUZAMENTO, HabilidadeAcao.ARMACAO));
		}
		return habilidadesAcaoFim; 
	}

	@JsonIgnore
	public List<HabilidadeValor> getHabilidadesAcaoMeioFimValor() {
		if (habilidadesAcaoMeioFim == null) {
			habilidadesAcaoMeioFim = getHabilidades(
					Arrays.asList(HabilidadeAcao.PASSE, HabilidadeAcao.FINALIZACAO, HabilidadeAcao.CRUZAMENTO,
							HabilidadeAcao.ARMACAO, HabilidadeAcao.VELOCIDADE, HabilidadeAcao.DRIBLE, HabilidadeAcao.FORCA));
		}
		return habilidadesAcaoMeioFim;
	}
}

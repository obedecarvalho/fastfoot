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
import com.fastfoot.player.model.HabilidadeAcaoJogavel;
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.HabilidadeGrupoAcao;
import com.fastfoot.player.model.HabilidadeJogavel;
import com.fastfoot.player.model.HabilidadeValorJogavel;
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
	private List<HabilidadeValor> habilidadesValor;
	
	@JsonIgnore
	@OneToMany(mappedBy = "jogador", fetch = FetchType.LAZY)
	private List<HabilidadeGrupoValor> habilidadesGrupoValor;
	
	private ModoDesenvolvimentoJogador modoDesenvolvimentoJogador;
	
	//###	TRANSIENT	###
	
	@Transient
	private JogadorEstatisticasTemporada jogadorEstatisticasTemporadaAtual;
	
	@Transient
	private JogadorEstatisticasTemporada jogadorEstatisticasAmistososTemporadaAtual;
	
	/*@ManyToOne
	@JoinColumn(name = "id_contrato_atual")*/
	@Transient
	private Contrato contratoAtual;

	@JsonIgnore
	@Transient
	private List<HabilidadeValor> habilidadesValorAcaoFim;

	@JsonIgnore
	@Transient
	private List<HabilidadeGrupoValor> habilidadesGrupoValorAcaoFim;

	@JsonIgnore
	@Transient
	private List<HabilidadeValor> habilidadesValorAcaoMeioFim;

	@JsonIgnore
	@Transient
	private List<HabilidadeGrupoValor> habilidadesGrupoValorAcaoMeioFim;

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

	public List<HabilidadeValor> getHabilidadesValor() {
		return habilidadesValor;
	}

	public void setHabilidadesValor(List<HabilidadeValor> habilidadeValor) {
		this.habilidadesValor = habilidadeValor;
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

	public List<HabilidadeGrupoValor> getHabilidadesGrupoValor() {
		return habilidadesGrupoValor;
	}

	public void setHabilidadesGrupoValor(List<HabilidadeGrupoValor> habilidadesGrupo) {
		this.habilidadesGrupoValor = habilidadesGrupo;
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

	public JogadorEstatisticasTemporada getJogadorEstatisticasTemporadaAtual() {
		return jogadorEstatisticasTemporadaAtual;
	}

	public void setJogadorEstatisticasTemporadaAtual(JogadorEstatisticasTemporada jogadorEstatisticasTemporadaAtual) {
		this.jogadorEstatisticasTemporadaAtual = jogadorEstatisticasTemporadaAtual;
	}

	public JogadorEstatisticasTemporada getJogadorEstatisticasAmistososTemporadaAtual() {
		return jogadorEstatisticasAmistososTemporadaAtual;
	}

	public void setJogadorEstatisticasAmistososTemporadaAtual(JogadorEstatisticasTemporada jogadorEstatisticasAmistososTemporadaAtual) {
		this.jogadorEstatisticasAmistososTemporadaAtual = jogadorEstatisticasAmistososTemporadaAtual;
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
	public void addHabilidadeValor(HabilidadeValor habilidadeValor) {
		if (getHabilidadesValor() == null) {
			setHabilidadesValor(new ArrayList<HabilidadeValor>());
		}
		getHabilidadesValor().add(habilidadeValor);
	}

	@JsonIgnore
	public List<HabilidadeValor> getHabilidadesValor(List<HabilidadeAcao> habilidades) {
		return getHabilidadesValor().stream().filter(hv -> habilidades.contains(hv.getHabilidadeAcao())).collect(Collectors.toList());
	}

	@JsonIgnore
	public List<HabilidadeGrupoValor> getHabilidadesGrupoValor(List<HabilidadeGrupoAcao> habilidades) {
		return getHabilidadesGrupoValor().stream().filter(hv -> habilidades.contains(hv.getHabilidadeGrupoAcao())).collect(Collectors.toList());
	}
	
	@JsonIgnore
	public List<? extends HabilidadeValorJogavel> getHabilidadesValorJogavel(Boolean agrupado){
		if (agrupado) {
			return getHabilidadesGrupoValor();
		} else {
			return getHabilidadesValor();
		}
	}
	
	@JsonIgnore
	public List<? extends HabilidadeValorJogavel> getHabilidadesValorJogavel(Boolean agrupado,
			List<? extends HabilidadeAcaoJogavel> habilidades) {
		/*if (agrupado) {
			return getHabilidadesGrupoValor((List<HabilidadeGrupoAcao>) habilidades);
		} else {
			return getHabilidadesValor((List<HabilidadeAcao>) habilidades);
		}*/
		return getHabilidadesValorJogavel(agrupado).stream().filter(hv -> habilidades.contains(hv.getHabilidadeAcaoJogavel())).collect(Collectors.toList());
	}
	
	@JsonIgnore
	public List<HabilidadeValor> getHabilidadeValorByHabilidade(List<Habilidade> habilidades) {
		return getHabilidadesValor().stream().filter(hv -> habilidades.contains(hv.getHabilidade())).collect(Collectors.toList());
	}
	
	@JsonIgnore
	public HabilidadeValor getHabilidadeValorByHabilidade(Habilidade habilidade) {
		return getHabilidadesValor().stream().filter(hv -> habilidade.equals(hv.getHabilidade())).findFirst().get();
	}
	
	@JsonIgnore
	public HabilidadeGrupoValor getHabilidadeGrupoValorByHabilidade(HabilidadeGrupo habilidade) {
		return getHabilidadesGrupoValor().stream().filter(hv -> habilidade.equals(hv.getHabilidadeGrupo())).findFirst().get();
	}
	
	@JsonIgnore
	public HabilidadeValorJogavel getHabilidadeValorJogavelByHabilidade(Boolean agrupado, HabilidadeJogavel habilidadeJogavel) {
		return getHabilidadesValorJogavel(agrupado).stream().filter(hv -> habilidadeJogavel.equals(hv.getHabilidadeJogavel())).findFirst().get();
	}

	@JsonIgnore
	public List<HabilidadeValor> getHabilidadesValorAcaoFim() {
		if (habilidadesValorAcaoFim == null) {
			habilidadesValorAcaoFim = getHabilidadesValor(Arrays.asList(HabilidadeAcao.PASSE, HabilidadeAcao.FINALIZACAO,
					HabilidadeAcao.CRUZAMENTO, HabilidadeAcao.ARMACAO));
		}
		return habilidadesValorAcaoFim; 
	}
	
	@JsonIgnore
	public List<HabilidadeGrupoValor> getHabilidadesGrupoValorAcaoFim() {
		if (habilidadesGrupoValorAcaoFim == null) {
			habilidadesGrupoValorAcaoFim = getHabilidadesGrupoValor(
					Arrays.asList(HabilidadeGrupoAcao.CONCLUSAO, HabilidadeGrupoAcao.CRIACAO));
		}
		return habilidadesGrupoValorAcaoFim; 
	}

	@JsonIgnore
	public List<HabilidadeValor> getHabilidadesValorAcaoMeioFim() {
		if (habilidadesValorAcaoMeioFim == null) {
			habilidadesValorAcaoMeioFim = getHabilidadesValor(
					Arrays.asList(HabilidadeAcao.PASSE, HabilidadeAcao.FINALIZACAO, HabilidadeAcao.CRUZAMENTO,
							HabilidadeAcao.ARMACAO, HabilidadeAcao.VELOCIDADE, HabilidadeAcao.DRIBLE, HabilidadeAcao.FORCA));
		}
		return habilidadesValorAcaoMeioFim;
	}

	@JsonIgnore
	public List<HabilidadeGrupoValor> getHabilidadesGrupoValorAcaoMeioFim() {
		if (habilidadesGrupoValorAcaoMeioFim == null) {
			habilidadesGrupoValorAcaoMeioFim = getHabilidadesGrupoValor(Arrays.asList(HabilidadeGrupoAcao.QUEBRA_LINHA,
					HabilidadeGrupoAcao.CONCLUSAO, HabilidadeGrupoAcao.CRIACAO));
		}
		return habilidadesGrupoValorAcaoMeioFim;
	}
}

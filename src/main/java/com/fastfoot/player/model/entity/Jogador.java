package com.fastfoot.player.model.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeAcao;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.StatusJogador;

@Entity
@Table(indexes = { @Index(columnList = "id_clube") }) //TODO: i-dex JogadorEstatisticasTemporada
public class Jogador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jogadorSequence")	
	@SequenceGenerator(name = "jogadorSequence", sequenceName = "jogador_seq")
	private Long id;

	private String nome;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	private Posicao posicao;
	
	private Integer numero;

	private Integer idade;

	private Integer forcaGeral;
	
	private Integer forcaGeralPotencial;
	
	//
	/*private Double tmpForcaGeralZag;
	private Double tmpForcaGeralLat;
	private Double tmpForcaGeralVol;
	private Double tmpForcaGeralMei;
	private Double tmpForcaGeralAta;
	
	private Double tmpForcaGeralZag2;
	private Double tmpForcaGeralLat2;
	private Double tmpForcaGeralVol2;
	private Double tmpForcaGeralMei2;
	private Double tmpForcaGeralAta2;*/
	//
	
	private Double forcaGeralPotencialEfetiva;
	
	private StatusJogador statusJogador;
	
	private Double valorTransferencia;
	
	@ManyToOne
	@JoinColumn(name = "id_jogador_estatisticas_temporada_atual")
	/*@OneToOne(mappedBy = "jogador")*/
	private JogadorEstatisticasTemporada jogadorEstatisticasTemporadaAtual;
	
	@ManyToOne
	@JoinColumn(name = "id_jogador_estatisticas_amistosos_temporada_atual")
	/*@OneToOne(mappedBy = "jogador")*/
	private JogadorEstatisticasTemporada jogadorEstatisticasAmistososTemporadaAtual;

	/*@OneToOne(mappedBy = "jogador")
	private GrupoDesenvolvimentoJogador grupoDesenvolvimentoJogador;*/
	
	/*@OneToOne(mappedBy = "jogador")
	private EscalacaoJogadorPosicao escalacaoJogadorPosicao;*/
	
	//@Transient
	@OneToMany(mappedBy = "jogador", fetch = FetchType.LAZY)
	private List<HabilidadeValor> habilidades;
	
	@Transient
	private List<HabilidadeValor> habilidadesAcaoFim;
	
	@Transient
	private List<HabilidadeValor> habilidadesAcaoMeioFim;

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

	//
	/*public Double getTmpForcaGeralZag() {
		return tmpForcaGeralZag;
	}

	public void setTmpForcaGeralZag(Double tmpForcaGeralZag) {
		this.tmpForcaGeralZag = tmpForcaGeralZag;
	}

	public Double getTmpForcaGeralLat() {
		return tmpForcaGeralLat;
	}

	public void setTmpForcaGeralLat(Double tmpForcaGeralLat) {
		this.tmpForcaGeralLat = tmpForcaGeralLat;
	}

	public Double getTmpForcaGeralVol() {
		return tmpForcaGeralVol;
	}

	public void setTmpForcaGeralVol(Double tmpForcaGeralVol) {
		this.tmpForcaGeralVol = tmpForcaGeralVol;
	}

	public Double getTmpForcaGeralMei() {
		return tmpForcaGeralMei;
	}

	public void setTmpForcaGeralMei(Double tmpForcaGeralMei) {
		this.tmpForcaGeralMei = tmpForcaGeralMei;
	}

	public Double getTmpForcaGeralZag2() {
		return tmpForcaGeralZag2;
	}

	public void setTmpForcaGeralZag2(Double tmpForcaGeralZag2) {
		this.tmpForcaGeralZag2 = tmpForcaGeralZag2;
	}

	public Double getTmpForcaGeralLat2() {
		return tmpForcaGeralLat2;
	}

	public void setTmpForcaGeralLat2(Double tmpForcaGeralLat2) {
		this.tmpForcaGeralLat2 = tmpForcaGeralLat2;
	}

	public Double getTmpForcaGeralVol2() {
		return tmpForcaGeralVol2;
	}

	public void setTmpForcaGeralVol2(Double tmpForcaGeralVol2) {
		this.tmpForcaGeralVol2 = tmpForcaGeralVol2;
	}

	public Double getTmpForcaGeralMei2() {
		return tmpForcaGeralMei2;
	}

	public void setTmpForcaGeralMei2(Double tmpForcaGeralMei2) {
		this.tmpForcaGeralMei2 = tmpForcaGeralMei2;
	}

	public Double getTmpForcaGeralAta2() {
		return tmpForcaGeralAta2;
	}

	public void setTmpForcaGeralAta2(Double tmpForcaGeralAta2) {
		this.tmpForcaGeralAta2 = tmpForcaGeralAta2;
	}

	public Double getTmpForcaGeralAta() {
		return tmpForcaGeralAta;
	}

	public void setTmpForcaGeralAta(Double tmpForcaGeralAta) {
		this.tmpForcaGeralAta = tmpForcaGeralAta;
	}*/
	//

	public Integer getForcaGeralPotencial() {
		return forcaGeralPotencial;
	}

	public void setForcaGeralPotencial(Integer forcaGeralPotencial) {
		this.forcaGeralPotencial = forcaGeralPotencial;
	}

	/*public GrupoDesenvolvimentoJogador getGrupoDesenvolvimentoJogador() {
		return grupoDesenvolvimentoJogador;
	}

	public void setGrupoDesenvolvimentoJogador(GrupoDesenvolvimentoJogador grupoDesenvolvimentoJogador) {
		this.grupoDesenvolvimentoJogador = grupoDesenvolvimentoJogador;
	}*/
	
	/*public EscalacaoJogadorPosicao getEscalacaoJogadorPosicao() {
		return escalacaoJogadorPosicao;
	}

	public void setEscalacaoJogadorPosicao(EscalacaoJogadorPosicao escalacaoJogadorPosicao) {
		this.escalacaoJogadorPosicao = escalacaoJogadorPosicao;
	}*/

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

	public JogadorEstatisticasTemporada getJogadorEstatisticasTemporadaAtual() {
		return jogadorEstatisticasTemporadaAtual;
	}

	public void setJogadorEstatisticasTemporadaAtual(JogadorEstatisticasTemporada jogadorEstatisticasTemporada) {
		this.jogadorEstatisticasTemporadaAtual = jogadorEstatisticasTemporada;
	}

	public JogadorEstatisticasTemporada getJogadorEstatisticasAmistososTemporadaAtual() {
		return jogadorEstatisticasAmistososTemporadaAtual;
	}

	public void setJogadorEstatisticasAmistososTemporadaAtual(
			JogadorEstatisticasTemporada jogadorEstatisticasAmistososTemporadaAtual) {
		this.jogadorEstatisticasAmistososTemporadaAtual = jogadorEstatisticasAmistososTemporadaAtual;
	}

	public Double getForcaGeralPotencialEfetiva() {
		return forcaGeralPotencialEfetiva;
	}

	public void setForcaGeralPotencialEfetiva(Double forcaGeralPotencialEfetiva) {
		this.forcaGeralPotencialEfetiva = forcaGeralPotencialEfetiva;
	}

	public Double getNivelDesenvolvimento() {
		return getForcaGeral() / getForcaGeralPotencialEfetiva();
	}

	@Override
	public String toString() {
		return "Jogador [posicao=" + posicao + ", numero=" + numero + "]";
	}
	
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

	public void addHabilidade(HabilidadeValor habilidadeValor) {
		if (getHabilidades() == null) {
			setHabilidades(new ArrayList<HabilidadeValor>());
		}
		getHabilidades().add(habilidadeValor);
	}

	public List<HabilidadeValor> getHabilidades(List<HabilidadeAcao> habilidades) {
		return getHabilidades().stream().filter(hv -> habilidades.contains(hv.getHabilidadeAcao())).collect(Collectors.toList());
	}
	
	public List<HabilidadeValor> getHabilidadeValorByHabilidade(List<Habilidade> habilidades) {
		return getHabilidades().stream().filter(hv -> habilidades.contains(hv.getHabilidade())).collect(Collectors.toList());
	}
	
	public HabilidadeValor getHabilidadeValorByHabilidade(Habilidade habilidade) {
		return getHabilidades().stream().filter(hv -> habilidade.equals(hv.getHabilidade())).findFirst().get();
	}

	public List<HabilidadeValor> getHabilidadesAcaoFimValor() {
		if (habilidadesAcaoFim == null) {
			habilidadesAcaoFim = getHabilidades(Arrays.asList(HabilidadeAcao.PASSE, HabilidadeAcao.FINALIZACAO,
					HabilidadeAcao.CRUZAMENTO, HabilidadeAcao.ARMACAO));
		}
		return habilidadesAcaoFim; 
	}

	public List<HabilidadeValor> getHabilidadesAcaoMeioFimValor() {
		if (habilidadesAcaoMeioFim == null) {
			habilidadesAcaoMeioFim = getHabilidades(
					Arrays.asList(HabilidadeAcao.PASSE, HabilidadeAcao.FINALIZACAO, HabilidadeAcao.CRUZAMENTO,
							HabilidadeAcao.ARMACAO, HabilidadeAcao.VELOCIDADE, HabilidadeAcao.DRIBLE, HabilidadeAcao.FORCA));
		}
		return habilidadesAcaoMeioFim;
	}
}

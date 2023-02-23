package com.fastfoot.transfer.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.crud.TemporadaCRUDService;
import com.fastfoot.transfer.model.AdequacaoJogadorDTO;
import com.fastfoot.transfer.model.NivelAdequacao;
import com.fastfoot.transfer.model.TipoNegociacao;
import com.fastfoot.transfer.model.entity.DisponivelNegociacao;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;
//import com.fastfoot.transfer.model.repository.AdequacaoJogadorRepository;
import com.fastfoot.transfer.model.repository.DisponivelNegociacaoRepository;
import com.fastfoot.transfer.model.repository.NecessidadeContratacaoClubeRepository;

@Service
public class AvaliarNecessidadeContratacaoClubeService {
	
	private static final Integer IDADE_MAX_EMPRESTAR = 22;
	
	private static Comparator<AdequacaoJogadorDTO> COMPARATOR;
	
	//###	REPOSITORY	###
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	/*@Autowired
	private AdequacaoJogadorRepository adequacaoJogadorRepository;*/
	
	@Autowired
	private NecessidadeContratacaoClubeRepository necessidadeContratacaoClubeRepository;
	
	@Autowired
	private DisponivelNegociacaoRepository disponivelNegociacaoRepository;
	
	//###	SERVICE	###

	@Autowired
	private TemporadaCRUDService temporadaService;
	
	private double getPercentualForcaJogadorForcaClube(Integer forcaJogador, Integer forcaClube) {		
		return (double) forcaJogador/forcaClube;
	}

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularNecessidadeContratacao(List<Clube> clubes) {
		Temporada temporada = temporadaService.getTemporadaAtual();
		
		List<NecessidadeContratacaoClube> necessidadeContratacaoClubes = new ArrayList<NecessidadeContratacaoClube>();
		List<DisponivelNegociacao> disponivelNegociacao = new ArrayList<DisponivelNegociacao>();
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		long inicio = 0, fim = 0;
		List<String> mensagens = new ArrayList<String>();
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		
		for (Clube c : clubes) {
			calcularNecessidadeContratacaoClube(c, temporada, disponivelNegociacao, necessidadeContratacaoClubes);
		}
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#calcularNecessidadeContratacaoClube:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		
		necessidadeContratacaoClubeRepository.saveAll(necessidadeContratacaoClubes);
		disponivelNegociacaoRepository.saveAll(disponivelNegociacao);
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#saveAll:" + (fim - inicio));
		
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());
		
		System.err.println(mensagens);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	private void calcularNecessidadeContratacaoClube(Clube clube, Temporada temporada,
			List<DisponivelNegociacao> disponivelNegociacao,
			List<NecessidadeContratacaoClube> necessidadeContratacaoClubes) {
		//TODO: fazer validação de Constantes.NUMERO_MINIMO_JOGADORES_LINHA e Constantes.NUMERO_MINIMO_GOLEIROS

		List<Jogador> jogs = jogadorRepository.findByClubeAndStatusJogador(clube, StatusJogador.ATIVO);
		
		List<AdequacaoJogadorDTO> jogsAdq = new ArrayList<AdequacaoJogadorDTO>();
		
		jogsAdq.addAll(jogs.stream()
				.filter(j -> (getPercentualForcaJogadorForcaClube(j.getForcaGeral(),
						clube.getForcaGeral())) >= NivelAdequacao.A.getPorcentagemMinima())
				.map(j -> new AdequacaoJogadorDTO(clube, j, NivelAdequacao.A)).collect(Collectors.toList()));

		jogsAdq.addAll(jogs.stream()
				.filter(j -> ((getPercentualForcaJogadorForcaClube(j.getForcaGeral(),
						clube.getForcaGeral())) >= NivelAdequacao.B.getPorcentagemMinima())
						&& ((getPercentualForcaJogadorForcaClube(j.getForcaGeral(),
								clube.getForcaGeral())) < NivelAdequacao.A.getPorcentagemMinima()))
				.map(j -> new AdequacaoJogadorDTO(clube, j, NivelAdequacao.B)).collect(Collectors.toList()));

		jogsAdq.addAll(jogs.stream()
				.filter(j -> ((getPercentualForcaJogadorForcaClube(j.getForcaGeral(),
						clube.getForcaGeral())) >= NivelAdequacao.C.getPorcentagemMinima())
						&& ((getPercentualForcaJogadorForcaClube(j.getForcaGeral(),
								clube.getForcaGeral())) < NivelAdequacao.B.getPorcentagemMinima()))
				.map(j -> new AdequacaoJogadorDTO(clube, j, NivelAdequacao.C)).collect(Collectors.toList()));

		jogsAdq.addAll(jogs.stream()
				.filter(j -> ((getPercentualForcaJogadorForcaClube(j.getForcaGeral(),
						clube.getForcaGeral())) >= NivelAdequacao.D.getPorcentagemMinima())
						&& ((getPercentualForcaJogadorForcaClube(j.getForcaGeral(),
								clube.getForcaGeral())) < NivelAdequacao.C.getPorcentagemMinima()))
				.map(j -> new AdequacaoJogadorDTO(clube, j, NivelAdequacao.D)).collect(Collectors.toList()));

		jogsAdq.addAll(jogs.stream()
				.filter(j -> ((getPercentualForcaJogadorForcaClube(j.getForcaGeral(),
						clube.getForcaGeral())) >= NivelAdequacao.E.getPorcentagemMinima())
						&& ((getPercentualForcaJogadorForcaClube(j.getForcaGeral(),
								clube.getForcaGeral())) < NivelAdequacao.D.getPorcentagemMinima()))
				.map(j -> new AdequacaoJogadorDTO(clube, j, NivelAdequacao.E)).collect(Collectors.toList()));

		jogsAdq.addAll(jogs.stream()
				.filter(j -> (getPercentualForcaJogadorForcaClube(j.getForcaGeral(),
						clube.getForcaGeral())) < NivelAdequacao.E.getPorcentagemMinima())
				.map(j -> new AdequacaoJogadorDTO(clube, j, NivelAdequacao.F)).collect(Collectors.toList()));

		//adequacaoJogadorRepository.saveAll(jogsAdq);
		
		/*List<NecessidadeContratacaoClube> contratacoes = new ArrayList<NecessidadeContratacaoClube>(); 
		List<DisponivelNegociacao> negociaveis = new ArrayList<DisponivelNegociacao>();*/
		
		calcularNegociacaoesGoleiro(
				jogsAdq.stream().filter(ja -> ja.getJogador().getPosicao().isGoleiro()).collect(Collectors.toList()),
				necessidadeContratacaoClubes, disponivelNegociacao, temporada, clube);
		
		calcularNegociacaoes(
				jogsAdq.stream().filter(ja -> ja.getJogador().getPosicao().isZagueiro()).collect(Collectors.toList()),
				necessidadeContratacaoClubes, disponivelNegociacao, temporada, clube, Posicao.ZAGUEIRO);
		
		calcularNegociacaoes(
				jogsAdq.stream().filter(ja -> ja.getJogador().getPosicao().isLateral()).collect(Collectors.toList()),
				necessidadeContratacaoClubes, disponivelNegociacao, temporada, clube, Posicao.LATERAL);
		
		calcularNegociacaoes(
				jogsAdq.stream().filter(ja -> ja.getJogador().getPosicao().isVolante()).collect(Collectors.toList()),
				necessidadeContratacaoClubes, disponivelNegociacao, temporada, clube, Posicao.VOLANTE);
		
		calcularNegociacaoes(
				jogsAdq.stream().filter(ja -> ja.getJogador().getPosicao().isMeia()).collect(Collectors.toList()),
				necessidadeContratacaoClubes, disponivelNegociacao, temporada, clube, Posicao.MEIA);
		
		calcularNegociacaoes(
				jogsAdq.stream().filter(ja -> ja.getJogador().getPosicao().isAtacante()).collect(Collectors.toList()),
				necessidadeContratacaoClubes, disponivelNegociacao, temporada, clube, Posicao.ATACANTE);
		
		/*necessidadeContratacaoClubeRepository.saveAll(contratacoes);
		disponivelNegociacaoRepository.saveAll(negociaveis);*/

	}
	
	private static Comparator<AdequacaoJogadorDTO> getComparator() {
		if (COMPARATOR == null) {
			COMPARATOR = new Comparator<AdequacaoJogadorDTO>() {
	
				@Override
				public int compare(AdequacaoJogadorDTO o1, AdequacaoJogadorDTO o2) {

					int compare = o2.getJogador().getForcaGeral().compareTo(o1.getJogador().getForcaGeral());//reverse

					if (compare == 0) {
						o1.getJogador().getIdade().compareTo(o2.getJogador().getIdade());
					}

					return compare;
				}
			};
		}
		return COMPARATOR;
	}
	
	private void calcularNegociacaoesGoleiro(List<AdequacaoJogadorDTO> jogsAdq,
			List<NecessidadeContratacaoClube> contratacoes, List<DisponivelNegociacao> negociaveis, Temporada temporada,
			Clube clube) {
		
		Collections.sort(jogsAdq, getComparator());
		
		int i = 0;

		//T
		if (jogsAdq.size() > i && NivelAdequacao.A.equals(jogsAdq.get(i).getNivelAdequacao())) {
			i++;
		} else {
			//Contratar
			contratacoes.add(new NecessidadeContratacaoClube(temporada, clube, Posicao.GOLEIRO, NivelAdequacao.A,
					NivelAdequacao.A, false, true));
		}
		
		//R
		if (jogsAdq.size() > i && (NivelAdequacao.A.equals(jogsAdq.get(i).getNivelAdequacao())
				|| NivelAdequacao.B.equals(jogsAdq.get(i).getNivelAdequacao())
				|| NivelAdequacao.C.equals(jogsAdq.get(i).getNivelAdequacao()))) {
			i++;
		} else {
			// Contratar
			contratacoes.add(new NecessidadeContratacaoClube(temporada, clube, Posicao.GOLEIRO, NivelAdequacao.B,
					NivelAdequacao.C, false, false));
		}
		
		//3
		if (jogsAdq.size() > i && (NivelAdequacao.B.equals(jogsAdq.get(i).getNivelAdequacao())
				|| NivelAdequacao.C.equals(jogsAdq.get(i).getNivelAdequacao())
				|| NivelAdequacao.D.equals(jogsAdq.get(i).getNivelAdequacao())
				|| NivelAdequacao.E.equals(jogsAdq.get(i).getNivelAdequacao()))) {
			i++;
		} else if (jogsAdq.size() > i && NivelAdequacao.A.equals(jogsAdq.get(i).getNivelAdequacao())) {
			// Vender
			negociaveis.add(new DisponivelNegociacao(temporada, clube, jogsAdq.get(i).getJogador(),
					TipoNegociacao.COMPRA_VENDA, true));
			i++;
		} else {
			// Contratar
			contratacoes.add(new NecessidadeContratacaoClube(temporada, clube, Posicao.GOLEIRO, NivelAdequacao.C,
					NivelAdequacao.E, false, false));
		}
		
		while (jogsAdq.size() > i) {
			if (NivelAdequacao.E.equals(jogsAdq.get(i).getNivelAdequacao())
					&& jogsAdq.get(i).getJogador().getIdade() <= IDADE_MAX_EMPRESTAR) {
				//Emprestar
				negociaveis.add(new DisponivelNegociacao(temporada, clube, jogsAdq.get(i).getJogador(),
						TipoNegociacao.EMPRESTIMO, true));
			} else {
				//Vender
				negociaveis.add(new DisponivelNegociacao(temporada, clube, jogsAdq.get(i).getJogador(),
						TipoNegociacao.COMPRA_VENDA, true));
			}
			i++;
		}
	}
	
	private void calcularNegociacaoes(List<AdequacaoJogadorDTO> jogsAdq, List<NecessidadeContratacaoClube> contratacoes,
			List<DisponivelNegociacao> negociaveis, Temporada temporada, Clube clube, Posicao posicao) {
		
		Collections.sort(jogsAdq, getComparator());
		
		int i = 0;
		
		//T
		if (jogsAdq.size() > i && NivelAdequacao.A.equals(jogsAdq.get(i).getNivelAdequacao())) {
			i++;
		} else {
			//Contratar
			contratacoes.add(new NecessidadeContratacaoClube(temporada, clube, posicao, NivelAdequacao.A,
					NivelAdequacao.A, false, true));
		}
		
		//T
		if (jogsAdq.size() > i && (NivelAdequacao.A.equals(jogsAdq.get(i).getNivelAdequacao())
				|| NivelAdequacao.B.equals(jogsAdq.get(i).getNivelAdequacao()))) {
			i++;
		} else {
			// Contratar
			contratacoes.add(new NecessidadeContratacaoClube(temporada, clube, posicao, NivelAdequacao.B,
					NivelAdequacao.B, false, true));
		}
		
		//R
		if (jogsAdq.size() > i && (NivelAdequacao.A.equals(jogsAdq.get(i).getNivelAdequacao())
				|| NivelAdequacao.B.equals(jogsAdq.get(i).getNivelAdequacao())
				|| NivelAdequacao.C.equals(jogsAdq.get(i).getNivelAdequacao()))) {
			i++;
		} else {
			// Contratar
			contratacoes.add(new NecessidadeContratacaoClube(temporada, clube, posicao, NivelAdequacao.B,
					NivelAdequacao.C, false, false));
		}
		
		//4
		if (jogsAdq.size() > i && (NivelAdequacao.B.equals(jogsAdq.get(i).getNivelAdequacao())
				|| NivelAdequacao.C.equals(jogsAdq.get(i).getNivelAdequacao())
				|| NivelAdequacao.D.equals(jogsAdq.get(i).getNivelAdequacao()))) {
			i++;
		} else if (jogsAdq.size() > i && NivelAdequacao.A.equals(jogsAdq.get(i).getNivelAdequacao())) {
			// Vender
			negociaveis.add(new DisponivelNegociacao(temporada, clube, jogsAdq.get(i).getJogador(),
					TipoNegociacao.COMPRA_VENDA, true));
			i++;
		} else {
			// Contratar
			contratacoes.add(new NecessidadeContratacaoClube(temporada, clube, posicao, NivelAdequacao.C,
					NivelAdequacao.D, false, false));
		}
		
		//5
		if (jogsAdq.size() > i && (NivelAdequacao.D.equals(jogsAdq.get(i).getNivelAdequacao())
				|| NivelAdequacao.E.equals(jogsAdq.get(i).getNivelAdequacao()))) {
			i++;
		} else if (jogsAdq.size() > i && (NivelAdequacao.A.equals(jogsAdq.get(i).getNivelAdequacao())
				|| NivelAdequacao.B.equals(jogsAdq.get(i).getNivelAdequacao())
				|| NivelAdequacao.C.equals(jogsAdq.get(i).getNivelAdequacao())
				)) {
			// Vender
			negociaveis.add(new DisponivelNegociacao(temporada, clube, jogsAdq.get(i).getJogador(),
					TipoNegociacao.COMPRA_VENDA, true));
			i++;
		}
		
		while (jogsAdq.size() > i) {
			if (NivelAdequacao.E.equals(jogsAdq.get(i).getNivelAdequacao())
					&& jogsAdq.get(i).getJogador().getIdade() <= IDADE_MAX_EMPRESTAR) {
				//Emprestar
				negociaveis.add(new DisponivelNegociacao(temporada, clube, jogsAdq.get(i).getJogador(),
						TipoNegociacao.EMPRESTIMO, true));
			} else {
				//Vender
				negociaveis.add(new DisponivelNegociacao(temporada, clube, jogsAdq.get(i).getJogador(),
						TipoNegociacao.COMPRA_VENDA, true));
			}
			i++;
		}
	}
}

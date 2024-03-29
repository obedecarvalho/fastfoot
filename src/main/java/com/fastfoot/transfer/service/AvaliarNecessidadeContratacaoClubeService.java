package com.fastfoot.transfer.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.transfer.model.AdequacaoJogadorDTO;
import com.fastfoot.transfer.model.NivelAdequacao;
import com.fastfoot.transfer.model.TipoNegociacao;
import com.fastfoot.transfer.model.entity.DisponivelNegociacao;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;

@Service
public class AvaliarNecessidadeContratacaoClubeService {
	
	private static final Integer IDADE_MAX_EMPRESTAR = 22;
	
	private static final Comparator<AdequacaoJogadorDTO> COMPARATOR;
	
	static {
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
	
	private double getPercentualForcaJogadorForcaClube(Integer forcaJogador, Integer forcaClube) {		
		return (double) forcaJogador/forcaClube;
	}
	
	public void calcularNecessidadeContratacaoEDisponivelNegociacao(
			Temporada temporada,
			Map<Clube, List<Jogador>> jogadoresClube,
			List<NecessidadeContratacaoClube> necessidadeContratacaoClubes, 
			List<DisponivelNegociacao> disponivelNegociacao) {

		for (Clube c : jogadoresClube.keySet()) {
			calcularNecessidadeContratacaoClube(jogadoresClube.get(c), c, temporada, disponivelNegociacao, necessidadeContratacaoClubes);
		}

	}

	private void calcularNecessidadeContratacaoClube(List<Jogador> jogs, Clube clube, Temporada temporada,
			List<DisponivelNegociacao> disponivelNegociacao,
			List<NecessidadeContratacaoClube> necessidadeContratacaoClubes) {
		//TODO: fazer validação de Constantes.NUMERO_MINIMO_JOGADORES_LINHA e Constantes.NUMERO_MINIMO_GOLEIROS

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

	}
	
	private static Comparator<AdequacaoJogadorDTO> getComparator() {
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

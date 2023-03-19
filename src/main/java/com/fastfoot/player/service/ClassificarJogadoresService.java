package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.service.util.ClusterKMeans;
import com.fastfoot.service.util.ElementoKMeansImpl;
import com.fastfoot.service.util.KMeansUtil;

@Service
public class ClassificarJogadoresService {
	
	public ClusterKMeans[] agruparJogadoresPorHabilidade(List<Jogador> jogadores, int numeroClusters) {

		ClusterKMeans[] clustersKMeans = KMeansUtil.executarKMeans(
				transformToElementoKMeansPorHabilidade(jogadores, (Habilidade[]) Habilidade.getAll().toArray()),
				numeroClusters);

		return clustersKMeans;
	}
	
	public ClusterKMeans[] classificarJogadoresPorHabilidade(List<Jogador> jogadores) {
		
		ClusterKMeans[] clustersKMeans = KMeansUtil.classificar(
				transformToElementoKMeansPorHabilidade(jogadores, (Habilidade[]) Habilidade.getAll().toArray()),
				getClustersPorHabilidade());
		
		return clustersKMeans;
	}
	
	private List<ElementoKMeansImpl<Jogador>> transformToElementoKMeansPorHabilidade(List<Jogador> jogadores, Habilidade[] habilidade) {
		Map<Jogador, ElementoKMeansImpl<Jogador>> x = jogadores.stream()
				.map(j -> new ElementoKMeansImpl<Jogador>(j, habilidade.length))
				.collect(Collectors.toMap(ElementoKMeansImpl<Jogador>::getElemento, Function.identity()));

		for (int i = 0; i < habilidade.length; i++) {
			int k = i;
			jogadores.stream().forEach(j -> x.get(j)
					.getAtributos()[k] = (j.getHabilidadeValorByHabilidade(habilidade[k]).getPotencialDesenvolvimento()
							/ j.getForcaGeralPotencialEfetiva()));
		}
		
		return new ArrayList<ElementoKMeansImpl<Jogador>>(x.values());
	}
	
	public ClusterKMeans[] agruparJogadoresPorGrupoHabilidade(List<Jogador> jogadores, int numeroClusters) {
		
		ClusterKMeans[] clustersKMeans = KMeansUtil.executarKMeans(transformToElementoKMeansPorHabilidadeGrupo(
				jogadores, (HabilidadeGrupo[]) HabilidadeGrupo.getAll().toArray()), numeroClusters);
		
		return clustersKMeans;
	}
	
	public ClusterKMeans[] classificarJogadoresPorGrupoHabilidade(List<Jogador> jogadores) {
		
		ClusterKMeans[] clustersKMeans = KMeansUtil.classificar(transformToElementoKMeansPorHabilidadeGrupo(jogadores,
				(HabilidadeGrupo[]) HabilidadeGrupo.getAll().toArray()), getClustersPorGrupoHabilidade());
		
		return clustersKMeans;
	}
	
	private List<ElementoKMeansImpl<Jogador>> transformToElementoKMeansPorHabilidadeGrupo(List<Jogador> jogadores, HabilidadeGrupo[] habilidade) {
		
		Map<Jogador, ElementoKMeansImpl<Jogador>> x = jogadores.stream()
				.map(j -> new ElementoKMeansImpl<Jogador>(j, habilidade.length))
				.collect(Collectors.toMap(ElementoKMeansImpl<Jogador>::getElemento, Function.identity()));

		for (int i = 0; i < habilidade.length; i++) {
			int k = i;
			jogadores.stream().forEach(j -> x.get(j)
					.getAtributos()[k] = (j.getHabilidadeGrupoValorByHabilidade(habilidade[k]).getValor()
							/ j.getForcaGeral()));
		}
		
		return new ArrayList<ElementoKMeansImpl<Jogador>>(x.values());
	}

	private ClusterKMeans[] getClustersPorHabilidade() {
		
		//PASSE, FINALIZACAO, CRUZAMENTO, ARMACAO, MARCACAO, DESARME, INTERCEPTACAO,
		//VELOCIDADE, DRIBLE, FORCA, CABECEIO, POSICIONAMENTO, DOMINIO, REFLEXO, JOGO_AEREO
		Double[] centroideG = new Double[]{ 0.330, 0.330, 0.330, 0.330, 0.330, 0.330, 0.330, 0.330, 0.330, 0.330, 0.330, 0.330, 0.330, 1.000, 1.000 };
		Double[] centroideZ = new Double[]{ 0.660, 0.330, 0.330, 0.330, 1.000, 1.000, 1.000, 0.900, 0.330, 0.900, 0.900, 0.330, 0.660, 0.330, 0.330 };
		Double[] centroideL = new Double[]{ 0.900, 0.400, 0.900, 0.400, 0.900, 0.900, 0.900, 0.900, 0.900, 0.400, 0.400, 0.400, 0.660, 0.330, 0.330 };
		Double[] centroideV = new Double[]{ 0.900, 0.450, 0.330, 0.450, 1.000, 1.000, 0.900, 0.450, 0.330, 0.900, 0.450, 0.450, 0.900, 0.330, 0.330 };
		Double[] centroideM = new Double[]{ 1.000, 0.900, 0.900, 1.000, 0.450, 0.330, 0.330, 0.900, 0.900, 0.450, 0.450, 0.900, 1.000, 0.330, 0.330 };
		Double[] centroideA = new Double[]{ 0.900, 1.000, 0.600, 0.600, 0.330, 0.330, 0.330, 0.600, 0.600, 0.900, 0.600, 0.900, 1.000, 0.330, 0.330 };

		ClusterKMeans[] clustersKMeans = new ClusterKMeans[6];

		clustersKMeans[0] = new ClusterKMeans(0, "G", centroideG);
		clustersKMeans[1] = new ClusterKMeans(1, "Z", centroideZ);
		clustersKMeans[2] = new ClusterKMeans(2, "L", centroideL);
		clustersKMeans[3] = new ClusterKMeans(3, "V", centroideV);
		clustersKMeans[4] = new ClusterKMeans(4, "M", centroideM);
		clustersKMeans[5] = new ClusterKMeans(5, "A", centroideA);
		
		return clustersKMeans;
	}
	
	private ClusterKMeans[] getClustersPorGrupoHabilidade() {
		
		//CONCLUSAO, CRIACAO, DEFESA, GOLEIRO, POSSE_BOLA, QUEBRA_LINHA
		Double[] centroideG = new Double[] {0.330, 0.330, 0.330, 1.000, 0.330, 0.330};
		Double[] centroideZ = new Double[] {0.600, 0.450, 0.950, 0.330, 0.650, 0.600};
		Double[] centroideL = new Double[] {0.400, 0.750, 0.700, 0.330, 0.850, 0.650};
		Double[] centroideV = new Double[] {0.450, 0.550, 0.850, 0.330, 0.650, 0.550};
		Double[] centroideM = new Double[] {0.650, 0.950, 0.400, 0.330, 0.950, 0.750};
		Double[] centroideA = new Double[] {0.800, 0.700, 0.500, 0.330, 0.750, 0.750};
		
		ClusterKMeans[] clustersKMeans = new ClusterKMeans[6];
		
		clustersKMeans[0] = new ClusterKMeans(0, "G", centroideG);
		clustersKMeans[1] = new ClusterKMeans(1, "Z", centroideZ);
		clustersKMeans[2] = new ClusterKMeans(2, "L", centroideL);
		clustersKMeans[3] = new ClusterKMeans(3, "V", centroideV);
		clustersKMeans[4] = new ClusterKMeans(4, "M", centroideM);
		clustersKMeans[5] = new ClusterKMeans(5, "A", centroideA);
		
		return clustersKMeans;
	}
}

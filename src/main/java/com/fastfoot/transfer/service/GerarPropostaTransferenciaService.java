package com.fastfoot.transfer.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.TemporadaService;
import com.fastfoot.transfer.model.NivelAdequacao;
import com.fastfoot.transfer.model.dto.JogadorAlvoDTO;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;
//import com.fastfoot.transfer.model.entity.AdequacaoElencoPosicao;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;
import com.fastfoot.transfer.model.repository.NecessidadeContratacaoClubeRepository;
//import com.fastfoot.transfer.model.repository.AdequacaoElencoPosicaoRepository;
import com.fastfoot.transfer.model.repository.PropostaTransferenciaJogadorRepository;

@Service
public class GerarPropostaTransferenciaService {
	
	//###	PARAMETROS	###
	
	//private static final double PROPOSTA_ADEQUACAO_MIN = 0.0d;
	
	//private static final double PROPOSTA_ADEQUACAO_MAX = 0.8d;
	
	private static final int NUM_MIN_JOGADORES = 1;
	
	private static final double DIFERENCA_FORCA_PASSO = 0.02d;
	
	private static final double DIFERENCA_FORCA_PASSO_MIN = 0.03d;
	
	private static final double VAR_FORCA_BASE = 0.10d;
	
	//private static final double DIFERENCA_FORCA_PASSO_MAX = 0.20d;
	
	//###	REPOSITORY	###
	
	/*@Autowired
	private AdequacaoElencoPosicaoRepository adequacaoElencoPosicaoRepository;*/
	
	@Autowired
	private PropostaTransferenciaJogadorRepository propostaTransferenciaJogadorRepository;

	@Autowired
	private NecessidadeContratacaoClubeRepository necessidadeContratacaoClubeRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private TemporadaService temporadaService;
	
	public void gerarPropostaTransferenciaClube(Clube clube) {
		Temporada temporada = temporadaService.getTemporadaAtual();
		
		List<Map<String, Object>> possiveisJogadores = null;

		List<NecessidadeContratacaoClube> necessidadesContratacao = necessidadeContratacaoClubeRepository
				.findByClubeAndTemporadaAndNivelAdequacaoMaxAndNivelAdequacaoMin(clube, temporada, NivelAdequacao.A, NivelAdequacao.A);

		necessidadesContratacao.addAll(necessidadeContratacaoClubeRepository
				.findByClubeAndTemporadaAndNivelAdequacaoMaxAndNivelAdequacaoMin(clube, temporada, NivelAdequacao.B, NivelAdequacao.B));
		
		for (NecessidadeContratacaoClube nc : necessidadesContratacao) {
			/*possiveisJogadores = jogadorRepository.findByTemporadaAndClubeAndPosicaoAndVariacaoForcaMinMax(temporada.getId(), clube.getId(),
					nc.getPosicao().ordinal(), nc.getNivelAdequacaoMin().getPorcentagemMinima(),
					nc.getNivelAdequacaoMax().getPorcentagemMinima());*/
			
			double diferencaForca = DIFERENCA_FORCA_PASSO_MIN;
			int jogadoresEncontrados = 0;
			//System.err.println("\n\t\tXXX");
			double forcaBase = nc.getNivelAdequacaoMin().getPorcentagemMinima() + VAR_FORCA_BASE;
			
			while (diferencaForca < (1.0 - nc.getNivelAdequacaoMin().getPorcentagemMinima()) && jogadoresEncontrados < NUM_MIN_JOGADORES) {
				possiveisJogadores = jogadorRepository.findByTemporadaAndClubeAndPosicaoAndVariacaoForcaMinMax(
						/*temporada.getId(), clube.getId(), nc.getPosicao().ordinal(),*/
						nc.getId(), forcaBase - diferencaForca, forcaBase + diferencaForca);
				
				jogadoresEncontrados = possiveisJogadores.size();
				diferencaForca += DIFERENCA_FORCA_PASSO;
				
				//System.err.println("\t\t" + (forcaBase - diferencaForca) + "\t" + (forcaBase + diferencaForca));
				//System.err.println(jogadoresEncontrados);
				//System.err.println(diferenca_forca);
			}
			
			//System.err.println(diferenca_forca);
			
			if (possiveisJogadores.size() > 0) {
				
				List<JogadorAlvoDTO> jogadoresPossiveis = new ArrayList<JogadorAlvoDTO>();
				
				JogadorAlvoDTO j = null;
			
				for (Map<String, Object> jogMap : possiveisJogadores) {
					j = new JogadorAlvoDTO();
					
					j.setIdJogador(((BigInteger) jogMap.get("id_jogador")).longValue());
					j.setIdClube((int) jogMap.get("id_clube"));
					j.setForcaGeralJogador((int) jogMap.get("forca_geral"));
					j.setTitular((Boolean) jogMap.get("titular"));
					j.setDisponivelNegociacao((Boolean) jogMap.get("disponivel_negociacao"));
					j.setPosicao(Posicao.values()[(int) jogMap.get("posicao")]);
					
					jogadoresPossiveis.add(j);
				}
				
				List<PropostaTransferenciaJogador> props = new ArrayList<PropostaTransferenciaJogador>();
				PropostaTransferenciaJogador prop = null;
				
				for (JogadorAlvoDTO jog : jogadoresPossiveis) {
					prop = new PropostaTransferenciaJogador();
					
					prop.setJogador(new Jogador(jog.getIdJogador()));
					prop.setClubeDestino(clube);
					prop.setClubeOrigem(new Clube(jog.getIdClube()));
					prop.setNecessidadeContratacaoClube(nc);
					prop.setTemporada(temporada);
					
					props.add(prop);
				}
				
				propostaTransferenciaJogadorRepository.saveAll(props);
				
			} else {
				System.err.println("\t\tNão foi encontrado jogadores para negociar");
			}
		}
		
	}

	/*
	 * TODO: levar em conta no futuro:
	 * 	* tempo de contrato
	 *  * valor em caixa
	 *  * estatisticas
	 *  * outros...
	 */
	/*public void gerarPropostaTransferenciaClube(Clube clube) {//TODO: usar forca geral atual??
		List<Map<String, Object>> possiveisJogadores = null;	

		List<AdequacaoElencoPosicao> adqs = adequacaoElencoPosicaoRepository.findByClubeAndAdequacaoBetween(clube, PROPOSTA_ADEQUACAO_MIN, PROPOSTA_ADEQUACAO_MAX);

		for (AdequacaoElencoPosicao adq : adqs) {

			double diferenca_forca = DIFERENCA_FORCA_PASSO_MIN;
			int jogadoresEncontrados = 0;

			while (diferenca_forca < DIFERENCA_FORCA_PASSO_MAX && jogadoresEncontrados < NUM_MIN_JOGADORES) {
				possiveisJogadores = adequacaoElencoPosicaoRepository.findByClubeAndAdequacaoMinMaxAndVariacaoForcaMinMax(clube.getId(), adq.getPosicao().ordinal(), 1 - diferenca_forca, 1 + diferenca_forca);
				jogadoresEncontrados = possiveisJogadores.size();
				diferenca_forca += DIFERENCA_FORCA_PASSO;
			}

			if (jogadoresEncontrados > 0) {
				
				List<Jogador> jogadoresPossiveis = new ArrayList<Jogador>();
				
				Jogador j = null;
			
				for (Map<String, Object> jogMap : possiveisJogadores) {
					j = new Jogador();
					j.setClube(new Clube());
					
					j.setId(((BigInteger) jogMap.get("id_jogador")).longValue());
					j.getClube().setId((int) jogMap.get("id_clube_jog"));
					j.setForcaGeral((int) jogMap.get("forca_geral"));
					
					jogadoresPossiveis.add(j);
				}
				
				List<PropostaTransferenciaJogador> props = new ArrayList<PropostaTransferenciaJogador>();
				PropostaTransferenciaJogador prop = null;
				
				for (Jogador jog : jogadoresPossiveis) {
					prop = new PropostaTransferenciaJogador();
					
					prop.setJogador(jog);
					prop.setClubeDestino(clube);
					prop.setClubeOrigem(j.getClube());
					prop.setAdequacaoElencoPosicao(adq);
					
					props.add(prop);
				}
				
				propostaTransferenciaJogadorRepository.saveAll(props);
				
			} else {
				System.err.println("\t\tNão foi encontrado jogadores para negociar");
			}
		}

	}*/

}

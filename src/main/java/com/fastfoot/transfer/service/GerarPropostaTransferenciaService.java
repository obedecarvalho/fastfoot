package com.fastfoot.transfer.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.TemporadaService;
import com.fastfoot.service.util.RoletaUtil;
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
	
	private static final double DIFERENCA_FORCA_PASSO = 0.025d;
	
	private static final double DIFERENCA_FORCA_PASSO_MIN = 0.05d;
	
	//private static final double VAR_FORCA_BASE = 0.10d;
	
	private static final double DIFERENCA_FORCA_PASSO_MAX = 0.20d;
	
	//###	REPOSITORY	###
	
	/*@Autowired
	private AdequacaoElencoPosicaoRepository adequacaoElencoPosicaoRepository;*/
	
	@Autowired
	private PropostaTransferenciaJogadorRepository propostaTransferenciaJogadorRepository;

	@Autowired
	private NecessidadeContratacaoClubeRepository necessidadeContratacaoClubeRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;

	//###	SERVICE	###

	@Autowired
	private TemporadaService temporadaService;
	
	@Async("transferenciaExecutor")
	public CompletableFuture<Boolean> gerarPropostaTransferencia(Temporada temporada, List<Clube> clubes, Map<Clube, List<NecessidadeContratacaoClube>> necessidadesContratacaoClube) {
		
		List<PropostaTransferenciaJogador> propostaTransferenciaJogadores = new ArrayList<PropostaTransferenciaJogador>();
		
		for (Clube c : clubes) {
			gerarPropostaTransferenciaClube(c, temporada, necessidadesContratacaoClube.get(c), propostaTransferenciaJogadores);
		}
		
		propostaTransferenciaJogadorRepository.saveAll(propostaTransferenciaJogadores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	@Async("transferenciaExecutor")
	public CompletableFuture<Boolean> gerarPropostaTransferencia(List<Clube> clubes) {
		
		Temporada temporada = temporadaService.getTemporadaAtual();
		
		List<PropostaTransferenciaJogador> propostaTransferenciaJogadores = new ArrayList<PropostaTransferenciaJogador>();
		
		for (Clube c : clubes) {
			
			List<NecessidadeContratacaoClube> necessidadesContratacao = necessidadeContratacaoClubeRepository
					.findByClubeAndTemporadaAndNivelAdequacaoMaxAndNivelAdequacaoMinAndNecessidadeSatisfeita(c, temporada, NivelAdequacao.A, NivelAdequacao.A, false);

			necessidadesContratacao.addAll(necessidadeContratacaoClubeRepository
					.findByClubeAndTemporadaAndNivelAdequacaoMaxAndNivelAdequacaoMinAndNecessidadeSatisfeita(c, temporada, NivelAdequacao.B, NivelAdequacao.B, false));
			
			gerarPropostaTransferenciaClube(c, temporada, necessidadesContratacao, propostaTransferenciaJogadores);
		}
		
		propostaTransferenciaJogadorRepository.saveAll(propostaTransferenciaJogadores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	/*
	 * TODO: levar em conta no futuro:
	 * 	* tempo de contrato
	 *  * valor em caixa
	 *  * estatisticas
	 *  * outros...
	 */
	public void gerarPropostaTransferenciaClube(Clube clube, Temporada temporada, List<NecessidadeContratacaoClube> necessidadesContratacao, List<PropostaTransferenciaJogador> propostaTransferenciaJogadores) {

		List<Map<String, Object>> possiveisJogadores = null;
		
		for (NecessidadeContratacaoClube nc : necessidadesContratacao) {
			
			double diferencaForca = DIFERENCA_FORCA_PASSO_MIN;
			//int jogadoresEncontrados = 0;
			long jogadoresDispNeg = 0;
			//double forcaBase = nc.getNivelAdequacaoMin().getPorcentagemMinima() + VAR_FORCA_BASE;
			
			while (diferencaForca < DIFERENCA_FORCA_PASSO_MAX //VAR_FORCA_BASE/2 //(forcaBase - nc.getNivelAdequacaoMin().getPorcentagemMinima()) 
					//&& jogadoresEncontrados < NUM_MIN_JOGADORES
					&& jogadoresDispNeg < NUM_MIN_JOGADORES) {

				possiveisJogadores = jogadorRepository.findByTemporadaAndClubeAndPosicaoAndVariacaoForcaMinMax(
						//nc.getId(), forcaBase - diferencaForca, forcaBase + diferencaForca);
						nc.getId(), nc.getNivelAdequacaoMin().getPorcentagemMinima(), nc.getNivelAdequacaoMin().getPorcentagemMinima() + diferencaForca);
				
				//jogadoresEncontrados = possiveisJogadores.size();
				diferencaForca += DIFERENCA_FORCA_PASSO;
				
				//
				jogadoresDispNeg = possiveisJogadores.stream().filter(
						jm -> jm.get("disponivel_negociacao") != null && ((Boolean) jm.get("disponivel_negociacao")))
						.count();
				possiveisJogadores = possiveisJogadores.stream().filter(
						jm -> jm.get("disponivel_negociacao") != null && ((Boolean) jm.get("disponivel_negociacao")))
						.collect(Collectors.toList());
				//

			}
			
			if (possiveisJogadores.size() > 0) {
				
				List<JogadorAlvoDTO> jogadoresPossiveis = new ArrayList<JogadorAlvoDTO>();
				
				JogadorAlvoDTO j = null;
			
				for (Map<String, Object> jogMap : possiveisJogadores) {
					j = new JogadorAlvoDTO();
					
					j.setIdJogador(((BigInteger) jogMap.get("id_jogador")).longValue());
					j.setIdClube((int) jogMap.get("id_clube"));
					j.setForcaGeralJogador((int) jogMap.get("forca_geral_jog"));
					j.setForcaGeralClube((int) jogMap.get("forca_geral_clube"));
					j.setTitular((Boolean) jogMap.get("titular"));
					j.setDisponivelNegociacao((Boolean) jogMap.get("disponivel_negociacao"));
					j.setPosicao(Posicao.values()[(int) jogMap.get("posicao")]);
					j.setIdade((int) jogMap.get("idade"));
					j.setValorTransferencia((double) jogMap.get("valor_transferencia"));
					
					jogadoresPossiveis.add(j);
				}

				jogadoresPossiveis.stream().forEach(JogadorAlvoDTO::calcularRankTransferencia);
				
				JogadorAlvoDTO jogadorSelecionado = (JogadorAlvoDTO) RoletaUtil.executarN(jogadoresPossiveis);

				/*List<PropostaTransferenciaJogador> props = new ArrayList<PropostaTransferenciaJogador>();
				PropostaTransferenciaJogador prop = null;
				
				for (JogadorAlvoDTO jog : jogadoresPossiveis) {
					prop = new PropostaTransferenciaJogador();
					
					prop.setJogador(new Jogador(jog.getIdJogador()));
					prop.setClubeDestino(clube);
					prop.setClubeOrigem(new Clube(jog.getIdClube()));
					prop.setNecessidadeContratacaoClube(nc);
					prop.setTemporada(temporada);
					
					props.add(prop);
				}*/
				
				PropostaTransferenciaJogador proposta = new PropostaTransferenciaJogador();
				proposta.setJogador(new Jogador(jogadorSelecionado.getIdJogador()));
				proposta.setClubeDestino(clube);
				proposta.setClubeOrigem(new Clube(jogadorSelecionado.getIdClube()));
				proposta.setNecessidadeContratacaoClube(nc);
				proposta.setTemporada(temporada);
				
				propostaTransferenciaJogadores.add(proposta);
				//propostaTransferenciaJogadorRepository.save(proposta);
				
			} else {
				//System.err.println("\t\tNão foram encontrados jogadores para negociar");
			}
		}

	}

	/*public void gerarPropostaTransferenciaClube(Clube clube) {
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

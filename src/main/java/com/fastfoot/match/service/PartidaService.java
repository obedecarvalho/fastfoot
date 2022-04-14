package com.fastfoot.match.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaFactory;
import com.fastfoot.match.model.EsquemaTransicao;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.service.util.ElementoRoleta;
import com.fastfoot.service.util.RoletaUtil;

@Service
public class PartidaService {
	
	@Autowired
	private JogadorRepository jogadorRepository;

	private Integer NUM_LANCES = 80;

	public void jogar(PartidaResultadoJogavel partidaResultado) {
		List<Jogador> jogadoresMandante = jogadorRepository.findByClube(partidaResultado.getClubeMandante());//TODO
		List<Jogador> jogadoresVisitante = jogadorRepository.findByClube(partidaResultado.getClubeVisitante());//TODO

		Esquema esquema = EsquemaFactory.gerarEsquema(jogadoresMandante, jogadoresVisitante);
		
		jogar(esquema, partidaResultado);
	}

	private void jogar(Esquema esquema, PartidaResultadoJogavel partidaResultado) {

		Integer golMandante = 0, golVisitante = 0;

		HabilidadeValor habilidadeVencedorAnterior = null;
		HabilidadeValor habilidadeValorAcao = null;
		HabilidadeValor habilidadeValorReacao = null;
		HabilidadeValor habilidadeFora = null;
		HabilidadeValor habilidadeVencedora = null;
		
		Boolean jogadorAcaoVenceu = null;
		
		for (int i = 0; i < NUM_LANCES; i++) {
			
			do {
			
				if (habilidadeVencedorAnterior != null && habilidadeVencedorAnterior.getHabilidade().contemAcoesSubsequentes()) {
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil.executar((List<? extends ElementoRoleta>) esquema.getHabilidadeValor(habilidadeVencedorAnterior.getHabilidade().getAcoesSubsequentes()));
				} else {
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil.executar((List<? extends ElementoRoleta>) esquema.getHabilidadeValorAcaoMeioFimJogadorPosicaoAtualPosse());
				}
				
				habilidadeValorReacao = (HabilidadeValor) RoletaUtil.executar((List<? extends ElementoRoleta>) esquema.getHabilidadeValorJogadorPosicaoAtualSemPosse(habilidadeValorAcao.getHabilidade().getPossiveisReacoes()));
				
				jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedor(habilidadeValorAcao, habilidadeValorReacao);
				
				//
				//print(esquema, habilidadeValorAcao, habilidadeValorReacao, jogadorAcaoVenceu);
				//
				
				habilidadeVencedorAnterior = habilidadeValorAcao;
			
			} while (jogadorAcaoVenceu && habilidadeValorAcao.getHabilidade().isAcaoInicial());//Dominio
			
			if (jogadorAcaoVenceu) {

				if (habilidadeValorAcao.getHabilidade().isAcaoMeio() || habilidadeValorAcao.getHabilidade().isAcaoInicioMeio()) {
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil.executar((List<? extends ElementoRoleta>) esquema.getHabilidadeValorAcaoFimJogadorPosicaoAtualPosse());
					//System.err.println(String.format("\t\t-> %s", habilidadeValorAcao.getHabilidade().getDescricao()));
				}
				
				if (habilidadeValorAcao.getHabilidade().isExigeGoleiro()) {//FINALIZACAO, CABECEIO
					habilidadeValorReacao = (HabilidadeValor) RoletaUtil.executar((List<? extends ElementoRoleta>) esquema.getGoleiroSemPosse().getGoleiro().getHabilidadeValor(Arrays.asList(habilidadeValorAcao.getHabilidade().getReacaoGoleiro())));
					
					//fora
					habilidadeFora = new HabilidadeValor(Habilidade.NULL, (habilidadeValorAcao.getValor() + habilidadeValorReacao.getValor())/2);
					habilidadeVencedora = (HabilidadeValor) RoletaUtil.executar(Arrays.asList(habilidadeValorAcao, habilidadeValorReacao, habilidadeFora));
					jogadorAcaoVenceu = habilidadeVencedora.equals(habilidadeValorAcao) ? true : false;
					
					if (habilidadeVencedora.equals(habilidadeFora)) System.err.println("\t\tFORA!!!!");
					//
					
					//jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedor(habilidadeValorAcao, habilidadeValorReacao);
					
					//
					//print(esquema, habilidadeValorAcao, habilidadeValorReacao, jogadorAcaoVenceu);
					//
					
					if (jogadorAcaoVenceu) {
						//System.err.println("\t\tGOLLL");
						if (esquema.getPosseBolaMandante()) {
							golMandante++;
						} else {
							golVisitante++;
						}
					} else {
						//System.err.println("\t\tGOLEIRO DEFENDEU");
					}
					esquema.inverterPosse();//TODO
				} else {
					//PASSE, CRUZAMENTO, ARMACAO
					EsquemaTransicao t = (EsquemaTransicao) RoletaUtil.executar((List<? extends ElementoRoleta>) esquema.getTransicoesPosse());
					//System.err.println(String.format("%d ==> %d (%s)", esquema.getPosicaoAtual().getNumero(), t.getPosFinal().getNumero(), esquema.getPosseBolaMandante() ? "M" : "V"));
					esquema.setPosicaoAtual(t.getPosFinal());
				}
				habilidadeVencedorAnterior = habilidadeValorAcao;
			} else {
				esquema.inverterPosse();
				//System.err.println("\t\tPOSSE INVERTIDA");
				//habilidadeVencedorAnterior = habilidadeValorReacao;
				habilidadeVencedorAnterior = null;
			}
		}
		partidaResultado.setGolsMandante(golMandante);
		partidaResultado.setGolsVisitante(golVisitante);
		//System.err.println(String.format("\n\t\t%d x %d", golMandante, golVisitante));
		//TODO: penalts
	}
	
	//###	TESTE	###
	
	public void jogarPartida() {
		int overhallMandante = 85, overhallVisitante = 85;
		
		
		List<Jogador> jogadorMandante =
		Arrays.asList(JogadorFactory.gerarGoleiro(overhallMandante, 1),
				JogadorFactory.gerarZagueiro(overhallMandante, 3), JogadorFactory.gerarZagueiro(overhallMandante, 4),
				JogadorFactory.gerarLateral(overhallMandante, 2), JogadorFactory.gerarVolante(overhallMandante, 5),
				JogadorFactory.gerarLateral(overhallMandante, 6), JogadorFactory.gerarMeiaLateral(overhallMandante, 7),
				JogadorFactory.gerarMeia(overhallMandante, 10), JogadorFactory.gerarMeiaLateral(overhallMandante, 8),
				JogadorFactory.gerarAtacante(overhallMandante, 9), JogadorFactory.gerarAtacante(overhallMandante, 11));

		List<Jogador> jogadorVisitante =
		Arrays.asList(JogadorFactory.gerarGoleiro(overhallVisitante, 12),
				JogadorFactory.gerarZagueiro(overhallVisitante, 23),
				JogadorFactory.gerarZagueiro(overhallVisitante, 24), JogadorFactory.gerarLateral(overhallVisitante, 22),
				JogadorFactory.gerarVolante(overhallVisitante, 25), JogadorFactory.gerarLateral(overhallVisitante, 26),
				JogadorFactory.gerarMeiaLateral(overhallVisitante, 27), JogadorFactory.gerarMeia(overhallVisitante, 30),
				JogadorFactory.gerarMeiaLateral(overhallVisitante, 28),
				JogadorFactory.gerarAtacante(overhallVisitante, 29),
				JogadorFactory.gerarAtacante(overhallVisitante, 31));
		
		Esquema esquema = EsquemaFactory.gerarEsquema(jogadorMandante, jogadorVisitante);
		
		jogar(esquema, null);
	}

	public void print(Esquema esquema, HabilidadeValor habilidadeValorAcao, HabilidadeValor habilidadeValorReacao, boolean jogadorAcaoVenceu) {
		System.err.println(String.format("\t\t%s (%d) x %s (%d) [%s] [%s]", habilidadeValorAcao.getHabilidade().getDescricao(),
				habilidadeValorAcao.getValor(), habilidadeValorReacao.getHabilidade().getDescricao(),
				habilidadeValorReacao.getValor(), esquema.getPosseBolaMandante() ? "M" : "V",
				jogadorAcaoVenceu ? "Venceu" : "Perdeu"));
	}
}

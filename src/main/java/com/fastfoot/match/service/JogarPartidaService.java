package com.fastfoot.match.service;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.PartidaJogadorEstatisticaDTO;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

public abstract class JogarPartidaService {

	public abstract void jogar(PartidaResultadoJogavel partidaResultado,
			PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO);

	public abstract void jogar(PartidaResultadoJogavel partidaResultado, EscalacaoClube escalacaoMandante,
			EscalacaoClube escalacaoVisitante, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO);
	
	protected void calcularMinutosJogador(Esquema esquema) {
		
		if (esquema.getGoleiroMandante().getGoleiro().getJogadorEstatisticasSemana().getMinutoFinal() == null) {
			esquema.getGoleiroMandante().getGoleiro().getJogadorEstatisticasSemana().setMinutoFinal(90);
		}

		if (esquema.getGoleiroVisitante().getGoleiro().getJogadorEstatisticasSemana().getMinutoFinal() == null) {
			esquema.getGoleiroVisitante().getGoleiro().getJogadorEstatisticasSemana().setMinutoFinal(90);
		}

		esquema.getGoleiroMandante().getGoleiro().getJogadorEstatisticasSemana().setNumeroMinutosJogados(
				esquema.getGoleiroMandante().getGoleiro().getJogadorEstatisticasSemana().getMinutoFinal()
						- esquema.getGoleiroMandante().getGoleiro().getJogadorEstatisticasSemana().getMinutoInicial());

		esquema.getGoleiroVisitante().getGoleiro().getJogadorEstatisticasSemana().setNumeroMinutosJogados(
				esquema.getGoleiroVisitante().getGoleiro().getJogadorEstatisticasSemana().getMinutoFinal()
						- esquema.getGoleiroVisitante().getGoleiro().getJogadorEstatisticasSemana().getMinutoInicial());

		esquema.getPosicoes().stream().filter(
				p -> p.getMandante() != null && p.getMandante().getJogadorEstatisticasSemana().getMinutoFinal() == null)
				.forEach(p -> p.getMandante().getJogadorEstatisticasSemana().setMinutoFinal(90));

		esquema.getPosicoes().stream()
				.filter(p -> p.getVisitante() != null
						&& p.getVisitante().getJogadorEstatisticasSemana().getMinutoFinal() == null)
				.forEach(p -> p.getVisitante().getJogadorEstatisticasSemana().setMinutoFinal(90));

		esquema.getPosicoes().stream().filter(p -> p.getMandante() != null)
				.forEach(p -> p.getMandante().getJogadorEstatisticasSemana()
						.setNumeroMinutosJogados(p.getMandante().getJogadorEstatisticasSemana().getMinutoFinal()
								- p.getMandante().getJogadorEstatisticasSemana().getMinutoInicial()));

		esquema.getPosicoes().stream().filter(p -> p.getVisitante() != null)
				.forEach(p -> p.getVisitante().getJogadorEstatisticasSemana()
						.setNumeroMinutosJogados(p.getVisitante().getJogadorEstatisticasSemana().getMinutoFinal()
								- p.getVisitante().getJogadorEstatisticasSemana().getMinutoInicial()));

	}

	protected void salvarEstatisticasJogador(List<Jogador> jogadores,
			PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		partidaJogadorEstatisticaDTO.adicionarJogadorEstatisticaSemana(
				jogadores.stream().filter(j -> j.getJogadorEstatisticasSemana() != null)
						.map(Jogador::getJogadorEstatisticasSemana).collect(Collectors.toList()));
	}
	
	protected void salvarJogadorEnergia(List<Jogador> jogadores,
			PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		partidaJogadorEstatisticaDTO
				.adicionarJogadorEnergias(jogadores.stream().map(j -> j.getJogadorEnergia())
						.filter(je -> je.getEnergia() != 0).collect(Collectors.toList()));
	}

	/**
	 * Todos os jogadores relacionados para o jogo tem o consumo fixo de energia (Viagens, treinamento....).
	 * 
	 * 
	 * @param escalacao
	 */
	protected void inserirConsumoEnergiaFixo(EscalacaoClube escalacao) {
		escalacao.getListEscalacaoJogadorPosicao().forEach(
				e -> e.getJogador().getJogadorEnergia().adicionarEnergia(-Constantes.CONSUMO_PARCIAL_ENERGIA_FIXA));
	}
}

package com.fastfoot.financial.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.entity.ClubeSaldoSemana;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.model.repository.ClubeSaldoSemanaRepository;
import com.fastfoot.scheduler.model.entity.Semana;

@Service
public class CalcularClubeSaldoSemanaService {

	@Autowired
	private ClubeSaldoSemanaRepository clubeSaldoSemanaRepository;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularClubeSaldoSemana(List<Semana> semanas,
			Map<Clube, List<MovimentacaoFinanceira>> movimentacoesClube) {

		List<ClubeSaldoSemana> clubeSaldoSemanas = new ArrayList<ClubeSaldoSemana>();

		for (Clube c : movimentacoesClube.keySet()) {
			calcularClubeSaldoSemana(c, semanas, movimentacoesClube.get(c), clubeSaldoSemanas);
		}

		clubeSaldoSemanaRepository.saveAll(clubeSaldoSemanas);

		return CompletableFuture.completedFuture(Boolean.TRUE);

	}

	private void calcularClubeSaldoSemana(Clube clube, List<Semana> semanas,
			List<MovimentacaoFinanceira> movimentacaoFinanceiras, List<ClubeSaldoSemana> clubeSaldoSemanas) {

		Map<Semana, List<MovimentacaoFinanceira>> semanaMovimentacoes = movimentacaoFinanceiras.stream()
				.filter(mf -> mf.getSemana() != null)//TODO
				.collect(Collectors.groupingBy(MovimentacaoFinanceira::getSemana));

		double saldoAnterior = 0.0d;

		List<MovimentacaoFinanceira> movimentacoesSemana = null;

		ClubeSaldoSemana clubeSaldoSemana = null;

		for (Semana s : semanas) {

			movimentacoesSemana = semanaMovimentacoes.get(s);

			if (movimentacoesSemana != null) {
				for (MovimentacaoFinanceira mf : movimentacoesSemana) {
					saldoAnterior += mf.getValorMovimentacao();
				}
			}

			clubeSaldoSemana = new ClubeSaldoSemana();
			clubeSaldoSemana.setClube(clube);
			clubeSaldoSemana.setSemana(s);
			clubeSaldoSemana.setSaldo(saldoAnterior);
			
			clubeSaldoSemanas.add(clubeSaldoSemana);

		}

	}
}

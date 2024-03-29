package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.model.Constantes;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.player.model.entity.Contrato;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.ContratoRepository;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.crud.SemanaCRUDService;
import com.fastfoot.service.util.RandomUtil;

@Service
public class RenovarContratosAutomaticamenteService {

	@Autowired
	private ContratoRepository contratoRepository;

	/*@Autowired
	private TemporadaCRUDService temporadaCRUDService;*/

	@Autowired
	private SemanaCRUDService semanaCRUDService;
	
	@Autowired
	private CalcularSalarioContratoService calcularSalarioContratoService;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> renovarContratosAutomaticamente(Temporada temporada, LigaJogo liga, boolean primeirosIds) {

		//Temporada temporada = temporadaCRUDService.getTemporadaAtual();

		Semana s = semanaCRUDService.getProximaSemana(temporada.getJogo());

		List<Contrato> contratos = null;

		if (primeirosIds) {
			contratos = contratoRepository.findByLigaJogoAndAtivoAndVencidos(liga, true, liga.getIdClubeInicial(),
					liga.getIdClubeInicial() + 15, temporada.getAno()/*, StatusJogador.ATIVO*/);
		} else {
			contratos = contratoRepository.findByLigaJogoAndAtivoAndVencidos(liga, true, liga.getIdClubeInicial() + 16,
					liga.getIdClubeFinal(), temporada.getAno()/*, StatusJogador.ATIVO*/);
		}

		List<Contrato> contratosNovos = new ArrayList<Contrato>();
		for (Contrato c : contratos.stream().filter(c -> c.getJogador().isJogadorAtivo())
				.collect(Collectors.toList())) {

			int tempoContrato = RenovarContratosAutomaticamenteService.sortearTempoContrato(c.getJogador().getIdade());
			double salario = calcularSalarioContratoService.calcularSalarioContrato(c.getJogador(), tempoContrato);

			contratosNovos.add(new Contrato(c.getClube(), c.getJogador(), s, tempoContrato, true, salario));
		}

		contratoRepository.saveAll(contratosNovos);

		contratos.stream().forEach(c -> c.setAtivo(false));
		contratoRepository.saveAll(contratos);

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	public static Integer sortearTempoContrato(Integer idade) {

		Integer tempoContrato = null;

		if (idade <= Constantes.IDADE_MAX_JOGADOR_JOVEM) {
			tempoContrato = RandomUtil.sortearIntervalo(Constantes.NUMERO_ANO_MIN_CONTRATO_PADRAO,
					Constantes.NUMERO_ANO_MAX_CONTRATO_PADRAO_JOGADOR_JOVEM + 1);
		} else {
			tempoContrato = RandomUtil.sortearIntervalo(Constantes.NUMERO_ANO_MIN_CONTRATO_PADRAO,
					Constantes.NUMERO_ANO_MAX_CONTRATO_PADRAO + 1);
			tempoContrato = Math.min(tempoContrato, JogadorFactory.IDADE_MAX - idade);
		}

		return tempoContrato;
	}
}

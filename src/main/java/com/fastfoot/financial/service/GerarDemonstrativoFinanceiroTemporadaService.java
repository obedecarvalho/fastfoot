package com.fastfoot.financial.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceira;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceiraAttributeConverter;
import com.fastfoot.financial.model.entity.DemonstrativoFinanceiroTemporada;
import com.fastfoot.financial.model.repository.DemonstrativoFinanceiroTemporadaRepository;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.service.util.DatabaseUtil;

@Service
public class GerarDemonstrativoFinanceiroTemporadaService {
	
	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;
	
	@Autowired
	private DemonstrativoFinanceiroTemporadaRepository demonstrativoFinanceiroTemporadaRepository;

	@Autowired
	private JogadorRepository jogadorRepository;
	
	public void gerarDemonstrativoFinanceiroTemporada(Temporada temporada) {

		List<DemonstrativoFinanceiroTemporada> demonstrativosFinanceiroTemporada = new ArrayList<DemonstrativoFinanceiroTemporada>();

		demonstrativosFinanceiroTemporada.addAll(gerarDemonstrativoFinanceiroOperacionalTemporada(temporada));
		demonstrativosFinanceiroTemporada.addAll(gerarDemonstrativoFinanceiroTemporadaCaixa(temporada));
		demonstrativosFinanceiroTemporada.addAll(gerarDemonstrativoFinanceiroAtivosTemporada(temporada));

		demonstrativoFinanceiroTemporadaRepository.saveAll(demonstrativosFinanceiroTemporada);

	}
	
	protected List<DemonstrativoFinanceiroTemporada> gerarDemonstrativoFinanceiroOperacionalTemporada(Temporada temporada) {

		List<DemonstrativoFinanceiroTemporada> demonstrativosFinanceiroTemporada = new ArrayList<DemonstrativoFinanceiroTemporada>();

		DemonstrativoFinanceiroTemporada demonstrativoFinanceiroTemporada;

		List<Map<String, Object>> movimentacoesTemporada = movimentacaoFinanceiraRepository
				.findAgrupadoPorTipoAndTemporada(temporada.getId());

		for (Map<String, Object> mov : movimentacoesTemporada) {
			demonstrativoFinanceiroTemporada = new DemonstrativoFinanceiroTemporada();

			demonstrativoFinanceiroTemporada.setClube(new Clube(DatabaseUtil.getValueLong(mov.get("id_clube"))));
			demonstrativoFinanceiroTemporada.setTemporada(temporada);
			/*demonstrativoFinanceiroTemporada
					.setTipoMovimentacao(TipoMovimentacaoFinanceira.values()[(int) mov.get("tipo_movimentacao")]);*/
			demonstrativoFinanceiroTemporada.setTipoMovimentacao(TipoMovimentacaoFinanceiraAttributeConverter
					.getInstance().convertToEntityAttribute((int) mov.get("tipo_movimentacao")));
			demonstrativoFinanceiroTemporada.setValorMovimentacao(DatabaseUtil.getValueDecimal(mov.get("valor_movimentacao")));

			demonstrativosFinanceiroTemporada.add(demonstrativoFinanceiroTemporada);
		}

		//demonstrativoFinanceiroTemporadaRepository.saveAll(demonstrativosFinanceiroTemporada);
		return demonstrativosFinanceiroTemporada;
	}
	
	protected List<DemonstrativoFinanceiroTemporada> gerarDemonstrativoFinanceiroTemporadaCaixa(Temporada temporada) {
		List<Map<String, Object>> saldoAtual = movimentacaoFinanceiraRepository.findSaldoPorClubeByIdJogo(temporada.getJogo().getId());
		List<Map<String, Object>> saldoTemporadaAtual = movimentacaoFinanceiraRepository.findSaldoPorClube(temporada.getId());
		
		Map<Long, Double[]> saldoClube = new HashMap<Long, Double[]>();
		List<DemonstrativoFinanceiroTemporada> demonstrativosFinanceiroTemporada = new ArrayList<DemonstrativoFinanceiroTemporada>();
		Long idClube;
		Double saldo;
		DemonstrativoFinanceiroTemporada demonstrativoFinanceiroTemporada;
		
		for (Map<String, Object> s : saldoAtual) {
			idClube = DatabaseUtil.getValueLong(s.get("id_clube"));
			saldo = DatabaseUtil.getValueDecimal(s.get("saldo"));
			
			saldoClube.put(idClube, new Double[3]);
			saldoClube.get(idClube)[0] = saldo;
		}
		
		for (Map<String, Object> s : saldoTemporadaAtual) {
			idClube = DatabaseUtil.getValueLong(s.get("id_clube"));
			saldo = DatabaseUtil.getValueDecimal(s.get("saldo"));
			
			saldoClube.get(idClube)[1] = saldo;
			
			saldoClube.get(idClube)[2] = saldoClube.get(idClube)[0] - saldoClube.get(idClube)[1];
		}

		for (Long id : saldoClube.keySet()) {
			demonstrativoFinanceiroTemporada = new DemonstrativoFinanceiroTemporada();
			
			demonstrativoFinanceiroTemporada.setClube(new Clube(id));
			demonstrativoFinanceiroTemporada.setTemporada(temporada);
			demonstrativoFinanceiroTemporada.setTipoMovimentacao(TipoMovimentacaoFinanceira.CAIXA_INICIO_TEMPORADA);
			demonstrativoFinanceiroTemporada.setValorMovimentacao(saldoClube.get(id)[2]);
			
			demonstrativosFinanceiroTemporada.add(demonstrativoFinanceiroTemporada);
			
			demonstrativoFinanceiroTemporada = new DemonstrativoFinanceiroTemporada();
			
			demonstrativoFinanceiroTemporada.setClube(new Clube(id));
			demonstrativoFinanceiroTemporada.setTemporada(temporada);
			demonstrativoFinanceiroTemporada.setTipoMovimentacao(TipoMovimentacaoFinanceira.CAIXA_VARIACAO_TEMPORADA);
			demonstrativoFinanceiroTemporada.setValorMovimentacao(saldoClube.get(id)[1]);
			
			demonstrativosFinanceiroTemporada.add(demonstrativoFinanceiroTemporada);
			
			demonstrativoFinanceiroTemporada = new DemonstrativoFinanceiroTemporada();
			
			demonstrativoFinanceiroTemporada.setClube(new Clube(id));
			demonstrativoFinanceiroTemporada.setTemporada(temporada);
			demonstrativoFinanceiroTemporada.setTipoMovimentacao(TipoMovimentacaoFinanceira.CAIXA_FIM_TEMPORADA);
			demonstrativoFinanceiroTemporada.setValorMovimentacao(saldoClube.get(id)[0]);
			
			demonstrativosFinanceiroTemporada.add(demonstrativoFinanceiroTemporada);
		}
		
		//demonstrativoFinanceiroTemporadaRepository.saveAll(demonstrativosFinanceiroTemporada);
		return demonstrativosFinanceiroTemporada;
	}

	protected List<DemonstrativoFinanceiroTemporada> gerarDemonstrativoFinanceiroAtivosTemporada(Temporada temporada) {
		
		List<Map<String, Object>> valorTransferenciaClubes = jogadorRepository.findValorTransferenciaPorClube(temporada.getJogo().getId());
		
		List<DemonstrativoFinanceiroTemporada> ativos = new ArrayList<DemonstrativoFinanceiroTemporada>();
		
		double valorElenco;
		Long idClube;
		DemonstrativoFinanceiroTemporada demonstrativoFinanceiroTemporada;
		
		for (Map<String, Object> vtc : valorTransferenciaClubes) {
			valorElenco = DatabaseUtil.getValueDecimal(vtc.get("valor_transferencia"));
			idClube = DatabaseUtil.getValueLong(vtc.get("id_clube"));
			
			demonstrativoFinanceiroTemporada = new DemonstrativoFinanceiroTemporada();
			
			demonstrativoFinanceiroTemporada.setClube(new Clube(idClube));
			demonstrativoFinanceiroTemporada.setTemporada(temporada);
			demonstrativoFinanceiroTemporada.setTipoMovimentacao(TipoMovimentacaoFinanceira.ATIVO_VALOR_ELENCO);
			demonstrativoFinanceiroTemporada.setValorMovimentacao(valorElenco);
			
			ativos.add(demonstrativoFinanceiroTemporada);
			
		}

		return ativos;
	}
}

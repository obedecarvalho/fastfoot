package com.fastfoot.player.service.crud;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.PosicaoAttributeConverter;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;
import com.fastfoot.player.model.repository.JogadorEstatisticasSemanaRepository;
import com.fastfoot.player.model.repository.JogadorEstatisticasTemporadaRepository;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.crud.TemporadaCRUDService;
import com.fastfoot.service.CRUDService;

@Service
public class JogadorEstatisticasTemporadaCRUDService implements CRUDService<JogadorEstatisticasTemporada, Long> {

	@Autowired
	private JogadorEstatisticasTemporadaRepository jogadorEstatisticasTemporadaRepository;
	
	@Autowired
	private TemporadaCRUDService temporadaService;
	
	@Autowired
	private JogadorEstatisticasSemanaRepository jogadorEstatisticasSemanaRepository;

	@Override
	public List<JogadorEstatisticasTemporada> getAll() {
		return jogadorEstatisticasTemporadaRepository.findAll();
	}

	@Override
	public JogadorEstatisticasTemporada getById(Long id) {
		Optional<JogadorEstatisticasTemporada> jogadorEstatisticasTemporadaOpt = jogadorEstatisticasTemporadaRepository.findById(id);
		
		if (jogadorEstatisticasTemporadaOpt.isPresent()) {
			return jogadorEstatisticasTemporadaOpt.get();
		}
		
		return null;
	}

	@Override
	public void delete(Long id) {
		jogadorEstatisticasTemporadaRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		jogadorEstatisticasTemporadaRepository.deleteAll();
	}

	@Override
	public JogadorEstatisticasTemporada create(JogadorEstatisticasTemporada t) {
		return jogadorEstatisticasTemporadaRepository.save(t);
	}

	@Override
	public JogadorEstatisticasTemporada update(JogadorEstatisticasTemporada t) {
		return jogadorEstatisticasTemporadaRepository.save(t);
	}

	public List<JogadorEstatisticasTemporada> getByClube(Clube clube) {
		return jogadorEstatisticasTemporadaRepository.findByClube(clube);
	}
	
	public List<JogadorEstatisticasTemporada> getByTemporadaAndClube(Temporada temporada, Clube clube) {
		return jogadorEstatisticasTemporadaRepository.findByTemporadaAndClube(temporada, clube);
	}
	
	public List<JogadorEstatisticasTemporada> getAgrupadoTemporadaAtualByClube(Clube clube, Boolean amistoso) {

		List<Map<String, Object>> estatisticasAgrupada = jogadorEstatisticasSemanaRepository
				.findAgrupadoByTemporadaAndClube(temporadaService.getTemporadaAtual().getId(), clube.getId(), amistoso);
		
		return transformMapToObj(estatisticasAgrupada);

		/*List<JogadorEstatisticasTemporada> estatisticasTemporadas = new ArrayList<JogadorEstatisticasTemporada>();
		
		for (Map<String, Object> e : estatisticasAgrupada) {
			JogadorEstatisticasTemporada jogadorEstatisticasTemporada = new JogadorEstatisticasTemporada();
			
			jogadorEstatisticasTemporada.setJogador(new Jogador(((BigInteger) e.get("id_jogador")).longValue()));
			jogadorEstatisticasTemporada.getJogador().setNome((String) e.get("nome_jogador"));
			jogadorEstatisticasTemporada.setClube(new Clube((Integer) e.get("id_clube")));
			jogadorEstatisticasTemporada.getClube().setNome((String) e.get("nome_clube"));
			jogadorEstatisticasTemporada.getJogador().setClube(jogadorEstatisticasTemporada.getClube());
			jogadorEstatisticasTemporada.setTemporada(new Temporada(((BigInteger) e.get("id_temporada")).longValue()));
			jogadorEstatisticasTemporada.setAmistoso((Boolean) e.get("amistoso"));
			jogadorEstatisticasTemporada.setAssistencias(((BigInteger) e.get("assistencias")).intValue());
			jogadorEstatisticasTemporada.setDefesasDisputaPenalt(((BigInteger) e.get("defesas_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setFaltas(((BigInteger) e.get("faltas")).intValue());
			jogadorEstatisticasTemporada.setFinalizacoesDefendidas(((BigInteger) e.get("finalizacoes_defendidas")).intValue());
			jogadorEstatisticasTemporada.setFinalizacoesFora(((BigInteger) e.get("finalizacoes_fora")).intValue());
			jogadorEstatisticasTemporada.setGoleiroFinalizacoesDefendidas(((BigInteger) e.get("goleiro_finalizacoes_defendidas")).intValue());
			jogadorEstatisticasTemporada.setGolsDisputaPenalt(((BigInteger) e.get("gols_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setGolsMarcados(((BigInteger) e.get("gols_marcados")).intValue());
			jogadorEstatisticasTemporada.setGolsPerdidosDisputaPenalt(((BigInteger) e.get("gols_perdidos_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setGolsSofridos(((BigInteger) e.get("gols_sofridos")).intValue());
			jogadorEstatisticasTemporada.setGolsSofridosDisputaPenalt(((BigInteger) e.get("gols_sofridos_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setNumeroJogos(((BigInteger) e.get("numero_jogos")).intValue());
			jogadorEstatisticasTemporada.setNumeroJogosTitular(((BigInteger) e.get("numero_jogos_titular")).intValue());
			jogadorEstatisticasTemporada.setNumeroMinutosJogados(((BigInteger) e.get("numero_minutos_jogados")).intValue());
			jogadorEstatisticasTemporada.setNumeroRodadaDisputaPenalt(((BigInteger) e.get("numero_rodada_disputa_penalt")).intValue());
			
			estatisticasTemporadas.add(jogadorEstatisticasTemporada);
		}

		return estatisticasTemporadas;*/
	}
	
	public List<JogadorEstatisticasTemporada> getAgrupadoByTemporadaAtual(Boolean amistoso) {
		return getAgrupadoByTemporada(temporadaService.getTemporadaAtual(), amistoso);
	}
	
	public List<JogadorEstatisticasTemporada> getAgrupadoByTemporada(Temporada temporada, Boolean amistoso) {

		List<Map<String, Object>> estatisticasAgrupada = jogadorEstatisticasSemanaRepository
				.findAgrupadoByTemporada(temporada.getId(), amistoso);
		
		return transformMapToObj(estatisticasAgrupada);

		/*List<JogadorEstatisticasTemporada> estatisticasTemporadas = new ArrayList<JogadorEstatisticasTemporada>();
		
		for (Map<String, Object> e : estatisticasAgrupada) {
			JogadorEstatisticasTemporada jogadorEstatisticasTemporada = new JogadorEstatisticasTemporada();
			
			jogadorEstatisticasTemporada.setJogador(new Jogador(((BigInteger) e.get("id_jogador")).longValue()));
			jogadorEstatisticasTemporada.getJogador().setNome((String) e.get("nome_jogador"));
			jogadorEstatisticasTemporada.setClube(new Clube((Integer) e.get("id_clube")));
			jogadorEstatisticasTemporada.getClube().setNome((String) e.get("nome_clube"));
			jogadorEstatisticasTemporada.getJogador().setClube(jogadorEstatisticasTemporada.getClube());
			jogadorEstatisticasTemporada.setTemporada(new Temporada(((BigInteger) e.get("id_temporada")).longValue()));
			jogadorEstatisticasTemporada.setAmistoso((Boolean) e.get("amistoso"));
			jogadorEstatisticasTemporada.setAssistencias(((BigInteger) e.get("assistencias")).intValue());
			jogadorEstatisticasTemporada.setDefesasDisputaPenalt(((BigInteger) e.get("defesas_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setFaltas(((BigInteger) e.get("faltas")).intValue());
			jogadorEstatisticasTemporada.setFinalizacoesDefendidas(((BigInteger) e.get("finalizacoes_defendidas")).intValue());
			jogadorEstatisticasTemporada.setFinalizacoesFora(((BigInteger) e.get("finalizacoes_fora")).intValue());
			jogadorEstatisticasTemporada.setGoleiroFinalizacoesDefendidas(((BigInteger) e.get("goleiro_finalizacoes_defendidas")).intValue());
			jogadorEstatisticasTemporada.setGolsDisputaPenalt(((BigInteger) e.get("gols_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setGolsMarcados(((BigInteger) e.get("gols_marcados")).intValue());
			jogadorEstatisticasTemporada.setGolsPerdidosDisputaPenalt(((BigInteger) e.get("gols_perdidos_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setGolsSofridos(((BigInteger) e.get("gols_sofridos")).intValue());
			jogadorEstatisticasTemporada.setGolsSofridosDisputaPenalt(((BigInteger) e.get("gols_sofridos_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setNumeroJogos(((BigInteger) e.get("numero_jogos")).intValue());
			jogadorEstatisticasTemporada.setNumeroJogosTitular(((BigInteger) e.get("numero_jogos_titular")).intValue());
			jogadorEstatisticasTemporada.setNumeroMinutosJogados(((BigInteger) e.get("numero_minutos_jogados")).intValue());
			jogadorEstatisticasTemporada.setNumeroRodadaDisputaPenalt(((BigInteger) e.get("numero_rodada_disputa_penalt")).intValue());
			
			estatisticasTemporadas.add(jogadorEstatisticasTemporada);
		}

		return estatisticasTemporadas;*/
	}
	

	public List<JogadorEstatisticasTemporada> getAgrupadoByIdCampeonato(Long idCampeonato) {

		List<Map<String, Object>> estatisticasAgrupada = jogadorEstatisticasSemanaRepository
				.findAgrupadoByIdCampeonato(idCampeonato);
		
		return transformMapToObj(estatisticasAgrupada);

		/*List<JogadorEstatisticasTemporada> estatisticasTemporadas = new ArrayList<JogadorEstatisticasTemporada>();
		
		for (Map<String, Object> e : estatisticasAgrupada) {
			JogadorEstatisticasTemporada jogadorEstatisticasTemporada = new JogadorEstatisticasTemporada();
			
			jogadorEstatisticasTemporada.setJogador(new Jogador(((BigInteger) e.get("id_jogador")).longValue()));
			jogadorEstatisticasTemporada.getJogador().setNome((String) e.get("nome_jogador"));
			jogadorEstatisticasTemporada.setClube(new Clube((Integer) e.get("id_clube")));
			jogadorEstatisticasTemporada.getClube().setNome((String) e.get("nome_clube"));
			jogadorEstatisticasTemporada.getJogador().setClube(jogadorEstatisticasTemporada.getClube());
			jogadorEstatisticasTemporada.setTemporada(new Temporada(((BigInteger) e.get("id_temporada")).longValue()));
			jogadorEstatisticasTemporada.setAmistoso((Boolean) e.get("amistoso"));
			jogadorEstatisticasTemporada.setAssistencias(((BigInteger) e.get("assistencias")).intValue());
			jogadorEstatisticasTemporada.setDefesasDisputaPenalt(((BigInteger) e.get("defesas_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setFaltas(((BigInteger) e.get("faltas")).intValue());
			jogadorEstatisticasTemporada.setFinalizacoesDefendidas(((BigInteger) e.get("finalizacoes_defendidas")).intValue());
			jogadorEstatisticasTemporada.setFinalizacoesFora(((BigInteger) e.get("finalizacoes_fora")).intValue());
			jogadorEstatisticasTemporada.setGoleiroFinalizacoesDefendidas(((BigInteger) e.get("goleiro_finalizacoes_defendidas")).intValue());
			jogadorEstatisticasTemporada.setGolsDisputaPenalt(((BigInteger) e.get("gols_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setGolsMarcados(((BigInteger) e.get("gols_marcados")).intValue());
			jogadorEstatisticasTemporada.setGolsPerdidosDisputaPenalt(((BigInteger) e.get("gols_perdidos_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setGolsSofridos(((BigInteger) e.get("gols_sofridos")).intValue());
			jogadorEstatisticasTemporada.setGolsSofridosDisputaPenalt(((BigInteger) e.get("gols_sofridos_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setNumeroJogos(((BigInteger) e.get("numero_jogos")).intValue());
			jogadorEstatisticasTemporada.setNumeroJogosTitular(((BigInteger) e.get("numero_jogos_titular")).intValue());
			jogadorEstatisticasTemporada.setNumeroMinutosJogados(((BigInteger) e.get("numero_minutos_jogados")).intValue());
			jogadorEstatisticasTemporada.setNumeroRodadaDisputaPenalt(((BigInteger) e.get("numero_rodada_disputa_penalt")).intValue());
			
			estatisticasTemporadas.add(jogadorEstatisticasTemporada);
		}

		return estatisticasTemporadas;*/
	}
	
	private List<JogadorEstatisticasTemporada> transformMapToObj(List<Map<String, Object>> estatisticasAgrupada) {
		List<JogadorEstatisticasTemporada> estatisticasTemporadas = new ArrayList<JogadorEstatisticasTemporada>();
		
		for (Map<String, Object> e : estatisticasAgrupada) {
			JogadorEstatisticasTemporada jogadorEstatisticasTemporada = new JogadorEstatisticasTemporada();
			
			jogadorEstatisticasTemporada.setJogador(new Jogador(((BigInteger) e.get("id_jogador")).longValue()));
			jogadorEstatisticasTemporada.getJogador().setNome((String) e.get("nome_jogador"));
			//jogadorEstatisticasTemporada.getJogador().setPosicao(Posicao.values()[(Integer) e.get("posicao")]);
			jogadorEstatisticasTemporada.getJogador().setPosicao(PosicaoAttributeConverter.getInstance().convertToEntityAttribute((String) e.get("posicao")));
			jogadorEstatisticasTemporada.setClube(new Clube((Integer) e.get("id_clube")));
			jogadorEstatisticasTemporada.getClube().setNome((String) e.get("nome_clube"));
			jogadorEstatisticasTemporada.getJogador().setClube(jogadorEstatisticasTemporada.getClube());
			jogadorEstatisticasTemporada.setTemporada(new Temporada(((BigInteger) e.get("id_temporada")).longValue()));
			jogadorEstatisticasTemporada.setAmistoso((Boolean) e.get("amistoso"));
			jogadorEstatisticasTemporada.setAssistencias(((BigInteger) e.get("assistencias")).intValue());
			jogadorEstatisticasTemporada.setDefesasDisputaPenalt(((BigInteger) e.get("defesas_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setFaltas(((BigInteger) e.get("faltas")).intValue());
			jogadorEstatisticasTemporada.setFinalizacoesDefendidas(((BigInteger) e.get("finalizacoes_defendidas")).intValue());
			jogadorEstatisticasTemporada.setFinalizacoesFora(((BigInteger) e.get("finalizacoes_fora")).intValue());
			jogadorEstatisticasTemporada.setGoleiroFinalizacoesDefendidas(((BigInteger) e.get("goleiro_finalizacoes_defendidas")).intValue());
			jogadorEstatisticasTemporada.setGolsDisputaPenalt(((BigInteger) e.get("gols_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setGolsMarcados(((BigInteger) e.get("gols_marcados")).intValue());
			jogadorEstatisticasTemporada.setGolsPerdidosDisputaPenalt(((BigInteger) e.get("gols_perdidos_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setGolsSofridos(((BigInteger) e.get("gols_sofridos")).intValue());
			jogadorEstatisticasTemporada.setGolsSofridosDisputaPenalt(((BigInteger) e.get("gols_sofridos_disputa_penalt")).intValue());
			jogadorEstatisticasTemporada.setNumeroJogos(((BigInteger) e.get("numero_jogos")).intValue());
			jogadorEstatisticasTemporada.setNumeroJogosTitular(((BigInteger) e.get("numero_jogos_titular")).intValue());
			jogadorEstatisticasTemporada.setNumeroMinutosJogados(((BigInteger) e.get("numero_minutos_jogados")).intValue());
			jogadorEstatisticasTemporada.setNumeroRodadaDisputaPenalt(((BigInteger) e.get("numero_rodada_disputa_penalt")).intValue());
			
			estatisticasTemporadas.add(jogadorEstatisticasTemporada);
		}

		return estatisticasTemporadas;
	}
}

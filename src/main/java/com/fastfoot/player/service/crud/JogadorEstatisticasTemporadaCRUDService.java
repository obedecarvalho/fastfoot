package com.fastfoot.player.service.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.player.model.PosicaoAttributeConverter;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;
import com.fastfoot.player.model.repository.JogadorEstatisticasSemanaRepository;
import com.fastfoot.player.model.repository.JogadorEstatisticasTemporadaRepository;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.crud.TemporadaCRUDService;
import com.fastfoot.service.CRUDService;
import com.fastfoot.service.util.DatabaseUtil;

@Service
public class JogadorEstatisticasTemporadaCRUDService implements CRUDService<JogadorEstatisticasTemporada, Long> {

	@Autowired
	private JogadorEstatisticasTemporadaRepository jogadorEstatisticasTemporadaRepository;
	
	@Autowired
	private TemporadaCRUDService temporadaCRUDService;
	
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
	
	public List<JogadorEstatisticasTemporada> getByTemporadaAndClube(Temporada temporada, Clube clube) {
		return jogadorEstatisticasTemporadaRepository.findByTemporadaAndClube(temporada, clube);
	}
	
	public List<JogadorEstatisticasTemporada> getAgrupadoTemporadaAtualByClube(Clube clube, Boolean amistoso) {

		List<Map<String, Object>> estatisticasAgrupada = jogadorEstatisticasSemanaRepository
				.findAgrupadoByTemporadaAndClube(temporadaCRUDService.getTemporadaAtual(clube.getLigaJogo().getJogo()).getId(), clube.getId(), amistoso);

		return transformMapToObj(estatisticasAgrupada);

	}
	
	public List<JogadorEstatisticasTemporada> getAgrupadoByTemporadaAtual(Jogo jogo, Boolean amistoso) {
		return getAgrupadoByTemporada(temporadaCRUDService.getTemporadaAtual(jogo), amistoso);
	}
	
	public List<JogadorEstatisticasTemporada> getAgrupadoByTemporada(Temporada temporada, Boolean amistoso) {

		List<Map<String, Object>> estatisticasAgrupada = jogadorEstatisticasSemanaRepository
				.findAgrupadoByTemporada(temporada.getId(), amistoso);

		return transformMapToObj(estatisticasAgrupada);

	}
	

	public List<JogadorEstatisticasTemporada> getAgrupadoByIdCampeonato(Long idCampeonato) {

		List<Map<String, Object>> estatisticasAgrupada = jogadorEstatisticasSemanaRepository
				.findAgrupadoByIdCampeonato(idCampeonato);

		return transformMapToObj(estatisticasAgrupada);

	}
	
	private List<JogadorEstatisticasTemporada> transformMapToObj(List<Map<String, Object>> estatisticasAgrupada) {
		List<JogadorEstatisticasTemporada> estatisticasTemporadas = new ArrayList<JogadorEstatisticasTemporada>();
		
		for (Map<String, Object> e : estatisticasAgrupada) {
			JogadorEstatisticasTemporada jogadorEstatisticasTemporada = new JogadorEstatisticasTemporada();
			
			jogadorEstatisticasTemporada.setJogador(new Jogador(DatabaseUtil.getValueLong(e.get("id_jogador"))));
			jogadorEstatisticasTemporada.getJogador().setNome((String) e.get("nome_jogador"));
			//jogadorEstatisticasTemporada.getJogador().setPosicao(Posicao.values()[(Integer) e.get("posicao")]);
			jogadorEstatisticasTemporada.getJogador().setPosicao(PosicaoAttributeConverter.getInstance().convertToEntityAttribute((String) e.get("posicao")));
			jogadorEstatisticasTemporada.setClube(new Clube(DatabaseUtil.getValueLong(e.get("id_clube"))));
			jogadorEstatisticasTemporada.getClube().setNome((String) e.get("nome_clube"));
			jogadorEstatisticasTemporada.getClube().setLogo((String) e.get("logo_clube"));
			jogadorEstatisticasTemporada.getJogador().setClube(jogadorEstatisticasTemporada.getClube());
			jogadorEstatisticasTemporada.setTemporada(new Temporada(DatabaseUtil.getValueLong(e.get("id_temporada"))));
			jogadorEstatisticasTemporada.setAmistoso((Boolean) e.get("amistoso"));
			jogadorEstatisticasTemporada.setAssistencias(DatabaseUtil.getValueInteger(e.get("assistencias")));
			jogadorEstatisticasTemporada.setDefesasDisputaPenalties(DatabaseUtil.getValueInteger(e.get("defesas_disputa_penalties")));
			jogadorEstatisticasTemporada.setFaltas(DatabaseUtil.getValueInteger(e.get("faltas")));
			jogadorEstatisticasTemporada.setFinalizacoesDefendidas(DatabaseUtil.getValueInteger(e.get("finalizacoes_defendidas")));
			jogadorEstatisticasTemporada.setFinalizacoesFora(DatabaseUtil.getValueInteger(e.get("finalizacoes_fora")));
			jogadorEstatisticasTemporada.setGoleiroFinalizacoesDefendidas(DatabaseUtil.getValueInteger(e.get("goleiro_finalizacoes_defendidas")));
			jogadorEstatisticasTemporada.setGolsDisputaPenalties(DatabaseUtil.getValueInteger(e.get("gols_disputa_penalties")));
			jogadorEstatisticasTemporada.setGolsMarcados(DatabaseUtil.getValueInteger(e.get("gols_marcados")));
			jogadorEstatisticasTemporada.setGolsPerdidosDisputaPenalties(DatabaseUtil.getValueInteger(e.get("gols_perdidos_disputa_penalties")));
			jogadorEstatisticasTemporada.setGolsSofridos(DatabaseUtil.getValueInteger(e.get("gols_sofridos")));
			jogadorEstatisticasTemporada.setGolsSofridosDisputaPenalties(DatabaseUtil.getValueInteger(e.get("gols_sofridos_disputa_penalties")));
			jogadorEstatisticasTemporada.setJogos(DatabaseUtil.getValueInteger(e.get("jogos")));
			jogadorEstatisticasTemporada.setJogosTitular(DatabaseUtil.getValueInteger(e.get("jogos_titular")));
			jogadorEstatisticasTemporada.setMinutosJogados(DatabaseUtil.getValueInteger(e.get("minutos_jogados")));
			jogadorEstatisticasTemporada.setRodadasDisputaPenalties(DatabaseUtil.getValueInteger(e.get("rodadas_disputa_penalties")));
			
			estatisticasTemporadas.add(jogadorEstatisticasTemporada);
		}

		return estatisticasTemporadas;
	}
}

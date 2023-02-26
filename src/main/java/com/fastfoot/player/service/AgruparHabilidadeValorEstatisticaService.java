package com.fastfoot.player.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.HabilidadeEstatisticaPercentil;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatisticaGrupo;
import com.fastfoot.player.model.repository.HabilidadeValorEstatisticaGrupoRepository;
import com.fastfoot.player.model.repository.HabilidadeValorEstatisticaRepository;
import com.fastfoot.scheduler.model.entity.Temporada;

@Service
public class AgruparHabilidadeValorEstatisticaService {

	@Autowired
	private HabilidadeValorEstatisticaRepository habilidadeValorEstatisticaRepository;
	
	@Autowired
	private HabilidadeValorEstatisticaGrupoRepository habilidadeValorEstatisticaGrupoRepository;
	
	public void agrupar2(Temporada temporada) {
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		long inicio = 0, fim = 0;
		List<String> mensagens = new ArrayList<String>();
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();

		//habilidadeValorEstatisticaGrupoRepository.agruparHabilidadeValorEstatisticas(temporada.getId());
		habilidadeValorEstatisticaGrupoRepository.agruparHabilidadeValorEstatisticas();
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#agruparHabilidadeValorEstatisticas:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();

		//habilidadeValorEstatisticaRepository.deleteByIdTemporada(temporada.getId());
		habilidadeValorEstatisticaRepository.deleteAllInBatch();
		//habilidadeValorEstatisticaRepository.deleteAll();
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#deleteAll:" + (fim - inicio));
		
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());
		
		//System.err.println(mensagens);
	}
	
	public void agrupar(Temporada temporada) {
		
		List<HabilidadeValorEstatisticaGrupo> estatisticasGrupo = new ArrayList<HabilidadeValorEstatisticaGrupo>();
		
		HabilidadeValorEstatisticaGrupo habilidadeValorEstatisticaGrupo = null;
		
		HabilidadeValor habilidadeValor = null;
		
		List<Map<String, Object>> estatisticas = habilidadeValorEstatisticaRepository.findAgrupadoByTemporada(temporada.getId());
		
		for (Map<String, Object> e : estatisticas) {
			habilidadeValorEstatisticaGrupo = new HabilidadeValorEstatisticaGrupo();
			habilidadeValor = new HabilidadeValor();
			habilidadeValor.setId(((BigInteger) e.get("id_habilidade_valor")).longValue());
			habilidadeValorEstatisticaGrupo.setHabilidadeValor(habilidadeValor);
			habilidadeValorEstatisticaGrupo.setTemporada(temporada);
			habilidadeValorEstatisticaGrupo.setQuantidadeUso(((BigInteger) e.get("quantidade_uso")).intValue());
			habilidadeValorEstatisticaGrupo.setQuantidadeUsoVencedor(((BigInteger) e.get("quantidade_uso_vencedor")).intValue());
			habilidadeValorEstatisticaGrupo.setAmistoso((Boolean) e.get("amistoso"));
			
			/*habilidadeValorEstatisticaGrupo
					.setPorcAcerto(new Double(habilidadeValorEstatisticaGrupo.getQuantidadeUsoVencedor())
							/ habilidadeValorEstatisticaGrupo.getQuantidadeUso());*///verificar necessidade de armazenar campo
			
			estatisticasGrupo.add(habilidadeValorEstatisticaGrupo);
		}

		habilidadeValorEstatisticaGrupoRepository.saveAll(estatisticasGrupo);
		
		habilidadeValorEstatisticaRepository.deleteByIdTemporada(temporada.getId());
	}

	public HabilidadeEstatisticaPercentil getPercentilHabilidadeValor(Temporada temporada) {

		List<Map<String, Object>> estatisticas = habilidadeValorEstatisticaGrupoRepository.findPercentilByTemporada(temporada.getId(), false);
		
		HabilidadeEstatisticaPercentil habilidadeEstatisticaPercentil = null;
		
		if (estatisticas.size() > 0) {

			habilidadeEstatisticaPercentil = new HabilidadeEstatisticaPercentil();
			
			habilidadeEstatisticaPercentil
					.setQuantidadeUsoQ3((Integer) estatisticas.get(0).get("qu_q3"));
			habilidadeEstatisticaPercentil
					.setQuantidadeUsoQ2((Integer) estatisticas.get(0).get("qu_q2"));
			habilidadeEstatisticaPercentil
					.setQuantidadeUsoQ1((Integer) estatisticas.get(0).get("qu_q1"));
			habilidadeEstatisticaPercentil
					.setQuantidadeUsoVencedorQ3((Integer) estatisticas.get(0).get("quv_q3"));
			habilidadeEstatisticaPercentil
					.setQuantidadeUsoVencedorQ2((Integer) estatisticas.get(0).get("quv_q2"));
			habilidadeEstatisticaPercentil
					.setQuantidadeUsoVencedorQ1((Integer) estatisticas.get(0).get("quv_q1"));
			habilidadeEstatisticaPercentil.setPorcAcertoQ1((Double) estatisticas.get(0).get("pa_q1"));
			habilidadeEstatisticaPercentil.setPorcAcertoQ2((Double) estatisticas.get(0).get("pa_q2"));
			habilidadeEstatisticaPercentil.setPorcAcertoQ3((Double) estatisticas.get(0).get("pa_q3"));
		}
		
		return habilidadeEstatisticaPercentil;
	}
}

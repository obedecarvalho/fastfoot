package com.fastfoot.player.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	
	public void agrupar(Temporada temporada) {//TODO: excluir HabilidadeValorEstatistica após agrupar
		
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
			
			/*habilidadeValorEstatisticaGrupo
					.setPorcAcerto(new Double(habilidadeValorEstatisticaGrupo.getQuantidadeUsoVencedor())
							/ habilidadeValorEstatisticaGrupo.getQuantidadeUso());*///TODO: verificar necessidade de armazenar campo
			
			estatisticasGrupo.add(habilidadeValorEstatisticaGrupo);
		}

		habilidadeValorEstatisticaGrupoRepository.saveAll(estatisticasGrupo);
		
		habilidadeValorEstatisticaRepository.deleteByIdTemporada(temporada.getId());
	}

	/*public HabilidadeEstatisticaPercentil getPercentilHabilidadeValor(Temporada temporada) {

		List<Map<String, Object>> estatisticas = habilidadeValorEstatisticaRepository.findPercentilByTemporada(temporada.getId());
		
		HabilidadeEstatisticaPercentil habilidadeEstatisticaPercentil = null;
		
		if (estatisticas.size() > 0) {

			habilidadeEstatisticaPercentil = new HabilidadeEstatisticaPercentil();
			
			habilidadeEstatisticaPercentil
					.setQuantidadeUsoQ3(((BigInteger) estatisticas.get(0).get("qu_q3")).intValue());
			habilidadeEstatisticaPercentil
					.setQuantidadeUsoQ2(((BigInteger) estatisticas.get(0).get("qu_q2")).intValue());
			habilidadeEstatisticaPercentil
					.setQuantidadeUsoQ1(((BigInteger) estatisticas.get(0).get("qu_q1")).intValue());
			habilidadeEstatisticaPercentil
					.setQuantidadeUsoVencedorQ3(((BigInteger) estatisticas.get(0).get("quv_q3")).intValue());
			habilidadeEstatisticaPercentil
					.setQuantidadeUsoVencedorQ2(((BigInteger) estatisticas.get(0).get("quv_q2")).intValue());
			habilidadeEstatisticaPercentil
					.setQuantidadeUsoVencedorQ1(((BigInteger) estatisticas.get(0).get("quv_q1")).intValue());

		}
		
		return habilidadeEstatisticaPercentil;
	}*/

	public HabilidadeEstatisticaPercentil getPercentilHabilidadeValor(Temporada temporada) {

		List<Map<String, Object>> estatisticas = habilidadeValorEstatisticaGrupoRepository.findPercentilByTemporada(temporada.getId());
		
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

package com.fastfoot.player.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
			
			habilidadeValorEstatisticaGrupo.setPorcAcerto(new Double(habilidadeValorEstatisticaGrupo.getQuantidadeUsoVencedor())/habilidadeValorEstatisticaGrupo.getQuantidadeUso());
			
			estatisticasGrupo.add(habilidadeValorEstatisticaGrupo);
		}

		habilidadeValorEstatisticaGrupoRepository.saveAll(estatisticasGrupo);
	}
}

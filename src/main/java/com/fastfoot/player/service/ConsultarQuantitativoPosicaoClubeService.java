package com.fastfoot.player.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.PosicaoAttributeConverter;
import com.fastfoot.player.model.QuantitativoPosicaoClubeDTO;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.service.util.DatabaseUtil;

@Service
public class ConsultarQuantitativoPosicaoClubeService {

	@Autowired
	private JogadorRepository jogadorRepository;

	public List<QuantitativoPosicaoClubeDTO> consultarQuantitativoPosicaoClube(Jogo jogo) {

		List<Map<String, Object>> result = jogadorRepository.findQtdeJogadorPorPosicaoPorClube(
				PosicaoAttributeConverter.getInstance().convertToDatabaseColumn(Posicao.GOLEIRO),
				JogadorFactory.IDADE_MAX, jogo.getId());

		List<QuantitativoPosicaoClubeDTO> quantitativoPosicaoClubeList = new ArrayList<QuantitativoPosicaoClubeDTO>();
		QuantitativoPosicaoClubeDTO dto = null;

		for (Map<String, Object> map : result) {
			dto = new QuantitativoPosicaoClubeDTO();
			dto.setClube(new Clube(DatabaseUtil.getValueLong(map.get("id_clube"))));
			dto.setPosicao(PosicaoAttributeConverter.getInstance().convertToEntityAttribute((String) map.get("posicao")));
			dto.setQtde(((BigInteger) map.get("total")).intValue());
			quantitativoPosicaoClubeList.add(dto);
		}

		return quantitativoPosicaoClubeList;
	}

}

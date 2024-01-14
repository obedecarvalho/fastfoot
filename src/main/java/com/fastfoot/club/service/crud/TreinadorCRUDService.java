package com.fastfoot.club.service.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Treinador;
import com.fastfoot.club.model.repository.TreinadorRepository;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.service.CRUDServiceJogavel;
import com.fastfoot.service.util.DatabaseUtil;

@Service
public class TreinadorCRUDService implements CRUDServiceJogavel<Treinador, Long> {
	
	@Autowired
	private TreinadorRepository treinadorRepository;

	@Override
	public List<Treinador> getAll() {
		return treinadorRepository.findAll();
	}
	
	@Override
	public List<Treinador> getByJogo(Jogo jogo) {
		return treinadorRepository.findByJogoAndAtivo(jogo, true);
	}

	@Override
	public Treinador getById(Long id) {
		Optional<Treinador> treinadorOpt = treinadorRepository.findById(id);
		if (treinadorOpt.isPresent()) {
			return treinadorOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		treinadorRepository.deleteById(id);		
	}

	@Override
	public void deleteAll() {
		treinadorRepository.deleteAll();
	}

	@Override
	public Treinador create(Treinador t) {
		return treinadorRepository.save(t);
	}

	@Override
	public Treinador update(Treinador t) {
		return treinadorRepository.save(t);
	}

	public List<Treinador> getTreinadoresSemClube(Jogo jogo){
		
		List<Map<String, Object>> result = treinadorRepository.findByClubeIsNull(jogo.getId(), true);
		
		List<Treinador> treinadoresDisponiveis = new ArrayList<Treinador>();
		
		Treinador treinador = null;
		
		for (Map<String, Object> map : result) {
			
			treinador = new Treinador();

			treinador.setId(DatabaseUtil.getValueLong(map.get("id")));
			treinador.setNome((String) map.get("nome"));
			Long idJogo = DatabaseUtil.getValueLong(map.get("id_jogo"));
			treinador.setJogo(new Jogo(idJogo));
			treinador.setAtivo((Boolean) map.get("ativo"));

			treinadoresDisponiveis.add(treinador);
		}
		
		return treinadoresDisponiveis;
	}
}

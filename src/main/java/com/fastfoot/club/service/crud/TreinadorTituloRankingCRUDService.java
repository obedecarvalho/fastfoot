package com.fastfoot.club.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.TreinadorTituloRanking;
import com.fastfoot.club.model.repository.TreinadorTituloRankingRepository;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.service.CRUDServiceJogavel;

@Service
public class TreinadorTituloRankingCRUDService implements CRUDServiceJogavel<TreinadorTituloRanking, Long> {

	@Autowired
	private TreinadorTituloRankingRepository treinadorTituloRankingRepository;

	@Override
	public List<TreinadorTituloRanking> getAll() {
		return treinadorTituloRankingRepository.findAll();
	}
	
	@Override
	public List<TreinadorTituloRanking> getByJogo(Jogo jogo) {
		return treinadorTituloRankingRepository.findByJogo(jogo);
	}

	@Override
	public TreinadorTituloRanking getById(Long id) {
		Optional<TreinadorTituloRanking> treinadorOpt = treinadorTituloRankingRepository.findById(id);
		if (treinadorOpt.isPresent()) {
			return treinadorOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		treinadorTituloRankingRepository.deleteById(id);		
	}

	@Override
	public void deleteAll() {
		treinadorTituloRankingRepository.deleteAll();
	}

	@Override
	public TreinadorTituloRanking create(TreinadorTituloRanking t) {
		return treinadorTituloRankingRepository.save(t);
	}

	@Override
	public TreinadorTituloRanking update(TreinadorTituloRanking t) {
		return treinadorTituloRankingRepository.save(t);
	}
}

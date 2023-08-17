package com.fastfoot.match.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.repository.EscalacaoClubeRepository;
import com.fastfoot.service.CRUDService;

@Service
public class EscalacaoClubeCRUDService implements CRUDService<EscalacaoClube, Long> {
	
	@Autowired
	private EscalacaoClubeRepository escalacaoClubeRepository;

	@Override
	public List<EscalacaoClube> getAll() {
		return escalacaoClubeRepository.findAll();
	}
	
	public List<EscalacaoClube> getByClube(Clube clube) {
		//return escalacaoClubePartidaRepository.findByClube(clube);
		return escalacaoClubeRepository.findByClubeAndAtivoFetchEscalacaoJogadorPosicao(clube, true);
	}

	@Override
	public EscalacaoClube getById(Long id) {
		Optional<EscalacaoClube> escalacaoClubePartidaOpt = escalacaoClubeRepository.findById(id);
		if (escalacaoClubePartidaOpt.isPresent()) {
			return escalacaoClubePartidaOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		escalacaoClubeRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		escalacaoClubeRepository.deleteAll();
	}

	@Override
	public EscalacaoClube create(EscalacaoClube t) {
		return escalacaoClubeRepository.save(t);
	}

	@Override
	public EscalacaoClube update(EscalacaoClube t) {
		return escalacaoClubeRepository.save(t);
	}
}

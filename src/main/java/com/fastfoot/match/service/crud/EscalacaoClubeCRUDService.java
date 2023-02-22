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
	private EscalacaoClubeRepository escalacaoClubePartidaRepository;

	@Override
	public List<EscalacaoClube> getAll() {
		return escalacaoClubePartidaRepository.findAll();
	}
	
	public List<EscalacaoClube> getByClube(Clube clube) {
		//return escalacaoClubePartidaRepository.findByClube(clube);
		return escalacaoClubePartidaRepository.findByClubeAndAtivoFetchEscalacaoJogadorPosicao(clube, true);
	}

	@Override
	public EscalacaoClube getById(Long id) {
		Optional<EscalacaoClube> escalacaoClubePartidaOpt = escalacaoClubePartidaRepository.findById(id);
		if (escalacaoClubePartidaOpt.isPresent()) {
			return escalacaoClubePartidaOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		escalacaoClubePartidaRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		escalacaoClubePartidaRepository.deleteAll();
	}

	@Override
	public EscalacaoClube create(EscalacaoClube t) {
		return escalacaoClubePartidaRepository.save(t);
	}

	@Override
	public EscalacaoClube update(EscalacaoClube t) {
		return escalacaoClubePartidaRepository.save(t);
	}
}

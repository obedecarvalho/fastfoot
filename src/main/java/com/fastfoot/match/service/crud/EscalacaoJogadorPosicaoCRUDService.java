package com.fastfoot.match.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.match.model.repository.EscalacaoJogadorPosicaoRepository;
import com.fastfoot.service.CRUDService;

@Service
public class EscalacaoJogadorPosicaoCRUDService implements CRUDService<EscalacaoJogadorPosicao, Long> {
	
	@Autowired
	private EscalacaoJogadorPosicaoRepository escalacaoClubePartidaRepository;

	@Override
	public List<EscalacaoJogadorPosicao> getAll() {
		return escalacaoClubePartidaRepository.findAll();
	}
	
	public List<EscalacaoJogadorPosicao> getByClube(Clube clube) {
		return escalacaoClubePartidaRepository.findByClubeAndAtivo(clube, true);
	}

	@Override
	public EscalacaoJogadorPosicao getById(Long id) {
		Optional<EscalacaoJogadorPosicao> escalacaoClubePartidaOpt = escalacaoClubePartidaRepository.findById(id);
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
	public EscalacaoJogadorPosicao create(EscalacaoJogadorPosicao t) {
		return escalacaoClubePartidaRepository.save(t);
	}

	@Override
	public EscalacaoJogadorPosicao update(EscalacaoJogadorPosicao t) {
		return escalacaoClubePartidaRepository.save(t);
	}

}

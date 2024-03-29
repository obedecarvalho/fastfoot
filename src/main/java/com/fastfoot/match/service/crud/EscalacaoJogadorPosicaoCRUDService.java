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
	private EscalacaoJogadorPosicaoRepository escalacaoJogadorPosicaoRepository;

	@Override
	public List<EscalacaoJogadorPosicao> getAll() {
		return escalacaoJogadorPosicaoRepository.findAll();
	}
	
	public List<EscalacaoJogadorPosicao> getByClube(Clube clube) {
		return escalacaoJogadorPosicaoRepository.findByClubeAndAtivo(clube, true);
	}

	@Override
	public EscalacaoJogadorPosicao getById(Long id) {
		Optional<EscalacaoJogadorPosicao> escalacaoClubePartidaOpt = escalacaoJogadorPosicaoRepository.findById(id);
		if (escalacaoClubePartidaOpt.isPresent()) {
			return escalacaoClubePartidaOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		escalacaoJogadorPosicaoRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		escalacaoJogadorPosicaoRepository.deleteAll();
	}

	@Override
	public EscalacaoJogadorPosicao create(EscalacaoJogadorPosicao t) {
		return escalacaoJogadorPosicaoRepository.save(t);
	}

	@Override
	public EscalacaoJogadorPosicao update(EscalacaoJogadorPosicao t) {
		return escalacaoJogadorPosicaoRepository.save(t);
	}

}

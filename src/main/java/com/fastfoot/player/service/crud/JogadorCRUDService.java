package com.fastfoot.player.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.service.CRUDService;

@Service
public class JogadorCRUDService implements CRUDService<Jogador, Long> {
	
	@Autowired
	private JogadorRepository jogadorRepository;

	@Override
	public List<Jogador> getAll() {
		return jogadorRepository.findAll();
	}

	@Override
	public Jogador getById(Long id) {
		Optional<Jogador> jogadorOpt = jogadorRepository.findById(id);
		if (jogadorOpt.isPresent()) {
			return jogadorOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		jogadorRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		jogadorRepository.deleteAll();
	}

	@Override
	public Jogador create(Jogador t) {
		return jogadorRepository.save(t);
	}

	@Override
	public Jogador update(Jogador t) {
		return jogadorRepository.save(t);
	}

	public List<Jogador> getAtivosByClube(Clube clube){
		return jogadorRepository.findByClubeAndStatusJogador(clube, StatusJogador.ATIVO);
	}
	
	public List<Jogador> getAllAtivos(){
		return jogadorRepository.findByStatusJogador(StatusJogador.ATIVO);
	}
}

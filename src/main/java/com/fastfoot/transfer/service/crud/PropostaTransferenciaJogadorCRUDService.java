package com.fastfoot.transfer.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.service.CRUDService;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;
import com.fastfoot.transfer.model.repository.PropostaTransferenciaJogadorRepository;

@Service
public class PropostaTransferenciaJogadorCRUDService implements CRUDService<PropostaTransferenciaJogador, Long> {
	
	@Autowired
	private PropostaTransferenciaJogadorRepository propostaTransferenciaJogadorRepository;

	@Override
	public List<PropostaTransferenciaJogador> getAll() {
		return propostaTransferenciaJogadorRepository.findAll();
	}

	@Override
	public PropostaTransferenciaJogador getById(Long id) {
		Optional<PropostaTransferenciaJogador> propostaTransferenciaJogadorOpt = propostaTransferenciaJogadorRepository.findById(id);
		
		if (propostaTransferenciaJogadorOpt.isPresent()) {
			return propostaTransferenciaJogadorOpt.get();
		}
		
		return null;
	}

	@Override
	public void delete(Long id) {
		propostaTransferenciaJogadorRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		propostaTransferenciaJogadorRepository.deleteAll();
	}

	@Override
	public PropostaTransferenciaJogador create(PropostaTransferenciaJogador t) {
		return propostaTransferenciaJogadorRepository.save(t);
	}

	@Override
	public PropostaTransferenciaJogador update(PropostaTransferenciaJogador t) {
		return propostaTransferenciaJogadorRepository.save(t);
	}
	
	public List<PropostaTransferenciaJogador> getByPropostaAceita(boolean propostaAceita){
		return propostaTransferenciaJogadorRepository.findByPropostaAceita(propostaAceita);
	}

}

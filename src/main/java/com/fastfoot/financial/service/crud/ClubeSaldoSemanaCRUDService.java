package com.fastfoot.financial.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.financial.model.entity.ClubeSaldoSemana;
import com.fastfoot.financial.model.repository.ClubeSaldoSemanaRepository;
import com.fastfoot.service.CRUDService;

@Service
public class ClubeSaldoSemanaCRUDService implements CRUDService<ClubeSaldoSemana, Long> {

	@Autowired
	private ClubeSaldoSemanaRepository clubeSaldoSemanaRepository;

	@Override
	public List<ClubeSaldoSemana> getAll() {
		return clubeSaldoSemanaRepository.findAll();
	}

	@Override
	public ClubeSaldoSemana getById(Long id) {
		Optional<ClubeSaldoSemana> clubeOpt = clubeSaldoSemanaRepository.findById(id);
		if (clubeOpt.isPresent()) {
			return clubeOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		clubeSaldoSemanaRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		clubeSaldoSemanaRepository.deleteAll();
	}

	@Override
	public ClubeSaldoSemana create(ClubeSaldoSemana t) {
		return clubeSaldoSemanaRepository.save(t);
	}

	@Override
	public ClubeSaldoSemana update(ClubeSaldoSemana t) {
		return clubeSaldoSemanaRepository.save(t);
	}
}

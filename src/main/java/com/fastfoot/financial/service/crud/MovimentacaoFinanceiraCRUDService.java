package com.fastfoot.financial.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.service.CRUDService;

@Service
public class MovimentacaoFinanceiraCRUDService implements CRUDService<MovimentacaoFinanceira, Long> {

	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

	@Override
	public List<MovimentacaoFinanceira> getAll() {
		return movimentacaoFinanceiraRepository.findAll();
	}

	@Override
	public MovimentacaoFinanceira getById(Long id) {
		Optional<MovimentacaoFinanceira> movFinanceiraOpt = movimentacaoFinanceiraRepository.findById(id);
		if (movFinanceiraOpt.isPresent()) {
			return movFinanceiraOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		movimentacaoFinanceiraRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		movimentacaoFinanceiraRepository.deleteAll();
	}

	@Override
	public MovimentacaoFinanceira create(MovimentacaoFinanceira t) {
		return movimentacaoFinanceiraRepository.save(t);
	}

	@Override
	public MovimentacaoFinanceira update(MovimentacaoFinanceira t) {
		return movimentacaoFinanceiraRepository.save(t);
	}
}

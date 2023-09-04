package com.fastfoot.service;

import java.util.List;

import com.fastfoot.model.entity.Jogo;

public interface CRUDServiceJogavel<T, S> extends CRUDService<T, S> {

	public List<T> getByJogo(Jogo jogo);

}

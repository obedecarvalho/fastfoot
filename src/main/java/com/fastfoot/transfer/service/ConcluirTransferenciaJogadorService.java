package com.fastfoot.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.service.SemanaService;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;
import com.fastfoot.transfer.model.repository.PropostaTransferenciaJogadorRepository;

@Service
public class ConcluirTransferenciaJogadorService {
	
	//###	REPOSITORY	###
	
	@Autowired
	private PropostaTransferenciaJogadorRepository propostaTransferenciaJogadorRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	//###	SERVICE	###

	@Autowired
	private SemanaService semanaService;

	//É esperado que validações já tenham sido feitas: Elenco dos clubes, disponibilidade financeira, janela de transferencias
	public void concluirTransferenciaJogador(PropostaTransferenciaJogador propostaTransferenciaJogador) {
		
		Semana s = semanaService.getSemanaAtual();
		
		propostaTransferenciaJogador.setPropostaAceita(true);
		propostaTransferenciaJogador.setSemanaTransferencia(s);
		
		propostaTransferenciaJogador.getJogador().setClube(propostaTransferenciaJogador.getClubeDestino());

		propostaTransferenciaJogadorRepository.save(propostaTransferenciaJogador);
		jogadorRepository.save(propostaTransferenciaJogador.getJogador());
		
		//System.err.println("\t\tAQUI");

		//TODO: gerar entradas e saidas financeiras
		//TODO: criar novo JogadorEstatisticasTemporada com novo clube
	}
}

package com.fastfoot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.scheduler.model.dto.CampeonatoDTO;
import com.fastfoot.scheduler.model.dto.SemanaDTO;
import com.fastfoot.scheduler.model.dto.TemporadaDTO;
import com.fastfoot.scheduler.service.CriarCalendarioTemporadaService;
import com.fastfoot.scheduler.service.JogarProximaSemanaService;
import com.fastfoot.scheduler.service.TemporadaService;
import com.fastfoot.service.util.ValidatorUtil;
import com.fastfoot.transfer.service.GerenciarTemporadaService;

@RestController
@CrossOrigin("*")
public class TemporadaController {

	@Autowired
	private TemporadaService temporadaService;

	@Autowired
	private JogarProximaSemanaService jogarProximaSemanaService;
	
	@Autowired
	private CriarCalendarioTemporadaService criarCalendarioTemporadaService;
	
	@Autowired
	private GerenciarTemporadaService gerenciarTemporadaService;

	@GetMapping("/novaTemporada")
	public ResponseEntity<TemporadaDTO> criarTemporada() {
		try {
			return ResponseEntity.ok(criarCalendarioTemporadaService.criarTemporada());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@GetMapping("/gerarTransferencias")
	public ResponseEntity<Boolean> gerarTransferencias() {
		try {
			gerenciarTemporadaService.gerarTransferencias();
			return ResponseEntity.ok(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@GetMapping("/calcularClubeSaldoSemana")
	public ResponseEntity<Boolean> calcularClubeSaldoSemana() {
		try {
			gerenciarTemporadaService.calcularClubeSaldoSemana();
			return ResponseEntity.ok(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@GetMapping("/gerarMudancaClubeNivel")
	public ResponseEntity<Boolean> gerarMudancaClubeNivel() {
		try {
			gerenciarTemporadaService.gerarMudancaClubeNivel();
			return ResponseEntity.ok(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@GetMapping("/calcularTrajetoriaForcaClube")
	public ResponseEntity<Boolean> calcularTrajetoriaForcaClube() {
		try {
			gerenciarTemporadaService.calcularTrajetoriaForcaClube();
			return ResponseEntity.ok(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	/*@GetMapping("/gerarTransferencias2")
	@Deprecated
	public ResponseEntity<Boolean> gerarTransferencias2() {
		try {
			gerenciarTransferenciasTemporadaService.gerarTransferencias2();
			return ResponseEntity.ok(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}*/

	/*@GetMapping("/classificarClubesTemporadaAtual")
	public ResponseEntity<Boolean> classificarClubesTemporadaAtual() {
		try {
			temporadaService.classificarClubesTemporadaAtual();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}*/

	@GetMapping("/proximaSemana")
	public ResponseEntity<SemanaDTO> proximaSemana() {//TODO: renomear 'jogarProximaSemana'
		try {
			return ResponseEntity.ok(jogarProximaSemanaService.jogarProximaSemana());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/*@GetMapping("/jogarTemporadaCompleta")
	public ResponseEntity<SemanaDTO> jogarTemporadaCompleta() {
		try {
			SemanaDTO semana = null;

			do {
				semana = semanaService.proximaSemana();
			} while (semana.getNumero() < 25);

			return ResponseEntity.ok(semana);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}*/

	@GetMapping("/campeonatosTemporadaAtual")
	public ResponseEntity<List<CampeonatoDTO>> getCampeonatosTemporada(@RequestParam(name = "nivel") String nivel) {//'NACIONAL', 'COPA NACIONAL', 'CONTINENTAL'
		try {
			return ResponseEntity.ok(temporadaService.getCampeonatosTemporada(nivel));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/temporadas")
	public ResponseEntity<List<TemporadaDTO>> getTemporadas(){//TODO: renomear get***Itens??? //Olhar outros metodos como esse para padronizar nomes
		try {
			return ResponseEntity.ok(temporadaService.getTemporadas());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/anoTemporadaItens")
	public ResponseEntity<List<Integer>> getAnoTemporadaItens(){
		List<Integer> anos = temporadaService.getAnosTemporadas();
		if (ValidatorUtil.isEmpty(anos)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(anos);
	}
}

package com.fastfoot.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.bets.model.TipoProbabilidadeResultadoPartida;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.model.entity.Parametro;
import com.fastfoot.model.repository.ParametroRepository;
import com.fastfoot.probability.model.TipoCampeonatoClubeProbabilidade;
import com.fastfoot.scheduler.model.ClassificacaoNacional;
import com.fastfoot.scheduler.model.NivelCampeonato;

@Service
public class CarregarParametroService {

	@Autowired
	private ParametroRepository parametroRepository;

	private static final Map<String, Parametro> PARAMETRO_CACHE = new HashMap<String, Parametro>();
	
	public Parametro getParametro(String nome) {
		return getParametro(nome, false);
	}
	
	public Parametro getParametro(String nome, boolean force) {
		
		Parametro parametro = PARAMETRO_CACHE.get(nome);
		
		if (parametro == null || force) {		
			Optional<Parametro> parametroOpt = parametroRepository.findFirstByNome(nome);
			parametro = parametroOpt.get();
			PARAMETRO_CACHE.put(nome, parametro);
		}

		return parametro;
	}
	
	public Boolean getParametroBoolean(String nome) {
		Parametro parametro = getParametro(nome);		
		return Boolean.valueOf(parametro.getValor());
	}
	
	public Integer getParametroInteger(String nome) {
		Parametro parametro = getParametro(nome);		
		return Integer.valueOf(parametro.getValor());
	}

	public String getParametroString(String nome) {
		Parametro parametro = getParametro(nome);		
		return parametro.getValor();
	}

	public void reset() {
		PARAMETRO_CACHE.clear();
	}
	
	//######### PARAMETROS ESPECIFICOS
	
	public Integer getNumeroRodadasCopaNacional() {
		String numeroRodadas = getParametroString(ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL);
		
		if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_4R.equals(numeroRodadas)) {
			return 4;
		} else if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_5R.equals(numeroRodadas)) {
			return 5;
		} else if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R.equals(numeroRodadas)) {
			return 6;
		}
		
		return -1;
	}

	public TipoProbabilidadeResultadoPartida getTipoProbabilidadeResultadoPartida() {
		String metodoCalculo = getParametroString(ParametroConstantes.METODO_CALCULO_PROBABILIDADE);
		
		if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_POISSON_PARAM.equals(metodoCalculo)) {
			return TipoProbabilidadeResultadoPartida.ESTATISTICAS_FINALIZACAO_POISSON;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_PARAM.equals(metodoCalculo)) {
			return TipoProbabilidadeResultadoPartida.ESTATISTICAS_FINALIZACAO;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_DEFESA_PARAM.equals(metodoCalculo)) {
			return TipoProbabilidadeResultadoPartida.ESTATISTICAS_FINALIZACAO_DEFESA;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_SIMULAR_PARTIDA_PARAM.equals(metodoCalculo)) {
			return TipoProbabilidadeResultadoPartida.SIMULAR_PARTIDA;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_SIMULAR_PARTIDA_HABILIDADE_GRUPO_PARAM.equals(metodoCalculo)) {
			return TipoProbabilidadeResultadoPartida.SIMULAR_PARTIDA_HABILIDADE_GRUPO;
		}

		return TipoProbabilidadeResultadoPartida.ZERO;
	}

	public TipoCampeonatoClubeProbabilidade getTipoCampeonatoClubeProbabilidade() {
		String metodoCalculo = getParametroString(ParametroConstantes.METODO_CALCULO_PROBABILIDADE);
		
		if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_POISSON_PARAM.equals(metodoCalculo)) {
			return TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO_POISSON;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_PARAM.equals(metodoCalculo)) {
			return TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_DEFESA_PARAM.equals(metodoCalculo)) {
			return TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO_DEFESA;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_SIMULAR_PARTIDA_PARAM.equals(metodoCalculo)) {
			return TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_SIMULAR_PARTIDA_HABILIDADE_GRUPO_PARAM.equals(metodoCalculo)) {
			return TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_HABILIDADE_GRUPO;
		}

		return TipoCampeonatoClubeProbabilidade.ZERO;
	}

	public Integer getNumeroTimesParticipantesCopaNacional() {
		Integer numRodadas = getNumeroRodadasCopaNacional();
		Integer nroCompeticoesContinentais = getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		Boolean jogarCNCompleta = getParametroBoolean(ParametroConstantes.JOGAR_COPA_NACIONAL_COMPLETA);
		Boolean cIIIReduzido = getParametroBoolean(ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO);
		
		if (numRodadas == 4) {
			return 16;
		} else if (numRodadas == 5) {
			if (nroCompeticoesContinentais == 3) {
				if (cIIIReduzido) {
					return 22;
				} else {
					return 20;
				}
			} else if (nroCompeticoesContinentais == 2) {
				return 24;
			}
		} else if (numRodadas == 6) {
			if (nroCompeticoesContinentais == 3) {
				if (cIIIReduzido) {
					if (jogarCNCompleta) {
						return 32;
					} else {
						return 30;
					}
				} else {
					return 28;
				}
			} else if (nroCompeticoesContinentais == 2) {
				return 32;
			}
		}

		return -1;
	}

	public boolean isEstrategiaPromotorContinentalSegundoMelhorGrupo() {
		String estrategiaPromotorCont = getParametroString(ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL);
		return ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_SEG.equals(estrategiaPromotorCont);
	}
	
	public boolean isEstrategiaPromotorContinentalMelhorEliminado() {
		String estrategiaPromotorCont = getParametroString(ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL);
		return ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_ELI.equals(estrategiaPromotorCont);
	}
	
	public ClassificacaoNacional[] getClassificacaoNacionalNovoCampeonato(NivelCampeonato nivelCampeonato) {
		
		ClassificacaoNacional[] classificacao = new ClassificacaoNacional[Constantes.NRO_CLUBE_CAMP_NACIONAL];
		
		Integer nroClubesRebaixados = getParametroInteger(ParametroConstantes.NUMERO_CLUBES_REBAIXADOS);
		
		if (nivelCampeonato.isNacional()) {
			System.arraycopy(ClassificacaoNacional.NACIONAL, 0, classificacao, 0,
					Constantes.NRO_CLUBE_CAMP_NACIONAL - nroClubesRebaixados);
			System.arraycopy(ClassificacaoNacional.NACIONAL_II, 0, classificacao,
					Constantes.NRO_CLUBE_CAMP_NACIONAL - nroClubesRebaixados, nroClubesRebaixados);
		}
		
		if (nivelCampeonato.isNacionalII()) {
			System.arraycopy(ClassificacaoNacional.NACIONAL, Constantes.NRO_CLUBE_CAMP_NACIONAL - nroClubesRebaixados,
					classificacao, 0, nroClubesRebaixados);
			System.arraycopy(ClassificacaoNacional.NACIONAL_II, nroClubesRebaixados, classificacao, nroClubesRebaixados,
					Constantes.NRO_CLUBE_CAMP_NACIONAL - nroClubesRebaixados);
		}
		
		return classificacao;
	}
	
	public boolean isMarcarAmistososAutomaticamenteSemanaASemana() {
		String opcao = getParametroString(ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE);
		return ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_SEMANA_A_SEMANA_PARAM.equals(opcao);
	}
	
	public boolean isMarcarAmistososAutomaticamenteInicioTemporadaESemanaASemana() {
		String opcao = getParametroString(ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE);
		return ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_INICIO_TEMPORADA_E_SEMANA_A_SEMANA_PARAM.equals(opcao);
	}
	
	public boolean isMarcarAmistososAutomaticamenteInicioTemporada() {
		String opcao = getParametroString(ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE);
		return ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_INICIO_TEMPORADA_PARAM.equals(opcao);
	}
}

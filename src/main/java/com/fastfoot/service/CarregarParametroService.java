package com.fastfoot.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.bets.model.TipoProbabilidadeResultadoPartida;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.Parametro;
import com.fastfoot.model.repository.ParametroRepository;
import com.fastfoot.probability.model.TipoCampeonatoClubeProbabilidade;
import com.fastfoot.scheduler.model.ClassificacaoNacional;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class CarregarParametroService {

	@Autowired
	private ParametroRepository parametroRepository;

	private static final Map<Jogo, Map<String, Parametro>> PARAMETRO_CACHE_JOGO = new HashMap<Jogo, Map<String, Parametro>>();
	
	public Parametro getParametro(Jogo jogo, String nome) {
		return getParametro(jogo, nome, false);
	}
	
	public Parametro getParametro(Jogo jogo, String nome, boolean force) {
		
		Map<String, Parametro> cache = PARAMETRO_CACHE_JOGO.get(jogo);
		
		if (ValidatorUtil.isEmpty(cache)) {
			
			cache = new HashMap<String, Parametro>();
			
			PARAMETRO_CACHE_JOGO.put(jogo, cache);
		}
		
		Parametro parametro = cache.get(nome);
		
		if (parametro == null || force) {		
			Optional<Parametro> parametroOpt = parametroRepository.findFirstByNomeAndJogo(nome, jogo);
			parametro = parametroOpt.get();
			cache.put(nome, parametro);
		}

		return parametro;
	}
	
	public Boolean getParametroBoolean(Jogo jogo, String nome) {
		Parametro parametro = getParametro(jogo, nome);		
		return Boolean.valueOf(parametro.getValor());
	}
	
	public Integer getParametroInteger(Jogo jogo, String nome) {
		Parametro parametro = getParametro(jogo, nome);		
		return Integer.valueOf(parametro.getValor());
	}

	public String getParametroString(Jogo jogo, String nome) {
		Parametro parametro = getParametro(jogo, nome);		
		return parametro.getValor();
	}

	public void reset(Jogo jogo) {
		PARAMETRO_CACHE_JOGO.put(jogo, new HashMap<String, Parametro>());
	}
	
	//######### PARAMETROS ESPECIFICOS
	
	public Integer getNumeroRodadasCopaNacional(Jogo jogo) {
		String numeroRodadas = getParametroString(jogo, ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL);
		
		if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_4R.equals(numeroRodadas)) {
			return 4;
		} else if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_5R.equals(numeroRodadas)) {
			return 5;
		} else if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R.equals(numeroRodadas)) {
			return 6;
		}
		
		return -1;
	}

	public TipoProbabilidadeResultadoPartida getTipoProbabilidadeResultadoPartida(Jogo jogo) {
		String metodoCalculo = getParametroString(jogo, ParametroConstantes.METODO_CALCULO_PROBABILIDADE);
		
		if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_POISSON_PARAM.equals(metodoCalculo)) {
			return TipoProbabilidadeResultadoPartida.ESTATISTICAS_FINALIZACAO_POISSON;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_PARAM.equals(metodoCalculo)) {
			return TipoProbabilidadeResultadoPartida.ESTATISTICAS_FINALIZACAO;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_DEFESA_PARAM.equals(metodoCalculo)) {
			return TipoProbabilidadeResultadoPartida.ESTATISTICAS_FINALIZACAO_DEFESA;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_SIMULAR_PARTIDA_HABILIDADE_PARAM.equals(metodoCalculo)) {
			return TipoProbabilidadeResultadoPartida.SIMULAR_PARTIDA;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_SIMULAR_PARTIDA_HABILIDADE_GRUPO_PARAM.equals(metodoCalculo)) {
			return TipoProbabilidadeResultadoPartida.SIMULAR_PARTIDA_HABILIDADE_GRUPO;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_FORCA_GERAL_PARAM.equals(metodoCalculo)) {
			return TipoProbabilidadeResultadoPartida.FORCA_GERAL;
		}

		return TipoProbabilidadeResultadoPartida.ZERO;
	}

	public TipoCampeonatoClubeProbabilidade getTipoCampeonatoClubeProbabilidade(Jogo jogo) {
		String metodoCalculo = getParametroString(jogo, ParametroConstantes.METODO_CALCULO_PROBABILIDADE);
		
		if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_POISSON_PARAM.equals(metodoCalculo)) {
			return TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO_POISSON;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_PARAM.equals(metodoCalculo)) {
			return TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_DEFESA_PARAM.equals(metodoCalculo)) {
			return TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO_DEFESA;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_SIMULAR_PARTIDA_HABILIDADE_PARAM.equals(metodoCalculo)) {
			return TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_SIMULAR_PARTIDA_HABILIDADE_GRUPO_PARAM.equals(metodoCalculo)) {
			return TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_HABILIDADE_GRUPO;
		} else if (ParametroConstantes.METODO_CALCULO_PROBABILIDADE_FORCA_GERAL_PARAM.equals(metodoCalculo)) {
			return TipoCampeonatoClubeProbabilidade.FORCA_GERAL;
		}

		return TipoCampeonatoClubeProbabilidade.ZERO;
	}

	public Integer getNumeroTimesParticipantesCopaNacional(Jogo jogo) {
		Integer numRodadas = getNumeroRodadasCopaNacional(jogo);
		Integer nroCompeticoesContinentais = getParametroInteger(jogo, ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		Boolean jogarCNCompleta = getParametroBoolean(jogo, ParametroConstantes.JOGAR_COPA_NACIONAL_COMPLETA_32_TIMES);
		Boolean cIIIReduzido = getParametroBoolean(jogo, ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO);
		
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
	
	//	ESTRATEGIA_PROMOTOR_CONTINENTAL

	public boolean isEstrategiaPromotorContinentalSegundoMelhorGrupo(Jogo jogo) {
		String estrategiaPromotorCont = getParametroString(jogo, ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL);
		return ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_SEG.equals(estrategiaPromotorCont);
	}
	
	public boolean isEstrategiaPromotorContinentalMelhorEliminado(Jogo jogo) {
		String estrategiaPromotorCont = getParametroString(jogo, ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL);
		return ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_ELI.equals(estrategiaPromotorCont);
	}
	
	//	/ESTRATEGIA_PROMOTOR_CONTINENTAL
	
	public ClassificacaoNacional[] getClassificacaoNacionalNovoCampeonato(Jogo jogo, NivelCampeonato nivelCampeonato) {
		
		ClassificacaoNacional[] classificacao = new ClassificacaoNacional[Constantes.NRO_CLUBE_CAMP_NACIONAL];
		
		Integer nroClubesRebaixados = getParametroInteger(jogo, ParametroConstantes.NUMERO_CLUBES_REBAIXADOS);
		
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
	
	public List<ClassificacaoNacional> getClassificacaoNacionalRebaixados(Jogo jogo) {

		Integer nroClubesRebaixados = getParametroInteger(jogo, ParametroConstantes.NUMERO_CLUBES_REBAIXADOS);

		ClassificacaoNacional[] classificacao = new ClassificacaoNacional[nroClubesRebaixados];

		System.arraycopy(ClassificacaoNacional.NACIONAL, Constantes.NRO_CLUBE_CAMP_NACIONAL - nroClubesRebaixados,
				classificacao, 0, nroClubesRebaixados);

		return Arrays.asList(classificacao);
	}

	public List<ClassificacaoNacional> getClassificacaoNacionalPromovidos(Jogo jogo) {

		Integer nroClubesRebaixados = getParametroInteger(jogo, ParametroConstantes.NUMERO_CLUBES_REBAIXADOS);

		ClassificacaoNacional[] classificacao = new ClassificacaoNacional[nroClubesRebaixados];

		System.arraycopy(ClassificacaoNacional.NACIONAL_II, 0, classificacao, 0, nroClubesRebaixados);

		return Arrays.asList(classificacao);
	}

	//	MARCAR_AMISTOSOS_AUTOMATICAMENTE

	public boolean isMarcarAmistososAutomaticamenteSemanaASemana(Jogo jogo) {
		String opcao = getParametroString(jogo, ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE);
		return ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_SEMANA_A_SEMANA_PARAM.equals(opcao);
	}
	
	public boolean isMarcarAmistososAutomaticamenteInicioTemporadaESemanaASemana(Jogo jogo) {
		String opcao = getParametroString(jogo, ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE);
		return ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_INICIO_TEMPORADA_E_SEMANA_A_SEMANA_PARAM.equals(opcao);
	}
	
	public boolean isMarcarAmistososAutomaticamenteInicioTemporada(Jogo jogo) {
		String opcao = getParametroString(jogo, ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE);
		return ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_INICIO_TEMPORADA_PARAM.equals(opcao);
	}
	
	//	/MARCAR_AMISTOSOS_AUTOMATICAMENTE

	public Integer getPrimeiraPosicaoEliminadoContinental(Jogo jogo, NivelCampeonato nivelCampeonato) {

		if (isEstrategiaPromotorContinentalSegundoMelhorGrupo(jogo)) {
			return 3;
		}

		if (isEstrategiaPromotorContinentalMelhorEliminado(jogo)) {

			if (NivelCampeonato.CONTINENTAL.equals(nivelCampeonato)) {
				return 3;
			}

			if (NivelCampeonato.CONTINENTAL_II.equals(nivelCampeonato)) {
				return 2;
			}

			//Integer nroCompeticoesContinentais = getParametroInteger(jogo, ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
			Boolean cIIIReduzido = getParametroBoolean(jogo, ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO);

			if (NivelCampeonato.CONTINENTAL_III.equals(nivelCampeonato)) {
				if (cIIIReduzido) {
					return 3;
				} else {
					return 2;
				}
			}
		}

		return null;
	}
}

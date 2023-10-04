#!/bin/bash
#--remover jogadores
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "
delete from habilidade_valor_estatistica_grupo;
delete from habilidade_valor_estatistica;
delete from habilidade_grupo_valor_estatistica;
delete from habilidade_grupo_valor;
delete from disponivel_negociacao;
delete from proposta_transferencia_jogador;
delete from necessidade_contratacao_clube;
delete from jogador_estatisticas_semana;
delete from jogador_estatisticas_temporada;
delete from habilidade_valor;
delete from escalacao_jogador_posicao;
delete from escalacao_clube;
delete from contrato;
delete from jogador_energia;
delete from jogador;
delete from partida_lance;
delete from campeonato_clube_probabilidade;
delete from partida_amistosa_resultado;
delete from partida_resultado;
delete from partida_eliminatoria_resultado;
delete from partida_probabilidade_resultado;
delete from partida_torcida;
delete from partida_estatisticas;
delete from rodada_amistosa;
delete from rodada;
delete from rodada_eliminatoria;
delete from classificacao;
delete from grupo_campeonato;
delete from campeonato;
delete from campeonato_eliminatorio;
delete from campeonato_misto;
delete from clube_titulo_ranking;
delete from clube_resumo_temporada;
delete from clube_ranking;
delete from movimentacao_financeira;
delete from demonstrativo_financeiro_temporada;
delete from trajetoria_forca_clube;
delete from clube_saldo_semana;
delete from clube;
delete from treinador_resumo_temporada;
delete from treinador_titulo_ranking;
delete from mudanca_treinador;
delete from treinador;
delete from semana;
delete from mudanca_clube_nivel;
delete from temporada;
delete from parametro;
delete from liga_jogo;
delete from jogo;
";

#select setval(sequence_schema || '.' || sequence_name, 1000)
#from information_schema.sequences
#where sequence_schema = 'public';

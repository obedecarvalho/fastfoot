#!/bin/bash

psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "
truncate table jogo cascade;
delete from partida_estatisticas;
delete from partida_probabilidade_resultado;
delete from partida_torcida;
";

#psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "
#truncate table habilidade_valor_estatistica_grupo;
#truncate table habilidade_valor_estatistica;
#truncate table habilidade_grupo_valor_estatistica;
#truncate table habilidade_grupo_valor cascade;
#delete from disponivel_negociacao;
#delete from proposta_transferencia_jogador;
#delete from necessidade_contratacao_clube;
#truncate table jogador_estatisticas_semana;
#truncate table jogador_estatisticas_temporada;
#truncate table habilidade_valor cascade;
#delete from escalacao_jogador_posicao;
#delete from escalacao_clube;
#delete from contrato;
#truncate table jogador_energia;
#delete from jogador;
#delete from partida_lance;
#delete from campeonato_clube_probabilidade;
#delete from partida_amistosa_resultado;
#delete from partida_resultado;
#delete from partida_eliminatoria_resultado;
#delete from partida_probabilidade_resultado;
#delete from partida_torcida;
#delete from partida_estatisticas;
#delete from rodada_amistosa;
#delete from rodada;
#delete from rodada_eliminatoria;
#delete from classificacao;
#delete from grupo_campeonato;
#delete from campeonato;
#delete from campeonato_eliminatorio;
#delete from campeonato_misto;
#delete from clube_titulo_ranking;
#delete from clube_resumo_temporada;
#delete from clube_ranking;
#truncate table movimentacao_financeira;
#delete from demonstrativo_financeiro_temporada;
#delete from mudanca_treinador;
#delete from mudanca_clube_nivel;
#delete from treinador_resumo_temporada;
#delete from trajetoria_forca_clube;
#delete from clube_saldo_semana;
#delete from clube;
#delete from treinador_titulo_ranking;
#delete from treinador;
#delete from semana;
#delete from temporada;
#delete from parametro;
#delete from liga_jogo;
#delete from jogo;
#";

#select setval(sequence_schema || '.' || sequence_name, 1000)
#from information_schema.sequences
#where sequence_schema = 'public';

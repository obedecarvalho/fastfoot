#!/bin/bash

#--sem remover jogadores e clubes
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "
delete from jogador_estatistica_semana;
delete from jogador_estatisticas_temporada;
truncate table habilidade_valor_estatistica_grupo;
truncate table habilidade_valor_estatistica;
delete from disponivel_negociacao;
delete from proposta_transferencia_jogador;
delete from necessidade_contratacao_clube;
delete from campeonato_clube_probabilidade;
delete from partida_probabilidade_resultado;
delete from partida_torcida;
delete from partida_amistosa_resultado;
delete from rodada_amistosa;
delete from partida_resultado;
delete from partida_eliminatoria_resultado;
delete from partida_estatisticas;
delete from rodada;
delete from rodada_eliminatoria;
delete from classificacao;
delete from grupo_campeonato;
delete from campeonato;
delete from campeonato_eliminatorio;
delete from campeonato_misto;
delete from clube_titulo_ranking;
delete from clube_resumo_temporada;
delete from clube_ranking where ano <> 2022;
delete from movimentacao_financeira where tipo_movimentacao <> 12;
delete from demonstrativo_financeiro_temporada;
delete from trajetoria_forca_clube;
delete from semana;
delete from mudanca_clube_nivel;
delete from temporada;
";

#!/bin/bash

#--sem remover jogadores e clubes
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "
truncate table habilidade_valor_estatistica;
delete from clube_probabilidade;
delete from partida_amistosa_resultado;
delete from rodada_amistosa;
delete from partida_resultado;
delete from partida_eliminatoria_resultado;
delete from rodada;
delete from rodada_eliminatoria;
delete from classificacao;
delete from grupo_campeonato;
delete from campeonato;
delete from campeonato_eliminatorio;
delete from campeonato_misto;
delete from clube_titulo_ranking;
delete from clube_ranking where ano <> 2021;
delete from semana;
delete from temporada;
";

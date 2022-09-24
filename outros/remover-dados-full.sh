#!/bin/bash
#--remover jogadores
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "
truncate table habilidade_valor_estatistica_grupo;
truncate table habilidade_valor_estatistica;
delete from disponivel_negociacao;
delete from proposta_transferencia_jogador;
delete from necessidade_contratacao_clube;
update jogador set id_jogador_estatisticas_temporada_atual = null;
update jogador set id_jogador_estatisticas_amistosos_temporada_atual = null;
delete from jogador_estatisticas_temporada;
delete from habilidade_valor;
delete from escalacao_jogador_posicao ;
delete from grupo_desenvolvimento_jogador ;
delete from jogador;
delete from clube_probabilidade;
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
delete from clube_ranking where ano <> 2021;
delete from movimentacao_financeira_entrada;
delete from movimentacao_financeira_saida;
delete from semana;
delete from temporada;
";


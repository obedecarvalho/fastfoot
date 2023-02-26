#!/bin/bash
#--remover jogadores
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "
truncate table habilidade_valor_estatistica_grupo;
truncate table habilidade_valor_estatistica;
delete from habilidade_grupo_valor;
delete from disponivel_negociacao;
delete from proposta_transferencia_jogador;
delete from necessidade_contratacao_clube;
delete from jogador_estatisticas_temporada;
delete from habilidade_valor;
delete from escalacao_jogador_posicao;
delete from escalacao_clube;
delete from grupo_desenvolvimento_jogador;
delete from jogador;
delete from jogador_detalhe;
";


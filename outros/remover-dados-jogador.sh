#!/bin/bash
#--remover jogadores
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "
truncate table habilidade_valor_estatistica_grupo;
truncate table habilidade_valor_estatistica;
update jogador set id_jogador_estatisticas_temporada_atual = null;
delete from jogador_estatisticas_temporada;
delete from habilidade_valor;
delete from escalacao_jogador_posicao ;
delete from grupo_desenvolvimento_jogador ;
delete from jogador;
";


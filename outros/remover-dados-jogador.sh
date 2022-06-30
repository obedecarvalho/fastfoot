#!/bin/bash
#--remover jogadores
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "
delete from habilidade_valor_estatistica_grupo;
delete from habilidade_valor_estatistica;
delete from habilidade_valor;
delete from escalacao_jogador_posicao ;
delete from grupo_desenvolvimento_jogador ;
delete from jogador;
";


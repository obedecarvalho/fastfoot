#!/bin/bash
#--sem remover jogadores e clubes
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "
delete from habilidade_valor_estatistica;
delete from habilidade_valor;
delete from escalacao_jogador_posicao ;
delete from grupo_desenvolvimento_jogador ;
delete from jogador;
";


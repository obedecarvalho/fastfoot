#!/bin/bash

#--sem remover jogadores e clubes
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "
drop table if exists habilidade_valor_sem1;
drop table if exists jogador_sem1;

create table jogador_sem1 as
select * from jogador;

create table habilidade_valor_sem1 as
select * from habilidade_valor;
";

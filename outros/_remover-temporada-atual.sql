#!/bin/bash

#--sem remover jogadores e clubes
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "

with tmp as (
	select cr.id as id_deletar
	from clube_ranking cr
	inner join temporada t on t.id = cr.id_temporada
	where t.atual = true
)
delete from clube_ranking where id in (select id_deletar from tmp);

with tmp as (
	select c.id as id_deletar
	from classificacao c
	inner join campeonato cp on cp.id = c.id_campeonato
	inner join temporada t on cp.id_temporada = t.id
	where t.atual
	union 
	select c.id as id_deletar
	from classificacao c
	inner join grupo_campeonato gc on gc.id = c.id_grupo_campeonato
	inner join campeonato_misto cm on cm.id = gc.id_campeonato_misto
	inner join temporada t on cm.id_temporada = t.id
	where t.atual
)
delete from classificacao where id in (select id_deletar from tmp);

with tmp as (
	select cp.id as id_deletar
	from clube_probabilidade cp
	inner join semana s on s.id = cp.id_semana
	inner join temporada t on t.id = s.id_temporada
	where t.atual
)
delete from clube_probabilidade where id in (select id_deletar from tmp);

with tmp as (
	select hve.id as id_deletar
	from habilidade_valor_estatistica hve
	inner join partida_resultado pr on hve.id_partida_resultado = pr.id
	inner join rodada r on r.id = pr.id_rodada
	inner join semana s on r.id_semana = s.id
	inner join temporada t on s.id_temporada = t.id
	where t.atual
	union
	select hve.id as id_deletar
	from habilidade_valor_estatistica hve
	inner join partida_eliminatoria_resultado per on hve.id_partida_eliminatoria_resultado = per.id
	inner join rodada_eliminatoria re on re.id = per.id_rodada_eliminatoria
	inner join semana s on re.id_semana = s.id
	inner join temporada t on s.id_temporada = t.id
	where t.atual
	union
	select hve.id as id_deletar
	from habilidade_valor_estatistica hve
	inner join partida_amistosa_resultado par on hve.id_partida_amistosa_resultado = par.id
	inner join rodada_amistosa ra on ra.id = par.id_rodada_amistosa
	inner join semana s on ra.id_semana = s.id
	inner join temporada t on s.id_temporada = t.id
	where t.atual
)
delete from habilidade_valor_estatistica where id in (select id_deletar from tmp);

with tmp as (
	select pr.id as id_deletar
	from partida_resultado pr
	inner join rodada r on r.id = pr.id_rodada
	inner join semana s on r.id_semana = s.id
	inner join temporada t on s.id_temporada = t.id
	where t.atual
)
delete from partida_resultado where id in (select id_deletar from tmp);

with tmp as (
	select per.id as id_deletar
	from partida_eliminatoria_resultado per
	inner join rodada_eliminatoria re on per.id_rodada_eliminatoria = re.id
	inner join semana s on re.id_semana = s.id
	inner join temporada t on s.id_temporada = t.id
	where t.atual
)
delete from partida_eliminatoria_resultado where id in (select id_deletar from tmp);

with tmp as (
	select par.id as id_deletar
	from partida_amistosa_resultado par
	inner join rodada_amistosa ra on ra.id = par.id_rodada_amistosa
	inner join semana s on ra.id_semana = s.id
	inner join temporada t on s.id_temporada = t.id
	where t.atual
)
delete from partida_amistosa_resultado where id in (select id_deletar from tmp);

with tmp as (
	select pe.id as id_deletar
	from partida_estatisticas pe
	inner join partida_resultado pr on pr.id_partida_estatisticas = pe.id
	inner join rodada r on r.id = pr.id_rodada
	inner join semana s on r.id_semana = s.id
	inner join temporada t on s.id_temporada = t.id
	where t.atual
	union
	select pe.id as id_deletar
	from partida_estatisticas pe
	inner join partida_eliminatoria_resultado per on per.id_partida_estatisticas = pe.id
	inner join rodada_eliminatoria re on per.id_rodada_eliminatoria = re.id
	inner join semana s on re.id_semana = s.id
	inner join temporada t on s.id_temporada = t.id
	where t.atual
	union
	select pe.id as id_deletar
	from partida_estatisticas pe
	inner join partida_amistosa_resultado par on par.id_partida_estatisticas = pe.id
	inner join rodada_amistosa ra on ra.id = par.id_rodada_amistosa
	inner join semana s on ra.id_semana = s.id
	inner join temporada t on s.id_temporada = t.id
	where t.atual
)
delete from partida_estatisticas where id in (select id_deletar from tmp);

with tmp as (
	select r.id as id_deletar
	from rodada r
	inner join semana s on r.id_semana = s.id
	inner join temporada t on s.id_temporada = t.id
	where t.atual
)
delete from rodada where id in (select id_deletar from tmp);

with tmp as (
	select re.id as id_deletar
	from rodada_eliminatoria re
	inner join semana s on re.id_semana = s.id
	inner join temporada t on s.id_temporada = t.id
	where t.atual
)
delete from rodada_eliminatoria where id in (select id_deletar from tmp);

with tmp as (
	select ra.id as id_deletar
	from rodada_amistosa ra
	inner join semana s on ra.id_semana = s.id
	inner join temporada t on s.id_temporada = t.id
	where t.atual
)
delete from rodada_amistosa where id in (select id_deletar from tmp);

with tmp as (
	select gc.id as id_deletar
	from grupo_campeonato gc
	inner join campeonato_misto cm on gc.id_campeonato_misto = cm.id
	inner join temporada t on t.id = cm.id_temporada
	where t.atual
)
delete from grupo_campeonato where id in (select id_deletar from tmp);

with tmp as (
	select cm.id as id_deletar
	from campeonato_misto cm
	inner join temporada t on t.id = cm.id_temporada
	where t.atual
)
delete from campeonato_misto where id in (select id_deletar from tmp);

with tmp as (
	select c.id as id_deletar
	from campeonato c
	inner join temporada t on t.id = c.id_temporada
	where t.atual
)
delete from campeonato where id in (select id_deletar from tmp);

with tmp as (
	select ce.id as id_deletar
	from campeonato_eliminatorio ce
	inner join temporada t on t.id = ce.id_temporada
	where t.atual
)
delete from campeonato_eliminatorio where id in (select id_deletar from tmp);

with tmp as (
	select s.id as id_deletar
	from semana s
	inner join temporada t on t.id = s.id_temporada
	where t.atual
)
delete from semana where id in (select id_deletar from tmp);

update jogador set id_jogador_estatisticas_temporada_atual = null;

with tmp as (
	select jet.id as id_deletar
	from jogador_estatisticas_temporada jet
	inner join temporada t on t.id = jet.id_temporada
	where t.atual
)
delete from jogador_estatisticas_temporada where id in (select id_deletar from tmp);

with tmp as (
	select dn.id as id_deletar
	from disponivel_negociacao dn
	inner join temporada t on dn.id_temporada = t.id
	where t.atual
)
delete from disponivel_negociacao where id in (select id_deletar from tmp);

with tmp as (
	select ptj.id as id_deletar
	from proposta_transferencia_jogador ptj
	inner join temporada t on ptj.id_temporada = t.id
	where t.atual
)
delete from proposta_transferencia_jogador where id in (select id_deletar from tmp);

with tmp as (
	select ncc.id as id_deletar
	from necessidade_contratacao_clube ncc
	inner join temporada t on ncc.id_temporada = t.id
	where t.atual
)
delete from necessidade_contratacao_clube where id in (select id_deletar from tmp);

delete from temporada where atual;
";

#TODO: defazer as movimentações de jogadores em proposta_transferencia_jogador
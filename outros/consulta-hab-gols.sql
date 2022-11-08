select g.id_clube, sum(g.gols) as gols
	, h.quantidade_uso
from (
	select id_clube_mandante as id_clube, sum(gols_mandante) as gols
	from partida_resultado pr
	inner join rodada r on pr.id_rodada = r.id
	inner join semana s on s.id = r.id_semana
	where s.id_temporada = 18440
	group by id_clube_mandante
	union all
	select id_clube_visitante as id_clube, sum(gols_visitante) as gols
	from partida_resultado pr
	inner join rodada r on pr.id_rodada = r.id
	inner join semana s on s.id = r.id_semana
	where s.id_temporada = 18440
	group by id_clube_visitante
	union all
	select id_clube_mandante as id_clube, sum(gols_mandante) as gols
	from partida_eliminatoria_resultado per
	inner join rodada_eliminatoria re on per.id_rodada_eliminatoria = re.id
	inner join semana s on s.id = re.id_semana
	where s.id_temporada = 18440
	group by id_clube_mandante
	union all
	select id_clube_visitante as id_clube, sum(gols_visitante) as gols
	from partida_eliminatoria_resultado per
	inner join rodada_eliminatoria re on per.id_rodada_eliminatoria = re.id
	inner join semana s on s.id = re.id_semana
	where s.id_temporada = 18440
	group by id_clube_visitante
) as g
inner join (
	select j.id_clube, sum(hve.quantidade_uso) as quantidade_uso
	from habilidade_valor_estatistica hve
	inner join habilidade_valor hv on hv.id = hve.id_habilidade_valor
	inner join jogador j on j.id = hv.id_jogador
	inner join semana s on hve.id_semana = s.id
	where hv.habilidade in (2,11)
		and hve.amistoso = false
		and s.id_temporada = 18440
	group by j.id_clube
) as h on g.id_clube = h.id_clube
group by g.id_clube
	, h.quantidade_uso
;


select id_clube, 
	sum(gols_marcados),
	sum(gols_marcados + finalizacoes_defendidas + finalizacoes_fora),
	sum(gols_marcados)::float/sum(gols_marcados + finalizacoes_defendidas + finalizacoes_fora),
	sum(gols_marcados/numero_jogos::float) as gols_partida,
	sum((gols_marcados + finalizacoes_defendidas + finalizacoes_fora)/numero_jogos::float) as finalizacoes_partidas
from jogador_estatisticas_temporada
where id_temporada = 18440
	and amistoso = false
	and numero_jogos > 0
	--and numero_jogos = 17
	and (gols_marcados + finalizacoes_defendidas + finalizacoes_fora) > 0
group by id_clube;

select id_clube, 
	sum(gols_marcados/numero_jogos::float) as gols_partida,
	sum((gols_marcados + finalizacoes_defendidas + finalizacoes_fora)/numero_jogos::float) as finalizacoes_partidas,
	sum(gols_marcados/numero_jogos::float)/sum((gols_marcados + finalizacoes_defendidas + finalizacoes_fora)
		/numero_jogos::float) as probilidade_gols
from jogador_estatisticas_temporada
where id_temporada = 18440
	and amistoso = false
	and numero_jogos > 0
	and (gols_marcados + finalizacoes_defendidas + finalizacoes_fora) > 0
group by id_clube
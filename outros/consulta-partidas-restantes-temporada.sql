
--nacional_mandante
select id_clube_mandante as id_clube, count(*) as qtde, c.nivel_campeonato, null::int as numero, 'MANDANTE' as tipo, clu.clube_nivel as clube_nivel_mandante
from partida_resultado pr
inner join rodada r on pr.id_rodada = r.id
inner join campeonato c on c.id = r.id_campeonato
inner join clube clu on clu.id = id_clube_mandante
where partida_jogada = false
group by id_clube_mandante, c.nivel_campeonato, clu.clube_nivel

--nacional_visitante
select id_clube_visitante as id_clube, count(*) as qtde, c.nivel_campeonato, null::int as numero, 'VISITANTE' as tipo, clu.clube_nivel as clube_nivel_mandante
from partida_resultado pr
inner join rodada r on pr.id_rodada = r.id
inner join campeonato c on c.id = r.id_campeonato
inner join clube clu on clu.id = id_clube_visitante
inner join clube clu2 on clu2.id = id_clube_mandante
where partida_jogada = false
group by id_clube_visitante, c.nivel_campeonato, clu.clube_nivel

--continental_mandante
select id_clube_mandante as id_clube, count(*) as qtde, cm.nivel_campeonato, null::int as numero, 'MANDANTE' as tipo, clu.clube_nivel as clube_nivel_mandante
from partida_resultado pr
inner join rodada r on pr.id_rodada = r.id
inner join grupo_campeonato gc on gc.id = r.id_grupo_campeonato
inner join campeonato_misto cm on cm.id = gc.id_campeonato_misto
inner join clube clu on clu.id = id_clube_mandante
where partida_jogada = false
group by id_clube_mandante, cm.nivel_campeonato, clu.clube_nivel

--continental_visitante
select id_clube_visitante as id_clube, count(*) as qtde, cm.nivel_campeonato, null::int as numero, 'VISITANTE' as tipo, clu.clube_nivel as clube_nivel_mandante
from partida_resultado pr
inner join rodada r on pr.id_rodada = r.id
inner join grupo_campeonato gc on gc.id = r.id_grupo_campeonato
inner join campeonato_misto cm on cm.id = gc.id_campeonato_misto
inner join clube clu on clu.id = id_clube_visitante
inner join clube clu2 on clu2.id = id_clube_mandante
where partida_jogada = false
group by id_clube_visitante, cm.nivel_campeonato, clu.clube_nivel

--cn_mandante
select id_clube_mandante as id_clube, count(*) as qtde, ce.nivel_campeonato, re.numero, 'MANDANTE' as tipo, clu.clube_nivel as clube_nivel_mandante
from partida_eliminatoria_resultado per
inner join rodada_eliminatoria re on re.id = per.id_rodada_eliminatoria
inner join campeonato_eliminatorio ce on ce.id = re.id_campeonato_eliminatorio
inner join clube clu on clu.id = id_clube_mandante
where partida_jogada = false
	and id_clube_mandante is not null
group by id_clube_mandante, ce.nivel_campeonato, re.numero, clu.clube_nivel

--cn_visitante
select id_clube_visitante as id_clube, count(*) as qtde, ce.nivel_campeonato, re.numero, 'VISITANTE' as tipo, clu.clube_nivel as clube_nivel_mandante
from partida_eliminatoria_resultado per
inner join rodada_eliminatoria re on re.id = per.id_rodada_eliminatoria
inner join campeonato_eliminatorio ce on ce.id = re.id_campeonato_eliminatorio
inner join clube clu on clu.id = id_clube_visitante
inner join clube clu2 on clu2.id = id_clube_mandante
where partida_jogada = false
	and id_clube_visitante is not null
group by id_clube_visitante, ce.nivel_campeonato, re.numero, clu.clube_nivel

--continental_ff_mandante
select id_clube_mandante as id_clube, count(*) as qtde, cm.nivel_campeonato, re.numero, 'MANDANTE' as tipo, clu.clube_nivel as clube_nivel_mandante
from partida_eliminatoria_resultado per
inner join rodada_eliminatoria re on re.id = per.id_rodada_eliminatoria
inner join campeonato_misto cm on cm.id = re.id_campeonato_misto
inner join clube clu on clu.id = id_clube_mandante
where partida_jogada = false
	and id_clube_mandante is not null
group by id_clube_mandante, cm.nivel_campeonato, re.numero, clu.clube_nivel

--continental_ff_visitante
select id_clube_visitante as id_clube, count(*) as qtde, cm.nivel_campeonato, re.numero, 'VISITANTE' as tipo, clu.clube_nivel as clube_nivel_mandante
from partida_eliminatoria_resultado per
inner join rodada_eliminatoria re on re.id = per.id_rodada_eliminatoria
inner join campeonato_misto cm on cm.id = re.id_campeonato_misto
inner join clube clu on clu.id = id_clube_visitante
inner join clube clu2 on clu2.id = id_clube_mandante
where partida_jogada = false
	and id_clube_visitante is not null
group by id_clube_visitante, cm.nivel_campeonato, re.numero, clu.clube_nivel

--amistoso_mandante
select id_clube_mandante as id_clube, count(*) as qtde, null as nivel_campeonato, null::int as numero, 'MANDANTE' as tipo, clu.clube_nivel as clube_nivel_mandante
from partida_amistosa_resultado
inner join clube clu on clu.id = id_clube_mandante
where partida_jogada = false
group by id_clube_mandante, clu.clube_nivel

--amistoso_visitante
select id_clube_visitante as id_clube, count(*) as qtde, null as nivel_campeonato, null::int as numero, 'VISITANTE' as tipo, clu.clube_nivel as clube_nivel_mandante
from partida_amistosa_resultado
inner join clube clu on clu.id = id_clube_visitante
inner join clube clu2 on clu2.id = id_clube_mandante
where partida_jogada = false
group by id_clube_visitante, clu.clube_nivel

--total gols por clube pelo resultado
select tmp.id_clube, sum(tmp.gols) as gols
from (
	select pr.id_clube_mandante as id_clube, sum(gols_mandante) as gols
	from partida_resultado pr
	where pr.partida_jogada
	group by pr.id_clube_mandante
	union all
	select par.id_clube_mandante as id_clube, sum(par.gols_mandante) as gols
	from partida_eliminatoria_resultado par
	where par.partida_jogada
	group by par.id_clube_mandante
	union all
	select pr.id_clube_visitante as id_clube, sum(pr.gols_visitante) as gols
	from partida_resultado pr
	where pr.partida_jogada
	group by pr.id_clube_visitante
	union all
	select par.id_clube_visitante as id_clube, sum(par.gols_visitante) as gols
	from partida_eliminatoria_resultado par
	where par.partida_jogada
	group by par.id_clube_visitante
) tmp
group by tmp.id_clube
order by tmp.id_clube
;

--total gols por clube pelo estatisticas jog
select j.id_clube, sum(jet.gols_marcados)
from jogador_estatisticas_temporada jet
inner join jogador j on j.id = jet.id_jogador
group by j.id_clube
order by j.id_clube;

--total gols por clube pelo resultado
select tmp.id_clube, sum(tmp.gols) as gols, tmp.id_temporada
from (
	select pr.id_clube_mandante as id_clube, sum(gols_mandante) as gols, s.id_temporada
	from partida_resultado pr
	inner join rodada r on pr.id_rodada = r.id 
	inner join semana s on r.id_semana = s.id 
	where pr.partida_jogada
	group by pr.id_clube_mandante, s.id_temporada
	union all
	select par.id_clube_mandante as id_clube, sum(par.gols_mandante) as gols, s.id_temporada
	from partida_eliminatoria_resultado par
	inner join rodada_eliminatoria re on par.id_rodada_eliminatoria = re.id 
	inner join semana s on s.id = re.id_semana 
	where par.partida_jogada
	group by par.id_clube_mandante, s.id_temporada
	union all
	select pr.id_clube_visitante as id_clube, sum(pr.gols_visitante) as gols, s.id_temporada
	from partida_resultado pr
	inner join rodada r on pr.id_rodada = r.id 
	inner join semana s on r.id_semana = s.id 
	where pr.partida_jogada
	group by pr.id_clube_visitante, s.id_temporada
	union all
	select par.id_clube_visitante as id_clube, sum(par.gols_visitante) as gols, s.id_temporada
	from partida_eliminatoria_resultado par
	inner join rodada_eliminatoria re on par.id_rodada_eliminatoria = re.id 
	inner join semana s on s.id = re.id_semana 
	where par.partida_jogada
	group by par.id_clube_visitante, s.id_temporada
) tmp
group by tmp.id_clube, tmp.id_temporada
order by tmp.id_clube, tmp.id_temporada
;

--total gols por clube pelo estatisticas jog
select j.id_clube, sum(jet.gols_marcados), jet.id_temporada
from jogador_estatisticas_temporada jet
inner join jogador j on j.id = jet.id_jogador
group by j.id_clube, jet.id_temporada
order by j.id_clube, jet.id_temporada

--estatisticas por jogador
select jet.id_jogador, j.posicao,
	sum(jet.numero_jogos) as numero_jogos, sum(jet.gols_marcados) as gols_marcados 
from jogador_estatisticas_temporada jet 
inner join jogador j on j.id = jet.id_jogador 
group by jet.id_jogador, j.posicao

--gols por posicao
select j.posicao,
	sum(jet.numero_jogos) as numero_jogos, sum(jet.gols_marcados) as gols_marcados 
from jogador_estatisticas_temporada jet 
inner join jogador j on j.id = jet.id_jogador 
group by j.posicao
order by j.posicao;


--total de gols por posicao
select j.posicao,
	sum(jet.numero_jogos) as numero_jogos, sum(jet.gols_marcados) as gols_marcados 
from jogador_estatisticas_temporada jet 
inner join jogador j on j.id = jet.id_jogador 
group by j.posicao
order by j.posicao;

select j.posicao, hv.habilidade, hv.habilidade_tipo, count(*) 
from jogador j 
inner join habilidade_valor hv on hv.id_jogador = j.id 
where hv.habilidade in (2, 11)
	and j.posicao in (2, 3, 4)
	--and hv.habilidade_tipo in (0, 1)
group by j.posicao, hv.habilidade, hv.habilidade_tipo
order by j.posicao, hv.habilidade, hv.habilidade_tipo;

--estatisticas sobre habilidades por posicao
select j.posicao, hv.habilidade, hv.habilidade_tipo, sum(hve.quantidade_uso), sum(hve.quantidade_uso_vencedor)
from jogador j 
inner join habilidade_valor hv on hv.id_jogador = j.id 
inner join habilidade_valor_estatistica hve on hve.id_habilidade_valor = hv.id
group by j.posicao, hv.habilidade, hv.habilidade_tipo
order by j.posicao, hv.habilidade, hv.habilidade_tipo
;

--distribuição das habilidades por posição
select j.posicao, hv.habilidade, hv.habilidade_tipo, count(*)
from habilidade_valor hv
inner join jogador j on j.id = hv.id_jogador
group by j.posicao, hv.habilidade, hv.habilidade_tipo
order by j.posicao, hv.habilidade, hv.habilidade_tipo;


create user fastfoot with password 'fastfoot_';

create database fastfoot owner fastfoot;

select * from clube;
select * from clube_ranking;

select * from temporada;
select * from semana;

--Nacional
select * from campeonato;
select * from classificacao;
select * from rodada;
select * from partida_resultado;


select c.id_clube, c.pontos, c.vitorias, c.saldo_gols, c.gols_pro, c.posicao, c.num_jogos, c.pontos - 3*c.vitorias as empates
from classificacao c
where c.id_campeonato = 4805
order by c.pontos desc, c.vitorias desc, c.saldo_gols desc, c.gols_pro desc;

select
	r.numero,
	p.id_clube_mandante,p.gols_mandante,
	p.id_clube_visitante, p.gols_visitante
from rodada r
inner join partida_resultado p on p.id_rodada = r.id
where r.id_campeonato = 4805
order by r.numero;

--Copa

select * from campeonato_eliminatorio;

select * from rodada_eliminatoria;

select * from partida_eliminatoria_resultado;

select 
	r.id, r.id_proxima_rodada, r.numero,
	p.id_clube_mandante, p.gols_mandante,
	p.id_clube_visitante, p.gols_visitante,
	p.classificaamandante, p.id, p.id_proxima_partida
from partida_eliminatoria_resultado p
inner join rodada_eliminatoria r on p.id_rodada_eliminatoria = r.id
where r.id_campeonato_eliminatorio = 1618
order by r.numero desc
;


--Continental

select * from campeonato_misto;

select * from grupo_campeonato;
select * from classificacao;
select * from rodada;
select * from partida_resultado;

select * from rodada_eliminatoria;
select * from partida_eliminatoria_resultado;

--Jogos
select
	gc.numero, r.numero,
	p.id_clube_mandante,p.gols_mandante,
	p.id_clube_visitante, p.gols_visitante
from grupo_campeonato gc
inner join rodada r on r.id_grupo_campeonato = gc.id
inner join partida_resultado p on p.id_rodada = r.id
where gc.id_campeonato_misto = 1656
order by gc.numero, r.numero;

--Classificacao
select gc.numero, c.id_clube, c.pontos, c.vitorias, c.saldo_gols, c.gols_pro, c.posicao, c.num_jogos, c.pontos - 3*c.vitorias as empates
from grupo_campeonato gc
inner join classificacao c on c.id_grupo_campeonato = gc.id
where gc.id_campeonato_misto = 1656
order by gc.numero, c.pontos desc, c.vitorias desc, c.saldo_gols desc, c.gols_pro desc;

--Fase final
select 
	r.id, r.id_proxima_rodada, r.numero,
	p.id_clube_mandante, p.gols_mandante,
	p.id_clube_visitante, p.gols_visitante,
	p.classificaamandante, p.id, p.id_proxima_partida
from partida_eliminatoria_resultado p
inner join rodada_eliminatoria r on p.id_rodada_eliminatoria = r.id
where r.id_campeonato_misto = 1656
order by r.numero desc
;


--Remover

select id, 'campeonato'  from campeonato union
select id, 'campeonato_eliminatorio'  from campeonato_eliminatorio union
select id, 'campeonato_misto'  from campeonato_misto union
select id, 'classificacao'  from classificacao union
select id, 'clube'  from clube union
select id, 'clube_ranking'  from clube_ranking union
select id, 'grupo_campeonato'  from grupo_campeonato union
select id, 'partida'  from partida_resultado union
select id, 'partida_eliminatoria_resultado'  from partida_eliminatoria_resultado union
select id, 'rodada'  from rodada union
select id, 'rodada_eliminatoria'  from rodada_eliminatoria union
select id, 'semana'  from semana union
select id, 'temporada'  from temporada;

select count(*), 'campeonato'  from campeonato union
select count(*), 'campeonato_eliminatorio'  from campeonato_eliminatorio union
select count(*), 'campeonato_misto'  from campeonato_misto union
select count(*), 'classificacao'  from classificacao union
select count(*), 'clube'  from clube union
select count(*), 'clube_ranking'  from clube_ranking union
select count(*), 'grupo_campeonato'  from grupo_campeonato union
select count(*), 'partida'  from partida_resultado union
select count(*), 'partida_eliminatoria_resultado'  from partida_eliminatoria_resultado union
select count(*), 'rodada'  from rodada union
select count(*), 'rodada_eliminatoria'  from rodada_eliminatoria union
select count(*), 'semana'  from semana union
select count(*), 'temporada'  from temporada;

delete from partida_resultado;
delete from partida_eliminatoria_resultado;
delete from rodada;
delete from rodada_eliminatoria;
delete from classificacao;
delete from grupo_campeonato;
delete from campeonato;
delete from campeonato_eliminatorio;
delete from campeonato_misto;
delete from clube_ranking;
delete from semana;
delete from clube;
delete from temporada;
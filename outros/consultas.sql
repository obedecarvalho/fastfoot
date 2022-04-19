
create user fastfoot with password 'fastfoot_';

create database fastfoot owner fastfoot;

create table tmp_hab (
	id int4, dsc text
);


INSERT INTO tmp_hab (id, dsc) VALUES (1, 'Passe');
INSERT INTO tmp_hab (id, dsc) VALUES (2, 'Finalizacao');
INSERT INTO tmp_hab (id, dsc) VALUES (3, 'Cruzamento');
INSERT INTO tmp_hab (id, dsc) VALUES (4, 'Armacao');
INSERT INTO tmp_hab (id, dsc) VALUES (5, 'Marcacao');
INSERT INTO tmp_hab (id, dsc) VALUES (6, 'Desarme');
INSERT INTO tmp_hab (id, dsc) VALUES (7, 'Interceptacao');
INSERT INTO tmp_hab (id, dsc) VALUES (8, 'Velocidade');
INSERT INTO tmp_hab (id, dsc) VALUES (9, 'Dible');
INSERT INTO tmp_hab (id, dsc) VALUES (10, 'Forca');
INSERT INTO tmp_hab (id, dsc) VALUES (11, 'Cabeceio');
INSERT INTO tmp_hab (id, dsc) VALUES (12, 'Posicionamento');
INSERT INTO tmp_hab (id, dsc) VALUES (13, 'Dominio');
INSERT INTO tmp_hab (id, dsc) VALUES (14, 'Reflexo');
INSERT INTO tmp_hab (id, dsc) VALUES (15, 'Jogo Aereo');

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

--habilidade
select habilidade, count(*)
from habilidade_valor
group by habilidade;

--estatisticas
select *
from partida_estatisticas;

select
	ja.ordem, 
	ja.id_jogador, ha.dsc as habilidade_acao,
	jr.id_jogador, hr.dsc as habilidade_reacao,
	ja.vencedor,
	ja.vencedor = false and jr.vencedor = false as fora,
	jr.habilidade_usada in (15, 14) and ja.vencedor as gol
from jogador_estatisticas ja
inner join tmp_hab ha on ha.id = ja.habilidade_usada
left join jogador_estatisticas jr on (ja.id_partida_estatisticas = jr.id_partida_estatisticas and ja.ordem = jr.ordem and ja.id <> jr.id)
left join tmp_hab hr on hr.id = jr.habilidade_usada
where ja.acao
	and ja.id_partida_estatisticas = 252
	--and (jr.id is null or jr.acao = false)
	--and jr.habilidade_usada in (14, 15)
	--and jr.id is null
order by ja.ordem;

select id_partida_estatisticas, id_jogador, habilidade_usada, quantidade_uso, quantidade_uso_vencedor
from jogador_grupo_estatisticas
where id_partida_estatisticas in (2152,2153,2154,2155,2156,2157,2158,2159,2160,2161,2162,2163,2164,2165,2166,2167,2168,2169,2170,2171,2172,2173,2174,2175,2176,2177,2178,2179,2180,2181,2182,2183,2184,2185,2186,2187,2188,2189,2190,2191,2192,2193,2194,2195,2196,2197,2198,2199,2200,2201,2202,2203,2204,2205,2206,2207,2208,2209,2210,2211,2212,2213,2214,2215)
order by id_partida_estatisticas, id_jogador, habilidade_usada;

select id_partida_estatisticas, id_jogador, habilidade_usada, count(*) as quantidade_uso, sum(case when vencedor then 1 else 0 end) as quantidade_uso_vencedor
from jogador_estatisticas
where id_partida_estatisticas in (2152,2153,2154,2155,2156,2157,2158,2159,2160,2161,2162,2163,2164,2165,2166,2167,2168,2169,2170,2171,2172,2173,2174,2175,2176,2177,2178,2179,2180,2181,2182,2183,2184,2185,2186,2187,2188,2189,2190,2191,2192,2193,2194,2195,2196,2197,2198,2199,2200,2201,2202,2203,2204,2205,2206,2207,2208,2209,2210,2211,2212,2213,2214,2215)
group by id_jogador, id_partida_estatisticas, habilidade_usada
order by id_partida_estatisticas, id_jogador, habilidade_usada;

--Remover

select id, 'campeonato'  from campeonato union
select id, 'campeonato_eliminatorio'  from campeonato_eliminatorio union
select id, 'campeonato_misto'  from campeonato_misto union
select id, 'classificacao'  from classificacao union
select id, 'clube'  from clube union
select id, 'clube_ranking'  from clube_ranking union
select id, 'grupo_campeonato'  from grupo_campeonato union
select id, 'habilidade_valor'  from habilidade_valor union
select id, 'jogador'  from jogador union
select id, 'jogador_grupo_estatisticas'  from jogador_grupo_estatisticas union
select id, 'jogador_estatisticas'  from jogador_estatisticas union
select id, 'partida_estatisticas'  from partida_estatisticas union
select id, 'partida_resultado'  from partida_resultado union
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
select count(*), 'habilidade_valor'  from habilidade_valor union
select count(*), 'jogador'  from jogador union
select count(*), 'jogador_grupo_estatisticas'  from jogador_grupo_estatisticas union
select count(*), 'jogador_estatisticas'  from jogador_estatisticas union
select count(*), 'partida_estatisticas'  from partida_estatisticas union
select count(*), 'partida_resultado'  from partida_resultado union
select count(*), 'partida_eliminatoria_resultado'  from partida_eliminatoria_resultado union
select count(*), 'rodada'  from rodada union
select count(*), 'rodada_eliminatoria'  from rodada_eliminatoria union
select count(*), 'semana'  from semana union
select count(*), 'temporada'  from temporada;

delete from jogador_grupo_estatisticas;
delete from jogador_estatisticas;
delete from partida_estatisticas;
delete from partida_resultado;
delete from partida_eliminatoria_resultado;
delete from rodada;
delete from rodada_eliminatoria;
delete from classificacao;
delete from grupo_campeonato;
delete from campeonato;
delete from campeonato_eliminatorio;
delete from campeonato_misto;
delete from habilidade_valor;
delete from jogador;
delete from clube_ranking;
delete from semana;
delete from clube;
delete from temporada;
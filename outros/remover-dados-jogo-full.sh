#!/bin/bash
#--remover jogadores
[ -z $1 ] && exit;
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "
BEGIN;
delete
from disponivel_negociacao dn
where id_jogador in (
	select j.id
	from jogador j
	inner join clube c on j.id_clube = c.id
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from proposta_transferencia_jogador ptj
where id_jogador in (
	select j.id
	from jogador j
	inner join clube c on j.id_clube = c.id
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from jogador_estatisticas_semana jes
where id_jogador in (
	select j.id
	from jogador j
	inner join clube c on j.id_clube = c.id
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from jogador_estatisticas_temporada jet
where id_jogador in (
	select j.id
	from jogador j
	inner join clube c on j.id_clube = c.id
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from jogador_energia je
where id_jogador in (
	select j.id
	from jogador j
	inner join clube c on j.id_clube = c.id
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from campeonato_clube_probabilidade
where id_campeonato in (
	select c.id
	from temporada t
	inner join campeonato c on c.id_temporada = t.id
	where t.id_jogo = $1
);
delete
from partida_amistosa_resultado
where id_rodada_amistosa in (
	select r.id
	from temporada t
	inner join semana s on t.id = s.id_temporada
	inner join rodada_amistosa r on r.id_semana = s.id
	where t.id_jogo = $1
);
delete
from partida_eliminatoria_resultado
where id_rodada_eliminatoria in (
	select r.id
	from campeonato_misto cm
	inner join temporada t on cm.id_temporada = t.id
	inner join rodada_eliminatoria r on r.id_campeonato_misto = cm.id
	where t.id_jogo = $1
);
delete
from partida_eliminatoria_resultado
where id_rodada_eliminatoria in (
	select r.id
	from campeonato_eliminatorio ce
	inner join temporada t on ce.id_temporada = t.id
	inner join rodada_eliminatoria r on r.id_campeonato_eliminatorio = ce.id
	where t.id_jogo = $1
);
delete
from partida_resultado
where id_rodada in (
	select r.id
	from grupo_campeonato gc
	inner join campeonato_misto cm on cm.id = gc.id_campeonato_misto
	inner join temporada t on cm.id_temporada = t.id
	inner join rodada r on gc.id = r.id_grupo_campeonato
	where t.id_jogo = $1
);
delete
from partida_resultado
where id_rodada in (
	select r.id
	from campeonato cm
	inner join temporada t on cm.id_temporada = t.id
	inner join rodada r on cm.id = r.id_campeonato
	where t.id_jogo = $1
);
delete
from partida_estatisticas
where id not in (
	select id_partida_estatisticas
	from partida_amistosa_resultado
	union all
	select id_partida_estatisticas
	from partida_eliminatoria_resultado
	union all
	select id_partida_estatisticas
	from partida_resultado
);
delete
from partida_torcida
where id not in (
	select id_partida_torcida
	from partida_amistosa_resultado
	union all
	select id_partida_torcida
	from partida_eliminatoria_resultado
	union all
	select id_partida_torcida
	from partida_resultado
);
delete
from partida_disputa_penalties
where id not in (
	select id_partida_disputa_penalties
	from partida_eliminatoria_resultado
);
delete
from partida_probabilidade_resultado
where id not in (
	select id_partida_probabilidade_resultado
	from partida_amistosa_resultado
	union all
	select id_partida_probabilidade_resultado
	from partida_eliminatoria_resultado
	union all
	select id_partida_probabilidade_resultado
	from partida_resultado
);
delete
from rodada
where id_campeonato in (
	select cm.id
	from campeonato cm
	inner join temporada t on cm.id_temporada = t.id
	where t.id_jogo = $1
);
delete
from rodada
where id_grupo_campeonato in (
	select gc.id
	from grupo_campeonato gc
	inner join campeonato_misto cm on cm.id = gc.id_campeonato_misto
	inner join temporada t on cm.id_temporada = t.id
	where t.id_jogo = $1
);
delete
from rodada_amistosa
where id_semana in (
	select s.id
	from temporada t
	inner join semana s on t.id = s.id_temporada
	where t.id_jogo = $1
);
delete
from rodada_eliminatoria
where id_campeonato_eliminatorio in (
	select ce.id
	from campeonato_eliminatorio ce
	inner join temporada t on ce.id_temporada = t.id
	where t.id_jogo = $1
);
delete
from rodada_eliminatoria
where id_campeonato_misto in (
	select cm.id
	from campeonato_misto cm
	inner join temporada t on cm.id_temporada = t.id
	where t.id_jogo = $1
);
delete
from classificacao
where id_grupo_campeonato in (
	select gc.id
	from grupo_campeonato gc
	inner join campeonato_misto cm on cm.id = gc.id_campeonato_misto
	inner join temporada t on cm.id_temporada = t.id
	where t.id_jogo = $1
);
delete
from classificacao
where id_campeonato in (
	select cm.id
	from campeonato cm
	inner join temporada t on cm.id_temporada = t.id
	where t.id_jogo = $1
);
delete
from grupo_campeonato
where id_campeonato_misto in (
	select cm.id
	from campeonato_misto cm
	inner join temporada t on cm.id_temporada = t.id
	where t.id_jogo = $1
);
delete
from campeonato
where id_temporada in (
	select id
	from temporada
	where id_jogo = $1
);
delete
from campeonato_eliminatorio
where id_temporada in (
	select id
	from temporada
	where id_jogo = $1
);
delete
from campeonato_misto
where id_temporada in (
	select id
	from temporada
	where id_jogo = $1
);
delete
from necessidade_contratacao_clube
where id_clube in (
	select c.id
	from clube c
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from clube_resumo_temporada
where id_clube in (
	select c.id
	from clube c
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from clube_ranking
where id_clube in (
	select c.id
	from clube c
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from movimentacao_financeira
where id_clube in (
	select c.id
	from clube c
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from demonstrativo_financeiro_temporada
where id_clube in (
	select c.id
	from clube c
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from trajetoria_forca_clube
where id_clube in (
	select c.id
	from clube c
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from mudanca_clube_nivel
where id_clube in (
	select c.id
	from clube c
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
update contrato
set id_semana_inicial = null
from jogador j
inner join clube c on j.id_clube = c.id
inner join liga_jogo lj on c.id_liga_jogo = lj.id
where contrato.id_jogador = j.id and lj.id_jogo = $1;
delete
from treinador_resumo_temporada
where id_treinador in (
	select id
	from treinador
	where id_jogo = $1
);
delete 
from mudanca_treinador
where id_temporada in (
	select id
	from temporada
	where id_jogo = $1
);
delete
from semana
where id_temporada in (
	select id
	from temporada
	where id_jogo = $1
);
delete
from temporada
where id_jogo = $1;
delete
from habilidade_grupo_valor
where id_jogador in (
	select j.id
	from jogador j
	inner join clube c on j.id_clube = c.id
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from habilidade_valor
where id_jogador in (
	select j.id
	from jogador j
	inner join clube c on j.id_clube = c.id
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from contrato
where id_jogador in (
	select j.id
	from jogador j
	inner join clube c on j.id_clube = c.id
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from jogador
where id in (
	select j.id
	from jogador j
	inner join clube c on j.id_clube = c.id
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from clube_titulo_ranking
where id_clube in (
	select c.id
	from clube c
	inner join liga_jogo lj on c.id_liga_jogo = lj.id
	where lj.id_jogo = $1
);
delete
from clube c
where id_liga_jogo in (
	select lj.id
	from liga_jogo lj
	where lj.id_jogo = $1
);
delete
from treinador_titulo_ranking
where id_treinador in (
	select id
	from treinador
	where id_jogo = $1
);
delete
from treinador
where id_jogo = $1;
delete
from liga_jogo
where id_jogo = $1;
delete
from parametro
where id_jogo = $1;
delete
from jogo
where id = $1;
COMMIT;
";

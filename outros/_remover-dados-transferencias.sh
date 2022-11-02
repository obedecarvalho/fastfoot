#!/bin/bash
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "
update jogador 
set id_clube = t.id_clube_origem
from (
	select id_jogador, id_clube_origem 
	from proposta_transferencia_jogador ptj 
	inner join temporada t on t.id = ptj.id_temporada and t.atual 
	where proposta_aceita 
) t
where t.id_jogador = id;

delete
from movimentacao_financeira mf 
where tipo_movimentacao in (5, 11) and mf.id_semana in (
	select s.id
	from semana s 
	inner join temporada t on t.id = s.id_temporada and t.atual 
);

delete from disponivel_negociacao where id_temporada in (select id from temporada where atual);
delete from proposta_transferencia_jogador where id_temporada in (select id from temporada where atual);
delete from necessidade_contratacao_clube where id_temporada in (select id from temporada where atual);
";


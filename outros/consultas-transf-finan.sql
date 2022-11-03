create table tmp_tamanho_torcida (
	clube_nivel int4, tamanho int4
);

INSERT INTO tmp_tamanho_torcida (clube_nivel, tamanho) VALUES (0, 60000);
INSERT INTO tmp_tamanho_torcida (clube_nivel, tamanho) VALUES (1, 50000);
INSERT INTO tmp_tamanho_torcida (clube_nivel, tamanho) VALUES (2, 42000);
INSERT INTO tmp_tamanho_torcida (clube_nivel, tamanho) VALUES (3, 36000);
INSERT INTO tmp_tamanho_torcida (clube_nivel, tamanho) VALUES (4, 30000);
INSERT INTO tmp_tamanho_torcida (clube_nivel, tamanho) VALUES (5, 25000);
INSERT INTO tmp_tamanho_torcida (clube_nivel, tamanho) VALUES (6, 20000);

--qtde transferencias por clube
select c.id, origem.total, destino.total
from clube c
left join (
	select id_clube_origem, count(*) as total
	from proposta_transferencia_jogador
	where proposta_aceita
	group by id_clube_origem
) as origem on origem.id_clube_origem = c.id
left join (
	select id_clube_destino, count(*) as total
	from proposta_transferencia_jogador
	where proposta_aceita
	group by id_clube_destino
) as destino on destino.id_clube_destino = c.id;

select c.id as id_clube, mov.saldo, sal.salarios as salarios_projetado
from clube c
inner join (
	select id_clube, sum(valor_movimentacao) as saldo
	from movimentacao_financeira
	group by id_clube
) as mov on mov.id_clube = c.id
inner join (
	select id_clube, sum(valor_transferencia) * 0.1 as salarios
	from jogador j
	where j.status_jogador = 0
	group by id_clube
) as sal on sal.id_clube = c.id;

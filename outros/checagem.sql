
select id_clube, sum(valor_movimentacao), 'valor_movimentacao' as label
from movimentacao_financeira mf
group by id_clube
union all
select id_clube, sum(numero_jogos), 'numero_jogos' as label
from jogador_estatisticas_semana
group by id_clube
union all
select id_clube, sum(gols_marcados), 'gols_marcados' as label
from jogador_estatisticas_semana
group by id_clube
union all
select id_clube, sum(energia), 'energia' as label
from jogador_energia je
inner join jogador j on j.id = je.id_jogador
group by id_clube
union all
select id_clube, sum(probabilidade_classificacaociii), 'probabilidade_classificacaociii' as label
from campeonato_clube_probabilidade
group by id_clube
union all
select id_clube_mandante, sum(case when partida_jogada then 1 else 0 end), 'partida_jogada' as label
from partida_resultado
group by id_clube_mandante
union all
select id_clube, sum(pontos), 'pontos' as label
from classificacao
group by id_clube
union all
select id_liga_jogo, sum(rodada_atual), 'rodada_atual' as label
from campeonato
group by id_liga_jogo
union all
select id_clube, sum(jogos), 'jogos' as label
from clube_resumo_temporada
group by id_clube
union all
select id_clube, sum(posicao_geral), 'posicao_geral' as label
from clube_ranking
group by id_clube
union all
select id_clube, sum(forca_geral), 'forca_geral' as label
from jogador
group by id_clube
union all
select id_clube, sum(idade), 'idade' as label
from jogador
group by id_clube
union all
select j.id_clube, sum(passo_desenvolvimento), 'passo_desenvolvimento' as label
from habilidade_valor hv
inner join jogador j on hv.id_jogador = j.id
group by j.id_clube
union all
select j.id_clube, sum(valor_decimal), 'valor_decimal' as label
from habilidade_valor hv
inner join jogador j on hv.id_jogador = j.id
group by j.id_clube
union all
select j.id_clube, sum(valor_total), 'valor_total' as label
from habilidade_grupo_valor hv
inner join jogador j on hv.id_jogador = j.id
group by j.id_clube


delete
from partida_eliminatoria where id_rodada_eliminatoria in (
	select id as id_rodada_eliminatoria
	from rodada_eliminatoria where id_campeonato_eliminatorio in (
	select id as id_campeonato_eliminatorio from campeonato_eliminatorio where id_temporada in (8475,10131,11787,13571)
	)
)

delete
from partida_eliminatoria where id_rodada_eliminatoria in (
	select id as id_rodada_eliminatoria
	from rodada_eliminatoria where id_campeonato_misto in (
	select id as id_campeonato_misto from campeonato_misto where id_temporada in (8475,10131,11787,13571)
	)
)

delete
from partida where id_rodada in (
	select id as id_rodada
	from rodada where id_grupo_campeonato in (
	select id as id_grupo_campeonato from grupo_campeonato where id_campeonato_misto in (9869,9936,11525,11592,13181,13248,14965,15032)
	)
)

delete
from partida where id_rodada in (
	select id as id_rodada
	from rodada where id_campeonato in (
		select id as id_campeonato from campeonato where id_temporada in (8475,10131,11787,13571)
	)
)

delete
from rodada where id_campeonato in (
	select id as id_campeonato from campeonato where id_temporada in (8475,10131,11787,13571)
)

delete
from rodada where id_grupo_campeonato in (
	select id as id_grupo_campeonato from grupo_campeonato where id_campeonato_misto in (9869,9936,11525,11592,13181,13248,14965,15032)
)

delete
from rodada_eliminatoria where id_campeonato_eliminatorio in (
	select id as id_campeonato_eliminatorio from campeonato_eliminatorio where id_temporada in (8475,10131,11787,13571)
);

delete
from rodada_eliminatoria where id_campeonato_misto in (
	select id as id_campeonato_misto from campeonato_misto where id_temporada in (8475,10131,11787,13571)
);

delete from classificacao where id_grupo_campeonato in (9880,9881,9882,9883,9947,9948,9949,9950,11536,11537,11538,11539,11603,11604,11605,11606,13192,13193,13194,13195,13259,13260,13261,13262,14976,14977,14978,14979,15043,15044,15045,15046);
delete from classificacao where id_campeonato in (8501,8653,8805,8957,9109,9261,9413,9565,10157,10309,10461,10613,10765,10917,11069,11221,11813,11965,12117,12269,12421,12573,12725,12877,13597,13749,13901,14053,14205,14357,14509,14661);

delete from grupo_campeonato where id_campeonato_misto in (9869,9936,11525,11592,13181,13248,14965,15032);
delete from campeonato where id_temporada in (8475,10131,11787,13571);
delete from campeonato_eliminatorio where id_temporada in (8475,10131,11787,13571);
delete from campeonato_misto where id_temporada in (8475,10131,11787,13571);
delete from semana where id_temporada in (8475,10131,11787,13571);
delete from temporada where id in (8475,10131,11787,13571);
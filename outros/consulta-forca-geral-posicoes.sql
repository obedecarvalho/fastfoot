select 'ATA', 'MEI', '1', count(*), avg(j.tmp_forca_geral_mei - j.tmp_forca_geral_ata) --255
from jogador j 
where j.posicao = 5 
	and (j.tmp_forca_geral_mei > j.tmp_forca_geral_ata)
union
select 'ATA', 'MEI', '2' , count(*), avg(j.tmp_forca_geral_mei2 - j.tmp_forca_geral_ata2) --301
from jogador j 
where j.posicao = 5 
	and (j.tmp_forca_geral_mei2  > j.tmp_forca_geral_ata2)
;


select 'VOL', 'ZAG', '1' , count(*), avg(j.tmp_forca_geral_zag - j.tmp_forca_geral_vol) --34
from jogador j 
where j.posicao = 3 
		and (j.tmp_forca_geral_zag  > j.tmp_forca_geral_vol)
union
select 'VOL', 'ZAG', '2' , count(*), avg(j.tmp_forca_geral_zag2 - j.tmp_forca_geral_vol2) --1
from jogador j 
where j.posicao = 3 
		and (j.tmp_forca_geral_zag2  > j.tmp_forca_geral_vol2)
;


select 'LAT', 'VOL', '1' , count(*), avg(j.tmp_forca_geral_vol - j.tmp_forca_geral_lat) --267
from jogador j 
where j.posicao = 2 
		and (j.tmp_forca_geral_vol  > j.tmp_forca_geral_lat)
union
select 'LAT', 'VOL', '2' , count(*), avg(j.tmp_forca_geral_vol2 - j.tmp_forca_geral_lat2) --263
from jogador j 
where j.posicao = 2 
		and (j.tmp_forca_geral_vol2  > j.tmp_forca_geral_lat2)
;


select 'ZAG', 'VOL', '1' , count(*), avg(j.tmp_forca_geral_vol - j.tmp_forca_geral_zag)) --264
from jogador j 
where j.posicao = 1 
		and (j.tmp_forca_geral_vol  > j.tmp_forca_geral_zag )
union
select 'ZAG', 'VOL', '2' , count(*), avg(j.tmp_forca_geral_vol2 - j.tmp_forca_geral_zag2) --172
from jogador j 
where j.posicao = 1 
		and (j.tmp_forca_geral_vol2  > j.tmp_forca_geral_zag2)
union
select 'ZAG', 'LAT', '1' , count(*), avg(j.tmp_forca_geral_zag - j.tmp_forca_geral_lat)) --512 (diff:9.379178881189773)
from jogador j 
where j.posicao = 1 
		and (j.tmp_forca_geral_zag  > j.tmp_forca_geral_lat )
union
select 'ZAG', 'LAT', '2', count(*), avg(j.tmp_forca_geral_lat2 - j.tmp_forca_geral_zag2) --NAO TEM
from jogador j 
where j.posicao = 1 
		and (j.tmp_forca_geral_lat2  > j.tmp_forca_geral_zag2)
;

------------------------------------------------------------


select 'ATA', 'MEI', '1' --AQUI
from jogador j 
where j.posicao = 5 
	and (j.tmp_forca_geral_mei > j.tmp_forca_geral_ata or j.tmp_forca_geral_mei2  > j.tmp_forca_geral_ata2)
union
select 'ATA', 'LAT', '2'
from jogador j 
where j.posicao = 5 
		and (j.tmp_forca_geral_lat  > j.tmp_forca_geral_ata or j.tmp_forca_geral_lat2  > j.tmp_forca_geral_ata2)
union
select 'ATA', 'VOL', '3'
from jogador j 
where j.posicao = 5 
		and ( j.tmp_forca_geral_vol  > j.tmp_forca_geral_ata or j.tmp_forca_geral_vol2  > j.tmp_forca_geral_ata2)
union
select 'ATA', 'ZAG', '4'
from jogador j 
where j.posicao = 5 
		and ( j.tmp_forca_geral_zag  > j.tmp_forca_geral_ata or j.tmp_forca_geral_zag2  > j.tmp_forca_geral_ata2)
;

select 'MEI', 'ATA', '1'
from jogador j 
where j.posicao = 4 
	and (j.tmp_forca_geral_ata > j.tmp_forca_geral_mei or j.tmp_forca_geral_ata2  > j.tmp_forca_geral_mei2)
union
select 'MEI', 'LAT', '2'
from jogador j 
where j.posicao = 4 
		and (j.tmp_forca_geral_lat  > j.tmp_forca_geral_mei or j.tmp_forca_geral_lat2  > j.tmp_forca_geral_mei2)
union
select 'MEI', 'VOL', '3'
from jogador j 
where j.posicao = 4 
		and ( j.tmp_forca_geral_vol  > j.tmp_forca_geral_mei or j.tmp_forca_geral_vol2  > j.tmp_forca_geral_mei2)
union
select 'MEI', 'ZAG', '4'
from jogador j 
where j.posicao = 4 
		and ( j.tmp_forca_geral_zag  > j.tmp_forca_geral_mei or j.tmp_forca_geral_zag2  > j.tmp_forca_geral_mei2)
;

select 'VOL', 'ATA', '1'
from jogador j 
where j.posicao = 3 
	and (j.tmp_forca_geral_ata > j.tmp_forca_geral_vol or j.tmp_forca_geral_ata2  > j.tmp_forca_geral_vol2)
union
select 'VOL', 'LAT', '2'
from jogador j 
where j.posicao = 3 
		and (j.tmp_forca_geral_lat  > j.tmp_forca_geral_vol or j.tmp_forca_geral_lat2  > j.tmp_forca_geral_vol2)
union
select 'VOL', 'MEI', '3'
from jogador j 
where j.posicao = 3 
		and ( j.tmp_forca_geral_mei  > j.tmp_forca_geral_vol or j.tmp_forca_geral_mei2  > j.tmp_forca_geral_vol2)
union
select 'VOL', 'ZAG', '4' --AQUI
from jogador j 
where j.posicao = 3 
		and ( j.tmp_forca_geral_zag  > j.tmp_forca_geral_vol or j.tmp_forca_geral_zag2  > j.tmp_forca_geral_vol2)
;

select 'LAT', 'ATA', '1'
from jogador j 
where j.posicao = 2 
	and (j.tmp_forca_geral_ata > j.tmp_forca_geral_lat or j.tmp_forca_geral_ata2  > j.tmp_forca_geral_lat2)
union
select 'LAT', 'VOL', '2' --AQUI
from jogador j 
where j.posicao = 2 
		and (j.tmp_forca_geral_vol  > j.tmp_forca_geral_lat or j.tmp_forca_geral_vol2  > j.tmp_forca_geral_lat2)
union
select 'LAT', 'MEI', '3'
from jogador j 
where j.posicao = 2 
		and ( j.tmp_forca_geral_mei  > j.tmp_forca_geral_lat or j.tmp_forca_geral_mei2  > j.tmp_forca_geral_lat2)
union
select 'LAT', 'ZAG', '4' 
from jogador j 
where j.posicao = 2 
		and ( j.tmp_forca_geral_zag  > j.tmp_forca_geral_lat or j.tmp_forca_geral_zag2  > j.tmp_forca_geral_lat2)
;

select 'ZAG', 'ATA', '1'
from jogador j 
where j.posicao = 1 
	and (j.tmp_forca_geral_ata > j.tmp_forca_geral_zag or j.tmp_forca_geral_ata2  > j.tmp_forca_geral_zag2)
union
select 'ZAG', 'VOL', '2' --AQUI
from jogador j 
where j.posicao = 1 
		and (j.tmp_forca_geral_vol  > j.tmp_forca_geral_zag or j.tmp_forca_geral_vol2  > j.tmp_forca_geral_zag2)
union
select 'ZAG', 'MEI', '3'
from jogador j 
where j.posicao = 1 
		and ( j.tmp_forca_geral_mei  > j.tmp_forca_geral_zag or j.tmp_forca_geral_mei2  > j.tmp_forca_geral_zag2)
union
select 'ZAG', 'LAT', '4' --AQUI
from jogador j 
where j.posicao = 1 
		and ( j.tmp_forca_geral_zag  > j.tmp_forca_geral_lat or j.tmp_forca_geral_lat2  > j.tmp_forca_geral_zag2)
;

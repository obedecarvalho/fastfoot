package com.fastfoot.club.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.scheduler.model.ClassificacaoNacional;

@Repository
public interface ClubeRepository extends JpaRepository<Clube, Long>{
	
	@Query("SELECT cr.clube FROM ClubeRanking cr WHERE cr.clube.ligaJogo = :ligaJogo AND cr.posicaoGeral BETWEEN :menor AND :maior AND cr.ano = :ano ORDER BY cr.posicaoGeral ")
	public List<Clube> findByLigaJogoAndAnoAndPosicaoGeralBetween(@Param("ligaJogo") LigaJogo liga, @Param("ano") Integer ano,
			@Param("menor") Integer menor, @Param("maior") Integer maior);
	
	@Query("SELECT cr.clube FROM ClubeRanking cr WHERE cr.clube.ligaJogo = :ligaJogo AND cr.classificacaoNacional IN :classNac AND cr.ano = :ano ORDER BY cr.classificacaoNacional ")
	public List<Clube> findByLigaJogoAndAnoAndClassificacaoNacionalBetween(@Param("ligaJogo") LigaJogo liga,
			@Param("ano") Integer ano, @Param("classNac") ClassificacaoNacional[] classNac);
	
	@Query("SELECT max(c.id) FROM Clube c")
	public Optional<Long> findLastId();
	
	public List<Clube> findByLigaJogo(LigaJogo ligaJogo);
	
	@Query("SELECT c FROM Clube c WHERE c.ligaJogo.jogo = :jogo")
	public List<Clube> findByJogo(@Param("jogo") Jogo jogo);

	//###	INSERT, UPDATE E DELETE	###

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" update clube " +
			" set forca_geral_atual = tmp.forca_atual " +
			" from ( " +
			" 	select jog.id_clube, floor(avg(jog.forca_geral)) as forca_atual " +
			" 	from ( " +
			" 		select " +
			" 			j.id_clube, " +
			" 			j.posicao, " +
			" 			j.forca_geral, " +
			" 			j.numero, " +
			" 			j.idade, " +
			" 			row_number() over (partition by j.id_clube, j.posicao order by j.forca_geral desc) as ordem " +
			" 		from jogador j " +
			" 		inner join clube c on c.id = j.id_clube " +
			" 		inner join liga_jogo lj on c.id_liga_jogo = lj.id " +
			" 		where j.status_jogador = 0 " + //StatusJogador.ATIVO
			" 			and lj.id_jogo = ?1 " +
			" 	) as jog " +
			" 	where  " +
			" 		(jog.posicao = 'G' and ordem = 1) " + //Posicao.GOLEIRO
			" 		or (jog.posicao <> 'G' and jog.ordem <= 2) " + //Posicao.GOLEIRO
			" 	group by jog.id_clube " +
			" ) as tmp " +
			" where clube.id = tmp.id_clube "
	)
	public void calcularForcaGeral(Long idJogo);

	//###	/INSERT, UPDATE E DELETE	###
}

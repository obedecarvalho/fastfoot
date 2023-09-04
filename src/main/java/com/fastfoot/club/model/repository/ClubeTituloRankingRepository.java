package com.fastfoot.club.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeTituloRanking;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;

@Repository
public interface ClubeTituloRankingRepository extends JpaRepository<ClubeTituloRanking, Long> {

	public List<ClubeTituloRanking> findByClube(Clube clube);

	@Query("SELECT crt FROM ClubeTituloRanking crt WHERE crt.clube.ligaJogo = :ligaJogo")
	public List<ClubeTituloRanking> findByLigaJogo(@Param("ligaJogo") LigaJogo ligaJogo);

	@Query("SELECT ctr FROM ClubeTituloRanking ctr WHERE ctr.clube.ligaJogo.jogo = :jogo ")
	public List<ClubeTituloRanking> findByJogo(@Param("jogo") Jogo jogo);
}

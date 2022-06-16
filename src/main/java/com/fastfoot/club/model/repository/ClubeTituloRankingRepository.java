package com.fastfoot.club.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.ClubeTituloRanking;
import com.fastfoot.model.Liga;
import com.fastfoot.model.entity.Clube;

@Repository
public interface ClubeTituloRankingRepository extends JpaRepository<ClubeTituloRanking, Long> {

	public List<ClubeTituloRanking> findByClube(Clube clube);

	@Query("SELECT crt FROM ClubeTituloRanking crt WHERE crt.clube.liga = :liga")
	public List<ClubeTituloRanking> findByLiga(@Param("liga") Liga liga);

}

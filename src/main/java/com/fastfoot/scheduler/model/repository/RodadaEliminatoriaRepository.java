package com.fastfoot.scheduler.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Semana;

@Repository
public interface RodadaEliminatoriaRepository extends JpaRepository<RodadaEliminatoria, Long>{

	@Query("SELECT re FROM RodadaEliminatoria re WHERE re.campeonatoEliminatorio = :campeonatoEliminatorio AND re.numero = :numero ")
	public List<RodadaEliminatoria> findByCampeonatoEliminatorioAndNumero(@Param("campeonatoEliminatorio") CampeonatoEliminatorio campeonatoEliminatorio, @Param("numero") Integer numero);

	@Query("SELECT re FROM RodadaEliminatoria re WHERE re.campeonatoMisto = :campeonatoMisto AND re.numero = :numero ")
	public List<RodadaEliminatoria> findByCampeonatoMistoAndNumero(@Param("campeonatoMisto") CampeonatoMisto campeonatoMisto, @Param("numero") Integer numero);
	
	public List<RodadaEliminatoria> findBySemana(Semana semana);

	public List<RodadaEliminatoria> findByCampeonatoEliminatorio(CampeonatoEliminatorio campeonatoEliminatorio);
	
	public List<RodadaEliminatoria> findByCampeonatoMisto(CampeonatoMisto campeonatoMisto);

	public List<RodadaEliminatoria> findByProximaRodada(RodadaEliminatoria proximaRodada);

	public Optional<RodadaEliminatoria> findFirstByCampeonatoMistoAndNumero(CampeonatoMisto campeonatoMisto, Integer numero);

	public Optional<RodadaEliminatoria> findFirstByCampeonatoEliminatorioAndNumero(CampeonatoEliminatorio campeonatoEliminatorio, Integer numero);
}

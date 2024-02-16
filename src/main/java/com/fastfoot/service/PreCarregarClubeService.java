package com.fastfoot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fastfoot.club.model.ClubeNivel;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.entity.ClubeTituloRanking;
import com.fastfoot.club.model.entity.Treinador;
import com.fastfoot.club.model.entity.TreinadorTituloRanking;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceira;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.player.service.util.NomeUtil;
import com.fastfoot.scheduler.model.ClassificacaoContinental;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacional;
import com.fastfoot.scheduler.model.ClassificacaoNacional;
import com.fastfoot.scheduler.model.NivelCampeonato;

/**
 * Pre configurar dados
 * @author obede
 *
 */

@Service
public class PreCarregarClubeService {
	
	public static final Integer FORCA_NIVEL_1 = 90;
	
	public static final Integer FORCA_NIVEL_2 = 87;
	
	public static final Integer FORCA_NIVEL_3 = 84;
	
	public static final Integer FORCA_NIVEL_4 = 81;
	
	public static final Integer FORCA_NIVEL_5 = 78;
	
	public static final Integer FORCA_NIVEL_6 = 74;
	
	public static final Integer FORCA_NIVEL_7 = 70;

	public List<Clube> inicializarClubesPadraoGENEBE(LigaJogo liga) {
		
		List<Clube> clubes = new ArrayList<Clube>();

		clubes.add(new Clube(liga.getIdClubeInicial() + 0, liga, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Bayern Munich", "bayern_munich.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 1, liga, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Ajax", "ajax.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 2, liga, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Borussia Dortmund", "borussia_dortmund.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 3, liga, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Bayer Leverkusen", "bayer_leverkusen.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 4, liga, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "PSV", "psv.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 5, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "RB Leipzig", "rb_leipzig.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 6, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Wolfsburg", "wolfsburg.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 7, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Borussia M'gladbach", "borussia_monchengladbach.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 8, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Club Brugge", "club_brugge.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 9, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Eintracht Frankfurt", "eintracht_frankfurt.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 10, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Freiburg", "freiburg.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 11, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Hoffenheim", "hoffenheim.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 12, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Feyenoord", "feyenoord.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 13, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "AZ Alkmaar", "az_alkmaar.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 14, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Union Berlin", "union_berlin.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 15, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Mainz 05", "mainz_05.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 16, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "FC Koln", "fc_koln.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 17, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Schalke 04", "schalke_04.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 18, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Hertha Berlin", "hertha_berlin.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 19, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "VfB Stuttgart", "vfb_stuttgart.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 20, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Vitesse", "vitesse.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 21, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Fortuna Düsseldorf", "fortuna_dusseldorf.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 22, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Hamburgo", "hamburgo.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 23, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Utrecht", "utrecht.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 24, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Gent", "gent.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 25, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Antwerp", "antwerp.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 26, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Arminia Bielefeld", "arminia_bielefeld.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 27, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Augsburg", "augsburg.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 28, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Anderlecht", "anderlecht.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 29, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Bochum", "bochum.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 30, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Racing Genk", "racing_genk.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 31, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Werder Bremen", "werder_bremen.png"));
		
		criarTreinador(liga.getJogo(), clubes);
		
		return clubes;
	}

	public List<Clube> inicializarClubesPadraoSPAPOR(LigaJogo liga) {
		
		List<Clube> clubes = new ArrayList<Clube>();
			
		clubes.add(new Clube(liga.getIdClubeInicial() + 0, liga, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Real Madrid", "real_madrid.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 1, liga, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Barcelona", "barcelona.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 2, liga, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Porto", "porto.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 3, liga, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Atlético Madrid", "atletico_madrid.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 4, liga, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Benfica", "benfica.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 5, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Sevilla", "sevilla.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 6, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Sporting", "sporting.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 7, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Real Betis", "real_betis.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 8, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Valencia", "valencia.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 9, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Real Sociedad", "real_sociedad.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 10, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Villarreal", "villarreal.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 11, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Athletic Bilbao", "athletic_bilbao.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 12, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Getafe", "getafe.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 13, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Celta de Vigo", "celta_de_vigo.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 14, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Rayo Vallecano", "rayo_vallecano.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 15, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Osasuna", "osasuna.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 16, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Real Valladolid", "real_valladolid.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 17, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Granada", "granada.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 18, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Espanyol", "espanyol.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 19, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Mallorca", "mallorca.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 20, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Almería", "almeria.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 21, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Alaves", "alaves.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 22, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Elche", "elche.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 23, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Braga", "braga.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 24, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Levante", "levante.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 25, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Cádiz", "cadiz.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 26, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Leganes", "leganes.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 27, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Sporting Gijón", "sporting_gijon.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 28, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Girona", "girona.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 29, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Eibar", "eibar.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 30, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Zaragoza", "zaragoza.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 31, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Málaga", "malaga.png"));
		
		criarTreinador(liga.getJogo(), clubes);
		
		return clubes;
	}

	public List<Clube> inicializarClubesPadraoITAFRA(LigaJogo liga) {
		
		List<Clube> clubes = new ArrayList<Clube>();
			
		clubes.add(new Clube(liga.getIdClubeInicial() + 0, liga, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Internazionale", "internazionale.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 1, liga, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Juventus", "juventus.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 2, liga, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Paris Saint-Germain", "paris_saint_germain.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 3, liga, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Napoli", "napoli.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 4, liga, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Milan", "milan.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 5, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Atalanta", "atalanta.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 6, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Olympique de Marseille", "olympique_de_marseille.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 7, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Lazio", "lazio.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 8, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Lyon", "lyon.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 9, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Roma", "roma.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 10, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Lille", "lille.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 11, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Monaco", "monaco.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 12, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Fiorentina", "fiorentina.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 13, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Sassuolo", "sassuolo.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 14, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Rennes", "rennes.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 15, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Verona", "verona.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 16, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "RC Strasbourg", "rc_strasbourg.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 17, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Montpellier", "montpellier.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 18, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Bologna", "bologna.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 19, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Nice", "nice.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 20, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Lens", "lens.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 21, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Torino", "torino.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 22, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Udinese", "udinese.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 23, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Empoli", "empoli.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 24, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Sampdoria", "sampdoria.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 25, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Stade Brestois", "stade_brestois.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 26, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Nantes", "nantes.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 27, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Saint-Étienne", "saint_etienne.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 28, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Bordeaux", "bordeaux.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 29, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Reims", "reims.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 30, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Genoa", "genoa.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 31, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Venezia", "venezia.png"));
		
		criarTreinador(liga.getJogo(), clubes);
		
		return clubes;
	}
	
	public List<Clube> inicializarClubesPadraoENGLND(LigaJogo liga) {
		
		List<Clube> clubes = new ArrayList<Clube>();
			
		clubes.add(new Clube(liga.getIdClubeInicial() + 0, liga, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Liverpool", "liverpool.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 1, liga, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Manchester United", "manchester_united.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 2, liga, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Manchester City", "manchester_city.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 3, liga, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Chelsea", "chelsea.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 4, liga, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Arsenal", "arsenal.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 5, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "West Ham", "west_ham.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 6, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Tottenham", "tottenham.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 7, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Leicester City", "leicester_city.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 8, liga, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Aston Villa", "aston_villa.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 9, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Rangers", "rangers.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 10, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Wolves", "wolves.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 11, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Crystal Palace", "crystal_palace.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 12, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Everton", "everton.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 13, liga, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Newcastle", "newcastle.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 14, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Leeds United", "leeds_united.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 15, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Brighton", "brighton.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 16, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Southampton", "southampton.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 17, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Burnley", "burnley.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 18, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Watford", "watford.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 19, liga, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Celtic", "celtic.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 20, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Norwich City", "norwich_city.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 21, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Brentford", "brentford.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 22, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Fulham", "fulham.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 23, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Bournemouth", "bournemouth.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 24, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Sheffield United", "sheffield_united.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 25, liga, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Blackburn Rovers", "blackburn_rovers.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 26, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Nottingham Forest", "nottingham_forest.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 27, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Hull City", "hull_city.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 28, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Stoke City", "stoke_city.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 29, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Swansea City", "swansea_city.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 30, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "West Bromwich", "west_bromwich.png"));
		clubes.add(new Clube(liga.getIdClubeInicial() + 31, liga, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Blackpool", "blackpool.png"));
		
		criarTreinador(liga.getJogo(), clubes);
		
		return clubes;
	}

	public List<ClubeRanking> inicializarClubesRankingPorLiga(List<Clube> clubes) {
		
		List<ClubeRanking> ranking = new ArrayList<ClubeRanking>();
		
		int ano = Constantes.ANO_INICIAL - 1;
		
		for (int i = 1; i <= Constantes.NRO_CLUBES_POR_LIGA; i++) {
			ranking.add(new ClubeRanking(clubes.get(i - 1), ano, i, posicaoToClassificacaoNacional(i),
					ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
		}
		
		return ranking;
	}
	
	public List<ClubeTituloRanking> inicializarClubeTituloRankingPorLiga(List<Clube> clubes) {
		List<ClubeTituloRanking> rankingTitulos = new ArrayList<ClubeTituloRanking>();
		
		for (int i = 1; i <= Constantes.NRO_CLUBES_POR_LIGA; i++) {
			rankingTitulos.add(new ClubeTituloRanking(clubes.get(i - 1).getId(), clubes.get(i - 1)));
		}

		return rankingTitulos;
	}
	
	public List<MovimentacaoFinanceira> inicializarCaixaInicial(List<Clube> clubes) {

		List<MovimentacaoFinanceira> movimentacoesFinanceiras = new ArrayList<MovimentacaoFinanceira>();

		for (Clube c : clubes) {
			movimentacoesFinanceiras.add(new MovimentacaoFinanceira(c, null, TipoMovimentacaoFinanceira.CAIXA_INICIAL,
					c.getNivelNacional().getCaixaInicial(), "Caixa Inicial"));
		}

		return movimentacoesFinanceiras;
	}
	
	public void criarTreinador(Jogo jogo, List<Clube> clubes) {
		
		Treinador treinador = null;
		TreinadorTituloRanking treinadorTituloRanking = null;
		
		for (Clube c : clubes) {
			treinador = new Treinador();
			//treinador.setClube(c);
			treinador.setNome(NomeUtil.sortearNome());
			treinador.setJogo(jogo);
			treinador.setAtivo(true);
			
			treinadorTituloRanking = new TreinadorTituloRanking(treinador);
			treinador.setTreinadorTituloRanking(treinadorTituloRanking);

			c.setTreinador(treinador);
		}

	}

	private ClassificacaoNacional posicaoToClassificacaoNacional(Integer pos) {
		return ClassificacaoNacional.getClassificacao(pos > 16 ? NivelCampeonato.NACIONAL_II : NivelCampeonato.NACIONAL,
				pos % 16 == 0 ? 16 : pos % 16);
	}
}

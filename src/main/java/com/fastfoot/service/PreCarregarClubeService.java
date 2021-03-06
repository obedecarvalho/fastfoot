package com.fastfoot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeTituloRanking;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.model.repository.ClubeTituloRankingRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.ClassificacaoContinentalFinal;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacionalFinal;
import com.fastfoot.scheduler.model.ClassificacaoNacionalFinal;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.ClubeRanking;
import com.fastfoot.scheduler.model.repository.ClubeRankingRepository;

/**
 * Pre configurar dados
 * @author obede
 *
 */

@Service
public class PreCarregarClubeService {
	
	private static final Integer FORCA_NIVEL_1 = 85;
	
	private static final Integer FORCA_NIVEL_2 = 82;
	
	private static final Integer FORCA_NIVEL_3 = 78;
	
	private static final Integer FORCA_NIVEL_4 = 75;
	
	private static final Integer FORCA_NIVEL_5 = 71;
	
	private static final Integer FORCA_NIVEL_6 = 68;
	
	private static final Integer FORCA_NIVEL_7 = 64;
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private ClubeRankingRepository clubeRankingRepository;

	@Autowired
	private ClubeTituloRankingRepository clubeTituloRankingRepository;

	public void preCarregarClubes () {
		inserirClubes();
		inserirClubesRanking();
		inserirClubeTituloRanking();
	}

	private void inserirClubes() {
		
		if (clubeRepository.findAll().size() == 0) {
		
			List<Clube> clubes = new ArrayList<Clube>();
	
			clubes.add(new Clube(101, Liga.GENEBE, FORCA_NIVEL_1, /*"Bayern München"*/ "Bayern Munich"));
			clubes.add(new Clube(102, Liga.GENEBE, FORCA_NIVEL_1, "Ajax"));
			clubes.add(new Clube(103, Liga.GENEBE, FORCA_NIVEL_2, "Borussia Dortmund"));
			clubes.add(new Clube(104, Liga.GENEBE, FORCA_NIVEL_2, "Bayer Leverkusen"));
			clubes.add(new Clube(105, Liga.GENEBE, FORCA_NIVEL_2, "PSV"));
			clubes.add(new Clube(106, Liga.GENEBE, FORCA_NIVEL_3, "RB Leipzig"));
			clubes.add(new Clube(107, Liga.GENEBE, FORCA_NIVEL_3, "Wolfsburg"));
			clubes.add(new Clube(108, Liga.GENEBE, FORCA_NIVEL_3, "Borussia Mönchengladbach"));
			clubes.add(new Clube(109, Liga.GENEBE, FORCA_NIVEL_3, "Club Brugge"));
			clubes.add(new Clube(110, Liga.GENEBE, FORCA_NIVEL_4, "Eintracht Frankfurt"));
			clubes.add(new Clube(111, Liga.GENEBE, FORCA_NIVEL_4, "Freiburg"));
			clubes.add(new Clube(112, Liga.GENEBE, FORCA_NIVEL_4, "Hoffenheim"));
			clubes.add(new Clube(113, Liga.GENEBE, FORCA_NIVEL_4, "Feyenoord"));
			clubes.add(new Clube(114, Liga.GENEBE, FORCA_NIVEL_4, "AZ Alkmaar"));
			clubes.add(new Clube(115, Liga.GENEBE, FORCA_NIVEL_5, "Union Berlin"));
			clubes.add(new Clube(116, Liga.GENEBE, FORCA_NIVEL_5, "Mainz 05"));
			clubes.add(new Clube(117, Liga.GENEBE, FORCA_NIVEL_5, /*"Colônia"*/ "FC Koln"));
			clubes.add(new Clube(118, Liga.GENEBE, FORCA_NIVEL_5, "Schalke 04"));
			clubes.add(new Clube(119, Liga.GENEBE, FORCA_NIVEL_5, "Hertha Berlin"));
			clubes.add(new Clube(120, Liga.GENEBE, FORCA_NIVEL_5, "VfB Stuttgart"));
			clubes.add(new Clube(121, Liga.GENEBE, FORCA_NIVEL_6, "Vitesse"));
			clubes.add(new Clube(122, Liga.GENEBE, FORCA_NIVEL_6, "Fortuna Düsseldorf"));
			clubes.add(new Clube(123, Liga.GENEBE, FORCA_NIVEL_6, "Hamburgo"));
			clubes.add(new Clube(124, Liga.GENEBE, FORCA_NIVEL_6, "Utrecht"));
			clubes.add(new Clube(125, Liga.GENEBE, FORCA_NIVEL_6, "Gent"));
			clubes.add(new Clube(126, Liga.GENEBE, FORCA_NIVEL_6, "Antwerp"));
			clubes.add(new Clube(127, Liga.GENEBE, FORCA_NIVEL_7, "Arminia Bielefeld"));
			clubes.add(new Clube(128, Liga.GENEBE, FORCA_NIVEL_7, "Augsburg"));
			clubes.add(new Clube(129, Liga.GENEBE, FORCA_NIVEL_7, "Anderlecht"));
			clubes.add(new Clube(130, Liga.GENEBE, FORCA_NIVEL_7, "Bochum"));
			clubes.add(new Clube(131, Liga.GENEBE, FORCA_NIVEL_7, "Racing Genk"));
			clubes.add(new Clube(132, Liga.GENEBE, FORCA_NIVEL_7, "Werder Bremen"));
			clubes.add(new Clube(201, Liga.SPAPOR, FORCA_NIVEL_1, "Real Madrid"));
			clubes.add(new Clube(202, Liga.SPAPOR, FORCA_NIVEL_1, "Barcelona"));
			clubes.add(new Clube(203, Liga.SPAPOR, FORCA_NIVEL_2, "Porto"));
			clubes.add(new Clube(204, Liga.SPAPOR, FORCA_NIVEL_2, "Atlético Madrid"));
			clubes.add(new Clube(205, Liga.SPAPOR, FORCA_NIVEL_2, "Benfica"));
			clubes.add(new Clube(206, Liga.SPAPOR, FORCA_NIVEL_3, "Sevilla"));
			clubes.add(new Clube(207, Liga.SPAPOR, FORCA_NIVEL_3, "Sporting"));
			clubes.add(new Clube(208, Liga.SPAPOR, FORCA_NIVEL_3, "Real Betis"));
			clubes.add(new Clube(209, Liga.SPAPOR, FORCA_NIVEL_3, "Valencia"));
			clubes.add(new Clube(210, Liga.SPAPOR, FORCA_NIVEL_4, "Real Sociedad"));
			clubes.add(new Clube(211, Liga.SPAPOR, FORCA_NIVEL_4, "Villarreal"));
			clubes.add(new Clube(212, Liga.SPAPOR, FORCA_NIVEL_4, "Athletic Bilbao"));
			clubes.add(new Clube(213, Liga.SPAPOR, FORCA_NIVEL_4, "Getafe"));
			clubes.add(new Clube(214, Liga.SPAPOR, FORCA_NIVEL_4, "Celta de Vigo"));
			clubes.add(new Clube(215, Liga.SPAPOR, FORCA_NIVEL_5, "Rayo Vallecano"));
			clubes.add(new Clube(216, Liga.SPAPOR, FORCA_NIVEL_5, "Osasuna"));
			clubes.add(new Clube(217, Liga.SPAPOR, FORCA_NIVEL_5, "Real Valladolid"));
			clubes.add(new Clube(218, Liga.SPAPOR, FORCA_NIVEL_5, "Granada"));
			clubes.add(new Clube(219, Liga.SPAPOR, FORCA_NIVEL_5, "Espanyol"));
			clubes.add(new Clube(220, Liga.SPAPOR, FORCA_NIVEL_5, "Mallorca"));
			clubes.add(new Clube(221, Liga.SPAPOR, FORCA_NIVEL_6, "Almería"));
			clubes.add(new Clube(222, Liga.SPAPOR, FORCA_NIVEL_6, "Alaves"));
			clubes.add(new Clube(223, Liga.SPAPOR, FORCA_NIVEL_6, "Elche"));
			clubes.add(new Clube(224, Liga.SPAPOR, FORCA_NIVEL_6, "Braga"));
			clubes.add(new Clube(225, Liga.SPAPOR, FORCA_NIVEL_6, "Levante"));
			clubes.add(new Clube(226, Liga.SPAPOR, FORCA_NIVEL_6, "Cádiz"));
			clubes.add(new Clube(227, Liga.SPAPOR, FORCA_NIVEL_7, "Leganes"));
			clubes.add(new Clube(228, Liga.SPAPOR, FORCA_NIVEL_7, "Sporting Gijón"));
			clubes.add(new Clube(229, Liga.SPAPOR, FORCA_NIVEL_7, "Girona"));
			clubes.add(new Clube(230, Liga.SPAPOR, FORCA_NIVEL_7, "Eibar"));
			clubes.add(new Clube(231, Liga.SPAPOR, FORCA_NIVEL_7, "Zaragoza"));
			clubes.add(new Clube(232, Liga.SPAPOR, FORCA_NIVEL_7, "Málaga"));
			clubes.add(new Clube(301, Liga.ITAFRA, FORCA_NIVEL_1, "Internazionale" /*"Inter Milan"*/));
			clubes.add(new Clube(302, Liga.ITAFRA, FORCA_NIVEL_1, "Juventus"));
			clubes.add(new Clube(303, Liga.ITAFRA, FORCA_NIVEL_2, "Paris Saint-Germain"));
			clubes.add(new Clube(304, Liga.ITAFRA, FORCA_NIVEL_2, "Napoli"));
			clubes.add(new Clube(305, Liga.ITAFRA, FORCA_NIVEL_2, "Milan"));
			clubes.add(new Clube(306, Liga.ITAFRA, FORCA_NIVEL_3, "Atalanta"));
			clubes.add(new Clube(307, Liga.ITAFRA, FORCA_NIVEL_3, "Olympique de Marseille"));
			clubes.add(new Clube(308, Liga.ITAFRA, FORCA_NIVEL_3, "Lazio"));
			clubes.add(new Clube(309, Liga.ITAFRA, FORCA_NIVEL_3, "Lyon"));
			clubes.add(new Clube(310, Liga.ITAFRA, FORCA_NIVEL_4, "Roma"));
			clubes.add(new Clube(311, Liga.ITAFRA, FORCA_NIVEL_4, "Lille"));
			clubes.add(new Clube(312, Liga.ITAFRA, FORCA_NIVEL_4, "Monaco"));
			clubes.add(new Clube(313, Liga.ITAFRA, FORCA_NIVEL_4, "Fiorentina"));
			clubes.add(new Clube(314, Liga.ITAFRA, FORCA_NIVEL_4, "Sassuolo"));
			clubes.add(new Clube(315, Liga.ITAFRA, FORCA_NIVEL_5, "Rennes"));
			clubes.add(new Clube(316, Liga.ITAFRA, FORCA_NIVEL_5, "Verona"));
			clubes.add(new Clube(317, Liga.ITAFRA, FORCA_NIVEL_5, "RC Strasbourg"));
			clubes.add(new Clube(318, Liga.ITAFRA, FORCA_NIVEL_5, "Montpellier"));
			clubes.add(new Clube(319, Liga.ITAFRA, FORCA_NIVEL_5, "Bologna"));
			clubes.add(new Clube(320, Liga.ITAFRA, FORCA_NIVEL_5, "Nice"));
			clubes.add(new Clube(321, Liga.ITAFRA, FORCA_NIVEL_6, "Lens"));
			clubes.add(new Clube(322, Liga.ITAFRA, FORCA_NIVEL_6, "Torino"));
			clubes.add(new Clube(323, Liga.ITAFRA, FORCA_NIVEL_6, "Udinese"));
			clubes.add(new Clube(324, Liga.ITAFRA, FORCA_NIVEL_6, "Empoli"));
			clubes.add(new Clube(325, Liga.ITAFRA, FORCA_NIVEL_6, "Sampdoria"));
			clubes.add(new Clube(326, Liga.ITAFRA, FORCA_NIVEL_6, "Stade Brestois"));
			clubes.add(new Clube(327, Liga.ITAFRA, FORCA_NIVEL_7, "Nantes"));
			clubes.add(new Clube(328, Liga.ITAFRA, FORCA_NIVEL_7, "Saint-Étienne"));
			clubes.add(new Clube(329, Liga.ITAFRA, FORCA_NIVEL_7, "Bordeaux"));
			clubes.add(new Clube(330, Liga.ITAFRA, FORCA_NIVEL_7, "Reims"));
			clubes.add(new Clube(331, Liga.ITAFRA, FORCA_NIVEL_7, "Genoa"));
			clubes.add(new Clube(332, Liga.ITAFRA, FORCA_NIVEL_7, "Venezia"));
			clubes.add(new Clube(401, Liga.ENGLND, FORCA_NIVEL_1, "Liverpool"));
			clubes.add(new Clube(402, Liga.ENGLND, FORCA_NIVEL_1, "Manchester United"));
			clubes.add(new Clube(403, Liga.ENGLND, FORCA_NIVEL_2, "Manchester City"));
			clubes.add(new Clube(404, Liga.ENGLND, FORCA_NIVEL_2, "Chelsea"));
			clubes.add(new Clube(405, Liga.ENGLND, FORCA_NIVEL_2, "Arsenal"));
			clubes.add(new Clube(406, Liga.ENGLND, FORCA_NIVEL_3, "West Ham"));
			clubes.add(new Clube(407, Liga.ENGLND, FORCA_NIVEL_3, "Tottenham"));
			clubes.add(new Clube(408, Liga.ENGLND, FORCA_NIVEL_3, "Leicester City"));
			clubes.add(new Clube(409, Liga.ENGLND, FORCA_NIVEL_3, "Aston Villa"));
			clubes.add(new Clube(410, Liga.ENGLND, FORCA_NIVEL_4, "Rangers"));
			clubes.add(new Clube(411, Liga.ENGLND, FORCA_NIVEL_4, /*"Wolverhampton Wanderers"*/ "Wolves"));
			clubes.add(new Clube(412, Liga.ENGLND, FORCA_NIVEL_4, "Crystal Palace"));
			clubes.add(new Clube(413, Liga.ENGLND, FORCA_NIVEL_4, "Everton"));
			clubes.add(new Clube(414, Liga.ENGLND, FORCA_NIVEL_4, "Newcastle"));
			clubes.add(new Clube(415, Liga.ENGLND, FORCA_NIVEL_5, "Leeds United"));
			clubes.add(new Clube(416, Liga.ENGLND, FORCA_NIVEL_5, "Brighton"));
			clubes.add(new Clube(417, Liga.ENGLND, FORCA_NIVEL_5, "Southampton"));
			clubes.add(new Clube(418, Liga.ENGLND, FORCA_NIVEL_5, "Burnley"));
			clubes.add(new Clube(419, Liga.ENGLND, FORCA_NIVEL_5, "Watford"));
			clubes.add(new Clube(420, Liga.ENGLND, FORCA_NIVEL_5, "Celtic"));
			clubes.add(new Clube(421, Liga.ENGLND, FORCA_NIVEL_6, "Norwich City"));
			clubes.add(new Clube(422, Liga.ENGLND, FORCA_NIVEL_6, "Brentford"));
			clubes.add(new Clube(423, Liga.ENGLND, FORCA_NIVEL_6, "Fulham"));
			clubes.add(new Clube(424, Liga.ENGLND, FORCA_NIVEL_6, "Bournemouth"));
			clubes.add(new Clube(425, Liga.ENGLND, FORCA_NIVEL_6, "Sheffield United"));
			clubes.add(new Clube(426, Liga.ENGLND, FORCA_NIVEL_6, "Blackburn Rovers"));
			clubes.add(new Clube(427, Liga.ENGLND, FORCA_NIVEL_7, "Nottingham Forest"));
			clubes.add(new Clube(428, Liga.ENGLND, FORCA_NIVEL_7, "Hull City"));
			clubes.add(new Clube(429, Liga.ENGLND, FORCA_NIVEL_7, "Stoke City"));
			clubes.add(new Clube(430, Liga.ENGLND, FORCA_NIVEL_7, "Swansea City"));
			clubes.add(new Clube(431, Liga.ENGLND, FORCA_NIVEL_7, "West Bromwich"));
			clubes.add(new Clube(432, Liga.ENGLND, FORCA_NIVEL_7, "Blackpool"));

			/*
			clubes.add(new Clube(501, Liga.BRASIL, FORCA_NIVEL_1, "Palmeiras"));
			clubes.add(new Clube(502, Liga.BRASIL, FORCA_NIVEL_1, "Atlético"));
			clubes.add(new Clube(503, Liga.BRASIL, FORCA_NIVEL_2, "Flamengo"));
			clubes.add(new Clube(504, Liga.BRASIL, FORCA_NIVEL_2, "Athlético"));
			clubes.add(new Clube(505, Liga.BRASIL, FORCA_NIVEL_2, "Corinthians"));
			clubes.add(new Clube(506, Liga.BRASIL, FORCA_NIVEL_3, "Santos"));
			clubes.add(new Clube(507, Liga.BRASIL, FORCA_NIVEL_3, "Fluminense"));
			clubes.add(new Clube(508, Liga.BRASIL, FORCA_NIVEL_3, "Internacional"));
			clubes.add(new Clube(509, Liga.BRASIL, FORCA_NIVEL_3, "São Paulo"));
			clubes.add(new Clube(510, Liga.BRASIL, FORCA_NIVEL_4, "Fortaleza"));
			clubes.add(new Clube(511, Liga.BRASIL, FORCA_NIVEL_4, "Botafogo"));
			clubes.add(new Clube(512, Liga.BRASIL, FORCA_NIVEL_4, "Ceará"));
			clubes.add(new Clube(513, Liga.BRASIL, FORCA_NIVEL_4, "América"));
			clubes.add(new Clube(514, Liga.BRASIL, FORCA_NIVEL_4, "Red Bull Bragantino"));
			clubes.add(new Clube(515, Liga.BRASIL, FORCA_NIVEL_5, "Bahia"));
			clubes.add(new Clube(516, Liga.BRASIL, FORCA_NIVEL_5, "Atlético - GO"));
			clubes.add(new Clube(517, Liga.BRASIL, FORCA_NIVEL_5, "Goiás"));
			clubes.add(new Clube(518, Liga.BRASIL, FORCA_NIVEL_5, "Cuiabá"));
			clubes.add(new Clube(519, Liga.BRASIL, FORCA_NIVEL_5, "Cruzeiro"));
			clubes.add(new Clube(520, Liga.BRASIL, FORCA_NIVEL_5, "Grêmio"));
			clubes.add(new Clube(521, Liga.BRASIL, FORCA_NIVEL_6, "Juventude"));
			clubes.add(new Clube(522, Liga.BRASIL, FORCA_NIVEL_6, "Coritiba"));
			clubes.add(new Clube(523, Liga.BRASIL, FORCA_NIVEL_6, "Vasco"));
			clubes.add(new Clube(524, Liga.BRASIL, FORCA_NIVEL_6, "Chapecoense"));
			clubes.add(new Clube(525, Liga.BRASIL, FORCA_NIVEL_6, "Avaí"));
			clubes.add(new Clube(526, Liga.BRASIL, FORCA_NIVEL_6, "Sport"));
			clubes.add(new Clube(527, Liga.BRASIL, FORCA_NIVEL_7, "CRB"));
			clubes.add(new Clube(528, Liga.BRASIL, FORCA_NIVEL_7, "Vitória"));
			clubes.add(new Clube(529, Liga.BRASIL, FORCA_NIVEL_7, "Vila Nova"));
			clubes.add(new Clube(530, Liga.BRASIL, FORCA_NIVEL_7, "Ponte Preta"));
			clubes.add(new Clube(531, Liga.BRASIL, FORCA_NIVEL_7, "Sampaio Corrêa"));
			clubes.add(new Clube(532, Liga.BRASIL, FORCA_NIVEL_7, "ABC"));
			*/

			clubeRepository.saveAll(clubes);
		}
	}

	private void inserirClubeTituloRanking() {
		if (clubeTituloRankingRepository.findAll().isEmpty()) {
			List<ClubeTituloRanking> rankingTitulos = new ArrayList<ClubeTituloRanking>();
			
			rankingTitulos.add(new ClubeTituloRanking(101, new Clube(101)));
			rankingTitulos.add(new ClubeTituloRanking(102, new Clube(102)));
			rankingTitulos.add(new ClubeTituloRanking(103, new Clube(103)));
			rankingTitulos.add(new ClubeTituloRanking(104, new Clube(104)));
			rankingTitulos.add(new ClubeTituloRanking(105, new Clube(105)));
			rankingTitulos.add(new ClubeTituloRanking(106, new Clube(106)));
			rankingTitulos.add(new ClubeTituloRanking(107, new Clube(107)));
			rankingTitulos.add(new ClubeTituloRanking(108, new Clube(108)));
			rankingTitulos.add(new ClubeTituloRanking(109, new Clube(109)));
			rankingTitulos.add(new ClubeTituloRanking(110, new Clube(110)));
			rankingTitulos.add(new ClubeTituloRanking(111, new Clube(111)));
			rankingTitulos.add(new ClubeTituloRanking(112, new Clube(112)));
			rankingTitulos.add(new ClubeTituloRanking(113, new Clube(113)));
			rankingTitulos.add(new ClubeTituloRanking(114, new Clube(114)));
			rankingTitulos.add(new ClubeTituloRanking(115, new Clube(115)));
			rankingTitulos.add(new ClubeTituloRanking(116, new Clube(116)));
			rankingTitulos.add(new ClubeTituloRanking(117, new Clube(117)));
			rankingTitulos.add(new ClubeTituloRanking(118, new Clube(118)));
			rankingTitulos.add(new ClubeTituloRanking(119, new Clube(119)));
			rankingTitulos.add(new ClubeTituloRanking(120, new Clube(120)));
			rankingTitulos.add(new ClubeTituloRanking(121, new Clube(121)));
			rankingTitulos.add(new ClubeTituloRanking(122, new Clube(122)));
			rankingTitulos.add(new ClubeTituloRanking(123, new Clube(123)));
			rankingTitulos.add(new ClubeTituloRanking(124, new Clube(124)));
			rankingTitulos.add(new ClubeTituloRanking(125, new Clube(125)));
			rankingTitulos.add(new ClubeTituloRanking(126, new Clube(126)));
			rankingTitulos.add(new ClubeTituloRanking(127, new Clube(127)));
			rankingTitulos.add(new ClubeTituloRanking(128, new Clube(128)));
			rankingTitulos.add(new ClubeTituloRanking(129, new Clube(129)));
			rankingTitulos.add(new ClubeTituloRanking(130, new Clube(130)));
			rankingTitulos.add(new ClubeTituloRanking(131, new Clube(131)));
			rankingTitulos.add(new ClubeTituloRanking(132, new Clube(132)));
			rankingTitulos.add(new ClubeTituloRanking(201, new Clube(201)));
			rankingTitulos.add(new ClubeTituloRanking(202, new Clube(202)));
			rankingTitulos.add(new ClubeTituloRanking(203, new Clube(203)));
			rankingTitulos.add(new ClubeTituloRanking(204, new Clube(204)));
			rankingTitulos.add(new ClubeTituloRanking(205, new Clube(205)));
			rankingTitulos.add(new ClubeTituloRanking(206, new Clube(206)));
			rankingTitulos.add(new ClubeTituloRanking(207, new Clube(207)));
			rankingTitulos.add(new ClubeTituloRanking(208, new Clube(208)));
			rankingTitulos.add(new ClubeTituloRanking(209, new Clube(209)));
			rankingTitulos.add(new ClubeTituloRanking(210, new Clube(210)));
			rankingTitulos.add(new ClubeTituloRanking(211, new Clube(211)));
			rankingTitulos.add(new ClubeTituloRanking(212, new Clube(212)));
			rankingTitulos.add(new ClubeTituloRanking(213, new Clube(213)));
			rankingTitulos.add(new ClubeTituloRanking(214, new Clube(214)));
			rankingTitulos.add(new ClubeTituloRanking(215, new Clube(215)));
			rankingTitulos.add(new ClubeTituloRanking(216, new Clube(216)));
			rankingTitulos.add(new ClubeTituloRanking(217, new Clube(217)));
			rankingTitulos.add(new ClubeTituloRanking(218, new Clube(218)));
			rankingTitulos.add(new ClubeTituloRanking(219, new Clube(219)));
			rankingTitulos.add(new ClubeTituloRanking(220, new Clube(220)));
			rankingTitulos.add(new ClubeTituloRanking(221, new Clube(221)));
			rankingTitulos.add(new ClubeTituloRanking(222, new Clube(222)));
			rankingTitulos.add(new ClubeTituloRanking(223, new Clube(223)));
			rankingTitulos.add(new ClubeTituloRanking(224, new Clube(224)));
			rankingTitulos.add(new ClubeTituloRanking(225, new Clube(225)));
			rankingTitulos.add(new ClubeTituloRanking(226, new Clube(226)));
			rankingTitulos.add(new ClubeTituloRanking(227, new Clube(227)));
			rankingTitulos.add(new ClubeTituloRanking(228, new Clube(228)));
			rankingTitulos.add(new ClubeTituloRanking(229, new Clube(229)));
			rankingTitulos.add(new ClubeTituloRanking(230, new Clube(230)));
			rankingTitulos.add(new ClubeTituloRanking(231, new Clube(231)));
			rankingTitulos.add(new ClubeTituloRanking(232, new Clube(232)));
			rankingTitulos.add(new ClubeTituloRanking(301, new Clube(301)));
			rankingTitulos.add(new ClubeTituloRanking(302, new Clube(302)));
			rankingTitulos.add(new ClubeTituloRanking(303, new Clube(303)));
			rankingTitulos.add(new ClubeTituloRanking(304, new Clube(304)));
			rankingTitulos.add(new ClubeTituloRanking(305, new Clube(305)));
			rankingTitulos.add(new ClubeTituloRanking(306, new Clube(306)));
			rankingTitulos.add(new ClubeTituloRanking(307, new Clube(307)));
			rankingTitulos.add(new ClubeTituloRanking(308, new Clube(308)));
			rankingTitulos.add(new ClubeTituloRanking(309, new Clube(309)));
			rankingTitulos.add(new ClubeTituloRanking(310, new Clube(310)));
			rankingTitulos.add(new ClubeTituloRanking(311, new Clube(311)));
			rankingTitulos.add(new ClubeTituloRanking(312, new Clube(312)));
			rankingTitulos.add(new ClubeTituloRanking(313, new Clube(313)));
			rankingTitulos.add(new ClubeTituloRanking(314, new Clube(314)));
			rankingTitulos.add(new ClubeTituloRanking(315, new Clube(315)));
			rankingTitulos.add(new ClubeTituloRanking(316, new Clube(316)));
			rankingTitulos.add(new ClubeTituloRanking(317, new Clube(317)));
			rankingTitulos.add(new ClubeTituloRanking(318, new Clube(318)));
			rankingTitulos.add(new ClubeTituloRanking(319, new Clube(319)));
			rankingTitulos.add(new ClubeTituloRanking(320, new Clube(320)));
			rankingTitulos.add(new ClubeTituloRanking(321, new Clube(321)));
			rankingTitulos.add(new ClubeTituloRanking(322, new Clube(322)));
			rankingTitulos.add(new ClubeTituloRanking(323, new Clube(323)));
			rankingTitulos.add(new ClubeTituloRanking(324, new Clube(324)));
			rankingTitulos.add(new ClubeTituloRanking(325, new Clube(325)));
			rankingTitulos.add(new ClubeTituloRanking(326, new Clube(326)));
			rankingTitulos.add(new ClubeTituloRanking(327, new Clube(327)));
			rankingTitulos.add(new ClubeTituloRanking(328, new Clube(328)));
			rankingTitulos.add(new ClubeTituloRanking(329, new Clube(329)));
			rankingTitulos.add(new ClubeTituloRanking(330, new Clube(330)));
			rankingTitulos.add(new ClubeTituloRanking(331, new Clube(331)));
			rankingTitulos.add(new ClubeTituloRanking(332, new Clube(332)));
			rankingTitulos.add(new ClubeTituloRanking(401, new Clube(401)));
			rankingTitulos.add(new ClubeTituloRanking(402, new Clube(402)));
			rankingTitulos.add(new ClubeTituloRanking(403, new Clube(403)));
			rankingTitulos.add(new ClubeTituloRanking(404, new Clube(404)));
			rankingTitulos.add(new ClubeTituloRanking(405, new Clube(405)));
			rankingTitulos.add(new ClubeTituloRanking(406, new Clube(406)));
			rankingTitulos.add(new ClubeTituloRanking(407, new Clube(407)));
			rankingTitulos.add(new ClubeTituloRanking(408, new Clube(408)));
			rankingTitulos.add(new ClubeTituloRanking(409, new Clube(409)));
			rankingTitulos.add(new ClubeTituloRanking(410, new Clube(410)));
			rankingTitulos.add(new ClubeTituloRanking(411, new Clube(411)));
			rankingTitulos.add(new ClubeTituloRanking(412, new Clube(412)));
			rankingTitulos.add(new ClubeTituloRanking(413, new Clube(413)));
			rankingTitulos.add(new ClubeTituloRanking(414, new Clube(414)));
			rankingTitulos.add(new ClubeTituloRanking(415, new Clube(415)));
			rankingTitulos.add(new ClubeTituloRanking(416, new Clube(416)));
			rankingTitulos.add(new ClubeTituloRanking(417, new Clube(417)));
			rankingTitulos.add(new ClubeTituloRanking(418, new Clube(418)));
			rankingTitulos.add(new ClubeTituloRanking(419, new Clube(419)));
			rankingTitulos.add(new ClubeTituloRanking(420, new Clube(420)));
			rankingTitulos.add(new ClubeTituloRanking(421, new Clube(421)));
			rankingTitulos.add(new ClubeTituloRanking(422, new Clube(422)));
			rankingTitulos.add(new ClubeTituloRanking(423, new Clube(423)));
			rankingTitulos.add(new ClubeTituloRanking(424, new Clube(424)));
			rankingTitulos.add(new ClubeTituloRanking(425, new Clube(425)));
			rankingTitulos.add(new ClubeTituloRanking(426, new Clube(426)));
			rankingTitulos.add(new ClubeTituloRanking(427, new Clube(427)));
			rankingTitulos.add(new ClubeTituloRanking(428, new Clube(428)));
			rankingTitulos.add(new ClubeTituloRanking(429, new Clube(429)));
			rankingTitulos.add(new ClubeTituloRanking(430, new Clube(430)));
			rankingTitulos.add(new ClubeTituloRanking(431, new Clube(431)));
			rankingTitulos.add(new ClubeTituloRanking(432, new Clube(432)));
			
			/*rankingTitulos.add(new ClubeTituloRanking(501, new Clube(501)));
			rankingTitulos.add(new ClubeTituloRanking(502, new Clube(502)));
			rankingTitulos.add(new ClubeTituloRanking(503, new Clube(503)));
			rankingTitulos.add(new ClubeTituloRanking(504, new Clube(504)));
			rankingTitulos.add(new ClubeTituloRanking(505, new Clube(505)));
			rankingTitulos.add(new ClubeTituloRanking(506, new Clube(506)));
			rankingTitulos.add(new ClubeTituloRanking(507, new Clube(507)));
			rankingTitulos.add(new ClubeTituloRanking(508, new Clube(508)));
			rankingTitulos.add(new ClubeTituloRanking(509, new Clube(509)));
			rankingTitulos.add(new ClubeTituloRanking(510, new Clube(510)));
			rankingTitulos.add(new ClubeTituloRanking(511, new Clube(511)));
			rankingTitulos.add(new ClubeTituloRanking(512, new Clube(512)));
			rankingTitulos.add(new ClubeTituloRanking(513, new Clube(513)));
			rankingTitulos.add(new ClubeTituloRanking(514, new Clube(514)));
			rankingTitulos.add(new ClubeTituloRanking(515, new Clube(515)));
			rankingTitulos.add(new ClubeTituloRanking(516, new Clube(516)));
			rankingTitulos.add(new ClubeTituloRanking(517, new Clube(517)));
			rankingTitulos.add(new ClubeTituloRanking(518, new Clube(518)));
			rankingTitulos.add(new ClubeTituloRanking(519, new Clube(519)));
			rankingTitulos.add(new ClubeTituloRanking(520, new Clube(520)));
			rankingTitulos.add(new ClubeTituloRanking(521, new Clube(521)));
			rankingTitulos.add(new ClubeTituloRanking(522, new Clube(522)));
			rankingTitulos.add(new ClubeTituloRanking(523, new Clube(523)));
			rankingTitulos.add(new ClubeTituloRanking(524, new Clube(524)));
			rankingTitulos.add(new ClubeTituloRanking(525, new Clube(525)));
			rankingTitulos.add(new ClubeTituloRanking(526, new Clube(526)));
			rankingTitulos.add(new ClubeTituloRanking(527, new Clube(527)));
			rankingTitulos.add(new ClubeTituloRanking(528, new Clube(528)));
			rankingTitulos.add(new ClubeTituloRanking(529, new Clube(529)));
			rankingTitulos.add(new ClubeTituloRanking(530, new Clube(530)));
			rankingTitulos.add(new ClubeTituloRanking(531, new Clube(531)));
			rankingTitulos.add(new ClubeTituloRanking(532, new Clube(532)));*/
			
			clubeTituloRankingRepository.saveAll(rankingTitulos);
		}
	}
	
	private void inserirClubesRanking() {
		
		if (clubeRankingRepository.findAll().isEmpty()) {
			List<ClubeRanking> ranking = new ArrayList<ClubeRanking>();
			
			int ano = Constantes.ANO_INICIAL - 1;
			
			ranking.add(new ClubeRanking(1, new Clube(101), ano, 1, posicaoToClassificacaoNacional(1), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(2, new Clube(102), ano, 2, posicaoToClassificacaoNacional(2), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(3, new Clube(103), ano, 3, posicaoToClassificacaoNacional(3), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(4, new Clube(104), ano, 4, posicaoToClassificacaoNacional(4), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(5, new Clube(105), ano, 5, posicaoToClassificacaoNacional(5), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(6, new Clube(106), ano, 6, posicaoToClassificacaoNacional(6), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(7, new Clube(107), ano, 7, posicaoToClassificacaoNacional(7), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(8, new Clube(108), ano, 8, posicaoToClassificacaoNacional(8), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(9, new Clube(109), ano, 9, posicaoToClassificacaoNacional(9), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(10, new Clube(110), ano, 10, posicaoToClassificacaoNacional(10), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(11, new Clube(111), ano, 11, posicaoToClassificacaoNacional(11), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(12, new Clube(112), ano, 12, posicaoToClassificacaoNacional(12), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(13, new Clube(113), ano, 13, posicaoToClassificacaoNacional(13), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(14, new Clube(114), ano, 14, posicaoToClassificacaoNacional(14), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(15, new Clube(115), ano, 15, posicaoToClassificacaoNacional(15), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(16, new Clube(116), ano, 16, posicaoToClassificacaoNacional(16), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(17, new Clube(117), ano, 17, posicaoToClassificacaoNacional(17), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(18, new Clube(118), ano, 18, posicaoToClassificacaoNacional(18), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(19, new Clube(119), ano, 19, posicaoToClassificacaoNacional(19), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(20, new Clube(120), ano, 20, posicaoToClassificacaoNacional(20), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(21, new Clube(121), ano, 21, posicaoToClassificacaoNacional(21), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(22, new Clube(122), ano, 22, posicaoToClassificacaoNacional(22), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(23, new Clube(123), ano, 23, posicaoToClassificacaoNacional(23), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(24, new Clube(124), ano, 24, posicaoToClassificacaoNacional(24), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(25, new Clube(125), ano, 25, posicaoToClassificacaoNacional(25), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(26, new Clube(126), ano, 26, posicaoToClassificacaoNacional(26), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(27, new Clube(127), ano, 27, posicaoToClassificacaoNacional(27), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(28, new Clube(128), ano, 28, posicaoToClassificacaoNacional(28), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(29, new Clube(129), ano, 29, posicaoToClassificacaoNacional(29), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(30, new Clube(130), ano, 30, posicaoToClassificacaoNacional(30), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(31, new Clube(131), ano, 31, posicaoToClassificacaoNacional(31), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(32, new Clube(132), ano, 32, posicaoToClassificacaoNacional(32), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(33, new Clube(201), ano, 1, posicaoToClassificacaoNacional(1), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(34, new Clube(202), ano, 2, posicaoToClassificacaoNacional(2), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(35, new Clube(203), ano, 3, posicaoToClassificacaoNacional(3), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(36, new Clube(204), ano, 4, posicaoToClassificacaoNacional(4), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(37, new Clube(205), ano, 5, posicaoToClassificacaoNacional(5), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(38, new Clube(206), ano, 6, posicaoToClassificacaoNacional(6), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(39, new Clube(207), ano, 7, posicaoToClassificacaoNacional(7), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(40, new Clube(208), ano, 8, posicaoToClassificacaoNacional(8), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(41, new Clube(209), ano, 9, posicaoToClassificacaoNacional(9), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(42, new Clube(210), ano, 10, posicaoToClassificacaoNacional(10), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(43, new Clube(211), ano, 11, posicaoToClassificacaoNacional(11), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(44, new Clube(212), ano, 12, posicaoToClassificacaoNacional(12), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(45, new Clube(213), ano, 13, posicaoToClassificacaoNacional(13), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(46, new Clube(214), ano, 14, posicaoToClassificacaoNacional(14), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(47, new Clube(215), ano, 15, posicaoToClassificacaoNacional(15), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(48, new Clube(216), ano, 16, posicaoToClassificacaoNacional(16), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(49, new Clube(217), ano, 17, posicaoToClassificacaoNacional(17), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(50, new Clube(218), ano, 18, posicaoToClassificacaoNacional(18), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(51, new Clube(219), ano, 19, posicaoToClassificacaoNacional(19), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(52, new Clube(220), ano, 20, posicaoToClassificacaoNacional(20), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(53, new Clube(221), ano, 21, posicaoToClassificacaoNacional(21), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(54, new Clube(222), ano, 22, posicaoToClassificacaoNacional(22), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(55, new Clube(223), ano, 23, posicaoToClassificacaoNacional(23), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(56, new Clube(224), ano, 24, posicaoToClassificacaoNacional(24), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(57, new Clube(225), ano, 25, posicaoToClassificacaoNacional(25), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(58, new Clube(226), ano, 26, posicaoToClassificacaoNacional(26), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(59, new Clube(227), ano, 27, posicaoToClassificacaoNacional(27), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(60, new Clube(228), ano, 28, posicaoToClassificacaoNacional(28), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(61, new Clube(229), ano, 29, posicaoToClassificacaoNacional(29), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(62, new Clube(230), ano, 30, posicaoToClassificacaoNacional(30), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(63, new Clube(231), ano, 31, posicaoToClassificacaoNacional(31), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(64, new Clube(232), ano, 32, posicaoToClassificacaoNacional(32), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(65, new Clube(301), ano, 1, posicaoToClassificacaoNacional(1), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(66, new Clube(302), ano, 2, posicaoToClassificacaoNacional(2), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(67, new Clube(303), ano, 3, posicaoToClassificacaoNacional(3), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(68, new Clube(304), ano, 4, posicaoToClassificacaoNacional(4), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(69, new Clube(305), ano, 5, posicaoToClassificacaoNacional(5), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(70, new Clube(306), ano, 6, posicaoToClassificacaoNacional(6), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(71, new Clube(307), ano, 7, posicaoToClassificacaoNacional(7), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(72, new Clube(308), ano, 8, posicaoToClassificacaoNacional(8), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(73, new Clube(309), ano, 9, posicaoToClassificacaoNacional(9), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(74, new Clube(310), ano, 10, posicaoToClassificacaoNacional(10), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(75, new Clube(311), ano, 11, posicaoToClassificacaoNacional(11), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(76, new Clube(312), ano, 12, posicaoToClassificacaoNacional(12), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(77, new Clube(313), ano, 13, posicaoToClassificacaoNacional(13), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(78, new Clube(314), ano, 14, posicaoToClassificacaoNacional(14), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(79, new Clube(315), ano, 15, posicaoToClassificacaoNacional(15), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(80, new Clube(316), ano, 16, posicaoToClassificacaoNacional(16), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(81, new Clube(317), ano, 17, posicaoToClassificacaoNacional(17), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(82, new Clube(318), ano, 18, posicaoToClassificacaoNacional(18), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(83, new Clube(319), ano, 19, posicaoToClassificacaoNacional(19), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(84, new Clube(320), ano, 20, posicaoToClassificacaoNacional(20), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(85, new Clube(321), ano, 21, posicaoToClassificacaoNacional(21), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(86, new Clube(322), ano, 22, posicaoToClassificacaoNacional(22), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(87, new Clube(323), ano, 23, posicaoToClassificacaoNacional(23), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(88, new Clube(324), ano, 24, posicaoToClassificacaoNacional(24), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(89, new Clube(325), ano, 25, posicaoToClassificacaoNacional(25), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(90, new Clube(326), ano, 26, posicaoToClassificacaoNacional(26), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(91, new Clube(327), ano, 27, posicaoToClassificacaoNacional(27), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(92, new Clube(328), ano, 28, posicaoToClassificacaoNacional(28), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(93, new Clube(329), ano, 29, posicaoToClassificacaoNacional(29), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(94, new Clube(330), ano, 30, posicaoToClassificacaoNacional(30), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(95, new Clube(331), ano, 31, posicaoToClassificacaoNacional(31), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(96, new Clube(332), ano, 32, posicaoToClassificacaoNacional(32), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(97, new Clube(401), ano, 1, posicaoToClassificacaoNacional(1), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(98, new Clube(402), ano, 2, posicaoToClassificacaoNacional(2), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(99, new Clube(403), ano, 3, posicaoToClassificacaoNacional(3), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(100, new Clube(404), ano, 4, posicaoToClassificacaoNacional(4), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(101, new Clube(405), ano, 5, posicaoToClassificacaoNacional(5), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(102, new Clube(406), ano, 6, posicaoToClassificacaoNacional(6), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(103, new Clube(407), ano, 7, posicaoToClassificacaoNacional(7), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(104, new Clube(408), ano, 8, posicaoToClassificacaoNacional(8), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(105, new Clube(409), ano, 9, posicaoToClassificacaoNacional(9), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(106, new Clube(410), ano, 10, posicaoToClassificacaoNacional(10), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(107, new Clube(411), ano, 11, posicaoToClassificacaoNacional(11), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(108, new Clube(412), ano, 12, posicaoToClassificacaoNacional(12), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(109, new Clube(413), ano, 13, posicaoToClassificacaoNacional(13), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(110, new Clube(414), ano, 14, posicaoToClassificacaoNacional(14), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(111, new Clube(415), ano, 15, posicaoToClassificacaoNacional(15), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(112, new Clube(416), ano, 16, posicaoToClassificacaoNacional(16), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(113, new Clube(417), ano, 17, posicaoToClassificacaoNacional(17), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(114, new Clube(418), ano, 18, posicaoToClassificacaoNacional(18), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(115, new Clube(419), ano, 19, posicaoToClassificacaoNacional(19), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(116, new Clube(420), ano, 20, posicaoToClassificacaoNacional(20), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(117, new Clube(421), ano, 21, posicaoToClassificacaoNacional(21), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(118, new Clube(422), ano, 22, posicaoToClassificacaoNacional(22), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(119, new Clube(423), ano, 23, posicaoToClassificacaoNacional(23), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(120, new Clube(424), ano, 24, posicaoToClassificacaoNacional(24), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(121, new Clube(425), ano, 25, posicaoToClassificacaoNacional(25), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(122, new Clube(426), ano, 26, posicaoToClassificacaoNacional(26), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(123, new Clube(427), ano, 27, posicaoToClassificacaoNacional(27), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(124, new Clube(428), ano, 28, posicaoToClassificacaoNacional(28), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(125, new Clube(429), ano, 29, posicaoToClassificacaoNacional(29), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(126, new Clube(430), ano, 30, posicaoToClassificacaoNacional(30), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(127, new Clube(431), ano, 31, posicaoToClassificacaoNacional(31), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(128, new Clube(432), ano, 32, posicaoToClassificacaoNacional(32), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			
			/*ranking.add(new ClubeRanking(129, new Clube(501), ano, 1, posicaoToClassificacaoNacional(1), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.C_CAMPEAO));
			ranking.add(new ClubeRanking(130, new Clube(502), ano, 2, posicaoToClassificacaoNacional(2), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.CII_CAMPEAO));
			ranking.add(new ClubeRanking(131, new Clube(503), ano, 3, posicaoToClassificacaoNacional(3), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.CIII_CAMPEAO));
			ranking.add(new ClubeRanking(132, new Clube(504), ano, 4, posicaoToClassificacaoNacional(4), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(133, new Clube(505), ano, 5, posicaoToClassificacaoNacional(5), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(134, new Clube(506), ano, 6, posicaoToClassificacaoNacional(6), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(135, new Clube(507), ano, 7, posicaoToClassificacaoNacional(7), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(136, new Clube(508), ano, 8, posicaoToClassificacaoNacional(8), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(137, new Clube(509), ano, 9, posicaoToClassificacaoNacional(9), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(138, new Clube(510), ano, 10, posicaoToClassificacaoNacional(10), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(139, new Clube(511), ano, 11, posicaoToClassificacaoNacional(11), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(140, new Clube(512), ano, 12, posicaoToClassificacaoNacional(12), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(141, new Clube(513), ano, 13, posicaoToClassificacaoNacional(13), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(142, new Clube(514), ano, 14, posicaoToClassificacaoNacional(14), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(143, new Clube(515), ano, 15, posicaoToClassificacaoNacional(15), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(144, new Clube(516), ano, 16, posicaoToClassificacaoNacional(16), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(145, new Clube(517), ano, 17, posicaoToClassificacaoNacional(17), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(146, new Clube(518), ano, 18, posicaoToClassificacaoNacional(18), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(147, new Clube(519), ano, 19, posicaoToClassificacaoNacional(19), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(148, new Clube(520), ano, 20, posicaoToClassificacaoNacional(20), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(149, new Clube(521), ano, 21, posicaoToClassificacaoNacional(21), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(150, new Clube(522), ano, 22, posicaoToClassificacaoNacional(22), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(151, new Clube(523), ano, 23, posicaoToClassificacaoNacional(23), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(152, new Clube(524), ano, 24, posicaoToClassificacaoNacional(24), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(153, new Clube(525), ano, 25, posicaoToClassificacaoNacional(25), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(154, new Clube(526), ano, 26, posicaoToClassificacaoNacional(26), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(155, new Clube(527), ano, 27, posicaoToClassificacaoNacional(27), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(156, new Clube(528), ano, 28, posicaoToClassificacaoNacional(28), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(157, new Clube(529), ano, 29, posicaoToClassificacaoNacional(29), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(158, new Clube(530), ano, 30, posicaoToClassificacaoNacional(30), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(159, new Clube(531), ano, 31, posicaoToClassificacaoNacional(31), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(160, new Clube(532), ano, 32, posicaoToClassificacaoNacional(32), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));*/
			
			clubeRankingRepository.saveAll(ranking);
		}
	}

	private ClassificacaoNacionalFinal posicaoToClassificacaoNacional(Integer pos) {
		return ClassificacaoNacionalFinal.getClassificacao(pos > 16 ? NivelCampeonato.NACIONAL_II : NivelCampeonato.NACIONAL, pos%16 == 0 ? 16 : pos%16);
	}
}

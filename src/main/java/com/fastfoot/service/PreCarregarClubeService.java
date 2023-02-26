package com.fastfoot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.ClubeNivel;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.entity.ClubeTituloRanking;
import com.fastfoot.club.model.repository.ClubeRankingRepository;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.model.repository.ClubeTituloRankingRepository;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceira;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
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
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private ClubeRankingRepository clubeRankingRepository;

	@Autowired
	private ClubeTituloRankingRepository clubeTituloRankingRepository;

	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

	public void preCarregarClubes () {
		List<Clube> clubes = inserirClubes();
		inserirClubesRanking();
		inserirClubeTituloRanking();
		inserirCaixaInicial(clubes);
	}
	
	private void inserirCaixaInicial(List<Clube> clubes) {

		List<MovimentacaoFinanceira> movimentacoesFinanceiras = new ArrayList<MovimentacaoFinanceira>();

		for (Clube c : clubes) {
			movimentacoesFinanceiras.add(new MovimentacaoFinanceira(c, null, TipoMovimentacaoFinanceira.CAIXA_INICIAL,
					c.getClubeNivel().getCaixaInicial(), "Caixa Inicial"));
		}

		movimentacaoFinanceiraRepository.saveAll(movimentacoesFinanceiras);
	}

	private List<Clube> inserirClubes() {
		
		List<Clube> clubes = new ArrayList<Clube>();
		
		if (clubeRepository.count() == 0) {

			/*clubes.add(new Clube(101, Liga.GENEBE, FORCA_NIVEL_1, /*"Bayern München"* / "Bayern Munich"));
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
			clubes.add(new Clube(117, Liga.GENEBE, FORCA_NIVEL_5, /*"Colônia"* / "FC Koln"));
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
			clubes.add(new Clube(301, Liga.ITAFRA, FORCA_NIVEL_1, "Internazionale" /*"Inter Milan"* /));
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
			clubes.add(new Clube(411, Liga.ENGLND, FORCA_NIVEL_4, /*"Wolverhampton Wanderers"* / "Wolves"));
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
			clubes.add(new Clube(432, Liga.ENGLND, FORCA_NIVEL_7, "Blackpool"));*/
			
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 1, Liga.GENEBE, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, /*"Bayern München"*/ "Bayern Munich"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 2, Liga.GENEBE, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Ajax"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 3, Liga.GENEBE, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Borussia Dortmund"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 4, Liga.GENEBE, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Bayer Leverkusen"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 5, Liga.GENEBE, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "PSV"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 6, Liga.GENEBE, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "RB Leipzig"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 7, Liga.GENEBE, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Wolfsburg"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 8, Liga.GENEBE, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Borussia Mönchengladbach"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 9, Liga.GENEBE, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Club Brugge"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 10, Liga.GENEBE, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Eintracht Frankfurt"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 11, Liga.GENEBE, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Freiburg"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 12, Liga.GENEBE, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Hoffenheim"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 13, Liga.GENEBE, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Feyenoord"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 14, Liga.GENEBE, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "AZ Alkmaar"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 15, Liga.GENEBE, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Union Berlin"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 16, Liga.GENEBE, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Mainz 05"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 17, Liga.GENEBE, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, /*"Colônia"*/ "FC Koln"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 18, Liga.GENEBE, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Schalke 04"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 19, Liga.GENEBE, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Hertha Berlin"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 20, Liga.GENEBE, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "VfB Stuttgart"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 21, Liga.GENEBE, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Vitesse"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 22, Liga.GENEBE, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Fortuna Düsseldorf"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 23, Liga.GENEBE, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Hamburgo"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 24, Liga.GENEBE, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Utrecht"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 25, Liga.GENEBE, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Gent"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 26, Liga.GENEBE, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Antwerp"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 27, Liga.GENEBE, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Arminia Bielefeld"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 28, Liga.GENEBE, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Augsburg"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 29, Liga.GENEBE, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Anderlecht"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 30, Liga.GENEBE, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Bochum"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 31, Liga.GENEBE, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Racing Genk"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 32, Liga.GENEBE, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Werder Bremen"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 1, Liga.SPAPOR, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Real Madrid"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 2, Liga.SPAPOR, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Barcelona"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 3, Liga.SPAPOR, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Porto"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 4, Liga.SPAPOR, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Atlético Madrid"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 5, Liga.SPAPOR, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Benfica"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 6, Liga.SPAPOR, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Sevilla"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 7, Liga.SPAPOR, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Sporting"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 8, Liga.SPAPOR, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Real Betis"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 9, Liga.SPAPOR, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Valencia"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 10, Liga.SPAPOR, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Real Sociedad"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 11, Liga.SPAPOR, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Villarreal"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 12, Liga.SPAPOR, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Athletic Bilbao"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 13, Liga.SPAPOR, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Getafe"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 14, Liga.SPAPOR, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Celta de Vigo"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 15, Liga.SPAPOR, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Rayo Vallecano"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 16, Liga.SPAPOR, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Osasuna"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 17, Liga.SPAPOR, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Real Valladolid"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 18, Liga.SPAPOR, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Granada"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 19, Liga.SPAPOR, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Espanyol"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 20, Liga.SPAPOR, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Mallorca"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 21, Liga.SPAPOR, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Almería"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 22, Liga.SPAPOR, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Alaves"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 23, Liga.SPAPOR, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Elche"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 24, Liga.SPAPOR, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Braga"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 25, Liga.SPAPOR, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Levante"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 26, Liga.SPAPOR, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Cádiz"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 27, Liga.SPAPOR, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Leganes"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 28, Liga.SPAPOR, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Sporting Gijón"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 29, Liga.SPAPOR, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Girona"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 30, Liga.SPAPOR, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Eibar"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 31, Liga.SPAPOR, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Zaragoza"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 32, Liga.SPAPOR, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Málaga"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 1, Liga.ITAFRA, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Internazionale" /*"Inter Milan"*/));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 2, Liga.ITAFRA, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Juventus"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 3, Liga.ITAFRA, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Paris Saint-Germain"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 4, Liga.ITAFRA, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Napoli"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 5, Liga.ITAFRA, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Milan"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 6, Liga.ITAFRA, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Atalanta"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 7, Liga.ITAFRA, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Olympique de Marseille"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 8, Liga.ITAFRA, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Lazio"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 9, Liga.ITAFRA, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Lyon"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 10, Liga.ITAFRA, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Roma"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 11, Liga.ITAFRA, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Lille"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 12, Liga.ITAFRA, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Monaco"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 13, Liga.ITAFRA, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Fiorentina"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 14, Liga.ITAFRA, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Sassuolo"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 15, Liga.ITAFRA, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Rennes"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 16, Liga.ITAFRA, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Verona"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 17, Liga.ITAFRA, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "RC Strasbourg"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 18, Liga.ITAFRA, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Montpellier"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 19, Liga.ITAFRA, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Bologna"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 20, Liga.ITAFRA, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Nice"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 21, Liga.ITAFRA, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Lens"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 22, Liga.ITAFRA, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Torino"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 23, Liga.ITAFRA, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Udinese"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 24, Liga.ITAFRA, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Empoli"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 25, Liga.ITAFRA, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Sampdoria"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 26, Liga.ITAFRA, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Stade Brestois"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 27, Liga.ITAFRA, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Nantes"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 28, Liga.ITAFRA, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Saint-Étienne"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 29, Liga.ITAFRA, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Bordeaux"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 30, Liga.ITAFRA, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Reims"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 31, Liga.ITAFRA, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Genoa"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 32, Liga.ITAFRA, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Venezia"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 1, Liga.ENGLND, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Liverpool"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 2, Liga.ENGLND, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Manchester United"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 3, Liga.ENGLND, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Manchester City"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 4, Liga.ENGLND, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Chelsea"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 5, Liga.ENGLND, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Arsenal"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 6, Liga.ENGLND, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "West Ham"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 7, Liga.ENGLND, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Tottenham"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 8, Liga.ENGLND, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Leicester City"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 9, Liga.ENGLND, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Aston Villa"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 10, Liga.ENGLND, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Rangers"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 11, Liga.ENGLND, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, /*"Wolverhampton Wanderers"*/ "Wolves"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 12, Liga.ENGLND, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Crystal Palace"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 13, Liga.ENGLND, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Everton"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 14, Liga.ENGLND, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Newcastle"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 15, Liga.ENGLND, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Leeds United"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 16, Liga.ENGLND, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Brighton"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 17, Liga.ENGLND, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Southampton"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 18, Liga.ENGLND, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Burnley"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 19, Liga.ENGLND, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Watford"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 20, Liga.ENGLND, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Celtic"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 21, Liga.ENGLND, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Norwich City"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 22, Liga.ENGLND, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Brentford"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 23, Liga.ENGLND, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Fulham"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 24, Liga.ENGLND, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Bournemouth"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 25, Liga.ENGLND, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Sheffield United"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 26, Liga.ENGLND, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Blackburn Rovers"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 27, Liga.ENGLND, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Nottingham Forest"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 28, Liga.ENGLND, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Hull City"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 29, Liga.ENGLND, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Stoke City"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 30, Liga.ENGLND, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Swansea City"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 31, Liga.ENGLND, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "West Bromwich"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 32, Liga.ENGLND, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Blackpool"));

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
		
		return clubes;
	}

	private void inserirClubeTituloRanking() {
		if (clubeTituloRankingRepository.count() == 0) {
			List<ClubeTituloRanking> rankingTitulos = new ArrayList<ClubeTituloRanking>();

			for (Liga liga : Liga.getAll()) {
				for (int i = 1; i <= Constantes.NRO_CLUBES_POR_LIGA; i++) {
					rankingTitulos
							.add(new ClubeTituloRanking(liga.getIdBaseLiga() + i, new Clube(liga.getIdBaseLiga() + i)));
				}
			}
			
			/*rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 1, new Clube(Liga.GENEBE.getIdBaseLiga() + 1)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 2, new Clube(Liga.GENEBE.getIdBaseLiga() + 2)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 3, new Clube(Liga.GENEBE.getIdBaseLiga() + 3)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 4, new Clube(Liga.GENEBE.getIdBaseLiga() + 4)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 5, new Clube(Liga.GENEBE.getIdBaseLiga() + 5)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 6, new Clube(Liga.GENEBE.getIdBaseLiga() + 6)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 7, new Clube(Liga.GENEBE.getIdBaseLiga() + 7)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 8, new Clube(Liga.GENEBE.getIdBaseLiga() + 8)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 9, new Clube(Liga.GENEBE.getIdBaseLiga() + 9)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 10, new Clube(Liga.GENEBE.getIdBaseLiga() + 10)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 11, new Clube(Liga.GENEBE.getIdBaseLiga() + 11)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 12, new Clube(Liga.GENEBE.getIdBaseLiga() + 12)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 13, new Clube(Liga.GENEBE.getIdBaseLiga() + 13)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 14, new Clube(Liga.GENEBE.getIdBaseLiga() + 14)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 15, new Clube(Liga.GENEBE.getIdBaseLiga() + 15)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 16, new Clube(Liga.GENEBE.getIdBaseLiga() + 16)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 17, new Clube(Liga.GENEBE.getIdBaseLiga() + 17)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 18, new Clube(Liga.GENEBE.getIdBaseLiga() + 18)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 19, new Clube(Liga.GENEBE.getIdBaseLiga() + 19)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 20, new Clube(Liga.GENEBE.getIdBaseLiga() + 20)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 21, new Clube(Liga.GENEBE.getIdBaseLiga() + 21)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 22, new Clube(Liga.GENEBE.getIdBaseLiga() + 22)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 23, new Clube(Liga.GENEBE.getIdBaseLiga() + 23)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 24, new Clube(Liga.GENEBE.getIdBaseLiga() + 24)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 25, new Clube(Liga.GENEBE.getIdBaseLiga() + 25)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 26, new Clube(Liga.GENEBE.getIdBaseLiga() + 26)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 27, new Clube(Liga.GENEBE.getIdBaseLiga() + 27)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 28, new Clube(Liga.GENEBE.getIdBaseLiga() + 28)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 29, new Clube(Liga.GENEBE.getIdBaseLiga() + 29)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 30, new Clube(Liga.GENEBE.getIdBaseLiga() + 30)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 31, new Clube(Liga.GENEBE.getIdBaseLiga() + 31)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.GENEBE.getIdBaseLiga() + 32, new Clube(Liga.GENEBE.getIdBaseLiga() + 32)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 1, new Clube(Liga.SPAPOR.getIdBaseLiga() + 1)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 2, new Clube(Liga.SPAPOR.getIdBaseLiga() + 2)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 3, new Clube(Liga.SPAPOR.getIdBaseLiga() + 3)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 4, new Clube(Liga.SPAPOR.getIdBaseLiga() + 4)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 5, new Clube(Liga.SPAPOR.getIdBaseLiga() + 5)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 6, new Clube(Liga.SPAPOR.getIdBaseLiga() + 6)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 7, new Clube(Liga.SPAPOR.getIdBaseLiga() + 7)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 8, new Clube(Liga.SPAPOR.getIdBaseLiga() + 8)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 9, new Clube(Liga.SPAPOR.getIdBaseLiga() + 9)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 10, new Clube(Liga.SPAPOR.getIdBaseLiga() + 10)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 11, new Clube(Liga.SPAPOR.getIdBaseLiga() + 11)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 12, new Clube(Liga.SPAPOR.getIdBaseLiga() + 12)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 13, new Clube(Liga.SPAPOR.getIdBaseLiga() + 13)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 14, new Clube(Liga.SPAPOR.getIdBaseLiga() + 14)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 15, new Clube(Liga.SPAPOR.getIdBaseLiga() + 15)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 16, new Clube(Liga.SPAPOR.getIdBaseLiga() + 16)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 17, new Clube(Liga.SPAPOR.getIdBaseLiga() + 17)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 18, new Clube(Liga.SPAPOR.getIdBaseLiga() + 18)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 19, new Clube(Liga.SPAPOR.getIdBaseLiga() + 19)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 20, new Clube(Liga.SPAPOR.getIdBaseLiga() + 20)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 21, new Clube(Liga.SPAPOR.getIdBaseLiga() + 21)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 22, new Clube(Liga.SPAPOR.getIdBaseLiga() + 22)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 23, new Clube(Liga.SPAPOR.getIdBaseLiga() + 23)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 24, new Clube(Liga.SPAPOR.getIdBaseLiga() + 24)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 25, new Clube(Liga.SPAPOR.getIdBaseLiga() + 25)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 26, new Clube(Liga.SPAPOR.getIdBaseLiga() + 26)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 27, new Clube(Liga.SPAPOR.getIdBaseLiga() + 27)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 28, new Clube(Liga.SPAPOR.getIdBaseLiga() + 28)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 29, new Clube(Liga.SPAPOR.getIdBaseLiga() + 29)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 30, new Clube(Liga.SPAPOR.getIdBaseLiga() + 30)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 31, new Clube(Liga.SPAPOR.getIdBaseLiga() + 31)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.SPAPOR.getIdBaseLiga() + 32, new Clube(Liga.SPAPOR.getIdBaseLiga() + 32)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 1, new Clube(Liga.ITAFRA.getIdBaseLiga() + 1)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 2, new Clube(Liga.ITAFRA.getIdBaseLiga() + 2)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 3, new Clube(Liga.ITAFRA.getIdBaseLiga() + 3)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 4, new Clube(Liga.ITAFRA.getIdBaseLiga() + 4)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 5, new Clube(Liga.ITAFRA.getIdBaseLiga() + 5)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 6, new Clube(Liga.ITAFRA.getIdBaseLiga() + 6)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 7, new Clube(Liga.ITAFRA.getIdBaseLiga() + 7)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 8, new Clube(Liga.ITAFRA.getIdBaseLiga() + 8)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 9, new Clube(Liga.ITAFRA.getIdBaseLiga() + 9)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 10, new Clube(Liga.ITAFRA.getIdBaseLiga() + 10)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 11, new Clube(Liga.ITAFRA.getIdBaseLiga() + 11)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 12, new Clube(Liga.ITAFRA.getIdBaseLiga() + 12)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 13, new Clube(Liga.ITAFRA.getIdBaseLiga() + 13)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 14, new Clube(Liga.ITAFRA.getIdBaseLiga() + 14)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 15, new Clube(Liga.ITAFRA.getIdBaseLiga() + 15)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 16, new Clube(Liga.ITAFRA.getIdBaseLiga() + 16)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 17, new Clube(Liga.ITAFRA.getIdBaseLiga() + 17)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 18, new Clube(Liga.ITAFRA.getIdBaseLiga() + 18)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 19, new Clube(Liga.ITAFRA.getIdBaseLiga() + 19)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 20, new Clube(Liga.ITAFRA.getIdBaseLiga() + 20)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 21, new Clube(Liga.ITAFRA.getIdBaseLiga() + 21)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 22, new Clube(Liga.ITAFRA.getIdBaseLiga() + 22)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 23, new Clube(Liga.ITAFRA.getIdBaseLiga() + 23)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 24, new Clube(Liga.ITAFRA.getIdBaseLiga() + 24)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 25, new Clube(Liga.ITAFRA.getIdBaseLiga() + 25)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 26, new Clube(Liga.ITAFRA.getIdBaseLiga() + 26)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 27, new Clube(Liga.ITAFRA.getIdBaseLiga() + 27)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 28, new Clube(Liga.ITAFRA.getIdBaseLiga() + 28)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 29, new Clube(Liga.ITAFRA.getIdBaseLiga() + 29)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 30, new Clube(Liga.ITAFRA.getIdBaseLiga() + 30)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 31, new Clube(Liga.ITAFRA.getIdBaseLiga() + 31)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ITAFRA.getIdBaseLiga() + 32, new Clube(Liga.ITAFRA.getIdBaseLiga() + 32)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 1, new Clube(Liga.ENGLND.getIdBaseLiga() + 1)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 2, new Clube(Liga.ENGLND.getIdBaseLiga() + 2)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 3, new Clube(Liga.ENGLND.getIdBaseLiga() + 3)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 4, new Clube(Liga.ENGLND.getIdBaseLiga() + 4)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 5, new Clube(Liga.ENGLND.getIdBaseLiga() + 5)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 6, new Clube(Liga.ENGLND.getIdBaseLiga() + 6)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 7, new Clube(Liga.ENGLND.getIdBaseLiga() + 7)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 8, new Clube(Liga.ENGLND.getIdBaseLiga() + 8)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 9, new Clube(Liga.ENGLND.getIdBaseLiga() + 9)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 10, new Clube(Liga.ENGLND.getIdBaseLiga() + 10)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 11, new Clube(Liga.ENGLND.getIdBaseLiga() + 11)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 12, new Clube(Liga.ENGLND.getIdBaseLiga() + 12)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 13, new Clube(Liga.ENGLND.getIdBaseLiga() + 13)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 14, new Clube(Liga.ENGLND.getIdBaseLiga() + 14)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 15, new Clube(Liga.ENGLND.getIdBaseLiga() + 15)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 16, new Clube(Liga.ENGLND.getIdBaseLiga() + 16)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 17, new Clube(Liga.ENGLND.getIdBaseLiga() + 17)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 18, new Clube(Liga.ENGLND.getIdBaseLiga() + 18)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 19, new Clube(Liga.ENGLND.getIdBaseLiga() + 19)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 20, new Clube(Liga.ENGLND.getIdBaseLiga() + 20)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 21, new Clube(Liga.ENGLND.getIdBaseLiga() + 21)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 22, new Clube(Liga.ENGLND.getIdBaseLiga() + 22)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 23, new Clube(Liga.ENGLND.getIdBaseLiga() + 23)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 24, new Clube(Liga.ENGLND.getIdBaseLiga() + 24)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 25, new Clube(Liga.ENGLND.getIdBaseLiga() + 25)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 26, new Clube(Liga.ENGLND.getIdBaseLiga() + 26)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 27, new Clube(Liga.ENGLND.getIdBaseLiga() + 27)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 28, new Clube(Liga.ENGLND.getIdBaseLiga() + 28)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 29, new Clube(Liga.ENGLND.getIdBaseLiga() + 29)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 30, new Clube(Liga.ENGLND.getIdBaseLiga() + 30)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 31, new Clube(Liga.ENGLND.getIdBaseLiga() + 31)));
			rankingTitulos.add(new ClubeTituloRanking(Liga.ENGLND.getIdBaseLiga() + 32, new Clube(Liga.ENGLND.getIdBaseLiga() + 32)));*/
			
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
		
		if (clubeRankingRepository.count() == 0) {
			List<ClubeRanking> ranking = new ArrayList<ClubeRanking>();
			
			int ano = Constantes.ANO_INICIAL - 1;
			
			for (Liga liga : Liga.getAll()) {
				for (int i = 1; i <= Constantes.NRO_CLUBES_POR_LIGA; i++) {
					ranking.add(new ClubeRanking(liga.getIdBaseLiga() + i, new Clube(liga.getIdBaseLiga() + i), ano, i,
							posicaoToClassificacaoNacional(i), ClassificacaoCopaNacional.NAO_PARTICIPOU,
							ClassificacaoContinental.NAO_PARTICIPOU));
				}
			}
			
			/*ranking.add(new ClubeRanking(1, new Clube(Liga.GENEBE.getIdBaseLiga() + 1), ano, 1, posicaoToClassificacaoNacional(1), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(2, new Clube(Liga.GENEBE.getIdBaseLiga() + 2), ano, 2, posicaoToClassificacaoNacional(2), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(3, new Clube(Liga.GENEBE.getIdBaseLiga() + 3), ano, 3, posicaoToClassificacaoNacional(3), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(4, new Clube(Liga.GENEBE.getIdBaseLiga() + 4), ano, 4, posicaoToClassificacaoNacional(4), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(5, new Clube(Liga.GENEBE.getIdBaseLiga() + 5), ano, 5, posicaoToClassificacaoNacional(5), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(6, new Clube(Liga.GENEBE.getIdBaseLiga() + 6), ano, 6, posicaoToClassificacaoNacional(6), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(7, new Clube(Liga.GENEBE.getIdBaseLiga() + 7), ano, 7, posicaoToClassificacaoNacional(7), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(8, new Clube(Liga.GENEBE.getIdBaseLiga() + 8), ano, 8, posicaoToClassificacaoNacional(8), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(9, new Clube(Liga.GENEBE.getIdBaseLiga() + 9), ano, 9, posicaoToClassificacaoNacional(9), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(10, new Clube(Liga.GENEBE.getIdBaseLiga() + 10), ano, 10, posicaoToClassificacaoNacional(10), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(11, new Clube(Liga.GENEBE.getIdBaseLiga() + 11), ano, 11, posicaoToClassificacaoNacional(11), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(12, new Clube(Liga.GENEBE.getIdBaseLiga() + 12), ano, 12, posicaoToClassificacaoNacional(12), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(13, new Clube(Liga.GENEBE.getIdBaseLiga() + 13), ano, 13, posicaoToClassificacaoNacional(13), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(14, new Clube(Liga.GENEBE.getIdBaseLiga() + 14), ano, 14, posicaoToClassificacaoNacional(14), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(15, new Clube(Liga.GENEBE.getIdBaseLiga() + 15), ano, 15, posicaoToClassificacaoNacional(15), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(16, new Clube(Liga.GENEBE.getIdBaseLiga() + 16), ano, 16, posicaoToClassificacaoNacional(16), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(17, new Clube(Liga.GENEBE.getIdBaseLiga() + 17), ano, 17, posicaoToClassificacaoNacional(17), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(18, new Clube(Liga.GENEBE.getIdBaseLiga() + 18), ano, 18, posicaoToClassificacaoNacional(18), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(19, new Clube(Liga.GENEBE.getIdBaseLiga() + 19), ano, 19, posicaoToClassificacaoNacional(19), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(20, new Clube(Liga.GENEBE.getIdBaseLiga() + 20), ano, 20, posicaoToClassificacaoNacional(20), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(21, new Clube(Liga.GENEBE.getIdBaseLiga() + 21), ano, 21, posicaoToClassificacaoNacional(21), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(22, new Clube(Liga.GENEBE.getIdBaseLiga() + 22), ano, 22, posicaoToClassificacaoNacional(22), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(23, new Clube(Liga.GENEBE.getIdBaseLiga() + 23), ano, 23, posicaoToClassificacaoNacional(23), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(24, new Clube(Liga.GENEBE.getIdBaseLiga() + 24), ano, 24, posicaoToClassificacaoNacional(24), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(25, new Clube(Liga.GENEBE.getIdBaseLiga() + 25), ano, 25, posicaoToClassificacaoNacional(25), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(26, new Clube(Liga.GENEBE.getIdBaseLiga() + 26), ano, 26, posicaoToClassificacaoNacional(26), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(27, new Clube(Liga.GENEBE.getIdBaseLiga() + 27), ano, 27, posicaoToClassificacaoNacional(27), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(28, new Clube(Liga.GENEBE.getIdBaseLiga() + 28), ano, 28, posicaoToClassificacaoNacional(28), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(29, new Clube(Liga.GENEBE.getIdBaseLiga() + 29), ano, 29, posicaoToClassificacaoNacional(29), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(30, new Clube(Liga.GENEBE.getIdBaseLiga() + 30), ano, 30, posicaoToClassificacaoNacional(30), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(31, new Clube(Liga.GENEBE.getIdBaseLiga() + 31), ano, 31, posicaoToClassificacaoNacional(31), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(32, new Clube(Liga.GENEBE.getIdBaseLiga() + 32), ano, 32, posicaoToClassificacaoNacional(32), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(33, new Clube(Liga.SPAPOR.getIdBaseLiga() + 1), ano, 1, posicaoToClassificacaoNacional(1), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(34, new Clube(Liga.SPAPOR.getIdBaseLiga() + 2), ano, 2, posicaoToClassificacaoNacional(2), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(35, new Clube(Liga.SPAPOR.getIdBaseLiga() + 3), ano, 3, posicaoToClassificacaoNacional(3), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(36, new Clube(Liga.SPAPOR.getIdBaseLiga() + 4), ano, 4, posicaoToClassificacaoNacional(4), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(37, new Clube(Liga.SPAPOR.getIdBaseLiga() + 5), ano, 5, posicaoToClassificacaoNacional(5), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(38, new Clube(Liga.SPAPOR.getIdBaseLiga() + 6), ano, 6, posicaoToClassificacaoNacional(6), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(39, new Clube(Liga.SPAPOR.getIdBaseLiga() + 7), ano, 7, posicaoToClassificacaoNacional(7), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(40, new Clube(Liga.SPAPOR.getIdBaseLiga() + 8), ano, 8, posicaoToClassificacaoNacional(8), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(41, new Clube(Liga.SPAPOR.getIdBaseLiga() + 9), ano, 9, posicaoToClassificacaoNacional(9), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(42, new Clube(Liga.SPAPOR.getIdBaseLiga() + 10), ano, 10, posicaoToClassificacaoNacional(10), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(43, new Clube(Liga.SPAPOR.getIdBaseLiga() + 11), ano, 11, posicaoToClassificacaoNacional(11), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(44, new Clube(Liga.SPAPOR.getIdBaseLiga() + 12), ano, 12, posicaoToClassificacaoNacional(12), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(45, new Clube(Liga.SPAPOR.getIdBaseLiga() + 13), ano, 13, posicaoToClassificacaoNacional(13), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(46, new Clube(Liga.SPAPOR.getIdBaseLiga() + 14), ano, 14, posicaoToClassificacaoNacional(14), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(47, new Clube(Liga.SPAPOR.getIdBaseLiga() + 15), ano, 15, posicaoToClassificacaoNacional(15), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(48, new Clube(Liga.SPAPOR.getIdBaseLiga() + 16), ano, 16, posicaoToClassificacaoNacional(16), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(49, new Clube(Liga.SPAPOR.getIdBaseLiga() + 17), ano, 17, posicaoToClassificacaoNacional(17), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(50, new Clube(Liga.SPAPOR.getIdBaseLiga() + 18), ano, 18, posicaoToClassificacaoNacional(18), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(51, new Clube(Liga.SPAPOR.getIdBaseLiga() + 19), ano, 19, posicaoToClassificacaoNacional(19), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(52, new Clube(Liga.SPAPOR.getIdBaseLiga() + 20), ano, 20, posicaoToClassificacaoNacional(20), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(53, new Clube(Liga.SPAPOR.getIdBaseLiga() + 21), ano, 21, posicaoToClassificacaoNacional(21), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(54, new Clube(Liga.SPAPOR.getIdBaseLiga() + 22), ano, 22, posicaoToClassificacaoNacional(22), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(55, new Clube(Liga.SPAPOR.getIdBaseLiga() + 23), ano, 23, posicaoToClassificacaoNacional(23), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(56, new Clube(Liga.SPAPOR.getIdBaseLiga() + 24), ano, 24, posicaoToClassificacaoNacional(24), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(57, new Clube(Liga.SPAPOR.getIdBaseLiga() + 25), ano, 25, posicaoToClassificacaoNacional(25), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(58, new Clube(Liga.SPAPOR.getIdBaseLiga() + 26), ano, 26, posicaoToClassificacaoNacional(26), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(59, new Clube(Liga.SPAPOR.getIdBaseLiga() + 27), ano, 27, posicaoToClassificacaoNacional(27), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(60, new Clube(Liga.SPAPOR.getIdBaseLiga() + 28), ano, 28, posicaoToClassificacaoNacional(28), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(61, new Clube(Liga.SPAPOR.getIdBaseLiga() + 29), ano, 29, posicaoToClassificacaoNacional(29), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(62, new Clube(Liga.SPAPOR.getIdBaseLiga() + 30), ano, 30, posicaoToClassificacaoNacional(30), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(63, new Clube(Liga.SPAPOR.getIdBaseLiga() + 31), ano, 31, posicaoToClassificacaoNacional(31), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(64, new Clube(Liga.SPAPOR.getIdBaseLiga() + 32), ano, 32, posicaoToClassificacaoNacional(32), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(65, new Clube(Liga.ITAFRA.getIdBaseLiga() + 1), ano, 1, posicaoToClassificacaoNacional(1), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(66, new Clube(Liga.ITAFRA.getIdBaseLiga() + 2), ano, 2, posicaoToClassificacaoNacional(2), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(67, new Clube(Liga.ITAFRA.getIdBaseLiga() + 3), ano, 3, posicaoToClassificacaoNacional(3), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(68, new Clube(Liga.ITAFRA.getIdBaseLiga() + 4), ano, 4, posicaoToClassificacaoNacional(4), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(69, new Clube(Liga.ITAFRA.getIdBaseLiga() + 5), ano, 5, posicaoToClassificacaoNacional(5), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(70, new Clube(Liga.ITAFRA.getIdBaseLiga() + 6), ano, 6, posicaoToClassificacaoNacional(6), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(71, new Clube(Liga.ITAFRA.getIdBaseLiga() + 7), ano, 7, posicaoToClassificacaoNacional(7), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(72, new Clube(Liga.ITAFRA.getIdBaseLiga() + 8), ano, 8, posicaoToClassificacaoNacional(8), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(73, new Clube(Liga.ITAFRA.getIdBaseLiga() + 9), ano, 9, posicaoToClassificacaoNacional(9), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(74, new Clube(Liga.ITAFRA.getIdBaseLiga() + 10), ano, 10, posicaoToClassificacaoNacional(10), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(75, new Clube(Liga.ITAFRA.getIdBaseLiga() + 11), ano, 11, posicaoToClassificacaoNacional(11), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(76, new Clube(Liga.ITAFRA.getIdBaseLiga() + 12), ano, 12, posicaoToClassificacaoNacional(12), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(77, new Clube(Liga.ITAFRA.getIdBaseLiga() + 13), ano, 13, posicaoToClassificacaoNacional(13), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(78, new Clube(Liga.ITAFRA.getIdBaseLiga() + 14), ano, 14, posicaoToClassificacaoNacional(14), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(79, new Clube(Liga.ITAFRA.getIdBaseLiga() + 15), ano, 15, posicaoToClassificacaoNacional(15), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(80, new Clube(Liga.ITAFRA.getIdBaseLiga() + 16), ano, 16, posicaoToClassificacaoNacional(16), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(81, new Clube(Liga.ITAFRA.getIdBaseLiga() + 17), ano, 17, posicaoToClassificacaoNacional(17), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(82, new Clube(Liga.ITAFRA.getIdBaseLiga() + 18), ano, 18, posicaoToClassificacaoNacional(18), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(83, new Clube(Liga.ITAFRA.getIdBaseLiga() + 19), ano, 19, posicaoToClassificacaoNacional(19), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(84, new Clube(Liga.ITAFRA.getIdBaseLiga() + 20), ano, 20, posicaoToClassificacaoNacional(20), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(85, new Clube(Liga.ITAFRA.getIdBaseLiga() + 21), ano, 21, posicaoToClassificacaoNacional(21), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(86, new Clube(Liga.ITAFRA.getIdBaseLiga() + 22), ano, 22, posicaoToClassificacaoNacional(22), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(87, new Clube(Liga.ITAFRA.getIdBaseLiga() + 23), ano, 23, posicaoToClassificacaoNacional(23), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(88, new Clube(Liga.ITAFRA.getIdBaseLiga() + 24), ano, 24, posicaoToClassificacaoNacional(24), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(89, new Clube(Liga.ITAFRA.getIdBaseLiga() + 25), ano, 25, posicaoToClassificacaoNacional(25), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(90, new Clube(Liga.ITAFRA.getIdBaseLiga() + 26), ano, 26, posicaoToClassificacaoNacional(26), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(91, new Clube(Liga.ITAFRA.getIdBaseLiga() + 27), ano, 27, posicaoToClassificacaoNacional(27), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(92, new Clube(Liga.ITAFRA.getIdBaseLiga() + 28), ano, 28, posicaoToClassificacaoNacional(28), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(93, new Clube(Liga.ITAFRA.getIdBaseLiga() + 29), ano, 29, posicaoToClassificacaoNacional(29), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(94, new Clube(Liga.ITAFRA.getIdBaseLiga() + 30), ano, 30, posicaoToClassificacaoNacional(30), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(95, new Clube(Liga.ITAFRA.getIdBaseLiga() + 31), ano, 31, posicaoToClassificacaoNacional(31), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(96, new Clube(Liga.ITAFRA.getIdBaseLiga() + 32), ano, 32, posicaoToClassificacaoNacional(32), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(97, new Clube(Liga.ENGLND.getIdBaseLiga() + 1), ano, 1, posicaoToClassificacaoNacional(1), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(98, new Clube(Liga.ENGLND.getIdBaseLiga() + 2), ano, 2, posicaoToClassificacaoNacional(2), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(99, new Clube(Liga.ENGLND.getIdBaseLiga() + 3), ano, 3, posicaoToClassificacaoNacional(3), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(100, new Clube(Liga.ENGLND.getIdBaseLiga() + 4), ano, 4, posicaoToClassificacaoNacional(4), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(101, new Clube(Liga.ENGLND.getIdBaseLiga() + 5), ano, 5, posicaoToClassificacaoNacional(5), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(102, new Clube(Liga.ENGLND.getIdBaseLiga() + 6), ano, 6, posicaoToClassificacaoNacional(6), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(103, new Clube(Liga.ENGLND.getIdBaseLiga() + 7), ano, 7, posicaoToClassificacaoNacional(7), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(104, new Clube(Liga.ENGLND.getIdBaseLiga() + 8), ano, 8, posicaoToClassificacaoNacional(8), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(105, new Clube(Liga.ENGLND.getIdBaseLiga() + 9), ano, 9, posicaoToClassificacaoNacional(9), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(106, new Clube(Liga.ENGLND.getIdBaseLiga() + 10), ano, 10, posicaoToClassificacaoNacional(10), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(107, new Clube(Liga.ENGLND.getIdBaseLiga() + 11), ano, 11, posicaoToClassificacaoNacional(11), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(108, new Clube(Liga.ENGLND.getIdBaseLiga() + 12), ano, 12, posicaoToClassificacaoNacional(12), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(109, new Clube(Liga.ENGLND.getIdBaseLiga() + 13), ano, 13, posicaoToClassificacaoNacional(13), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(110, new Clube(Liga.ENGLND.getIdBaseLiga() + 14), ano, 14, posicaoToClassificacaoNacional(14), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(111, new Clube(Liga.ENGLND.getIdBaseLiga() + 15), ano, 15, posicaoToClassificacaoNacional(15), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(112, new Clube(Liga.ENGLND.getIdBaseLiga() + 16), ano, 16, posicaoToClassificacaoNacional(16), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(113, new Clube(Liga.ENGLND.getIdBaseLiga() + 17), ano, 17, posicaoToClassificacaoNacional(17), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(114, new Clube(Liga.ENGLND.getIdBaseLiga() + 18), ano, 18, posicaoToClassificacaoNacional(18), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(115, new Clube(Liga.ENGLND.getIdBaseLiga() + 19), ano, 19, posicaoToClassificacaoNacional(19), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(116, new Clube(Liga.ENGLND.getIdBaseLiga() + 20), ano, 20, posicaoToClassificacaoNacional(20), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(117, new Clube(Liga.ENGLND.getIdBaseLiga() + 21), ano, 21, posicaoToClassificacaoNacional(21), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(118, new Clube(Liga.ENGLND.getIdBaseLiga() + 22), ano, 22, posicaoToClassificacaoNacional(22), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(119, new Clube(Liga.ENGLND.getIdBaseLiga() + 23), ano, 23, posicaoToClassificacaoNacional(23), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(120, new Clube(Liga.ENGLND.getIdBaseLiga() + 24), ano, 24, posicaoToClassificacaoNacional(24), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(121, new Clube(Liga.ENGLND.getIdBaseLiga() + 25), ano, 25, posicaoToClassificacaoNacional(25), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(122, new Clube(Liga.ENGLND.getIdBaseLiga() + 26), ano, 26, posicaoToClassificacaoNacional(26), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(123, new Clube(Liga.ENGLND.getIdBaseLiga() + 27), ano, 27, posicaoToClassificacaoNacional(27), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(124, new Clube(Liga.ENGLND.getIdBaseLiga() + 28), ano, 28, posicaoToClassificacaoNacional(28), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(125, new Clube(Liga.ENGLND.getIdBaseLiga() + 29), ano, 29, posicaoToClassificacaoNacional(29), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(126, new Clube(Liga.ENGLND.getIdBaseLiga() + 30), ano, 30, posicaoToClassificacaoNacional(30), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(127, new Clube(Liga.ENGLND.getIdBaseLiga() + 31), ano, 31, posicaoToClassificacaoNacional(31), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(128, new Clube(Liga.ENGLND.getIdBaseLiga() + 32), ano, 32, posicaoToClassificacaoNacional(32), ClassificacaoCopaNacional.NAO_PARTICIPOU, ClassificacaoContinental.NAO_PARTICIPOU));*/
			
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

	private ClassificacaoNacional posicaoToClassificacaoNacional(Integer pos) {
		return ClassificacaoNacional.getClassificacao(pos > 16 ? NivelCampeonato.NACIONAL_II : NivelCampeonato.NACIONAL,
				pos % 16 == 0 ? 16 : pos % 16);
	}
}

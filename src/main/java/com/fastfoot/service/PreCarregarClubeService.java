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
					c.getNivelNacional().getCaixaInicial(), "Caixa Inicial"));
		}

		movimentacaoFinanceiraRepository.saveAll(movimentacoesFinanceiras);
	}

	private List<Clube> inserirClubes() {
		
		List<Clube> clubes = new ArrayList<Clube>();
		
		if (clubeRepository.count() == 0) {

			/*clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 1, Liga.GENEBE, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, /*"Bayern München"* / "Bayern Munich"));
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
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 17, Liga.GENEBE, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, /*"Colônia"* / "FC Koln"));
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
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 1, Liga.ITAFRA, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Internazionale" /*"Inter Milan"* /));
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
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 11, Liga.ENGLND, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, /*"Wolverhampton Wanderers"* / "Wolves"));
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
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 32, Liga.ENGLND, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Blackpool"));*/
			
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 1, Liga.GENEBE, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Bayern Munich", "bayern_munich.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 2, Liga.GENEBE, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Ajax", "ajax.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 3, Liga.GENEBE, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Borussia Dortmund", "borussia_dortmund.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 4, Liga.GENEBE, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Bayer Leverkusen", "bayer_leverkusen.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 5, Liga.GENEBE, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "PSV", "psv.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 6, Liga.GENEBE, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "RB Leipzig", "rb_leipzig.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 7, Liga.GENEBE, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Wolfsburg", "wolfsburg.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 8, Liga.GENEBE, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Borussia M'gladbach", "borussia_monchengladbach.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 9, Liga.GENEBE, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Club Brugge", "club_brugge.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 10, Liga.GENEBE, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Eintracht Frankfurt", "eintracht_frankfurt.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 11, Liga.GENEBE, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Freiburg", "freiburg.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 12, Liga.GENEBE, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Hoffenheim", "hoffenheim.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 13, Liga.GENEBE, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Feyenoord", "feyenoord.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 14, Liga.GENEBE, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "AZ Alkmaar", "az_alkmaar.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 15, Liga.GENEBE, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Union Berlin", "union_berlin.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 16, Liga.GENEBE, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Mainz 05", "mainz_05.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 17, Liga.GENEBE, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "FC Koln", "fc_koln.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 18, Liga.GENEBE, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Schalke 04", "schalke_04.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 19, Liga.GENEBE, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Hertha Berlin", "hertha_berlin.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 20, Liga.GENEBE, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "VfB Stuttgart", "vfb_stuttgart.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 21, Liga.GENEBE, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Vitesse", "vitesse.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 22, Liga.GENEBE, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Fortuna Düsseldorf", "fortuna_dusseldorf.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 23, Liga.GENEBE, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Hamburgo", "hamburgo.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 24, Liga.GENEBE, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Utrecht", "utrecht.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 25, Liga.GENEBE, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Gent", "gent.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 26, Liga.GENEBE, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Antwerp", "antwerp.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 27, Liga.GENEBE, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Arminia Bielefeld", "arminia_bielefeld.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 28, Liga.GENEBE, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Augsburg", "augsburg.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 29, Liga.GENEBE, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Anderlecht", "anderlecht.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 30, Liga.GENEBE, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Bochum", "bochum.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 31, Liga.GENEBE, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Racing Genk", "racing_genk.png"));
			clubes.add(new Clube(Liga.GENEBE.getIdBaseLiga() + 32, Liga.GENEBE, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Werder Bremen", "werder_bremen.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 1, Liga.SPAPOR, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Real Madrid", "real_madrid.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 2, Liga.SPAPOR, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Barcelona", "barcelona.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 3, Liga.SPAPOR, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Porto", "porto.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 4, Liga.SPAPOR, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Atlético Madrid", "atletico_madrid.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 5, Liga.SPAPOR, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Benfica", "benfica.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 6, Liga.SPAPOR, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Sevilla", "sevilla.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 7, Liga.SPAPOR, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Sporting", "sporting.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 8, Liga.SPAPOR, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Real Betis", "real_betis.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 9, Liga.SPAPOR, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Valencia", "valencia.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 10, Liga.SPAPOR, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Real Sociedad", "real_sociedad.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 11, Liga.SPAPOR, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Villarreal", "villarreal.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 12, Liga.SPAPOR, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Athletic Bilbao", "athletic_bilbao.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 13, Liga.SPAPOR, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Getafe", "getafe.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 14, Liga.SPAPOR, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Celta de Vigo", "celta_de_vigo.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 15, Liga.SPAPOR, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Rayo Vallecano", "rayo_vallecano.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 16, Liga.SPAPOR, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Osasuna", "osasuna.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 17, Liga.SPAPOR, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Real Valladolid", "real_valladolid.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 18, Liga.SPAPOR, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Granada", "granada.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 19, Liga.SPAPOR, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Espanyol", "espanyol.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 20, Liga.SPAPOR, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Mallorca", "mallorca.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 21, Liga.SPAPOR, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Almería", "almeria.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 22, Liga.SPAPOR, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Alaves", "alaves.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 23, Liga.SPAPOR, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Elche", "elche.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 24, Liga.SPAPOR, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Braga", "braga.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 25, Liga.SPAPOR, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Levante", "levante.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 26, Liga.SPAPOR, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Cádiz", "cadiz.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 27, Liga.SPAPOR, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Leganes", "leganes.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 28, Liga.SPAPOR, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Sporting Gijón", "sporting_gijon.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 29, Liga.SPAPOR, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Girona", "girona.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 30, Liga.SPAPOR, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Eibar", "eibar.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 31, Liga.SPAPOR, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Zaragoza", "zaragoza.png"));
			clubes.add(new Clube(Liga.SPAPOR.getIdBaseLiga() + 32, Liga.SPAPOR, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Málaga", "malaga.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 1, Liga.ITAFRA, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Internazionale", "internazionale.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 2, Liga.ITAFRA, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Juventus", "juventus.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 3, Liga.ITAFRA, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Paris Saint-Germain", "paris_saint_germain.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 4, Liga.ITAFRA, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Napoli", "napoli.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 5, Liga.ITAFRA, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Milan", "milan.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 6, Liga.ITAFRA, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Atalanta", "atalanta.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 7, Liga.ITAFRA, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Olympique de Marseille", "olympique_de_marseille.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 8, Liga.ITAFRA, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Lazio", "lazio.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 9, Liga.ITAFRA, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Lyon", "lyon.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 10, Liga.ITAFRA, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Roma", "roma.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 11, Liga.ITAFRA, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Lille", "lille.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 12, Liga.ITAFRA, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Monaco", "monaco.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 13, Liga.ITAFRA, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Fiorentina", "fiorentina.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 14, Liga.ITAFRA, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Sassuolo", "sassuolo.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 15, Liga.ITAFRA, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Rennes", "rennes.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 16, Liga.ITAFRA, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Verona", "verona.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 17, Liga.ITAFRA, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "RC Strasbourg", "rc_strasbourg.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 18, Liga.ITAFRA, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Montpellier", "montpellier.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 19, Liga.ITAFRA, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Bologna", "bologna.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 20, Liga.ITAFRA, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Nice", "nice.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 21, Liga.ITAFRA, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Lens", "lens.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 22, Liga.ITAFRA, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Torino", "torino.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 23, Liga.ITAFRA, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Udinese", "udinese.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 24, Liga.ITAFRA, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Empoli", "empoli.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 25, Liga.ITAFRA, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Sampdoria", "sampdoria.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 26, Liga.ITAFRA, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Stade Brestois", "stade_brestois.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 27, Liga.ITAFRA, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Nantes", "nantes.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 28, Liga.ITAFRA, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Saint-Étienne", "saint_etienne.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 29, Liga.ITAFRA, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Bordeaux", "bordeaux.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 30, Liga.ITAFRA, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Reims", "reims.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 31, Liga.ITAFRA, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Genoa", "genoa.png"));
			clubes.add(new Clube(Liga.ITAFRA.getIdBaseLiga() + 32, Liga.ITAFRA, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Venezia", "venezia.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 1, Liga.ENGLND, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Liverpool", "liverpool.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 2, Liga.ENGLND, ClubeNivel.NIVEL_A, ClubeNivel.NIVEL_A, "Manchester United", "manchester_united.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 3, Liga.ENGLND, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Manchester City", "manchester_city.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 4, Liga.ENGLND, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Chelsea", "chelsea.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 5, Liga.ENGLND, ClubeNivel.NIVEL_B, ClubeNivel.NIVEL_B, "Arsenal", "arsenal.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 6, Liga.ENGLND, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "West Ham", "west_ham.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 7, Liga.ENGLND, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Tottenham", "tottenham.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 8, Liga.ENGLND, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Leicester City", "leicester_city.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 9, Liga.ENGLND, ClubeNivel.NIVEL_C, ClubeNivel.NIVEL_C, "Aston Villa", "aston_villa.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 10, Liga.ENGLND, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Rangers", "rangers.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 11, Liga.ENGLND, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Wolves", "wolves.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 12, Liga.ENGLND, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Crystal Palace", "crystal_palace.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 13, Liga.ENGLND, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Everton", "everton.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 14, Liga.ENGLND, ClubeNivel.NIVEL_D, ClubeNivel.NIVEL_D, "Newcastle", "newcastle.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 15, Liga.ENGLND, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Leeds United", "leeds_united.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 16, Liga.ENGLND, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Brighton", "brighton.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 17, Liga.ENGLND, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Southampton", "southampton.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 18, Liga.ENGLND, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Burnley", "burnley.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 19, Liga.ENGLND, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Watford", "watford.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 20, Liga.ENGLND, ClubeNivel.NIVEL_E, ClubeNivel.NIVEL_E, "Celtic", "celtic.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 21, Liga.ENGLND, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Norwich City", "norwich_city.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 22, Liga.ENGLND, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Brentford", "brentford.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 23, Liga.ENGLND, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Fulham", "fulham.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 24, Liga.ENGLND, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Bournemouth", "bournemouth.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 25, Liga.ENGLND, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Sheffield United", "sheffield_united.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 26, Liga.ENGLND, ClubeNivel.NIVEL_F, ClubeNivel.NIVEL_F, "Blackburn Rovers", "blackburn_rovers.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 27, Liga.ENGLND, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Nottingham Forest", "nottingham_forest.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 28, Liga.ENGLND, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Hull City", "hull_city.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 29, Liga.ENGLND, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Stoke City", "stoke_city.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 30, Liga.ENGLND, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Swansea City", "swansea_city.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 31, Liga.ENGLND, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "West Bromwich", "west_bromwich.png"));
			clubes.add(new Clube(Liga.ENGLND.getIdBaseLiga() + 32, Liga.ENGLND, ClubeNivel.NIVEL_G, ClubeNivel.NIVEL_G, "Blackpool", "blackpool.png"));


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

			clubeRankingRepository.saveAll(ranking);
		}
	}

	private ClassificacaoNacional posicaoToClassificacaoNacional(Integer pos) {
		return ClassificacaoNacional.getClassificacao(pos > 16 ? NivelCampeonato.NACIONAL_II : NivelCampeonato.NACIONAL,
				pos % 16 == 0 ? 16 : pos % 16);
	}
}

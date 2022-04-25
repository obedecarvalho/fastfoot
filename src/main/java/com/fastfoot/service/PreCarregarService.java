package com.fastfoot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.entity.Clube;
import com.fastfoot.model.repository.ClubeRepository;
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
public class PreCarregarService {
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private ClubeRankingRepository clubeRankingRepository;

	public void preCarregarClubes () {
		inserirClubes();
		inserirClubesRanking();
	}

	private void inserirClubes() {
		
		if (clubeRepository.findAll().size() == 0) {
		
			List<Clube> clubes = new ArrayList<Clube>();
	
			clubes.add(new Clube(1, Liga.GENEBE, 85, "Bayern München"));
			clubes.add(new Clube(2, Liga.GENEBE, 85, "Ajax"));
			clubes.add(new Clube(3, Liga.GENEBE, 82, "Borussia Dortmund"));
			clubes.add(new Clube(4, Liga.GENEBE, 82, "Bayer Leverkusen"));
			clubes.add(new Clube(5, Liga.GENEBE, 82, "PSV"));
			clubes.add(new Clube(6, Liga.GENEBE, 79, "RB Leipzig"));
			clubes.add(new Clube(7, Liga.GENEBE, 79, "Feyenoord"));
			clubes.add(new Clube(8, Liga.GENEBE, 79, "Borussia Mönchengladbach"));
			clubes.add(new Clube(9, Liga.GENEBE, 79, "Club Brugge"));
			clubes.add(new Clube(10, Liga.GENEBE, 76, "Eintracht Frankfurt"));
			clubes.add(new Clube(11, Liga.GENEBE, 76, "Freiburg"));
			clubes.add(new Clube(12, Liga.GENEBE, 76, "Union Berlin"));
			clubes.add(new Clube(13, Liga.GENEBE, 76, "Wolfsburg"));
			clubes.add(new Clube(14, Liga.GENEBE, 76, "AZ Alkmaar"));
			clubes.add(new Clube(15, Liga.GENEBE, 73, "Hoffenheim"));
			clubes.add(new Clube(16, Liga.GENEBE, 73, "Mainz 05"));
			clubes.add(new Clube(17, Liga.GENEBE, 73, "Colônia"));
			clubes.add(new Clube(18, Liga.GENEBE, 73, "Utrecht"));
			clubes.add(new Clube(19, Liga.GENEBE, 73, "Hertha Berlin"));
			clubes.add(new Clube(20, Liga.GENEBE, 73, "VfB Stuttgart"));
			clubes.add(new Clube(21, Liga.GENEBE, 70, "Vitesse"));
			clubes.add(new Clube(22, Liga.GENEBE, 70, "Fortuna Düsseldorf"));
			clubes.add(new Clube(23, Liga.GENEBE, 70, "Hamburgo"));
			clubes.add(new Clube(24, Liga.GENEBE, 70, "Ingolstadt"));
			clubes.add(new Clube(25, Liga.GENEBE, 70, "Gent"));
			clubes.add(new Clube(26, Liga.GENEBE, 70, "Antwerp"));
			clubes.add(new Clube(27, Liga.GENEBE, 67, "Arminia Bielefeld"));
			clubes.add(new Clube(28, Liga.GENEBE, 67, "Augsburg"));
			clubes.add(new Clube(29, Liga.GENEBE, 67, "Anderlecht"));
			clubes.add(new Clube(30, Liga.GENEBE, 67, "Bochum"));
			clubes.add(new Clube(31, Liga.GENEBE, 67, "Racing Genk"));
			clubes.add(new Clube(32, Liga.GENEBE, 67, "Werder Bremen"));
			clubes.add(new Clube(33, Liga.SPAPOR, 85, "Real Madrid"));
			clubes.add(new Clube(34, Liga.SPAPOR, 85, "Barcelona"));
			clubes.add(new Clube(35, Liga.SPAPOR, 82, "Porto"));
			clubes.add(new Clube(36, Liga.SPAPOR, 82, "Atlético Madrid"));
			clubes.add(new Clube(37, Liga.SPAPOR, 82, "Benfica"));
			clubes.add(new Clube(38, Liga.SPAPOR, 79, "Sevilla"));
			clubes.add(new Clube(39, Liga.SPAPOR, 79, "Sporting"));
			clubes.add(new Clube(40, Liga.SPAPOR, 79, "Real Betis"));
			clubes.add(new Clube(41, Liga.SPAPOR, 79, "Valencia"));
			clubes.add(new Clube(42, Liga.SPAPOR, 76, "Real Sociedad"));
			clubes.add(new Clube(43, Liga.SPAPOR, 76, "Villarreal"));
			clubes.add(new Clube(44, Liga.SPAPOR, 76, "Athletic Bilbao"));
			clubes.add(new Clube(45, Liga.SPAPOR, 76, "Braga"));
			clubes.add(new Clube(46, Liga.SPAPOR, 76, "Celta de Vigo"));
			clubes.add(new Clube(47, Liga.SPAPOR, 73, "Rayo Vallecano"));
			clubes.add(new Clube(48, Liga.SPAPOR, 73, "Osasuna"));
			clubes.add(new Clube(49, Liga.SPAPOR, 73, "Leganes"));
			clubes.add(new Clube(50, Liga.SPAPOR, 73, "Granada"));
			clubes.add(new Clube(51, Liga.SPAPOR, 73, "Espanyol"));
			clubes.add(new Clube(52, Liga.SPAPOR, 73, "Mallorca"));
			clubes.add(new Clube(53, Liga.SPAPOR, 70, "Huesca"));
			clubes.add(new Clube(54, Liga.SPAPOR, 70, "Alaves"));
			clubes.add(new Clube(55, Liga.SPAPOR, 70, "Elche"));
			clubes.add(new Clube(56, Liga.SPAPOR, 70, "Getafe"));
			clubes.add(new Clube(57, Liga.SPAPOR, 70, "Girona"));
			clubes.add(new Clube(58, Liga.SPAPOR, 70, "Eibar"));
			clubes.add(new Clube(59, Liga.SPAPOR, 67, "Real Valladolid"));
			clubes.add(new Clube(60, Liga.SPAPOR, 67, "Sporting Gijón"));
			clubes.add(new Clube(61, Liga.SPAPOR, 67, "Levante"));
			clubes.add(new Clube(62, Liga.SPAPOR, 67, "Cádiz"));
			clubes.add(new Clube(63, Liga.SPAPOR, 67, "Zaragoza"));
			clubes.add(new Clube(64, Liga.SPAPOR, 67, "Málaga"));
			clubes.add(new Clube(65, Liga.ITAFRA, 85, "Internazionale" /*"Inter Milan"*/));
			clubes.add(new Clube(66, Liga.ITAFRA, 85, "Juventus"));
			clubes.add(new Clube(67, Liga.ITAFRA, 82, "Paris Saint-Germain"));
			clubes.add(new Clube(68, Liga.ITAFRA, 82, "Napoli"));
			clubes.add(new Clube(69, Liga.ITAFRA, 82, "Milan"));
			clubes.add(new Clube(70, Liga.ITAFRA, 79, "Atalanta"));
			clubes.add(new Clube(71, Liga.ITAFRA, 79, "Lille"));
			clubes.add(new Clube(72, Liga.ITAFRA, 79, "Lazio"));
			clubes.add(new Clube(73, Liga.ITAFRA, 79, "Lyon"));
			clubes.add(new Clube(74, Liga.ITAFRA, 76, "Roma"));
			clubes.add(new Clube(75, Liga.ITAFRA, 76, "Olympique de Marseille"));
			clubes.add(new Clube(76, Liga.ITAFRA, 76, "Monaco"));
			clubes.add(new Clube(77, Liga.ITAFRA, 76, "Fiorentina"));
			clubes.add(new Clube(78, Liga.ITAFRA, 76, "Sassuolo"));
			clubes.add(new Clube(79, Liga.ITAFRA, 73, "Rennes"));
			clubes.add(new Clube(80, Liga.ITAFRA, 73, "Verona"));
			clubes.add(new Clube(81, Liga.ITAFRA, 73, "RC Strasbourg"));
			clubes.add(new Clube(82, Liga.ITAFRA, 73, "Montpellier"));
			clubes.add(new Clube(83, Liga.ITAFRA, 73, "Bologna"));
			clubes.add(new Clube(84, Liga.ITAFRA, 73, "Nice"));
			clubes.add(new Clube(85, Liga.ITAFRA, 70, "Lens"));
			clubes.add(new Clube(86, Liga.ITAFRA, 70, "Torino"));
			clubes.add(new Clube(87, Liga.ITAFRA, 70, "Udinese"));
			clubes.add(new Clube(88, Liga.ITAFRA, 70, "Empoli"));
			clubes.add(new Clube(89, Liga.ITAFRA, 70, "Sampdoria"));
			clubes.add(new Clube(90, Liga.ITAFRA, 70, "Stade Brestois"));
			clubes.add(new Clube(91, Liga.ITAFRA, 67, "Nantes"));
			clubes.add(new Clube(92, Liga.ITAFRA, 67, "Carpi"));
			clubes.add(new Clube(93, Liga.ITAFRA, 67, "Angers"));
			clubes.add(new Clube(94, Liga.ITAFRA, 67, "Reims"));
			clubes.add(new Clube(95, Liga.ITAFRA, 67, "Genoa"));
			clubes.add(new Clube(96, Liga.ITAFRA, 67, "Venezia"));
			//Rangers, Celtic, Aberdeen, Motherwell
			clubes.add(new Clube(97, Liga.ENGLND, 85, "Liverpool"));
			clubes.add(new Clube(98, Liga.ENGLND, 85, "Manchester United"));
			clubes.add(new Clube(99, Liga.ENGLND, 82, "Manchester City"));
			clubes.add(new Clube(100, Liga.ENGLND, 82, "Chelsea"));
			clubes.add(new Clube(101, Liga.ENGLND, 82, "Arsenal"));
			clubes.add(new Clube(102, Liga.ENGLND, 79, "West Ham"));
			clubes.add(new Clube(103, Liga.ENGLND, 79, "Tottenham"));
			clubes.add(new Clube(104, Liga.ENGLND, 79, "Leicester City"));
			clubes.add(new Clube(105, Liga.ENGLND, 79, "Leeds United"));
			clubes.add(new Clube(106, Liga.ENGLND, 76, "Aston Villa"));
			clubes.add(new Clube(107, Liga.ENGLND, 76, "Brighton"));
			clubes.add(new Clube(108, Liga.ENGLND, 76, "Wolverhampton Wanderers"));
			clubes.add(new Clube(109, Liga.ENGLND, 76, "Crystal Palace"));
			clubes.add(new Clube(110, Liga.ENGLND, 76, "Everton"));
			clubes.add(new Clube(111, Liga.ENGLND, 73, "Southampton"));
			clubes.add(new Clube(112, Liga.ENGLND, 73, "Burnley"));
			clubes.add(new Clube(113, Liga.ENGLND, 73, "Watford"));
			clubes.add(new Clube(114, Liga.ENGLND, 73, "Newcastle"));
			clubes.add(new Clube(115, Liga.ENGLND, 73, "Brentford"));
			clubes.add(new Clube(116, Liga.ENGLND, 73, "Stoke City"));
			clubes.add(new Clube(117, Liga.ENGLND, 70, "Swansea City"));
			clubes.add(new Clube(118, Liga.ENGLND, 70, "Wigan"));
			clubes.add(new Clube(119, Liga.ENGLND, 70, "Bournemouth"));
			clubes.add(new Clube(120, Liga.ENGLND, 70, "Birmingham"));
			clubes.add(new Clube(121, Liga.ENGLND, 70, "West Bromwich"));
			clubes.add(new Clube(122, Liga.ENGLND, 70, "Bolton Wanderers"));
			clubes.add(new Clube(123, Liga.ENGLND, 67, "Cardiff"));
			clubes.add(new Clube(124, Liga.ENGLND, 67, "Hull City"));
			clubes.add(new Clube(125, Liga.ENGLND, 67, "Blackpool"));
			clubes.add(new Clube(126, Liga.ENGLND, 67, "Sheffield United"));
			clubes.add(new Clube(127, Liga.ENGLND, 67, "Blackburn Rovers"));
			clubes.add(new Clube(128, Liga.ENGLND, 67, "Fulham"));
			
			clubeRepository.saveAll(clubes);
		}
	}
	
	private void inserirClubesRanking() {
		
		if (clubeRankingRepository.findAll().isEmpty()) {
			List<ClubeRanking> ranking = new ArrayList<ClubeRanking>();
			
			int ano = Constantes.ANO_INICIAL - 1;
			
			ranking.add(new ClubeRanking(1, new Clube(1), ano, 1, posicaoToClassificacaoNacional(1), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(2, new Clube(2), ano, 2, posicaoToClassificacaoNacional(2), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(3, new Clube(3), ano, 3, posicaoToClassificacaoNacional(3), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(4, new Clube(4), ano, 4, posicaoToClassificacaoNacional(4), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(5, new Clube(5), ano, 5, posicaoToClassificacaoNacional(5), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(6, new Clube(6), ano, 6, posicaoToClassificacaoNacional(6), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(7, new Clube(7), ano, 7, posicaoToClassificacaoNacional(7), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(8, new Clube(8), ano, 8, posicaoToClassificacaoNacional(8), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(9, new Clube(9), ano, 9, posicaoToClassificacaoNacional(9), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(10, new Clube(10), ano, 10, posicaoToClassificacaoNacional(10), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(11, new Clube(11), ano, 11, posicaoToClassificacaoNacional(11), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(12, new Clube(12), ano, 12, posicaoToClassificacaoNacional(12), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(13, new Clube(13), ano, 13, posicaoToClassificacaoNacional(13), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(14, new Clube(14), ano, 14, posicaoToClassificacaoNacional(14), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(15, new Clube(15), ano, 15, posicaoToClassificacaoNacional(15), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(16, new Clube(16), ano, 16, posicaoToClassificacaoNacional(16), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(17, new Clube(17), ano, 17, posicaoToClassificacaoNacional(17), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(18, new Clube(18), ano, 18, posicaoToClassificacaoNacional(18), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(19, new Clube(19), ano, 19, posicaoToClassificacaoNacional(19), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(20, new Clube(20), ano, 20, posicaoToClassificacaoNacional(20), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(21, new Clube(21), ano, 21, posicaoToClassificacaoNacional(21), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(22, new Clube(22), ano, 22, posicaoToClassificacaoNacional(22), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(23, new Clube(23), ano, 23, posicaoToClassificacaoNacional(23), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(24, new Clube(24), ano, 24, posicaoToClassificacaoNacional(24), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(25, new Clube(25), ano, 25, posicaoToClassificacaoNacional(25), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(26, new Clube(26), ano, 26, posicaoToClassificacaoNacional(26), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(27, new Clube(27), ano, 27, posicaoToClassificacaoNacional(27), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(28, new Clube(28), ano, 28, posicaoToClassificacaoNacional(28), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(29, new Clube(29), ano, 29, posicaoToClassificacaoNacional(29), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(30, new Clube(30), ano, 30, posicaoToClassificacaoNacional(30), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(31, new Clube(31), ano, 31, posicaoToClassificacaoNacional(31), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(32, new Clube(32), ano, 32, posicaoToClassificacaoNacional(32), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(33, new Clube(33), ano, 1, posicaoToClassificacaoNacional(1), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(34, new Clube(34), ano, 2, posicaoToClassificacaoNacional(2), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(35, new Clube(35), ano, 3, posicaoToClassificacaoNacional(3), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(36, new Clube(36), ano, 4, posicaoToClassificacaoNacional(4), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(37, new Clube(37), ano, 5, posicaoToClassificacaoNacional(5), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(38, new Clube(38), ano, 6, posicaoToClassificacaoNacional(6), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(39, new Clube(39), ano, 7, posicaoToClassificacaoNacional(7), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(40, new Clube(40), ano, 8, posicaoToClassificacaoNacional(8), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(41, new Clube(41), ano, 9, posicaoToClassificacaoNacional(9), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(42, new Clube(42), ano, 10, posicaoToClassificacaoNacional(10), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(43, new Clube(43), ano, 11, posicaoToClassificacaoNacional(11), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(44, new Clube(44), ano, 12, posicaoToClassificacaoNacional(12), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(45, new Clube(45), ano, 13, posicaoToClassificacaoNacional(13), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(46, new Clube(46), ano, 14, posicaoToClassificacaoNacional(14), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(47, new Clube(47), ano, 15, posicaoToClassificacaoNacional(15), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(48, new Clube(48), ano, 16, posicaoToClassificacaoNacional(16), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(49, new Clube(49), ano, 17, posicaoToClassificacaoNacional(17), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(50, new Clube(50), ano, 18, posicaoToClassificacaoNacional(18), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(51, new Clube(51), ano, 19, posicaoToClassificacaoNacional(19), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(52, new Clube(52), ano, 20, posicaoToClassificacaoNacional(20), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(53, new Clube(53), ano, 21, posicaoToClassificacaoNacional(21), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(54, new Clube(54), ano, 22, posicaoToClassificacaoNacional(22), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(55, new Clube(55), ano, 23, posicaoToClassificacaoNacional(23), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(56, new Clube(56), ano, 24, posicaoToClassificacaoNacional(24), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(57, new Clube(57), ano, 25, posicaoToClassificacaoNacional(25), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(58, new Clube(58), ano, 26, posicaoToClassificacaoNacional(26), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(59, new Clube(59), ano, 27, posicaoToClassificacaoNacional(27), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(60, new Clube(60), ano, 28, posicaoToClassificacaoNacional(28), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(61, new Clube(61), ano, 29, posicaoToClassificacaoNacional(29), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(62, new Clube(62), ano, 30, posicaoToClassificacaoNacional(30), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(63, new Clube(63), ano, 31, posicaoToClassificacaoNacional(31), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(64, new Clube(64), ano, 32, posicaoToClassificacaoNacional(32), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(65, new Clube(65), ano, 1, posicaoToClassificacaoNacional(1), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(66, new Clube(66), ano, 2, posicaoToClassificacaoNacional(2), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(67, new Clube(67), ano, 3, posicaoToClassificacaoNacional(3), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(68, new Clube(68), ano, 4, posicaoToClassificacaoNacional(4), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(69, new Clube(69), ano, 5, posicaoToClassificacaoNacional(5), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(70, new Clube(70), ano, 6, posicaoToClassificacaoNacional(6), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(71, new Clube(71), ano, 7, posicaoToClassificacaoNacional(7), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(72, new Clube(72), ano, 8, posicaoToClassificacaoNacional(8), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(73, new Clube(73), ano, 9, posicaoToClassificacaoNacional(9), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(74, new Clube(74), ano, 10, posicaoToClassificacaoNacional(10), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(75, new Clube(75), ano, 11, posicaoToClassificacaoNacional(11), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(76, new Clube(76), ano, 12, posicaoToClassificacaoNacional(12), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(77, new Clube(77), ano, 13, posicaoToClassificacaoNacional(13), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(78, new Clube(78), ano, 14, posicaoToClassificacaoNacional(14), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(79, new Clube(79), ano, 15, posicaoToClassificacaoNacional(15), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(80, new Clube(80), ano, 16, posicaoToClassificacaoNacional(16), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(81, new Clube(81), ano, 17, posicaoToClassificacaoNacional(17), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(82, new Clube(82), ano, 18, posicaoToClassificacaoNacional(18), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(83, new Clube(83), ano, 19, posicaoToClassificacaoNacional(19), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(84, new Clube(84), ano, 20, posicaoToClassificacaoNacional(20), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(85, new Clube(85), ano, 21, posicaoToClassificacaoNacional(21), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(86, new Clube(86), ano, 22, posicaoToClassificacaoNacional(22), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(87, new Clube(87), ano, 23, posicaoToClassificacaoNacional(23), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(88, new Clube(88), ano, 24, posicaoToClassificacaoNacional(24), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(89, new Clube(89), ano, 25, posicaoToClassificacaoNacional(25), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(90, new Clube(90), ano, 26, posicaoToClassificacaoNacional(26), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(91, new Clube(91), ano, 27, posicaoToClassificacaoNacional(27), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(92, new Clube(92), ano, 28, posicaoToClassificacaoNacional(28), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(93, new Clube(93), ano, 29, posicaoToClassificacaoNacional(29), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(94, new Clube(94), ano, 30, posicaoToClassificacaoNacional(30), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(95, new Clube(95), ano, 31, posicaoToClassificacaoNacional(31), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(96, new Clube(96), ano, 32, posicaoToClassificacaoNacional(32), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(97, new Clube(97), ano, 1, posicaoToClassificacaoNacional(1), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(98, new Clube(98), ano, 2, posicaoToClassificacaoNacional(2), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(99, new Clube(99), ano, 3, posicaoToClassificacaoNacional(3), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(100, new Clube(100), ano, 4, posicaoToClassificacaoNacional(4), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(101, new Clube(101), ano, 5, posicaoToClassificacaoNacional(5), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(102, new Clube(102), ano, 6, posicaoToClassificacaoNacional(6), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(103, new Clube(103), ano, 7, posicaoToClassificacaoNacional(7), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(104, new Clube(104), ano, 8, posicaoToClassificacaoNacional(8), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(105, new Clube(105), ano, 9, posicaoToClassificacaoNacional(9), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(106, new Clube(106), ano, 10, posicaoToClassificacaoNacional(10), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(107, new Clube(107), ano, 11, posicaoToClassificacaoNacional(11), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(108, new Clube(108), ano, 12, posicaoToClassificacaoNacional(12), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(109, new Clube(109), ano, 13, posicaoToClassificacaoNacional(13), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(110, new Clube(110), ano, 14, posicaoToClassificacaoNacional(14), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(111, new Clube(111), ano, 15, posicaoToClassificacaoNacional(15), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(112, new Clube(112), ano, 16, posicaoToClassificacaoNacional(16), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(113, new Clube(113), ano, 17, posicaoToClassificacaoNacional(17), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(114, new Clube(114), ano, 18, posicaoToClassificacaoNacional(18), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(115, new Clube(115), ano, 19, posicaoToClassificacaoNacional(19), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(116, new Clube(116), ano, 20, posicaoToClassificacaoNacional(20), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(117, new Clube(117), ano, 21, posicaoToClassificacaoNacional(21), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(118, new Clube(118), ano, 22, posicaoToClassificacaoNacional(22), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(119, new Clube(119), ano, 23, posicaoToClassificacaoNacional(23), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(120, new Clube(120), ano, 24, posicaoToClassificacaoNacional(24), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(121, new Clube(121), ano, 25, posicaoToClassificacaoNacional(25), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(122, new Clube(122), ano, 26, posicaoToClassificacaoNacional(26), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(123, new Clube(123), ano, 27, posicaoToClassificacaoNacional(27), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(124, new Clube(124), ano, 28, posicaoToClassificacaoNacional(28), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(125, new Clube(125), ano, 29, posicaoToClassificacaoNacional(29), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(126, new Clube(126), ano, 30, posicaoToClassificacaoNacional(30), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(127, new Clube(127), ano, 31, posicaoToClassificacaoNacional(31), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			ranking.add(new ClubeRanking(128, new Clube(128), ano, 32, posicaoToClassificacaoNacional(32), ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU, ClassificacaoContinentalFinal.NAO_PARTICIPOU));
			
			clubeRankingRepository.saveAll(ranking);
		}
	}

	private ClassificacaoNacionalFinal posicaoToClassificacaoNacional(Integer pos) {
		return ClassificacaoNacionalFinal.getClassificacao(pos > 16 ? NivelCampeonato.NACIONAL_II : NivelCampeonato.NACIONAL, pos%16 == 0 ? 16 : pos%16);
	}
}

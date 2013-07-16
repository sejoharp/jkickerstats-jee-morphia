package kickerstats.interfaces;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kickerstats.types.Game;
import kickerstats.usecases.GameServiceInterface;
import kickerstats.usecases.MatchServiceInterface;

import org.jsoup.nodes.Document;

public class KickerStatsUpdater {
	@Inject
	private KickerpageParser kickerpageParser;
	@Inject
	private PageDownloader pageDownloader;
	@Inject
	private MatchServiceInterface matchService;
	@Inject
	private GameServiceInterface gameService;

	private static String SEASONS_URL = "http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe";

	public List<Game> downloadAllGames() {
		List<Game> games = new ArrayList<>();

		List<Integer> seasonIds = getSeasonIDs();
		for (Integer seasonId : seasonIds) {
			List<String> ligaLinks = getLigaLinks(seasonId);
			for (String ligaLink : ligaLinks) {
				List<String> matchLinks = getMatchLinks(ligaLink);
				for (String matchLink : matchLinks) {
					games.addAll(getGames(matchLink));
				}
			}
		}
		return games;
	}

	public void updateStatistik() {
		List<Integer> seasonIds = getSeasonIDs();
		for (Integer seasonId : seasonIds) {
			List<String> ligaLinks = getLigaLinks(seasonId);
			for (String ligaLink : ligaLinks) {
				List<MatchWithLink> matches = getMatches(ligaLink);
				for (MatchWithLink match : matches) {
					if (matchService.isNewMatch(match)) {
						matchService.saveMatch(match);
						gameService.saveGames(getGames(match.getMatchLink()));
					}
				}
			}
		}
	}

	protected List<Game> getGames(String matchLink) {
		System.out.println("matchLink: " + matchLink);
		Document matchDoc = pageDownloader.downloadPage(matchLink);
		return kickerpageParser.findGames(matchDoc);
	}

	protected List<String> getMatchLinks(String ligaLink) {
		System.out.println("ligaLink: " + ligaLink);
		Document ligaDoc = pageDownloader.downloadPage(ligaLink);
		List<String> matchLinks = kickerpageParser.findMatchLinks(ligaDoc);
		return matchLinks;
	}

	protected List<MatchWithLink> getMatches(String ligaLink) {
		System.out.println("ligaLink: " + ligaLink);
		Document ligaDoc = pageDownloader.downloadPage(ligaLink);
		List<MatchWithLink> matchLinks = kickerpageParser.findMatches(ligaDoc);
		return matchLinks;
	}

	protected List<String> getLigaLinks(Integer seasonId) {
		System.out.println("seasonId: " + seasonId);
		Document seasonDoc = pageDownloader.downloadSeason(seasonId);
		List<String> ligaLinks = kickerpageParser.findLigaLinks(seasonDoc);
		return ligaLinks;
	}

	protected List<Integer> getSeasonIDs() {
		Document seasonsDoc = pageDownloader.downloadPage(SEASONS_URL);
		List<Integer> seasonIds = kickerpageParser.findSeasonIDs(seasonsDoc);
		return seasonIds;
	}
}

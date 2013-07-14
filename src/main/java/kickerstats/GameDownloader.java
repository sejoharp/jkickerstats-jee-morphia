package kickerstats;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jsoup.nodes.Document;

public class GameDownloader {
	@Inject
	private KickerpageParser kickerpageParser;
	@Inject
	private PageDownloader pageDownloader;

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

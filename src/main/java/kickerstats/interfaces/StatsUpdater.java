package kickerstats.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import kickerstats.types.Game;
import kickerstats.types.Match;
import kickerstats.usecases.MatchServiceInterface;

import org.jsoup.nodes.Document;

public class StatsUpdater {
	private static final Logger LOG = Logger.getLogger(StatsUpdater.class
			.getName());

	@Inject
	private PageParser kickerpageParser;
	@Inject
	private PageDownloader pageDownloader;
	@Inject
	private MatchServiceInterface matchService;

	private static String SEASONS_URL = "http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe";

	public void updateStats() {
		LOG.info("updater batch started.");
		if (matchService.noDataAvailable()) {
			getAllData();
		} else {
			updateData();
		}
	}

	protected List<Game> downloadAllGames() {
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

	protected List<Match> downloadAllMatches() {
		List<Match> allMatches = new ArrayList<>();
		List<Integer> seasonIds = getSeasonIDs();
		for (Integer seasonId : seasonIds) {
			List<String> ligaLinks = getLigaLinks(seasonId);
			for (String ligaLink : ligaLinks) {
				List<MatchWithLink> matches = getMatches(ligaLink);
				for (MatchWithLink match : matches) {
					match.setGames(getGames(match.getMatchLink()));
					allMatches.add(match);
				}
			}
		}
		return allMatches;
	}

	protected void getAllData() {
		List<Integer> seasonIds = getSeasonIDs();
		for (Integer seasonId : seasonIds) {
			List<String> ligaLinks = getLigaLinks(seasonId);
			for (String ligaLink : ligaLinks) {
				List<MatchWithLink> matches = getMatches(ligaLink);
				for (MatchWithLink match : matches) {
					match.setGames(getGames(match.getMatchLink()));
					matchService.saveMatch(match);
				}
			}
		}
	}

	protected void updateData() {
		List<Integer> seasonIds = getSeasonIDs();
		List<String> ligaLinks = getLigaLinks(getCurrentSeasonId(seasonIds));
		for (String ligaLink : ligaLinks) {
			List<MatchWithLink> matches = getMatches(ligaLink);
			for (MatchWithLink match : matches) {
				if (matchService.isNewMatch(match)) {
					match.setGames(getGames(match.getMatchLink()));
					matchService.saveMatch(match);
				}
			}
		}
	}

	protected int getCurrentSeasonId(List<Integer> seasons) {
		int newestSeason = seasons.get(0);
		for (Integer season : seasons) {
			if (season > newestSeason) {
				newestSeason = season;
			}
		}
		return newestSeason;
	}

	protected List<Game> getGames(String matchLink) {
		Document matchDoc = pageDownloader.downloadPage(matchLink);
		try {
			return kickerpageParser.findGames(matchDoc);
		} catch (Exception e) {
			LOG.severe("processing match: " + matchLink);
			throw e;
		}
	}

	protected List<String> getMatchLinks(String ligaLink) {
		Document ligaDoc = pageDownloader.downloadPage(ligaLink);
		List<String> matchLinks = kickerpageParser.findMatchLinks(ligaDoc);
		return matchLinks;
	}

	protected List<MatchWithLink> getMatches(String ligaLink) {
		Document ligaDoc = pageDownloader.downloadPage(ligaLink);
		List<MatchWithLink> matchLinks;
		try {
			matchLinks = kickerpageParser.findMatches(ligaDoc);
		} catch (Exception e) {
			LOG.severe("processing liga: " + ligaLink);
			throw e;
		}
		return matchLinks;
	}

	protected List<String> getLigaLinks(Integer seasonId) {
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

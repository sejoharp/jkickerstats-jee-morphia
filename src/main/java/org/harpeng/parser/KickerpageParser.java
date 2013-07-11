package org.harpeng.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class KickerpageParser {
	public static final String DOMAIN = "http://www.kickern-hamburg.de";

	public List<Game> findGames(Document doc) {
		final List<Game> games = new ArrayList<>();
		Elements gameSnippets = filterGameSnippets(doc);
		if (isValidGameList(gameSnippets)) {
			final String homeTeam = parseHomeTeam(doc);
			final String guestTeam = parseGuestTeam(doc);
			boolean matchDateAvailable = hasMatchDate(doc);
			final int matchDay = parseMatchDay(doc, matchDateAvailable);
			final Calendar matchDate = parseMatchDate(doc, matchDateAvailable);
			final boolean imagesAvailable = hasImages(gameSnippets);
			for (Element gameSnippet : gameSnippets) {
				Game game = new Game();
				game.setDoubleMatch(isDoubleMatch(gameSnippet));
				game.setPosition(parseGamePosition(gameSnippet));
				game.setHomeTeam(homeTeam);
				game.setGuestTeam(guestTeam);
				game.setMatchDate(matchDate);
				game.setMatchDay(matchDay);
				addPlayerNames(game, gameSnippet, game.isDoubleMatch());
				game.setHomeScore(parseHomeScore(gameSnippet, imagesAvailable));
				game.setGuestScore(parseGuestScore(gameSnippet, imagesAvailable));
				games.add(game);
			}
		}
		return games;
	}

	protected boolean hasImages(Elements elements) {
		return elements.first().children().size() == 6;
	}

	protected boolean isValidGameList(Elements elements) {
		int columnCount = elements.first().children().size();
		return columnCount == 6 || columnCount == 4;
	}

	protected Elements filterGameSnippets(Document doc) {
		Elements rawDoc = doc
				.select("div#Content > table.contentpaneopen:nth-child(6) > tbody");
		return rawDoc.select("tr.sectiontableentry1, tr.sectiontableentry2");
	}

	protected String parseHomeTeam(Document doc) {
		Elements teams = doc
				.select("html body div#Container div#Layout div.typography div#Content table.contentpaneopen tbody tr td table tbody tr td h2");
		return removeTeamDescriptions(teams.first().text());
	}

	protected String parseGuestTeam(Document doc) {
		Elements teams = doc
				.select("html body div#Container div#Layout div.typography div#Content table.contentpaneopen tbody tr td table tbody tr td h2");
		return removeTeamDescriptions(teams.last().text());
	}

	protected String removeTeamDescriptions(String teamString) {
		return teamString.split("\\(")[0].trim();
	}

	protected boolean hasMatchDate(Document doc) {
		String rawData = doc.select("#Content table tbody > tr > td").first()
				.text();
		String[] dateChunks = rawData.split(",");
		return dateChunks.length == 3;
	}

	protected Calendar parseMatchDate(Document doc, boolean matchDateAvailable) {
		if (matchDateAvailable == false) {
			Calendar matchDate = Calendar.getInstance();
			matchDate.setTimeInMillis(0);
			return matchDate;
		}
		String rawData = doc.select("#Content table tbody > tr > td").first()
				.text();
		String rawDate = rawData.split(",")[1];
		return parseDate(rawDate);
	}

	protected int parseMatchDay(Document doc, boolean matchDateAvailable) {
		String rawData = doc.select("#Content table tbody > tr > td").first()
				.text();
		String[] dateChunks = rawData.split(",");
		String matchDayString;
		if (matchDateAvailable) {
			matchDayString = dateChunks[2].split("\\.")[0];
		} else {
			matchDayString = dateChunks[1].split("\\.")[0];
		}
		return Integer.parseInt(matchDayString.trim());
	}

	protected String[] parseScore(Element doc, boolean imagesAvailable) {
		int index = imagesAvailable ? 3 : 2;
		String scoreString = doc.children().eq(index).text();
		return scoreString.split(":");
	}

	protected int parseHomeScore(Element doc, boolean imagesAvailable) {
		String homescore = parseScore(doc, imagesAvailable)[0].trim();
		return Integer.parseInt(homescore);
	}

	protected int parseGuestScore(Element doc, boolean imagesAvailable) {
		String guestscore = parseScore(doc, imagesAvailable)[1].trim();
		return Integer.parseInt(guestscore);
	}

	protected Calendar parseDate(String rawDate) {
		Calendar date = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		try {
			date.setTime(dateFormat.parse(rawDate));
		} catch (ParseException e) {
			throw new IllegalStateException(e);
		}
		return date;
	}

	public List<String> findLigaLinks(Document doc) {
		final List<String> ligaLinks = new ArrayList<>();
		Elements elements = doc
				.select("div#Content > table > tbody > tr > td > a.readon");
		for (Element element : elements) {
			ligaLinks.add(DOMAIN + element.attr("href"));
		}
		return ligaLinks;
	}

	public List<Integer> findSeasonIDs(Document doc) {
		final List<Integer> seasonIDs = new ArrayList<>();
		Elements elements = doc.select("div#Content select option");
		for (Element element : elements) {
			seasonIDs.add(Integer.valueOf(element.attr("value")));
		}
		return seasonIDs;
	}

	public List<String> findMatchLinks(Document doc) {
		final List<String> matchLinks = new ArrayList<>();
		Elements elements = filterMatchLinkSnippets(doc);
		for (Element element : elements) {
			if (isValidMatchLink(element)) {
				matchLinks.add(DOMAIN + element.select("a[href]").attr("href"));
			}
		}
		return matchLinks;
	}

	protected Elements filterMatchLinkSnippets(Document doc) {
		Elements rawDoc = doc
				.select("div#Content > table.contentpaneopen:nth-child(7)");
		return rawDoc.select("tr.sectiontableentry1, tr.sectiontableentry2");
	}

	protected boolean isValidMatchLink(Element element) {
		boolean alreadyPlayed = element.select("a").size() == 2;
		if (alreadyPlayed == false) {
			return false;
		}
		String scoreDescription = element.select("td:nth-child(5) small")
				.text();
		return isMatchUnconfirmed(scoreDescription) == false
				&& isMatchRunning(scoreDescription) == false;
	}

	protected boolean isMatchRunning(String scoreDescription) {
		return "live".equals(scoreDescription);
	}

	protected boolean isMatchUnconfirmed(String scoreDescription) {
		return "unbestätigt".equals(scoreDescription);
	}

	protected Boolean isDoubleMatch(Element gameDoc) {
		return gameDoc.select("td a").size() == 4;
	}

	protected Integer parseGamePosition(Element gameDoc) {
		return Integer.parseInt(gameDoc.children().first().text());
	}

	protected void addPlayerNames(Game game, Element gameDoc,
			boolean doubleMatch) {
		Elements rawPlayerNames = gameDoc.select("td a");
		if (doubleMatch) {
			game.setHomePlayer1(parsePlayerName(rawPlayerNames, 0));
			game.setHomePlayer2(parsePlayerName(rawPlayerNames, 1));
			game.setGuestPlayer1(parsePlayerName(rawPlayerNames, 2));
			game.setGuestPlayer2(parsePlayerName(rawPlayerNames, 3));
		} else {
			game.setHomePlayer1(parsePlayerName(rawPlayerNames, 0));
			game.setGuestPlayer1(parsePlayerName(rawPlayerNames, 1));
		}

	}

	protected String parsePlayerName(Elements rawPlayerNames, int position) {
		return rawPlayerNames.size() > position ? rawPlayerNames.get(position)
				.text() : "";
	}

}

package kickerstats.interfaces;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kickerstats.types.Game;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class PageParser {
	public static final String DOMAIN = "http://www.kickern-hamburg.de";

	public List<Game> findGames(Document doc) {
		final List<Game> games = new ArrayList<>();
		Elements gameSnippets = filterGameSnippets(doc);
		if (isValidGameList(gameSnippets)) {
			final boolean imagesAvailable = hasImages(gameSnippets);
			for (Element gameSnippet : gameSnippets) {
				Game game = new Game();
				game.setDoubleMatch(isDoubleMatch(gameSnippet));
				game.setPosition(parseGamePosition(gameSnippet));
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

	protected Date parseMatchDate(Document doc, boolean matchDateAvailable) {
		if (matchDateAvailable == false) {
			Calendar matchDate = Calendar.getInstance();
			matchDate.setTimeInMillis(0);
			return matchDate.getTime();
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

	protected Date parseDate(String rawDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		try {
			return dateFormat.parse(rawDate);
		} catch (ParseException e) {
			throw new IllegalStateException(e);
		}
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
				matchLinks.add(parseMatchLink(element));
			}
		}
		return matchLinks;
	}

	protected String parseMatchLink(Element element) {
		return DOMAIN + element.select("a[href]").attr("href");
	}

	public List<MatchWithLink> findMatches(Document doc) {
		final List<MatchWithLink> matches = new ArrayList<>();
		Elements elements = filterMatchSnippets(doc);
		int matchDay = 0;
		for (Element element : elements) {
			if (isMatchDayElement(element)) {
				String matchDayString = element.select("i").text();
				matchDay = Integer.parseInt(matchDayString.split("\\.")[0]);
			} else if (isValidMatchLink(element)) {
				matches.add(parseMatch(element, matchDay));
			}
		}
		return matches;
	}

	protected boolean isMatchDayElement(Element element) {
		return element.hasClass("sectiontableheader")
				&& element.select("i").text().isEmpty() == false;
	}

	protected MatchWithLink parseMatch(Element element, int matchDay) {
		MatchWithLink match = new MatchWithLink();
		match.setMatchDate(parseMatchDate(element));
		match.setMatchDay(matchDay);
		match.setHomeScore(parseMatchHomeScore(element));
		match.setGuestScore(parseMatchGuestScore(element));
		match.setHomeTeam(removeTeamDescriptions(element.children().eq(1)
				.text()));
		match.setGuestTeam(removeTeamDescriptions(element.children().eq(2)
				.text()));
		if (isNewMatchFormat(element)) {
			match.setGuestGoals(parseMatchGuestGoals(element));
			match.setHomeGoals(parseMatchHomeGoals(element));
		}
		match.setMatchLink(parseMatchLink(element));
		return match;
	}

	protected boolean isNewMatchFormat(Element element) {
		return element.children().size() == 5;
	}

	protected int parseMatchHomeGoals(Element element) {
		String goals = element.children().eq(3).text();
		return Integer.parseInt(goals.split(":")[0]);
	}

	protected int parseMatchGuestGoals(Element element) {
		String goals = element.children().eq(3).text();
		return Integer.parseInt(goals.split(":")[1]);
	}

	protected int parseMatchHomeScore(Element element) {
		String score = element.children().last().text();
		return Integer.parseInt(score.split(":")[0]);
	}

	protected int parseMatchGuestScore(Element element) {
		String score = element.children().last().text();
		return Integer.parseInt(score.split(":")[1]);
	}

	protected Date parseMatchDate(Element element) {
		String rawData = element.select("a").text();
		String[] dateString = rawData.split(",");
		if (dateString.length == 2) {
			return parseDate(dateString[1]);
		} else {
			Calendar matchDate = Calendar.getInstance();
			matchDate.setTimeInMillis(0);
			return matchDate.getTime();
		}
	}

	protected Elements filterMatchLinkSnippets(Document doc) {
		Elements rawDoc = doc
				.select("div#Content > table.contentpaneopen:nth-child(7)");
		return rawDoc.select("tr.sectiontableentry1, tr.sectiontableentry2");
	}

	protected Elements filterMatchSnippets(Document doc) {
		Elements rawDoc = doc
				.select("div#Content > table.contentpaneopen:nth-child(7)");
		return rawDoc
				.select("tr.sectiontableheader,tr.sectiontableentry1, tr.sectiontableentry2");
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

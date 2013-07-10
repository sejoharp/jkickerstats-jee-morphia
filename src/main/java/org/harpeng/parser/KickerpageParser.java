package org.harpeng.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;

public class KickerpageParser {
	public static final String DOMAIN = "http://www.kickern-hamburg.de";

	public List<Game> findGames(Jerry doc) {
		final List<Game> games = new ArrayList<>();
		Jerry gameSnippets = filterGameSnippets(doc);
		if (isValidGameList(gameSnippets)) {
			final String homeTeam = parseHomeTeam(doc);
			final String guestTeam = parseGuestTeam(doc);
			boolean matchDateAvailable = hasMatchDate(doc);
			final int matchDay = parseMatchDay(doc, matchDateAvailable);
			final Calendar matchDate = parseMatchDate(doc, matchDateAvailable);
			final boolean imagesAvailable = hasImages(gameSnippets);
			gameSnippets.each(new JerryFunction() {
				public boolean onNode(Jerry $this, int index) {
					Game game = new Game();
					game.setDoubleMatch(isDoubleMatch($this));
					game.setPosition(parseGamePosition($this));
					game.setHomeTeam(homeTeam);
					game.setGuestTeam(guestTeam);
					game.setMatchDate(matchDate);
					game.setMatchDay(matchDay);
					addPlayerNames(game, $this, game.isDoubleMatch());
					game.setHomeScore(parseHomeScore($this, imagesAvailable));
					game.setGuestScore(parseGuestScore($this, imagesAvailable));
					games.add(game);
					return true;
				}
			});
		}
		return games;
	}

	protected boolean hasImages(Jerry gameDoc) {
		return gameDoc.first().children().length() == 6;
	}

	protected boolean isValidGameList(Jerry doc) {
		int columnCount = doc.first().children().length();
		return columnCount == 6 || columnCount == 4;
	}

	protected Jerry filterGameSnippets(Jerry doc) {
		Jerry rawDoc = doc.$("div#Content table.contentpaneopen");
		return rawDoc.$("tr.sectiontableentry1, tr.sectiontableentry2");
	}

	protected String parseHomeTeam(Jerry doc) {
		Jerry teams = doc.$("div#Content table.contentpaneopen").eq(1)
				.$("tr > td > table h2");
		return removeTeamDescriptions(teams.first().text());
	}

	protected String parseGuestTeam(Jerry doc) {
		Jerry teams = doc.$("div#Content table.contentpaneopen").eq(1)
				.$("tr > td > table h2");
		return removeTeamDescriptions(teams.last().text());
	}

	protected String removeTeamDescriptions(String teamString) {
		return teamString.split("\\(")[0].trim();
	}

	protected boolean hasMatchDate(Jerry doc) {
		String rawData = doc.$("div#Content table > tr > td").first().text();
		String[] dateChunks = rawData.split(",");
		return dateChunks.length == 3;
	}

	protected Calendar parseMatchDate(Jerry doc, boolean matchDateAvailable) {
		if (matchDateAvailable == false) {
			Calendar matchDate = Calendar.getInstance();
			matchDate.setTimeInMillis(0);
			return matchDate;
		}
		String rawData = doc.$("div#Content table > tr > td").first().text();
		String rawDate = rawData.split(",")[1];
		return parseDate(rawDate);
	}

	protected int parseMatchDay(Jerry doc, boolean matchDateAvailable) {
		String rawData = doc.$("div#Content table > tr > td").first().text();
		String[] dateChunks = rawData.split(",");
		String matchDayString;
		if (matchDateAvailable) {
			matchDayString = dateChunks[2].split("\\.")[0];
		} else {
			matchDayString = dateChunks[1].split("\\.")[0];
		}
		return Integer.parseInt(matchDayString.trim());
	}

	protected String[] parseScore(Jerry doc, boolean imagesAvailable) {
		int index = imagesAvailable ? 3 : 2;
		String scoreString = doc.children().eq(index).text();
		return scoreString.split(":");
	}

	protected int parseHomeScore(Jerry doc, boolean imagesAvailable) {
		String homescore = parseScore(doc, imagesAvailable)[0].trim();
		return Integer.parseInt(homescore);
	}

	protected int parseGuestScore(Jerry doc, boolean imagesAvailable) {
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

	public List<String> findLigaLinks(Jerry doc) {
		final List<String> ligaLinks = new ArrayList<>();
		doc.$("div#Content > table.contentpaneopen > tr > td > a.readon").each(
				new JerryFunction() {
					public boolean onNode(Jerry $this, int index) {
						ligaLinks.add(DOMAIN + $this.attr("href"));
						return true;
					}
				});
		return ligaLinks;
	}

	public List<Integer> findSeasonIDs(Jerry doc) {
		final List<Integer> seasonIDs = new ArrayList<>();
		doc.$("div#Content select option").each(new JerryFunction() {
			public boolean onNode(Jerry $this, int index) {
				seasonIDs.add(Integer.valueOf($this.attr("value")));
				return true;
			}
		});
		return seasonIDs;
	}

	public List<String> findMatchLinks(Jerry doc) {
		final List<String> matchLinks = new ArrayList<>();
		filterMatchLinkSnippets(doc).each(new JerryFunction() {
			public boolean onNode(Jerry $this, int index) {
				if (isValidMatchLink($this)) {
					matchLinks.add(DOMAIN + $this.$("a[href]").attr("href"));
				}
				return true;
			}
		});
		return matchLinks;
	}

	protected Jerry filterMatchLinkSnippets(Jerry doc) {
		Jerry rawDoc = doc
				.$("div#Content > table.contentpaneopen:nth-child(7)");
		return rawDoc.$("tr.sectiontableentry1, tr.sectiontableentry2");
	}

	protected boolean isValidMatchLink(Jerry doc) {
		boolean alreadyPlayed = doc.$("a").length() == 2;
		if (alreadyPlayed == false) {
			return false;
		}
		String scoreDescription = doc.$("td:nth-child(5) small").text();
		boolean scoreConfirmed = "unbestätigt".equals(scoreDescription) == false
				&& "live".equals(scoreDescription) == false;
		if (scoreConfirmed == false) {
			return false;
		}
		return true;
	}

	protected Boolean isDoubleMatch(Jerry gameDoc) {
		return gameDoc.$("td a").length() == 4;
	}

	protected Integer parseGamePosition(Jerry gameDoc) {
		return Integer.parseInt(gameDoc.children().first().text());
	}

	protected void addPlayerNames(Game game, Jerry gameDoc, boolean doubleMatch) {
		Jerry rawPlayerNames = gameDoc.$("td a");
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

	protected String parsePlayerName(Jerry rawPlayerNames, int position) {
		return rawPlayerNames.size() > position ? rawPlayerNames.eq(position)
				.text() : "";
	}

}

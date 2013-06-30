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
		filterGameSnippets(doc).each(new JerryFunction() {
			public boolean onNode(Jerry $this, int index) {
				// games.add(DOMAIN + $this.attr("href"));
				return true;
			}
		});
		return games;
	}

	protected boolean isValidGameList(Jerry doc) {
		int columnCount = doc.first().children().length();
		return columnCount == 6 || columnCount == 4;
	}

	protected Jerry filterGameSnippets(Jerry doc) {
		Jerry rawDoc = doc
				.$("div#Content > table.contentpaneopen:nth-child(6)");
		return rawDoc.$("tr.sectiontableentry1, tr.sectiontableentry2");
	}

	protected String parseHomeTeam(Jerry doc) {
		Jerry teams = doc.$("table.contentpaneopen").eq(1)
				.$("tbody > tr > td > table > tbody h2");
		return removeTeamDescriptions(teams.first().text());
	}

	protected String parseGuestTeam(Jerry doc) {
		Jerry teams = doc.$("table.contentpaneopen").eq(1)
				.$("tbody > tr > td > table > tbody h2");
		return removeTeamDescriptions(teams.last().text());
	}

	protected String removeTeamDescriptions(String teamString) {
		return teamString.split("(")[0].trim();
	}

	protected boolean hasMatchDate(Jerry doc) {
		String rawData = doc.$("#Content table tbody > tr > td").first().text();
		String[] dateChunks = rawData.split(",");
		return dateChunks.length == 3;
	}

	protected Calendar parseMatchDate(Jerry doc, boolean matchDateAvailable) {
		if (matchDateAvailable == false) {
			Calendar matchDate = Calendar.getInstance();
			matchDate.setTimeInMillis(0);
			return matchDate;
		}
		String rawData = doc.$("#Content table tbody > tr > td").first().text();
		String rawDate = rawData.split(",")[1];
		return parseDate(rawDate);
	}

	protected Calendar parseDate(String rawDate) {
		Calendar date = Calendar.getInstance();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.YYYY HH:mm");
		try {
			date.setTime(dateFormatter.parse(rawDate));
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
}
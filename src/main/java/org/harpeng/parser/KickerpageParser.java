package org.harpeng.parser;

import java.util.ArrayList;
import java.util.List;

import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;

public class KickerpageParser {
	private static final String DOMAIN = "http://www.kickern-hamburg.de";

	public List<String> findLigaLinks(Jerry doc) {
		final List<String> ligaLinks = new ArrayList<>();
		doc.$("div#Content > table.contentpaneopen > tr > td > a.readon").each( //
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
		filterMatchLinks(doc).each(new JerryFunction() {
			public boolean onNode(Jerry $this, int index) {
				if (isValidMatchLink($this)) {
					matchLinks.add(DOMAIN + $this.$("a[href]").attr("href"));
				}
				return true;
			}
		});
		return matchLinks;
	}

	private Jerry filterMatchLinks(Jerry doc) {
		Jerry rawDoc = doc
				.$("div#Content > table.contentpaneopen:nth-child(7)");
		return rawDoc.$(".sectiontableentry1, .sectiontableentry2");
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
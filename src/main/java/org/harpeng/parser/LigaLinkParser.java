package org.harpeng.parser;

import java.util.ArrayList;
import java.util.List;

import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;

public class LigaLinkParser {
	public List<String> findLigaLinks(Jerry doc) {
		final List<String> ligaLinks = new ArrayList<>();
		doc.$("div#Content > table  > tr > td > a.readon").each( //
				new JerryFunction() {
					public boolean onNode(Jerry $this, int index) {
						ligaLinks.add("http://www.kickern-hamburg.de"
								+ $this.attr("href"));
						return true;
					}
				});
		System.err.println(ligaLinks);
		return ligaLinks;
	}
}
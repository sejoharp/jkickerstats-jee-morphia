package org.harpeng.parser;

import static jodd.jerry.Jerry.jerry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jodd.io.FileUtil;
import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;

public class SeasonParser {
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

	protected Jerry readDocument(File html) {
		try {
			return jerry().parse(FileUtil.readString(html));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}

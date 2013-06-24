package org.harpeng.parser;

import static jodd.jerry.Jerry.jerry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jodd.io.FileUtil;
import jodd.jerry.Jerry;

public class SeasonParser {
	public List<Integer> getSeasonIDs(Jerry doc) {
		return new ArrayList<Integer>();
	}

	protected Jerry readDocument(File html) {
		try {
			return jerry().parse(FileUtil.readString(html));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}

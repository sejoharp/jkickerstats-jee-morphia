package org.harpeng.parser;

import static jodd.jerry.Jerry.jerry;

import java.io.File;
import java.io.IOException;

import jodd.io.FileUtil;

public class SeasonParser {
	public void getSeasons(File html) {
		try {
			jerry().parse(FileUtil.readString(html));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package org.harpeng;

import javax.inject.Inject;

import org.harpeng.parser.SeasonParser;

public class App2 {
	@Inject
	private SeasonParser parser;
 
	public String hello() {
		return "hello: " + parser.toString();
	}
}

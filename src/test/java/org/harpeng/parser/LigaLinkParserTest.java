package org.harpeng.parser;

import static jodd.jerry.Jerry.jerry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import jodd.io.FileUtil;
import jodd.jerry.Jerry;

import org.harpeng.WeldJUnit4Runner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4Runner.class)
public class LigaLinkParserTest {
	@Inject
	private LigaLinkParser parser;

	@Before
	public void checkPreconditions() {
		assertThat(parser, notNullValue());
	}

	@Test
	public void returnsAllLigaLinks() throws IOException {
		Jerry doc = jerry().parse(
				FileUtil.readString(new File(
						SeasonParserTest.RECOURCES_DIRECTORY
								+ "uebersicht.html")));
		List<String> ligaLinksIDs = parser.findLigaLinks(doc);
		assertThat(ligaLinksIDs.size(), equalTo(5));
		assertThat(ligaLinksIDs.get(0), equalTo("http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=veranstaltung&veranstaltungid=8"));
	}
}

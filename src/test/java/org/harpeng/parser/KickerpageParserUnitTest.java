package org.harpeng.parser;

import static jodd.jerry.Jerry.jerry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jodd.io.FileUtil;
import jodd.jerry.Jerry;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class KickerpageParserUnitTest {

	private KickerpageParser parser;

	@Before
	public void setup() {
		parser = new KickerpageParser();
	}

	@Test
	public void theMatchIsNotConfirmed() {
		String htmlSnippet = "<tr class=\"sectiontableentry2\"><td nowrap><a name=\"id3837\"></a><a href=\"/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3837\">Do., 11.04.2013 20:30</a></td><td nowrap>St. Ellingen 1<br /><small>verantwortlich</small><small> seit 12.04.2013</small></td><td nowrap>Drehschieber FC St. Pauli				</td><td nowrap align=\"center\">60:83					</td><td nowrap align=\"center\">9:23<br /><small>unbest&auml;tigt</small>				</td></tr>";
		Jerry doc = jerry().parse(htmlSnippet);

		boolean isValid = parser.isValidMatchLink(doc);

		assertThat(isValid, equalTo(false));
	}

	@Test
	public void theMatchIsRunning() {
		String htmlSnippet = "<tr class=\"sectiontableentry2\"><td nowrap><a name=\"id3837\"></a><a href=\"/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3837\">Do., 11.04.2013 20:30</a></td><td nowrap>St. Ellingen 1<br /><small>verantwortlich</small><small> seit 12.04.2013</small></td><td nowrap>Drehschieber FC St. Pauli				</td><td nowrap align=\"center\">60:83					</td><td nowrap align=\"center\">9:23<br /><small>live</small>				</td></tr>";
		Jerry doc = jerry().parse(htmlSnippet);

		boolean isValid = parser.isValidMatchLink(doc);

		assertThat(isValid, equalTo(false));
	}

	@Test
	public void theMatchIsConfirmed() {
		String htmlSnippet = "<tr class=\"sectiontableentry2\"><td nowrap><a name=\"id3837\"></a><a href=\"/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3837\">Do., 11.04.2013 20:30</a></td><td nowrap>St. Ellingen 1<br /><small>verantwortlich</small><small> seit 12.04.2013</small></td><td nowrap>Drehschieber FC St. Pauli				</td><td nowrap align=\"center\">60:83					</td><td nowrap align=\"center\">9:23				</td></tr>";
		Jerry doc = jerry().parse(htmlSnippet);

		boolean isValid = parser.isValidMatchLink(doc);

		assertThat(isValid, equalTo(true));
	}

	@Test
	public void returnAllSeasons() throws IOException {
		List<Integer> expectedSeasonsIDs = Lists.newArrayList(7, 4, 3, 2, 1);
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "uebersicht.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));

		List<Integer> seasonsIDs = parser.findSeasonIDs(doc);

		assertThat(seasonsIDs, equalTo(expectedSeasonsIDs));
		assertThat(seasonsIDs.get(0), equalTo(expectedSeasonsIDs.get(0)));
	}

	@Test
	public void returnAllMatchLinks() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnungen.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));

		List<String> matchLinks = parser.findMatchLinks(doc);

		assertThat(matchLinks.size(), equalTo(14));
		String expectedMatchLink = "http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3815";
		assertThat(matchLinks.get(0), equalTo(expectedMatchLink));
	}

	@Test
	public void returnAllConfirmedMatchLinks() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnungen_live.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));

		List<String> matchLinks = parser.findMatchLinks(doc);

		assertThat(matchLinks.size(), equalTo(24));
	}

	@Test
	public void returnAllLigaLinks() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "uebersicht.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));

		List<String> ligaLinksIDs = parser.findLigaLinks(doc);

		assertThat(ligaLinksIDs.size(), equalTo(5));
		assertThat(
				ligaLinksIDs.get(0),
				equalTo("http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=veranstaltung&veranstaltungid=8"));
	}
}

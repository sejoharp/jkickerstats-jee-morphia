package org.harpeng.parser;

import static jodd.jerry.Jerry.jerry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
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

	@Test
	public void filteringAllMatchLinks() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnungen.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));

		assertThat(parser.filterMatchLinkSnippets(doc).size(), equalTo(110));
	}

	@Test
	public void filteringAllGames() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));

		assertThat(parser.filterGameSnippets(doc).size(), equalTo(16));
	}

	@Test
	public void gameIsValid() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));
		Jerry gameSnippets = parser.filterGameSnippets(doc);
		assertThat(parser.isValidGameList(gameSnippets), equalTo(true));
	}

	@Test
	public void gameIsValidWithImages() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung_bild.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));
		Jerry gameSnippets = parser.filterGameSnippets(doc);
		assertThat(parser.isValidGameList(gameSnippets), equalTo(true));
	}

	@Test
	public void teamnameWithoutDescription() {
		String teamname = "Team Hauff (A)";
		assertThat(parser.removeTeamDescriptions(teamname),
				equalTo("Team Hauff"));
	}

	@Test
	public void teamnameWithoutDescriptionIsStillComplete() {
		String teamname = "Team Hauff";
		assertThat(parser.removeTeamDescriptions(teamname),
				equalTo("Team Hauff"));
	}

	@Test
	public void gameHasHomeTeam() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));
		assertThat(parser.parseHomeTeam(doc),
				equalTo("Tingeltangel FC St. Pauli"));
	}

	@Test
	public void gameHasGuestTeam() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));
		assertThat(parser.parseGuestTeam(doc), equalTo("Hamburg Privateers 08"));
	}

	@Test
	public void gameHasNoMatchdate() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung_no_date.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));
		assertThat(parser.hasMatchDate(doc), equalTo(false));
	}

	@Test
	public void gameHasMatchdate() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));
		assertThat(parser.hasMatchDate(doc), equalTo(true));
	}

	@Test
	public void parsingADate() throws IOException {
		String rawDate = "27.02.2013 20:00";
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.setTimeInMillis(0);
		expectedDate.set(2013, 1, 27, 20, 0);

		Date resultDate = parser.parseDate(rawDate).getTime();

		assertThat(resultDate, equalTo(expectedDate.getTime()));
	}

	@Test
	public void parsingMatchdate() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung.html");
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.clear();
		expectedDate.set(2013, 1, 27, 20, 0);
		Jerry doc = jerry().parse(FileUtil.readString(testFile));

		assertThat(parser.parseMatchDate(doc, true), equalTo(expectedDate));
	}

	@Test
	public void parseGameWithoutMatchdate() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung_no_date.html");
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.setTimeInMillis(0);
		Jerry doc = jerry().parse(FileUtil.readString(testFile));

		assertThat(parser.parseMatchDate(doc, false), equalTo(expectedDate));
	}

	@Test
	public void parseMatchday() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));

		assertThat(parser.parseMatchDay(doc, true), equalTo(1));
	}

	@Test
	public void parseMatchdayWithoutDate() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung_no_date.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));

		assertThat(parser.parseMatchDay(doc, false), equalTo(5));
	}
	
	@Test
	public void parseHomeScore() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));

		Jerry rawGames = parser.filterGameSnippets(doc);
		assertThat(parser.parseHomeScore(rawGames.first(), false), equalTo(4));
	}
	
	@Test
	public void parseGuestScore() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));

		Jerry rawGames = parser.filterGameSnippets(doc);
		assertThat(parser.parseGuestScore(rawGames.first(), false), equalTo(7));
	}
	
	@Test
	public void parseHomeScoreWithImages() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung_bild.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));

		Jerry rawGames = parser.filterGameSnippets(doc);
		assertThat(parser.parseHomeScore(rawGames.first(), true), equalTo(4));
	}
	
	@Test
	public void parseGuestScoreWithImages() throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ "begegnung_bild.html");
		Jerry doc = jerry().parse(FileUtil.readString(testFile));

		Jerry rawGames = parser.filterGameSnippets(doc);
		assertThat(parser.parseGuestScore(rawGames.first(), true), equalTo(7));
	}
}

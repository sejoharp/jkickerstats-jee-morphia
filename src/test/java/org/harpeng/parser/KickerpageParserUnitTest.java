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
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;

public class KickerpageParserUnitTest {

	private KickerpageParser parser;
	private static Jerry begegnungDoc;
	private static Jerry begegnungNoDateDoc;
	private static Jerry begegnungenDoc;
	private static Jerry begegnungBildDoc;
	private static Jerry begegnungenLiveDoc;
	private static Jerry uebersichtDoc;

	@BeforeClass
	public static void setup() throws IOException {
		begegnungDoc = loadFile("begegnung.html");
		begegnungenDoc = loadFile("begegnungen.html");
		begegnungenLiveDoc = loadFile("begegnungen_live.html");
		begegnungBildDoc = loadFile("begegnung_bild.html");
		begegnungNoDateDoc = loadFile("begegnung_no_date.html");
		uebersichtDoc = loadFile("uebersicht.html");
	}

	@Before
	public void initialize() {
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

		List<Integer> seasonsIDs = parser.findSeasonIDs(uebersichtDoc);

		assertThat(seasonsIDs, equalTo(expectedSeasonsIDs));
		assertThat(seasonsIDs.get(0), equalTo(expectedSeasonsIDs.get(0)));
	}

	@Test
	public void returnAllMatchLinks() throws IOException {
		List<String> matchLinks = parser.findMatchLinks(begegnungenDoc);

		assertThat(matchLinks.size(), equalTo(14));
		String expectedMatchLink = "http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3815";
		assertThat(matchLinks.get(0), equalTo(expectedMatchLink));
	}

	@Test
	public void returnAllConfirmedMatchLinks() throws IOException {
		List<String> matchLinks = parser.findMatchLinks(begegnungenLiveDoc);

		assertThat(matchLinks.size(), equalTo(24));
	}

	@Test
	public void returnAllLigaLinks() throws IOException {
		List<String> ligaLinksIDs = parser.findLigaLinks(uebersichtDoc);

		assertThat(ligaLinksIDs.size(), equalTo(5));
		assertThat(
				ligaLinksIDs.get(0),
				equalTo("http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=veranstaltung&veranstaltungid=8"));
	}

	@Test
	public void filteringAllMatchLinks() throws IOException {
		int matchCount = parser.filterMatchLinkSnippets(begegnungenDoc).size();

		assertThat(matchCount, equalTo(110));
	}

	@Test
	public void filteringAllGames() throws IOException {
		int gameCount = parser.filterGameSnippets(begegnungDoc).size();

		assertThat(gameCount, equalTo(16));
	}

	@Test
	public void gameIsValid() throws IOException {
		Jerry gameSnippets = parser.filterGameSnippets(begegnungDoc);

		assertThat(parser.isValidGameList(gameSnippets), equalTo(true));
	}

	@Test
	public void gameIsValidWithImages() throws IOException {
		Jerry gameSnippets = parser.filterGameSnippets(begegnungBildDoc);

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
		assertThat(parser.parseHomeTeam(begegnungDoc),
				equalTo("Tingeltangel FC St. Pauli"));
	}

	@Test
	public void gameHasGuestTeam() throws IOException {
		assertThat(parser.parseGuestTeam(begegnungDoc),
				equalTo("Hamburg Privateers 08"));
	}

	@Test
	public void gameHasNoMatchdate() throws IOException {
		assertThat(parser.hasMatchDate(begegnungNoDateDoc), equalTo(false));
	}

	@Test
	public void gameHasMatchdate() throws IOException {
		assertThat(parser.hasMatchDate(begegnungDoc), equalTo(true));
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
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.clear();
		expectedDate.set(2013, 1, 27, 20, 0);

		assertThat(parser.parseMatchDate(begegnungDoc, true),
				equalTo(expectedDate));
	}

	@Test
	public void parseGameWithoutMatchdate() throws IOException {
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.setTimeInMillis(0);

		assertThat(parser.parseMatchDate(begegnungNoDateDoc, false),
				equalTo(expectedDate));
	}

	@Test
	public void parseMatchday() throws IOException {
		assertThat(parser.parseMatchDay(begegnungDoc, true), equalTo(1));
	}

	@Test
	public void parseMatchdayWithoutDate() throws IOException {
		assertThat(parser.parseMatchDay(begegnungNoDateDoc, false), equalTo(5));
	}

	@Test
	public void parseHomeScore() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);

		assertThat(parser.parseHomeScore(rawGames.first(), false), equalTo(4));
	}

	@Test
	public void parseGuestScore() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);

		assertThat(parser.parseGuestScore(rawGames.first(), false), equalTo(7));
	}

	@Test
	public void parseHomeScoreWithImages() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungBildDoc);

		assertThat(parser.parseHomeScore(rawGames.first(), true), equalTo(4));
	}

	@Test
	public void parseGuestScoreWithImages() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungBildDoc);

		assertThat(parser.parseGuestScore(rawGames.first(), true), equalTo(7));
	}

	@Test
	public void singleMatchIsNotADoubleMatch() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);

		assertThat(parser.isDoubleMatch(rawGames.first()), equalTo(false));
	}

	@Test
	public void detectDoubleMatch() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);

		assertThat(parser.isDoubleMatch(rawGames.last()), equalTo(true));
	}

	@Test
	public void detectDoubleMatchWithImages() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungBildDoc);

		assertThat(parser.isDoubleMatch(rawGames.last()), equalTo(true));
	}

	@Test
	public void parsePositionOfFirstGame() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);

		assertThat(parser.parseGamePosition(rawGames.first()), equalTo(1));
	}

	@Test
	public void parsePositionOfLastGame() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);

		assertThat(parser.parseGamePosition(rawGames.last()), equalTo(16));
	}

	@Test
	public void parseNamesOfLastGame() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);
		Game game = new Game();

		parser.addPlayerNames(game, rawGames.last(), true);

		assertThat(game.getHomePlayer1(), equalTo("Sommer, Sebastian"));
		assertThat(game.getHomePlayer2(), equalTo("Hölzer, Heinz"));
		assertThat(game.getGuestPlayer1(), equalTo("Nestvogel, Markus"));
		assertThat(game.getGuestPlayer2(), equalTo("Matheuszik, Sven"));
	}

	@Test
	public void parseNamesOfFirstGame() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);
		Game game = new Game();

		parser.addPlayerNames(game, rawGames.first(), false);

		assertThat(game.getHomePlayer1(), equalTo("Technau, Jerome"));
		assertThat(game.getGuestPlayer1(), equalTo("Hojas, René"));
	}

	private static Jerry loadFile(String fileName) throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ fileName);
		return jerry().parse(FileUtil.readString(testFile));
	}
}

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
	private static Jerry relegationDoc;
	private static Jerry begegnungNoNamesDoc;
	
	@BeforeClass
	public static void loadTestFiles() throws IOException {
		begegnungDoc = loadFile("begegnung.html");
		begegnungenDoc = loadFile("begegnungen.html");
		begegnungenLiveDoc = loadFile("begegnungen_live.html");
		begegnungBildDoc = loadFile("begegnung_bild.html");
		begegnungNoDateDoc = loadFile("begegnung_no_date.html");
		uebersichtDoc = loadFile("uebersicht.html");
		relegationDoc = loadFile("relegation.html");
		begegnungNoNamesDoc = loadFile("begegnung_no_names.html");
	}

	@Before
	public void initialize() {
		parser = new KickerpageParser();
	}

	@Test
	public void anUnconfirmedMatchIsNotAValidMatch() {
		String htmlSnippet = "<tr class=\"sectiontableentry2\"><td nowrap><a name=\"id3837\"></a><a href=\"/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3837\">Do., 11.04.2013 20:30</a></td><td nowrap>St. Ellingen 1<br /><small>verantwortlich</small><small> seit 12.04.2013</small></td><td nowrap>Drehschieber FC St. Pauli				</td><td nowrap align=\"center\">60:83					</td><td nowrap align=\"center\">9:23<br /><small>unbest&auml;tigt</small>				</td></tr>";
		Jerry doc = jerry().parse(htmlSnippet);

		boolean isValid = parser.isValidMatchLink(doc);

		assertThat(isValid, equalTo(false));
	}

	@Test
	public void aRunningMatchIsNotAValidMatch() {
		String htmlSnippet = "<tr class=\"sectiontableentry2\"><td nowrap><a name=\"id3837\"></a><a href=\"/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3837\">Do., 11.04.2013 20:30</a></td><td nowrap>St. Ellingen 1<br /><small>verantwortlich</small><small> seit 12.04.2013</small></td><td nowrap>Drehschieber FC St. Pauli				</td><td nowrap align=\"center\">60:83					</td><td nowrap align=\"center\">9:23<br /><small>live</small>				</td></tr>";
		Jerry doc = jerry().parse(htmlSnippet);

		boolean isValid = parser.isValidMatchLink(doc);

		assertThat(isValid, equalTo(false));
	}

	@Test
	public void aConfirmedMatchIsAValidMatch() {
		String htmlSnippet = "<tr class=\"sectiontableentry2\"><td nowrap><a name=\"id3837\"></a><a href=\"/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3837\">Do., 11.04.2013 20:30</a></td><td nowrap>St. Ellingen 1<br /><small>verantwortlich</small><small> seit 12.04.2013</small></td><td nowrap>Drehschieber FC St. Pauli				</td><td nowrap align=\"center\">60:83					</td><td nowrap align=\"center\">9:23				</td></tr>";
		Jerry doc = jerry().parse(htmlSnippet);

		boolean isValid = parser.isValidMatchLink(doc);

		assertThat(isValid, equalTo(true));
	}

	@Test
	public void returnsAllSeasons() throws IOException {
		List<Integer> expectedSeasonsIDs = Lists.newArrayList(7, 4, 3, 2, 1);

		List<Integer> seasonsIDs = parser.findSeasonIDs(uebersichtDoc);

		assertThat(seasonsIDs, equalTo(expectedSeasonsIDs));
		assertThat(seasonsIDs.get(0), equalTo(expectedSeasonsIDs.get(0)));
	}

	@Test
	public void returnsAllMatchLinks() throws IOException {
		List<String> matchLinks = parser.findMatchLinks(begegnungenDoc);

		assertThat(matchLinks.size(), equalTo(14));
		String expectedMatchLink = "http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3815";
		assertThat(matchLinks.get(0), equalTo(expectedMatchLink));
	}

	@Test
	public void returnsAllConfirmedMatchLinks() throws IOException {
		List<String> matchLinks = parser.findMatchLinks(begegnungenLiveDoc);

		assertThat(matchLinks.size(), equalTo(24));
	}

	@Test
	public void returnsAllLigaLinks() throws IOException {
		List<String> ligaLinksIDs = parser.findLigaLinks(uebersichtDoc);

		assertThat(ligaLinksIDs.size(), equalTo(5));
		assertThat(
				ligaLinksIDs.get(0),
				equalTo("http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=veranstaltung&veranstaltungid=8"));
	}

	@Test
	public void filtersAllMatchLinkSnippets() throws IOException {
		int matchCount = parser.filterMatchLinkSnippets(begegnungenDoc).size();

		assertThat(matchCount, equalTo(110));
	}

	@Test
	public void filtersAllGames() throws IOException {
		int gameCount = parser.filterGameSnippets(begegnungDoc).size();

		assertThat(gameCount, equalTo(16));
	}

	@Test
	public void detectsGamelistAsValid() throws IOException {
		Jerry gameSnippets = parser.filterGameSnippets(begegnungDoc);

		assertThat(parser.isValidGameList(gameSnippets), equalTo(true));
	}

	@Test
	public void detectsGameIsValidWithImagesAsValid() throws IOException {
		Jerry gameSnippets = parser.filterGameSnippets(begegnungBildDoc);

		assertThat(parser.isValidGameList(gameSnippets), equalTo(true));
	}

	@Test
	public void removesDescriptionFromTeamname() {
		String teamname = "Team Hauff (A)";
		assertThat(parser.removeTeamDescriptions(teamname),
				equalTo("Team Hauff"));
	}

	@Test
	public void returnsCompleteTeamnameIfItDoesNotContainDescriptions() {
		String teamname = "Team Hauff";
		assertThat(parser.removeTeamDescriptions(teamname),
				equalTo("Team Hauff"));
	}

	@Test
	public void returnsHomeTeamname() throws IOException {
		assertThat(parser.parseHomeTeam(begegnungDoc),
				equalTo("Tingeltangel FC St. Pauli"));
	}

	@Test
	public void returnsGuestTeamname() throws IOException {
		assertThat(parser.parseGuestTeam(begegnungDoc),
				equalTo("Hamburg Privateers 08"));
	}

	@Test
	public void detectsThatMatchHasNoMatchdate() throws IOException {
		assertThat(parser.hasMatchDate(begegnungNoDateDoc), equalTo(false));
	}

	@Test
	public void detectsThatMatchHasAMatchdate() throws IOException {
		assertThat(parser.hasMatchDate(begegnungDoc), equalTo(true));
	}

	@Test
	public void parsesAStringToDate() throws IOException {
		String rawDate = "27.02.2013 20:00";
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.setTimeInMillis(0);
		expectedDate.set(2013, 1, 27, 20, 0);

		Date resultDate = parser.parseDate(rawDate).getTime();

		assertThat(resultDate, equalTo(expectedDate.getTime()));
	}

	@Test
	public void parsesMatchdateFromMatchSnippet() throws IOException {
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.clear();
		expectedDate.set(2013, 1, 27, 20, 0);

		assertThat(parser.parseMatchDate(begegnungDoc, true),
				equalTo(expectedDate));
	}

	@Test
	public void returnsCleanCalendarIfMatchHasNoMatchDate() throws IOException {
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.setTimeInMillis(0);

		assertThat(parser.parseMatchDate(begegnungNoDateDoc, false),
				equalTo(expectedDate));
	}

	@Test
	public void parsesMatchdayFromMatchSnippet() throws IOException {
		assertThat(parser.parseMatchDay(begegnungDoc, true), equalTo(1));
	}

	@Test
	public void parsesMatchdayFromMatchSnippetWithoutDate() throws IOException {
		assertThat(parser.parseMatchDay(begegnungNoDateDoc, false), equalTo(5));
	}

	@Test
	public void parsesHomeScoreFromGameSnippet() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);

		assertThat(parser.parseHomeScore(rawGames.first(), false), equalTo(4));
	}

	@Test
	public void parsesGuestScoreFromGameSnippet() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);

		assertThat(parser.parseGuestScore(rawGames.first(), false), equalTo(7));
	}

	@Test
	public void parsesHomeScoreFromGameSnippetWithImages() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungBildDoc);

		assertThat(parser.parseHomeScore(rawGames.first(), true), equalTo(4));
	}

	@Test
	public void parsesGuestScoreFromGameSnippetWithImages() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungBildDoc);

		assertThat(parser.parseGuestScore(rawGames.first(), true), equalTo(7));
	}

	@Test
	public void singleMatchIsNotADoubleMatch() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);

		assertThat(parser.isDoubleMatch(rawGames.first()), equalTo(false));
	}

	@Test
	public void detectsDoubleMatch() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);

		assertThat(parser.isDoubleMatch(rawGames.last()), equalTo(true));
	}

	@Test
	public void detectsDoubleMatchWithImages() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungBildDoc);

		assertThat(parser.isDoubleMatch(rawGames.last()), equalTo(true));
	}

	@Test
	public void parsesPositionOfFirstGame() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);

		assertThat(parser.parseGamePosition(rawGames.first()), equalTo(1));
	}

	@Test
	public void parsesPositionOfLastGame() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);

		assertThat(parser.parseGamePosition(rawGames.last()), equalTo(16));
	}

	@Test
	public void parsesPlayerNamesOfLastGame() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);
		Game game = new Game();

		parser.addPlayerNames(game, rawGames.last(), true);

		assertThat(game.getHomePlayer1(), equalTo("Sommer, Sebastian"));
		assertThat(game.getHomePlayer2(), equalTo("Hölzer, Heinz"));
		assertThat(game.getGuestPlayer1(), equalTo("Nestvogel, Markus"));
		assertThat(game.getGuestPlayer2(), equalTo("Matheuszik, Sven"));
	}

	@Test
	public void parsesPlayerNamesOfFirstGame() throws IOException {
		Jerry rawGames = parser.filterGameSnippets(begegnungDoc);
		Game game = new Game();

		parser.addPlayerNames(game, rawGames.first(), false);

		assertThat(game.getHomePlayer1(), equalTo("Technau, Jerome"));
		assertThat(game.getGuestPlayer1(), equalTo("Hojas, René"));
	}

	@Test
	public void returnsAllGamesFromAMatch() {
		List<Game> games = parser.findGames(begegnungDoc);

		assertThat(games.size(), equalTo(16));
	}

	@Test
	public void returnsAllGamesWithImagesFromAMatch() {
		List<Game> games = parser.findGames(begegnungBildDoc);

		assertThat(games.size(), equalTo(16));
	}

	@Test
	public void returnsAFullFilledSingleGame() {
		Game game = new Game();
		game.setDoubleMatch(false);
		game.setGuestPlayer1("Matheuszik, Sven");
		game.setGuestScore(7);
		game.setGuestTeam("Hamburg Privateers 08");
		game.setHomePlayer1("Kränz, Ludwig");
		game.setHomeScore(5);
		game.setHomeTeam("Tingeltangel FC St. Pauli");
		game.setMatchDate(createCalendar(2013, 1, 27, 20, 0));
		game.setPosition(2);

		List<Game> games = parser.findGames(begegnungDoc);

		assertThat(games.get(1), equalTo(game));
	}

	@Test
	public void returnsAFullFilledDoubleGame() {
		Game game = new Game();
		game.setDoubleMatch(true);
		game.setGuestPlayer1("Zierott, Ulli");
		game.setGuestPlayer2("Hojas, René");
		game.setGuestScore(5);
		game.setGuestTeam("Hamburg Privateers 08");
		game.setHomePlayer1("Fischer, Harro");
		game.setHomePlayer2("Kränz, Ludwig");
		game.setHomeScore(4);
		game.setHomeTeam("Tingeltangel FC St. Pauli");
		game.setMatchDate(createCalendar(2013, 1, 27, 20, 0));
		game.setPosition(3);

		List<Game> games = parser.findGames(begegnungDoc);

		assertThat(games.get(2), equalTo(game));
	}

	@Test
	public void returnsAFullFilledSingleGameWithImages() {
		Game game = new Game();
		game.setDoubleMatch(false);
		game.setGuestPlayer1("Bai, Minyoung");
		game.setGuestScore(7);
		game.setGuestTeam("Die Maschinerie");
		game.setHomePlayer1("Arslan, Mehmet Emin");
		game.setHomeScore(4);
		game.setHomeTeam("Cim Bom Bom");
		game.setMatchDate(createCalendar(2013, 1, 28, 20, 0));
		game.setPosition(1);

		List<Game> games = parser.findGames(begegnungBildDoc);

		assertThat(games.get(0), equalTo(game));

	}

	@Test
	public void returnsAFullFilledDoubleGameWithImages() {
		Game game = new Game();
		game.setDoubleMatch(true);
		game.setHomePlayer1("Arslan, Mehmet Emin ");
		game.setHomePlayer2("Böckeler, Frank");
		game.setHomeScore(4);
		game.setHomeTeam("Cim Bom Bom");
		game.setGuestPlayer1("Bai, Minyoung");
		game.setGuestPlayer2("Linnenberg, Sebastian");
		game.setGuestScore(5);
		game.setGuestTeam("Die Maschinerie");
		game.setMatchDate(createCalendar(2013, 1, 28, 20, 0));
		game.setPosition(16);

		List<Game> games = parser.findGames(begegnungBildDoc);

		assertThat(games.get(15), equalTo(game));
	}

	@Test
	public void doesNotReturnGamesFromRelagation() {
		List<Game> games = parser.findGames(relegationDoc);

		assertThat(games.size(), equalTo(0));
	}

	@Test
	public void parsesGamesWithoutPlayernames() {
		List<Game> games = parser.findGames(begegnungNoNamesDoc);
		
		Game gameNoNames = games.get(13);
		assertThat(gameNoNames.getGuestPlayer1(), equalTo(""));
		assertThat(gameNoNames.getHomePlayer1(), equalTo(""));
		assertThat(gameNoNames.getHomeTeam(), equalTo("Die Hinkelsteinchen"));
		assertThat(gameNoNames.getGuestTeam(), equalTo("Kurbelkraft Bergedorf"));
		assertThat(gameNoNames.getHomeScore(), equalTo(7));
		assertThat(gameNoNames.getGuestScore(), equalTo(0));
		assertThat(gameNoNames.getPosition(), equalTo(14));
		assertThat(gameNoNames.getMatchDay(), equalTo(1));
		assertThat(gameNoNames.isDoubleMatch(), equalTo(false));
		assertThat(gameNoNames.getMatchDate(), equalTo(createCalendar(2013, 1, 28, 20, 0)));
	}

	private static Jerry loadFile(String fileName) throws IOException {
		File testFile = new File(KickerpageParserTest.RECOURCES_DIRECTORY
				+ fileName);
		return jerry().parse(FileUtil.readString(testFile));
	}

	private static Calendar createCalendar(int year, int month, int day,
			int hour, int min) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, month, day, hour, min);
		return calendar;
	}
}

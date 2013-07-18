package kickerstats.interfaces;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import kickerstats.interfaces.CsvCreator;
import kickerstats.types.Game;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class CsvCreatorTest {
	private CsvCreator csvCreator;

	@Before
	public void setup() {
		csvCreator = new CsvCreator();
	}

	@Test
	public void createsACompleteCsvFile() {
		List<Game> games = Lists.newArrayList(createSingleGame());

		List<String> csvGames = csvCreator.createCsvRowList(games);

		assertThat(
				csvGames.get(0),
				is("2013-02-27;1;2;Tingeltangel FC St. Pauli;Kränz, Ludwig;XXXX;5;7;Matheuszik, Sven;XXXX;Hamburg Privateers 08"));
	}

	@Test
	public void createsACompleteCsvFileWithADoubleGame() {
		List<Game> games = Lists.newArrayList(createDoubleGame());

		List<String> csvGames = csvCreator.createCsvRowList(games);

		assertThat(
				csvGames.get(0),
				is("2013-02-28;1;16;Cim Bom Bom;Arslan, Mehmet Emin;Böckeler, Frank;4;5;Bai, Minyoung;Linnenberg, Sebastian;Die Maschinerie"));
	}

	@Test
	public void createsCsvFile() {
		List<Game> games = Lists.newArrayList(createSingleGame(),
				createDoubleGame());
		List<String> gameStrings = csvCreator.createCsvRowList(games);

		csvCreator.createCsvFile(gameStrings);

		List<String> resultGameStrings = new ArrayList<>();

		Path path = Paths.get("allGames.csv");
		try (Scanner scanner = new Scanner(path, StandardCharsets.UTF_8.name())) {
			while (scanner.hasNextLine()) {
				resultGameStrings.add(scanner.nextLine());
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		path.toFile().delete();
		assertThat(resultGameStrings.get(0), containsString(gameStrings.get(0)));
		assertThat(resultGameStrings.get(1), containsString(gameStrings.get(1)));
	}

	@Test
	public void createsShortDates() {
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(0);
		date.set(2013, 1, 27, 20, 0);

		assertThat(csvCreator.formatDate(date), is("2013-02-27"));
	}

	@Test
	public void replacesEmptyName() {
		assertThat(csvCreator.replaceEmptyNames(""), is("XXXX"));
	}

	@Test
	public void replacesUnsetName() {
		assertThat(csvCreator.replaceEmptyNames(null), is("XXXX"));
	}

	@Test
	public void doesNotReplaceFilledNames() {
		assertThat(csvCreator.replaceEmptyNames("Ich"), is("Ich"));
	}

	protected static Game createSingleGame() {
		Game singleGame = new Game();
		singleGame.setDoubleMatch(false);
		singleGame.setGuestPlayer1("Matheuszik, Sven");
		singleGame.setGuestScore(7);
		singleGame.setGuestTeam("Hamburg Privateers 08");
		singleGame.setHomePlayer1("Kränz, Ludwig");
		singleGame.setHomeScore(5);
		singleGame.setHomeTeam("Tingeltangel FC St. Pauli");
		singleGame.setMatchDate(PageParserUnitTest.createCalendar(2013,
				1, 27, 20, 0));
		singleGame.setMatchDay(1);
		singleGame.setPosition(2);
		return singleGame;
	}

	protected static Game createSecondSingleGame() {
		Game game = new Game();
		game.setDoubleMatch(false);
		game.setGuestPlayer1("Bai, Minyoung");
		game.setGuestScore(7);
		game.setGuestTeam("Die Maschinerie");
		game.setHomePlayer1("Arslan, Mehmet Emin");
		game.setHomeScore(4);
		game.setHomeTeam("Cim Bom Bom");
		game.setMatchDate(PageParserUnitTest.createCalendar(2013, 1, 28,
				20, 0));
		game.setMatchDay(1);
		game.setPosition(1);
		return game;
	}

	protected static Game createDoubleGame() {
		Game doubleGame = new Game();
		doubleGame.setDoubleMatch(true);
		doubleGame.setHomePlayer1("Arslan, Mehmet Emin");
		doubleGame.setHomePlayer2("Böckeler, Frank");
		doubleGame.setHomeScore(4);
		doubleGame.setHomeTeam("Cim Bom Bom");
		doubleGame.setGuestPlayer1("Bai, Minyoung");
		doubleGame.setGuestPlayer2("Linnenberg, Sebastian");
		doubleGame.setGuestScore(5);
		doubleGame.setGuestTeam("Die Maschinerie");
		doubleGame.setMatchDay(1);
		doubleGame.setMatchDate(PageParserUnitTest.createCalendar(2013,
				1, 28, 20, 0));
		doubleGame.setPosition(16);
		return doubleGame;
	}
}

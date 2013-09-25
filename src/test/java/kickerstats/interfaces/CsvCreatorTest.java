package kickerstats.interfaces;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import kickerstats.GameTestdata;
import kickerstats.MatchTestdata;
import kickerstats.types.Game;
import kickerstats.types.Match;

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
		List<Match> matches = Arrays.asList(MatchTestdata.createMatchWithSinglegame());

		List<String> csvGames = csvCreator.createCsvRowList(matches);

		assertThat(
				csvGames.get(0),
				is("2013-02-27;1;2;Tingeltangel FC St. Pauli;Kränz, Ludwig;XXXX;5;7;Matheuszik, Sven;XXXX;Hamburg Privateers 08"));
	}

	@Test
	public void createsACompleteCsvFileWithADoubleGame() {
		List<Match> matches = Arrays.asList(MatchTestdata.createMatchWithDoublegame());

		List<String> csvGames = csvCreator.createCsvRowList(matches);

		assertThat(
				csvGames.get(0),
				is("2013-02-28;1;16;Cim Bom Bom;Arslan, Mehmet Emin;Böckeler, Frank;4;5;Bai, Minyoung;Linnenberg, Sebastian;Die Maschinerie"));
	}

	@Test
	public void createsCsvFile() {
		List<Match> matches = Arrays.asList(MatchTestdata.createMatch());

		List<String> gameStrings = csvCreator.createCsvRowList(matches);

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

		assertThat(csvCreator.formatDate(date.getTime()), is("2013-02-27"));
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

}

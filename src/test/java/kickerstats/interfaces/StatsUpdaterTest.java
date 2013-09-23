package kickerstats.interfaces;

import java.util.List;

import javax.inject.Inject;

import kickerstats.WeldJUnit4Runner;
import kickerstats.types.Match;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4Runner.class)
public class StatsUpdaterTest {
	@Inject
	private StatsUpdater statsUpdater;

	@Inject
	private CsvCreator csvCreator;

	@Ignore
	@Test
	public void createCSVFileWithAllGames() {
		List<Match> matches = statsUpdater.downloadAllMatches();
		List<String> gameStrings = csvCreator.createCsvRowList(matches);
		csvCreator.createCsvFile(gameStrings);
	}
	
	@Ignore
	@Test
	public void savesAllMatchesWithGames() {
		statsUpdater.getAllData();
	}
}

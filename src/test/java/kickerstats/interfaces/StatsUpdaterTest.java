package kickerstats.interfaces;

import java.util.List;

import javax.inject.Inject;

import kickerstats.WeldJUnit4Runner;
import kickerstats.interfaces.CsvCreator;
import kickerstats.interfaces.StatsUpdater;
import kickerstats.types.Game;

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
		List<Game> games = statsUpdater.downloadAllGames();
		List<String> gameStrings = csvCreator.createCsvRowList(games);
		csvCreator.createCsvFile(gameStrings);
	}
	
	@Ignore
	@Test
	public void savesAllMatchesWithGames() {
		statsUpdater.getAllData();
	}
}

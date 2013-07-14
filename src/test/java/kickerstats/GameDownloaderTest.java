package kickerstats;

import java.util.List;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4Runner.class)
public class GameDownloaderTest {
	@Inject
	private GameDownloader gameDownloader;

	@Inject
	private CsvCreator csvCreator;

	@Ignore
	@Test
	public void createCSVFileWithAllGames() {
		List<Game> games = gameDownloader.downloadAllGames();
		List<String> gameStrings = csvCreator.createGameStringList(games);
		csvCreator.createCsvFile(gameStrings);
	}
}

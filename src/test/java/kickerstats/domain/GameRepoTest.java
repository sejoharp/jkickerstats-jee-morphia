package kickerstats.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import kickerstats.GameTestdaten;
import kickerstats.WeldJUnit4Runner;
import kickerstats.types.Game;

import org.ektorp.BulkDeleteDocument;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4Runner.class)
public class GameRepoTest {

	@Inject
	private GameRepoInterface gameRepo;

	@Before
	public void cleanGamesInDb() {
		ViewQuery query = new ViewQuery().designDocId("_design/games")
				.viewName("by_date").includeDocs(true).limit(1000000);

		CouchDbConnector connection = new CouchDb().createConnection();

		List<GameCouchDb> allDocs = connection.queryView(query,
				GameCouchDb.class);

		List<BulkDeleteDocument> docsForDeletion = new ArrayList<>();
		for (GameCouchDb doc : allDocs) {
			docsForDeletion.add(BulkDeleteDocument.of(doc));
		}
		connection.executeBulk(docsForDeletion);
	}

	@Test
	public void dbConnectionIsAvailable() {
		CouchDbConnector connection = new CouchDb().createConnection();
		connection.getDbInfo();
	}

	@Test
	public void savesGameCouchDbInDb() {
		CouchDbConnector connection = new CouchDb().createConnection();
		GameCouchDb gameCouchDb = GameTestdaten.createDoubleGameCouchDb();
		assertThat(gameCouchDb.getId(), is(nullValue()));

		connection.create(gameCouchDb);

		assertThat(gameCouchDb.getId(), is(notNullValue()));
		assertThat(connection.contains(gameCouchDb.getId()), is(true));

		connection.delete(gameCouchDb);
	}

	@Test
	public void getsAllGames() {
		gameRepo.save(GameTestdaten.createDoubleGame());
		List<Game> allGames = gameRepo.getAllGames();

		assertThat(allGames.size(), is(1));
	}

	@Test
	public void savesAListOfGames() {
		gameRepo.save(Arrays.asList(GameTestdaten.createDoubleGame()));

		assertThat(gameRepo.getGameCount(), is(1));
	}
}

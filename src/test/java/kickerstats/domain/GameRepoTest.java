package kickerstats.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import kickerstats.WeldJUnit4Runner;
import kickerstats.types.Game;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4Runner.class)
public class GameRepoTest {

	@Inject
	private GameRepo gameRepo;

	@Before
	public void cleanGamesInDb() {
		ViewQuery query = new ViewQuery().designDocId("_design/games")
				.viewName("by_date").includeDocs(true);
		CouchDbConnector connection = new CouchDb().createConnection();
		List<GameCouchDb> allGames = connection.queryView(query,
				GameCouchDb.class);
		for (GameCouchDb game : allGames) {
			connection.delete(game);
		}
	}
	
	@Test
	public void dbConnectionIsAvailable() {
		CouchDbConnector connection = new CouchDb().createConnection();
		connection.getDbInfo();
	}

	@Test
	public void savesGameCouchDbInDb() {
		CouchDbConnector connection = new CouchDb().createConnection();
		GameCouchDb gameCouchDb = createDoubleGameCouchDb();
		assertThat(gameCouchDb.getId(), is(nullValue()));

		connection.create(gameCouchDb);

		assertThat(gameCouchDb.getId(), is(notNullValue()));
		assertThat(connection.contains(gameCouchDb.getId()), is(true));

		connection.delete(gameCouchDb);
	}

	@Test
	public void getsAllGames() {
		gameRepo.save(createDoubleGame());
		List<Game> allGames = gameRepo.getAllGames();

		assertThat(allGames.size(), is(1));
	}

	@Test
	public void savesAListOfGames() {
		gameRepo.save(Arrays.asList(createDoubleGame()));

		assertThat(gameRepo.getGameCount(), is(1));
	}

	protected GameCouchDb createDoubleGameCouchDb() {
		GameCouchDb gameCouchDb = new GameCouchDb();
		gameCouchDb.setDoubleMatch(true);
		gameCouchDb.setGuestPlayer1("guest player1");
		gameCouchDb.setGuestPlayer2("guest player2");
		gameCouchDb.setGuestScore(10);
		gameCouchDb.setGuestTeam("guestteam");
		gameCouchDb.setHomePlayer1("home player1");
		gameCouchDb.setHomePlayer2("home player2");
		gameCouchDb.setHomeScore(22);
		gameCouchDb.setHomeTeam("hometeam");
		gameCouchDb.setMatchDate(Calendar.getInstance());
		gameCouchDb.setMatchDay(1);
		gameCouchDb.setPosition(2);
		return gameCouchDb;
	}

	protected Game createDoubleGame() {
		Game game = new Game();
		game.setDoubleMatch(true);
		game.setGuestPlayer1("guest player1");
		game.setGuestPlayer2("guest player2");
		game.setGuestScore(10);
		game.setGuestTeam("guestteam");
		game.setHomePlayer1("home player1");
		game.setHomePlayer2("home player2");
		game.setHomeScore(22);
		game.setHomeTeam("hometeam");
		game.setMatchDate(Calendar.getInstance());
		game.setMatchDay(1);
		game.setPosition(2);
		return game;
	}
}

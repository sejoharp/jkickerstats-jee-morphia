package kickerstats.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import kickerstats.WeldJUnit4Runner;
import kickerstats.types.Game;

import org.ektorp.CouchDbConnector;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4Runner.class)
public class GameRepoTest {

	@Inject
	private GameRepo gameRepo;

	@Test
	public void dbConnectionIsAvailable() {
		CouchDbConnector connection = new CouchDbConnectionCreator().getConn();
		connection.getDbInfo();
	}

	@Test
	public void savesGameInDb() {
		CouchDbConnector connection = new CouchDbConnectionCreator().getConn();
		GameCouchDb gameCouchDb = createDoubleGame();
		assertThat(gameCouchDb.getId(), is(nullValue()));
		
		connection.create(gameCouchDb);
		
		assertThat(gameCouchDb.getId(), is(notNullValue()));
		assertThat(connection.contains(gameCouchDb.getId()), is(true));
		
		connection.delete(gameCouchDb);
	}

	@Test
	public void getsAllGames() {
		List<Game> allGames = gameRepo.getAllGames();
		
		assertThat(allGames.size(), is(1));
	}

	protected GameCouchDb createDoubleGame() {
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
}

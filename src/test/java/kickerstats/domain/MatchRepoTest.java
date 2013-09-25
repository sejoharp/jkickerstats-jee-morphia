package kickerstats.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.UnknownHostException;

import javax.inject.Inject;

import kickerstats.GameTestdata;
import kickerstats.MatchTestdata;
import kickerstats.MongoDbTestFactory;
import kickerstats.WeldJUnit4Runner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.query.Query;

@RunWith(WeldJUnit4Runner.class)
public class MatchRepoTest {
	@Inject
	private MatchRepoInterface matchRepo;
	@Inject
	private MongoDb mongoDb;

	@Before
	public void init() throws UnknownHostException {
		mongoDb.setDatastore(MongoDbTestFactory.createDatastoreForTest());
		cleanUpDb();
	}

	protected void cleanUpDb() {
		Query<MatchFromDb> query = mongoDb.getDatastore().createQuery(
				MatchFromDb.class);
		mongoDb.getDatastore().delete(query);
	}

	@Test
	public void dbHasNoMatches() {
		assertThat(matchRepo.noMatchesAvailable(), is(true));
	}

	@Test
	public void dbHasMatches() {
		matchRepo.save(MatchTestdata.createMatch());

		assertThat(matchRepo.noMatchesAvailable(), is(false));
		MatchFromDb matchFromDb = mongoDb.getDatastore()
				.find(MatchFromDb.class).get();
		assertThat(matchFromDb.getGames().size(), is(2));
	}
	
	@Test
	public void savesACompleteMatch() {
		matchRepo.save(MatchTestdata.createMatch());
		MatchFromDb matchFromDb = mongoDb.getDatastore()
				.find(MatchFromDb.class).get();
		
		assertThat(matchFromDb.getGuestScore(), is(10));
		assertThat(matchFromDb.getGuestTeam(), is("guestteam"));
		assertThat(matchFromDb.getHomeScore(), is(22));
		assertThat(matchFromDb.getHomeTeam(), is("hometeam"));
		assertThat(matchFromDb.getMatchDate(), is(GameTestdata.createDate(2013, 01, 27, 19, 1)));
		assertThat(matchFromDb.getMatchDay(), is(1));
		assertThat(matchFromDb.getHomeGoals(), is(10));
		assertThat(matchFromDb.getGuestGoals(), is(11));
		
		GameFromDb gameFromDb = matchFromDb.getGames().get(1);
		assertThat(gameFromDb.isDoubleMatch(), is(true));
		assertThat(gameFromDb.getHomePlayer1(), is("Arslan, Mehmet Emin"));
		assertThat(gameFromDb.getHomePlayer2(), is("Böckeler, Frank"));
		assertThat(gameFromDb.getHomeScore(), is(4));
		assertThat(gameFromDb.getGuestPlayer1(), is("Bai, Minyoung"));
		assertThat(gameFromDb.getGuestPlayer2(), is("Linnenberg, Sebastian"));
		assertThat(gameFromDb.getGuestScore(), is(5));
		assertThat(gameFromDb.getPosition(), is(16));
		
	}
}

package kickerstats.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.UnknownHostException;

import javax.inject.Inject;

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
}

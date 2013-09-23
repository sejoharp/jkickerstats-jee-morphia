package kickerstats.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.UnknownHostException;

import javax.inject.Inject;

import kickerstats.MatchTestdata;
import kickerstats.WeldJUnit4Runner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@RunWith(WeldJUnit4Runner.class)
public class MatchRepoTest {
	@Inject
	private MatchRepoInterface matchRepo;
	@Inject
	private MongoDb mongoDb;

	@Before
	public void init() throws UnknownHostException {
		Datastore datastore = initDb();
		mongoDb.setDatastore(datastore);

		cleanUpDb();
	}

	protected void cleanUpDb() {
		Query<MatchFromDb> query = mongoDb.getDatastore().createQuery(
				MatchFromDb.class);
		mongoDb.getDatastore().delete(query);
	}

	protected Datastore initDb() throws UnknownHostException {
		ServerAddress dbAddress = new ServerAddress("localhost", 27017);

		Morphia morphia = new Morphia();
		morphia.map(MatchFromDb.class, GameFromDb.class);
		Datastore datastore = morphia.createDatastore(
				new MongoClient(dbAddress), "kickerstats_test");

		return datastore;
	}

	@Test
	public void dbHasNoMatches() {
		assertThat(matchRepo.noMatchesAvailable(), is(true));
	}

	@Test
	public void dbHasMatches() {
		matchRepo.save(MatchTestdata.createMatch());
		
		assertThat(matchRepo.noMatchesAvailable(), is(false));
		MatchFromDb matchFromDb = mongoDb.getDatastore().find(MatchFromDb.class).get();
		assertThat(matchFromDb.getGames().size(), is(2));
	}
}

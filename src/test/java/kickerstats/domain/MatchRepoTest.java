package kickerstats.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kickerstats.MatchTestdaten;
import kickerstats.WeldJUnit4Runner;

import org.ektorp.BulkDeleteDocument;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4Runner.class)
public class MatchRepoTest {
	@Inject
	private MatchRepoInterface matchRepo;
	@Inject
	private CouchDb couchDb;

	@Before
	public void cleanMatchesInDb() {
		ViewQuery query = new ViewQuery().designDocId("_design/matches")
				.viewName("by_date_hometeam_guestteam").includeDocs(true)
				.limit(1000000);

		CouchDbConnector connection = couchDb.createConnection();

		List<MatchFromDb> allDocs = connection.queryView(query,
				MatchFromDb.class);

		List<BulkDeleteDocument> docsForDeletion = new ArrayList<>();
		for (MatchFromDb doc : allDocs) {
			docsForDeletion.add(BulkDeleteDocument.of(doc));
		}
		connection.executeBulk(docsForDeletion);
	}

	@Test
	public void dbHasNoMatches() {
		assertThat(matchRepo.noMatchesAvailable(), is(true));
	}

	@Test
	public void dbHasMatches() {
		matchRepo.save(MatchTestdaten.createMatch());
		assertThat(matchRepo.noMatchesAvailable(), is(false));
	}
}

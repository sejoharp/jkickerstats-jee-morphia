package kickerstats.domain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kickerstats.WeldJUnit4Runner;

import org.ektorp.BulkDeleteDocument;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(WeldJUnit4Runner.class)
public class MatchRepoTest {
	@Inject
	private MatchRepoInterface matchRepo;

	@Before
	public void cleanMatchesInDb() {
		ViewQuery query = new ViewQuery().designDocId("_design/matches")
				.viewName("by_date_hometeam_guestteam").includeDocs(true).limit(1000000);
		
		CouchDbConnector connection = new CouchDb().createConnection();
		
		List<MatchCouchDb> allDocs = connection.queryView(query,
				MatchCouchDb.class);
		
		List<BulkDeleteDocument> docsForDeletion = new ArrayList<>();
		for (MatchCouchDb doc : allDocs) {
			docsForDeletion.add(BulkDeleteDocument.of(doc));
		}
		connection.executeBulk(docsForDeletion);
	}
	
	@Test
	public void dbHasNoMatches() {
		assertThat(matchRepo.noMatchesAvailable(),is(true));
	}
}

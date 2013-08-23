package kickerstats.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;

import kickerstats.types.Match;

public class MatchRepo implements MatchRepoInterface {
	@Inject
	private CouchDb couchdb;

	@Override
	public boolean isNewMatch(Match match) {
		ViewQuery query = new ViewQuery()
				.designDocId("_design/matches")
				.viewName("by_date_hometeam_guestteam")
				.keys(Arrays.asList(match.getMatchDate(), match.getHomeTeam(),
						match.getGuestTeam()));

		ViewResult matches = couchdb.createConnection().queryView(query);
		return matches.isEmpty();
	}

	@Override
	public void save(Match match) {
		couchdb.createConnection().create(toMatchCouchDb(match));
	}

	@Override
	public void save(List<Match> matches) {
		for (Match match : matches) {
			save(match);
		}
	}

	@Override
	public boolean matchesAvailable() {
		ViewQuery query = new ViewQuery().designDocId("_design/matches")
				.viewName("by_date_hometeam_guestteam");

		ViewResult allMatches = couchdb.createConnection().queryView(query);
		return allMatches.isEmpty();
	}

	protected List<MatchCouchDb> toMatchCouchDbList(List<Match> matches) {
		List<MatchCouchDb> matchCouchDbs = new ArrayList<>();
		for (Match match : matches) {
			matchCouchDbs.add(toMatchCouchDb(match));
		}
		return matchCouchDbs;
	}

	protected List<Match> toMatchList(List<MatchCouchDb> matchCouchDbs) {
		List<Match> matches = new ArrayList<>();
		for (MatchCouchDb matchCouchDb : matchCouchDbs) {
			matches.add(toMatch(matchCouchDb));
		}
		return matches;
	}

	protected MatchCouchDb toMatchCouchDb(Match match) {
		MatchCouchDb matchCouchDb = new MatchCouchDb();
		matchCouchDb.setGuestGoals(match.getGuestGoals());
		matchCouchDb.setGuestScore(match.getGuestScore());
		matchCouchDb.setGuestTeam(match.getGuestTeam());
		matchCouchDb.setHomeGoals(match.getHomeGoals());
		matchCouchDb.setHomeScore(match.getHomeScore());
		matchCouchDb.setHomeTeam(match.getHomeTeam());
		matchCouchDb.setMatchDate(match.getMatchDate());
		matchCouchDb.setMatchDay(match.getMatchDay());
		return matchCouchDb;
	}

	protected Match toMatch(MatchCouchDb matchCouchDb) {
		Match match = new Match();
		match.setGuestGoals(matchCouchDb.getGuestGoals());
		match.setGuestScore(matchCouchDb.getGuestScore());
		match.setGuestTeam(matchCouchDb.getGuestTeam());
		match.setHomeGoals(matchCouchDb.getHomeGoals());
		match.setHomeScore(matchCouchDb.getHomeScore());
		match.setHomeTeam(matchCouchDb.getHomeTeam());
		match.setMatchDate(matchCouchDb.getMatchDate());
		match.setMatchDay(matchCouchDb.getMatchDay());
		return match;
	}
}

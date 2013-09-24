package kickerstats.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import kickerstats.types.Match;

public class MatchRepo implements MatchRepoInterface {

	@Inject
	private MongoDb mongoDb;

	@Override
	public boolean isNewMatch(Match match) {
		long numberOfMatches = mongoDb.getDatastore().find(MatchFromDb.class)
				.field("matchDate").equal(match.getMatchDate())
				.field("homeTeam").equal(match.getHomeTeam())
				.field("guestTeam").equal(match.getGuestTeam()).countAll();

		return numberOfMatches > 0;
	}

	@Override
	public void save(Match match) {
		mongoDb.getDatastore().save(convertToMatchFromDb(match));
	}

	@Override
	public void save(List<Match> matches) {
		mongoDb.getDatastore().save(convertToMatchFromDbList(matches));
	}

	@Override
	public boolean noMatchesAvailable() {
		long numberOfMatches = mongoDb.getDatastore().getCount(
				MatchFromDb.class);
		return numberOfMatches == 0;
	}

	@Override
	public List<Match> getAllMatches() {
		List<MatchFromDb> matches = mongoDb.getDatastore()
				.find(MatchFromDb.class).asList();
		return convertToMatchList(matches);
	}

	protected List<MatchFromDb> convertToMatchFromDbList(List<Match> matches) {
		List<MatchFromDb> matchCouchDbs = new ArrayList<>();
		for (Match match : matches) {
			matchCouchDbs.add(convertToMatchFromDb(match));
		}
		return matchCouchDbs;
	}

	protected List<Match> convertToMatchList(List<MatchFromDb> matchCouchDbs) {
		List<Match> matches = new ArrayList<>();
		for (MatchFromDb matchCouchDb : matchCouchDbs) {
			matches.add(convertToMatch(matchCouchDb));
		}
		return matches;
	}

	protected MatchFromDb convertToMatchFromDb(Match match) {
		MatchFromDb matchCouchDb = new MatchFromDb();
		matchCouchDb.setGuestGoals(match.getGuestGoals());
		matchCouchDb.setGuestScore(match.getGuestScore());
		matchCouchDb.setGuestTeam(match.getGuestTeam());
		matchCouchDb.setHomeGoals(match.getHomeGoals());
		matchCouchDb.setHomeScore(match.getHomeScore());
		matchCouchDb.setHomeTeam(match.getHomeTeam());
		matchCouchDb.setMatchDate(match.getMatchDate().getTime());
		matchCouchDb.setMatchDay(match.getMatchDay());
		return matchCouchDb;
	}

	protected Match convertToMatch(MatchFromDb matchCouchDb) {
		Match match = new Match();
		match.setGuestGoals(matchCouchDb.getGuestGoals());
		match.setGuestScore(matchCouchDb.getGuestScore());
		match.setGuestTeam(matchCouchDb.getGuestTeam());
		match.setHomeGoals(matchCouchDb.getHomeGoals());
		match.setHomeScore(matchCouchDb.getHomeScore());
		match.setHomeTeam(matchCouchDb.getHomeTeam());

		Calendar cal = Calendar.getInstance();
		cal.setTime(matchCouchDb.getMatchDate());
		match.setMatchDate(cal);
		match.setMatchDay(matchCouchDb.getMatchDay());
		return match;
	}
}

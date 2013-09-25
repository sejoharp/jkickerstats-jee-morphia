package kickerstats.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import kickerstats.types.Game;
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
		List<MatchFromDb> matchFromDbs = new ArrayList<>();
		for (Match match : matches) {
			matchFromDbs.add(convertToMatchFromDb(match));
		}
		return matchFromDbs;
	}

	protected List<Match> convertToMatchList(List<MatchFromDb> matchFromDbs) {
		List<Match> matches = new ArrayList<>();
		for (MatchFromDb matchFromDb : matchFromDbs) {
			matches.add(convertToMatch(matchFromDb));
		}
		return matches;
	}

	protected MatchFromDb convertToMatchFromDb(Match match) {
		MatchFromDb matchFromDb = new MatchFromDb();
		matchFromDb.setGuestGoals(match.getGuestGoals());
		matchFromDb.setGuestScore(match.getGuestScore());
		matchFromDb.setGuestTeam(match.getGuestTeam());
		matchFromDb.setHomeGoals(match.getHomeGoals());
		matchFromDb.setHomeScore(match.getHomeScore());
		matchFromDb.setHomeTeam(match.getHomeTeam());
		matchFromDb.setMatchDate(match.getMatchDate().getTime());
		matchFromDb.setMatchDay(match.getMatchDay());
		matchFromDb.setGames(convertToGameFromDbList(match.getGames()));
		return matchFromDb;
	}

	protected Match convertToMatch(MatchFromDb matchFromDb) {
		Match match = new Match();
		match.setGuestGoals(matchFromDb.getGuestGoals());
		match.setGuestScore(matchFromDb.getGuestScore());
		match.setGuestTeam(matchFromDb.getGuestTeam());
		match.setHomeGoals(matchFromDb.getHomeGoals());
		match.setHomeScore(matchFromDb.getHomeScore());
		match.setHomeTeam(matchFromDb.getHomeTeam());

		Calendar cal = Calendar.getInstance();
		cal.setTime(matchFromDb.getMatchDate());
		match.setMatchDate(cal);
		match.setMatchDay(matchFromDb.getMatchDay());
		match.setGames(convertToGameList(matchFromDb.getGames()));
		return match;
	}
	
	protected List<GameFromDb> convertToGameFromDbList(List<Game> games) {
		List<GameFromDb> gameFromDbList = new ArrayList<>();
		for (Game game : games) {
			gameFromDbList.add(convertToGameFromDb(game));
		}
		return gameFromDbList;
	}

	protected List<Game> convertToGameList(List<GameFromDb> gameCouchDbList) {
		List<Game> games = new ArrayList<>();
		for (GameFromDb gameFromDb : gameCouchDbList) {
			games.add(convertToGame(gameFromDb));
		}
		return games;
	}

	protected GameFromDb convertToGameFromDb(Game game) {
		GameFromDb gameFromDb = new GameFromDb();
		gameFromDb.setDoubleMatch(game.isDoubleMatch());
		gameFromDb.setGuestPlayer1(game.getGuestPlayer1());
		gameFromDb.setGuestPlayer2(game.getGuestPlayer2());
		gameFromDb.setGuestScore(game.getGuestScore());
		gameFromDb.setHomePlayer1(game.getHomePlayer1());
		gameFromDb.setHomePlayer2(game.getHomePlayer2());
		gameFromDb.setHomeScore(game.getHomeScore());
		gameFromDb.setPosition(game.getPosition());
		return gameFromDb;
	}

	protected Game convertToGame(GameFromDb gameCouchDb) {
		Game game = new Game();
		game.setDoubleMatch(gameCouchDb.isDoubleMatch());
		game.setGuestPlayer1(gameCouchDb.getGuestPlayer1());
		game.setGuestPlayer2(gameCouchDb.getGuestPlayer2());
		game.setGuestScore(gameCouchDb.getGuestScore());
		game.setHomePlayer1(gameCouchDb.getHomePlayer1());
		game.setHomePlayer2(gameCouchDb.getHomePlayer2());
		game.setHomeScore(gameCouchDb.getHomeScore());
		game.setPosition(gameCouchDb.getPosition());
		return game;
	}

}

package kickerstats.domain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kickerstats.types.Game;

import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;

public class GameRepo implements GameRepoInterface {

	@Inject
	private CouchDb couchdb;

	@Override
	public void save(Game game) {
		couchdb.createConnection().create(toGameCouchDb(game));
	}

	@Override
	public void save(List<Game> games) {
		for (Game game : games) {
			save(game);
		}
	}

	@Override
	public List<Game> getAllGames() {
		ViewQuery query = new ViewQuery().designDocId("_design/games")
				.viewName("by_date").descending(true).includeDocs(true);

		List<GameCouchDb> allGames = couchdb.createConnection().queryView(query,
				GameCouchDb.class);
		return toGameList(allGames);
	}

	@Override
	public int getGameCount() {
		ViewQuery query = new ViewQuery().designDocId("_design/games")
				.viewName("by_date");

		ViewResult allGames = couchdb.createConnection().queryView(query);
		return allGames.getSize();
	}
	
	protected List<GameCouchDb> toGameCouchDbList(List<Game> games) {
		List<GameCouchDb> gameCouchDbList = new ArrayList<>();
		for (Game game : games) {
			gameCouchDbList.add(toGameCouchDb(game));
		}
		return gameCouchDbList;
	}

	protected List<Game> toGameList(List<GameCouchDb> gameCouchDbList) {
		List<Game> games = new ArrayList<>();
		for (GameCouchDb gameCouchDb : gameCouchDbList) {
			games.add(toGame(gameCouchDb));
		}
		return games;
	}

	protected GameCouchDb toGameCouchDb(Game game) {
		GameCouchDb gameCouchDb = new GameCouchDb();
		gameCouchDb.setDoubleMatch(game.isDoubleMatch());
		gameCouchDb.setGuestPlayer1(game.getGuestPlayer1());
		gameCouchDb.setGuestPlayer2(game.getGuestPlayer2());
		gameCouchDb.setGuestScore(game.getGuestScore());
		gameCouchDb.setGuestTeam(game.getGuestTeam());
		gameCouchDb.setHomePlayer1(game.getHomePlayer1());
		gameCouchDb.setHomePlayer2(game.getHomePlayer2());
		gameCouchDb.setHomeScore(game.getHomeScore());
		gameCouchDb.setHomeTeam(game.getHomeTeam());
		gameCouchDb.setMatchDate(game.getMatchDate());
		gameCouchDb.setMatchDay(game.getMatchDay());
		gameCouchDb.setPosition(game.getPosition());
		return gameCouchDb;
	}

	protected Game toGame(GameCouchDb gameCouchDb) {
		Game game = new Game();
		game.setDoubleMatch(gameCouchDb.isDoubleMatch());
		game.setGuestPlayer1(gameCouchDb.getGuestPlayer1());
		game.setGuestPlayer2(gameCouchDb.getGuestPlayer2());
		game.setGuestScore(gameCouchDb.getGuestScore());
		game.setGuestTeam(gameCouchDb.getGuestTeam());
		game.setHomePlayer1(gameCouchDb.getHomePlayer1());
		game.setHomePlayer2(gameCouchDb.getHomePlayer2());
		game.setHomeScore(gameCouchDb.getHomeScore());
		game.setHomeTeam(gameCouchDb.getHomeTeam());
		game.setMatchDate(gameCouchDb.getMatchDate());
		game.setMatchDay(gameCouchDb.getMatchDay());
		game.setPosition(gameCouchDb.getPosition());
		return game;
	}

	public void setCouchDb(CouchDb db) {
		this.couchdb = db;
	}
}
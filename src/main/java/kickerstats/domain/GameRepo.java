package kickerstats.domain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kickerstats.types.Game;

import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.ektorp.support.Views;

@Views({
		@View(name = "all", map = "function(doc) { if (doc.type == 'game' ) emit( null, null );}"),
		@View(name = "by_date", map = "function(doc) {if(doc.type=='game'){emit(doc.matchDate, null)};}"),
		@View(name = "by_date_hometeam_guestteam", map = "function(doc) {if(doc.type=='game'){emit([doc.matchDate,doc.homeTeam,doc.guestTeam], null)};}") 
		})
public class GameRepo extends CouchDbRepositorySupport<GameCouchDb> implements
		GameRepoInterface {

	private static final String GAME_DESIGN_DOC_NAME = "games";

	@Inject
	public GameRepo(CouchDb couchdb) {
		super(GameCouchDb.class, couchdb.createConnection(),
				GAME_DESIGN_DOC_NAME);
		initStandardDesignDocument();
	}

	@Override
	public void save(Game game) {
		add(toGameCouchDb(game));
	}

	@Override
	public void save(List<Game> games) {
		db.executeBulk(toGameCouchDbList(games));
	}

	@Override
	public List<Game> getAllGames() {
		ViewQuery query = new ViewQuery().designDocId("_design/games")
				.viewName("by_date").descending(true).includeDocs(true);

		List<GameCouchDb> allGames = db.queryView(query, GameCouchDb.class);
		return toGameList(allGames);
	}

	@Override
	public int getGameCount() {
		return getAll().size();
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
}

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
public class GameRepo extends CouchDbRepositorySupport<GameFromDb> implements
		GameRepoInterface {

	private static final String GAME_DESIGN_DOC_NAME = "games";

	@Inject
	public GameRepo(CouchDb couchdb) {
		super(GameFromDb.class, couchdb.createConnection(),
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

		List<GameFromDb> allGames = db.queryView(query, GameFromDb.class);
		return toGameList(allGames);
	}

	@Override
	public int getGameCount() {
		return getAll().size();
	}

	protected List<GameFromDb> toGameCouchDbList(List<Game> games) {
		List<GameFromDb> gameCouchDbList = new ArrayList<>();
		for (Game game : games) {
			gameCouchDbList.add(toGameCouchDb(game));
		}
		return gameCouchDbList;
	}

	protected List<Game> toGameList(List<GameFromDb> gameCouchDbList) {
		List<Game> games = new ArrayList<>();
		for (GameFromDb gameCouchDb : gameCouchDbList) {
			games.add(toGame(gameCouchDb));
		}
		return games;
	}

	protected GameFromDb toGameCouchDb(Game game) {
		GameFromDb gameCouchDb = new GameFromDb();
		gameCouchDb.setDoubleMatch(game.isDoubleMatch());
		gameCouchDb.setGuestPlayer1(game.getGuestPlayer1());
		gameCouchDb.setGuestPlayer2(game.getGuestPlayer2());
		gameCouchDb.setGuestScore(game.getGuestScore());
		gameCouchDb.setHomePlayer1(game.getHomePlayer1());
		gameCouchDb.setHomePlayer2(game.getHomePlayer2());
		gameCouchDb.setHomeScore(game.getHomeScore());
		gameCouchDb.setPosition(game.getPosition());
		return gameCouchDb;
	}

	protected Game toGame(GameFromDb gameCouchDb) {
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

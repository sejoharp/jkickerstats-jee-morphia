package kickerstats.usecases;

import java.util.List;

import javax.inject.Inject;

import kickerstats.domain.GameRepoInterface;
import kickerstats.types.Game;

public class GameService implements GameServiceInterface {

	@Inject
	private GameRepoInterface gameRepo;

	@Override
	public void saveGames(List<Game> games) {
		gameRepo.save(games);
	}

	@Override
	public List<Game> getAllGames() {
		return gameRepo.getAllGames();
	}
}

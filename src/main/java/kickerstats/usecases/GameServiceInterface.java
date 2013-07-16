package kickerstats.usecases;

import java.util.List;

import kickerstats.types.Game;

public interface GameServiceInterface {
	public void saveGames(List<Game> games);

	public List<Game> getAllGames();
}

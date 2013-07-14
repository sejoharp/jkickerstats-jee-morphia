package kickerstats.usecases;

import java.util.List;

import kickerstats.types.Game;

public interface GameInterface {
	public void saveNewGames(List<Game> games);

	public List<Game> getAllGames();
}

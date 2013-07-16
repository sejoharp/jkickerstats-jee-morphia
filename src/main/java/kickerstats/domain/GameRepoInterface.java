package kickerstats.domain;

import java.util.List;

import kickerstats.types.Game;

public interface GameRepoInterface {
	public void save(Game game);

	public void save(List<Game> games);

	public List<Game> getAllGames();
}

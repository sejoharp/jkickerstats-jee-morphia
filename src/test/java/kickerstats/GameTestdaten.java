package kickerstats;

import java.util.Calendar;

import kickerstats.domain.GameFromDb;
import kickerstats.types.Game;

public class GameTestdaten {

	public static Game createSingleGame() {
		Game singleGame = new Game();
		singleGame.setDoubleMatch(false);
		singleGame.setGuestPlayer1("Matheuszik, Sven");
		singleGame.setGuestScore(7);
		singleGame.setHomePlayer1("Kränz, Ludwig");
		singleGame.setHomeScore(5);
		singleGame.setPosition(2);
		return singleGame;
	}

	public static Game createSecondSingleGame() {
		Game game = new Game();
		game.setDoubleMatch(false);
		game.setGuestPlayer1("Bai, Minyoung");
		game.setGuestScore(7);
		game.setHomePlayer1("Arslan, Mehmet Emin");
		game.setHomeScore(4);
		game.setPosition(1);
		return game;
	}

	public static Game createDoubleGame() {
		Game doubleGame = new Game();
		doubleGame.setDoubleMatch(true);
		doubleGame.setHomePlayer1("Arslan, Mehmet Emin");
		doubleGame.setHomePlayer2("Böckeler, Frank");
		doubleGame.setHomeScore(4);
		doubleGame.setGuestPlayer1("Bai, Minyoung");
		doubleGame.setGuestPlayer2("Linnenberg, Sebastian");
		doubleGame.setGuestScore(5);
		doubleGame.setPosition(16);
		return doubleGame;
	}

	public static Calendar createCalendar(int year, int month, int day,
			int hour, int min) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, min);
		return calendar;
	}

	public static Calendar createZeroCalendar() {
		Calendar matchDate = Calendar.getInstance();
		matchDate.setTimeInMillis(0);
		return matchDate;
	}

	public static GameFromDb createDoubleGameCouchDb() {
		GameFromDb gameCouchDb = new GameFromDb();
		gameCouchDb.setDoubleMatch(true);
		gameCouchDb.setGuestPlayer1("guest player1");
		gameCouchDb.setGuestPlayer2("guest player2");
		gameCouchDb.setGuestScore(10);
		gameCouchDb.setHomePlayer1("home player1");
		gameCouchDb.setHomePlayer2("home player2");
		gameCouchDb.setHomeScore(22);
		gameCouchDb.setPosition(2);
		return gameCouchDb;
	}

	public Game createDoubleGame2() {
		Game game = new Game();
		game.setDoubleMatch(true);
		game.setGuestPlayer1("guest player1");
		game.setGuestPlayer2("guest player2");
		game.setGuestScore(10);
		game.setHomePlayer1("home player1");
		game.setHomePlayer2("home player2");
		game.setHomeScore(22);
		game.setPosition(2);
		return game;
	}
}

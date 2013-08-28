package kickerstats;

import java.util.Calendar;

import kickerstats.domain.GameCouchDb;
import kickerstats.types.Game;

public class GameTestdaten {

	public static Game createSingleGame() {
		Game singleGame = new Game();
		singleGame.setDoubleMatch(false);
		singleGame.setGuestPlayer1("Matheuszik, Sven");
		singleGame.setGuestScore(7);
		singleGame.setGuestTeam("Hamburg Privateers 08");
		singleGame.setHomePlayer1("Kränz, Ludwig");
		singleGame.setHomeScore(5);
		singleGame.setHomeTeam("Tingeltangel FC St. Pauli");
		singleGame.setMatchDate(createCalendar(2013, 1, 27, 20, 0));
		singleGame.setMatchDay(1);
		singleGame.setPosition(2);
		return singleGame;
	}

	public static Game createSecondSingleGame() {
		Game game = new Game();
		game.setDoubleMatch(false);
		game.setGuestPlayer1("Bai, Minyoung");
		game.setGuestScore(7);
		game.setGuestTeam("Die Maschinerie");
		game.setHomePlayer1("Arslan, Mehmet Emin");
		game.setHomeScore(4);
		game.setHomeTeam("Cim Bom Bom");
		game.setMatchDate(createCalendar(2013, 1, 28, 20, 0));
		game.setMatchDay(1);
		game.setPosition(1);
		return game;
	}

	public static Game createDoubleGame() {
		Game doubleGame = new Game();
		doubleGame.setDoubleMatch(true);
		doubleGame.setHomePlayer1("Arslan, Mehmet Emin");
		doubleGame.setHomePlayer2("Böckeler, Frank");
		doubleGame.setHomeScore(4);
		doubleGame.setHomeTeam("Cim Bom Bom");
		doubleGame.setGuestPlayer1("Bai, Minyoung");
		doubleGame.setGuestPlayer2("Linnenberg, Sebastian");
		doubleGame.setGuestScore(5);
		doubleGame.setGuestTeam("Die Maschinerie");
		doubleGame.setMatchDay(1);
		doubleGame.setMatchDate(createCalendar(2013, 1, 28, 20, 0));
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

	public static GameCouchDb createDoubleGameCouchDb() {
		GameCouchDb gameCouchDb = new GameCouchDb();
		gameCouchDb.setDoubleMatch(true);
		gameCouchDb.setGuestPlayer1("guest player1");
		gameCouchDb.setGuestPlayer2("guest player2");
		gameCouchDb.setGuestScore(10);
		gameCouchDb.setGuestTeam("guestteam");
		gameCouchDb.setHomePlayer1("home player1");
		gameCouchDb.setHomePlayer2("home player2");
		gameCouchDb.setHomeScore(22);
		gameCouchDb.setHomeTeam("hometeam");
		gameCouchDb.setMatchDate(Calendar.getInstance());
		gameCouchDb.setMatchDay(1);
		gameCouchDb.setPosition(2);
		return gameCouchDb;
	}

	public Game createDoubleGame2() {
		Game game = new Game();
		game.setDoubleMatch(true);
		game.setGuestPlayer1("guest player1");
		game.setGuestPlayer2("guest player2");
		game.setGuestScore(10);
		game.setGuestTeam("guestteam");
		game.setHomePlayer1("home player1");
		game.setHomePlayer2("home player2");
		game.setHomeScore(22);
		game.setHomeTeam("hometeam");
		game.setMatchDate(Calendar.getInstance());
		game.setMatchDay(1);
		game.setPosition(2);
		return game;
	}
}

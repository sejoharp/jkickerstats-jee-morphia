package kickerstats.domain;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class GameFromDb {
	private String homePlayer1;
	private String homePlayer2;
	private int homeScore;
	private String guestPlayer1;
	private String guestPlayer2;
	private int guestScore;
	private int position;
	private boolean doubleMatch;

	public String getHomePlayer1() {
		return homePlayer1;
	}

	public void setHomePlayer1(String homePlayer1) {
		this.homePlayer1 = homePlayer1;
	}

	public String getHomePlayer2() {
		return homePlayer2;
	}

	public void setHomePlayer2(String homePlayer2) {
		this.homePlayer2 = homePlayer2;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	public String getGuestPlayer1() {
		return guestPlayer1;
	}

	public void setGuestPlayer1(String guestPlayer1) {
		this.guestPlayer1 = guestPlayer1;
	}

	public String getGuestPlayer2() {
		return guestPlayer2;
	}

	public void setGuestPlayer2(String guestPlayer2) {
		this.guestPlayer2 = guestPlayer2;
	}


	public int getGuestScore() {
		return guestScore;
	}

	public void setGuestScore(int guestScore) {
		this.guestScore = guestScore;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}


	public boolean isDoubleMatch() {
		return doubleMatch;
	}

	public void setDoubleMatch(boolean doubleMatch) {
		this.doubleMatch = doubleMatch;
	}
}

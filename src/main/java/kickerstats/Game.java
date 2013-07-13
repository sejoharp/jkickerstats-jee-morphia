package kickerstats;

import java.util.Calendar;

public class Game {
	private String homePlayer1;
	private String homePlayer2;
	private String homeTeam;
	private int homeScore;
	private String guestPlayer1;
	private String guestPlayer2;
	private String guestTeam;
	private int guestScore;
	private int position;
	private Calendar matchDate;
	private int matchDay;
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

	public String getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
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

	public String getGuestTeam() {
		return guestTeam;
	}

	public void setGuestTeam(String guestTeam) {
		this.guestTeam = guestTeam;
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

	public Calendar getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(Calendar matchDate) {
		this.matchDate = matchDate;
	}

	public int getMatchDay() {
		return matchDay;
	}

	public void setMatchDay(int matchDay) {
		this.matchDay = matchDay;
	}

	public boolean isDoubleMatch() {
		return doubleMatch;
	}

	public void setDoubleMatch(boolean doubleMatch) {
		this.doubleMatch = doubleMatch;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (doubleMatch ? 1231 : 1237);
		result = prime * result
				+ ((guestPlayer1 == null) ? 0 : guestPlayer1.hashCode());
		result = prime * result
				+ ((guestPlayer2 == null) ? 0 : guestPlayer2.hashCode());
		result = prime * result + guestScore;
		result = prime * result
				+ ((guestTeam == null) ? 0 : guestTeam.hashCode());
		result = prime * result
				+ ((homePlayer1 == null) ? 0 : homePlayer1.hashCode());
		result = prime * result
				+ ((homePlayer2 == null) ? 0 : homePlayer2.hashCode());
		result = prime * result + homeScore;
		result = prime * result
				+ ((homeTeam == null) ? 0 : homeTeam.hashCode());
		result = prime * result
				+ ((matchDate == null) ? 0 : matchDate.hashCode());
		result = prime * result + matchDay;
		result = prime * result + position;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println(this);
		System.out.println((Game) obj);

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (doubleMatch != other.doubleMatch)
			return false;
		if (guestPlayer1 == null) {
			if (other.guestPlayer1 != null)
				return false;
		} else if (!guestPlayer1.equals(other.guestPlayer1))
			return false;
		if (guestPlayer2 == null) {
			if (other.guestPlayer2 != null)
				return false;
		} else if (!guestPlayer2.equals(other.guestPlayer2))
			return false;
		if (guestScore != other.guestScore)
			return false;
		if (guestTeam == null) {
			if (other.guestTeam != null)
				return false;
		} else if (!guestTeam.equals(other.guestTeam))
			return false;
		if (homePlayer1 == null) {
			if (other.homePlayer1 != null)
				return false;
		} else if (!homePlayer1.equals(other.homePlayer1))
			return false;
		if (homePlayer2 == null) {
			if (other.homePlayer2 != null)
				return false;
		} else if (!homePlayer2.equals(other.homePlayer2))
			return false;
		if (homeScore != other.homeScore)
			return false;
		if (homeTeam == null) {
			if (other.homeTeam != null)
				return false;
		} else if (!homeTeam.equals(other.homeTeam))
			return false;
		if (matchDate == null) {
			if (other.matchDate != null)
				return false;
		} else if (matchDate.getTimeInMillis() != other.matchDate
				.getTimeInMillis())
			return false;
		if (matchDay != other.matchDay)
			return false;
		if (position != other.position)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Game [homePlayer1=" + homePlayer1 + ", homePlayer2="
				+ homePlayer2 + ", homeTeam=" + homeTeam + ", homeScore="
				+ homeScore + ", guestPlayer1=" + guestPlayer1
				+ ", guestPlayer2=" + guestPlayer2 + ", guestTeam=" + guestTeam
				+ ", guestScore=" + guestScore + ", position=" + position
				+ ", matchDate=" + matchDate + ", matchDay=" + matchDay
				+ ", doubleMatch=" + doubleMatch + "]";
	}
}

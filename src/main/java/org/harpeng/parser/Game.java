package org.harpeng.parser;

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
}

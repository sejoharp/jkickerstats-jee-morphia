package kickerstats.interfaces;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kickerstats.types.Game;
import kickerstats.types.Match;

public class CsvCreator {
	public void createCsvFile(List<String> csvRowList) {
		Path path = Paths.get("allGames.csv");
		try (BufferedWriter writer = Files.newBufferedWriter(path,
				StandardCharsets.UTF_8)) {
			for (String csvRow : csvRowList) {
				writer.write(csvRow);
				writer.newLine();
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public List<String> createCsvRowList(List<Match> matches) {
		List<String> csvList = new ArrayList<>();
		for (Match match : matches) {
			for (Game game : match.getGames()) {
				StringBuilder builder = new StringBuilder();

				builder.append(formatDate(match.getMatchDate()));
				builder.append(";");
				builder.append(match.getMatchDay());
				builder.append(";");
				builder.append(game.getPosition());
				builder.append(";");
				builder.append(match.getHomeTeam());
				builder.append(";");
				builder.append(replaceEmptyNames(game.getHomePlayer1()));
				builder.append(";");
				builder.append(replaceEmptyNames(game.getHomePlayer2()));
				builder.append(";");
				builder.append(game.getHomeScore());
				builder.append(";");
				builder.append(game.getGuestScore());
				builder.append(";");
				builder.append(replaceEmptyNames(game.getGuestPlayer1()));
				builder.append(";");
				builder.append(replaceEmptyNames(game.getGuestPlayer2()));
				builder.append(";");
				builder.append(match.getGuestTeam());
				csvList.add(builder.toString());
			}
		}
		return csvList;
	}

	protected String formatDate(Date matchDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(matchDate);
	}

	protected String replaceEmptyNames(String name) {
		String placeHolder = "XXXX";
		if (name == null) {
			return placeHolder;
		} else if (name.isEmpty()) {
			return placeHolder;
		} else {
			return name;
		}
	}
}

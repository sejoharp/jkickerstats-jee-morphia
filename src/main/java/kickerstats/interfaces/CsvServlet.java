package kickerstats.interfaces;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kickerstats.usecases.GameServiceInterface;

@WebServlet(urlPatterns = "/csvexport")
public class CsvServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private GameServiceInterface gameService;

	@Inject
	private CsvCreator csvCreator;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		List<String> csvList = csvCreator.createCsvRowList(gameService
				.getAllGames());

		response.setContentType("text/csv;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		for (String row : csvList) {
			writer.write(row);
			writer.write("\n");
		}
		writer.flush();
		writer.close();
	}

}

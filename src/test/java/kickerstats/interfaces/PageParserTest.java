package kickerstats.interfaces;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import kickerstats.WeldJUnit4Runner;
import kickerstats.interfaces.PageParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4Runner.class)
public class PageParserTest {
	public static final String RECOURCES_DIRECTORY = System
			.getProperty("user.dir") + "/src/test/resources/";

	@Inject
	private PageParser parser;

	@Before
	public void checkPreconditions() {
		assertThat(parser, notNullValue());
	}

	@Test
	public void returnsAllLigaLinks() throws IOException {
		File testFile = new File(RECOURCES_DIRECTORY + "uebersicht.html");
		Document doc = Jsoup.parse(testFile, "UTF-8", "");

		List<String> ligaLinksIDs = parser.findLigaLinks(doc);

		assertThat(ligaLinksIDs.size(), is(5));
		assertThat(
				ligaLinksIDs.get(0),
				is("http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=veranstaltung&veranstaltungid=8"));
	}
}

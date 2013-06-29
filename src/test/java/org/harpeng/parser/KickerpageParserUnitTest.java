package org.harpeng.parser;

import static jodd.jerry.Jerry.jerry;
import jodd.jerry.Jerry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.Before;
import org.junit.Test;

public class KickerpageParserUnitTest {

	private KickerpageParser parser;

	@Before
	public void setup() {
		parser = new KickerpageParser();
	}

	@Test
	public void theMatchIsNotConfirmed() {
		String htmlSnippet = "<tr class=\"sectiontableentry2\"><td nowrap><a name=\"id3837\"></a><a href=\"/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3837\">Do., 11.04.2013 20:30</a></td><td nowrap>St. Ellingen 1<br /><small>verantwortlich</small><small> seit 12.04.2013</small></td><td nowrap>Drehschieber FC St. Pauli				</td><td nowrap align=\"center\">60:83					</td><td nowrap align=\"center\">9:23<br /><small>unbest&auml;tigt</small>				</td></tr>";
		Jerry doc = jerry().parse(htmlSnippet);

		boolean isValid = parser.isValidMatchLink(doc);

		assertThat(isValid, equalTo(false));
	}

	@Test
	public void theMatchIsRunning() {
		String htmlSnippet = "<tr class=\"sectiontableentry2\"><td nowrap><a name=\"id3837\"></a><a href=\"/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3837\">Do., 11.04.2013 20:30</a></td><td nowrap>St. Ellingen 1<br /><small>verantwortlich</small><small> seit 12.04.2013</small></td><td nowrap>Drehschieber FC St. Pauli				</td><td nowrap align=\"center\">60:83					</td><td nowrap align=\"center\">9:23<br /><small>live</small>				</td></tr>";
		Jerry doc = jerry().parse(htmlSnippet);

		boolean isValid = parser.isValidMatchLink(doc);

		assertThat(isValid, equalTo(false));
	}

	@Test
	public void theMatchIsConfirmed() {
		String htmlSnippet = "<tr class=\"sectiontableentry2\"><td nowrap><a name=\"id3837\"></a><a href=\"/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3837\">Do., 11.04.2013 20:30</a></td><td nowrap>St. Ellingen 1<br /><small>verantwortlich</small><small> seit 12.04.2013</small></td><td nowrap>Drehschieber FC St. Pauli				</td><td nowrap align=\"center\">60:83					</td><td nowrap align=\"center\">9:23				</td></tr>";
		Jerry doc = jerry().parse(htmlSnippet);

		boolean isValid = parser.isValidMatchLink(doc);

		assertThat(isValid, equalTo(true));
	}
}

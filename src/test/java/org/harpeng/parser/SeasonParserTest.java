package org.harpeng.parser;

import static jodd.jerry.Jerry.jerry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import jodd.io.FileUtil;
import jodd.jerry.Jerry;

import org.harpeng.WeldJUnit4Runner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;

@RunWith(WeldJUnit4Runner.class)
public class SeasonParserTest {

	@Inject
	private SeasonParser parser;

	@Before
	public void checkPreconditions() {
		assertThat(parser, notNullValue());
	}

	public static final String RECOURCES_DIRECTORY = System
			.getProperty("user.dir") + "/src/test/resources/";

	@Test
	public void returnsAllSeasons() throws IOException {
		List<Integer> expectedSeasonsIDs = Lists.newArrayList(7, 4, 3, 2, 1);
		Jerry doc = jerry().parse(
				FileUtil.readString(new File(RECOURCES_DIRECTORY
						+ "uebersicht.html")));
		List<Integer> seasonsIDs = parser.findSeasonIDs(doc);
		assertThat(seasonsIDs, equalTo(expectedSeasonsIDs));
	}
}

package org.harpeng;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

;

/**
 * Unit test for simple App.
 */
public class BooleanTest {

	/**
	 * Rigourous Test :-)
	 */
	@Test
	public void should_be_compareable() {
		assertTrue(true);
		assertThat(Boolean.TRUE, equalTo(Boolean.TRUE));
		assertThat(Boolean.TRUE, is(Boolean.TRUE));
	}
}

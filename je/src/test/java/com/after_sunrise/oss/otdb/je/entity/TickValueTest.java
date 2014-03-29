package com.after_sunrise.oss.otdb.je.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * @author takanori.takase
 */
public class TickValueTest {

	private TickValue target;

	private Map<Integer, BigDecimal> decimals;

	private Map<Integer, String> strings;

	@Before
	public void setup() {

		decimals = new HashMap<Integer, BigDecimal>();

		decimals.put(1, BigDecimal.TEN);

		strings = new HashMap<Integer, String>();

		strings.put(1, "TEN");

		target = new TickValue(2, decimals, strings);

	}

	@Test
	public void testTickValue() {
		target = new TickValue(2, decimals, strings);
	}

	@Test
	public void testToString() {
		assertEquals("TickValue[sourceId=2,decimals={1=10},strings={1=TEN}]",
				target.toString());
	}

	@Test
	public void testGetSourceId() {
		assertEquals(2L, target.getSourceId());
	}

	@Test
	public void testGetDecimals() {
		assertEquals(decimals, target.getDecimals());
		assertNotSame(decimals, target.getDecimals());
	}

	@Test
	public void testGetStrings() {
		assertEquals(strings, target.getStrings());
		assertNotSame(strings, target.getStrings());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetDecimals_Unmodifiable() {
		target.getDecimals().clear();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetStrings_Unmodifiable() {
		target.getStrings().clear();
	}

}

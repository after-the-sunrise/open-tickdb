package com.after_sunrise.oss.otdb.je.binding;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.after_sunrise.oss.otdb.je.entity.TickValue;
import com.sleepycat.je.DatabaseEntry;

/**
 * @author takanori.takase
 */
public class TickValueTupleBindingTest {

	private TickValueTupleBinding target;

	private Map<Integer, BigDecimal> decimals;

	private Map<Integer, String> strings;

	@Before
	public void setUp() {

		target = new TickValueTupleBinding();

		decimals = new HashMap<>();

		strings = new HashMap<>();

	}

	@Test
	public void testTarget() {

		decimals.put(4, ONE);
		decimals.put(null, TEN);
		decimals.put(8, null);
		decimals.put(null, null);

		strings.put(4, "foo");
		strings.put(null, "bar");
		strings.put(8, null);
		strings.put(null, null);

		TickValue object = new TickValue(2, decimals, strings);

		DatabaseEntry entry = new DatabaseEntry();

		// Serialize
		target.objectToEntry(object, entry);

		// Deserialize
		TickValue result = target.entryToObject(entry);

		assertNotNull(result);
		assertEquals(2, result.getSourceId());
		assertEquals(1, result.getDecimals().size());
		assertEquals(ONE, result.getDecimals().get(4));
		assertEquals(1, result.getStrings().size());
		assertEquals("foo", result.getStrings().get(4));

	}

	@Test
	public void testTarget_EmptyValues() {

		TickValue object = new TickValue(2, decimals, strings);

		DatabaseEntry entry = new DatabaseEntry();

		// Serialize
		target.objectToEntry(object, entry);

		// Deserialize
		TickValue result = target.entryToObject(entry);

		assertNotNull(result);
		assertEquals(2L, result.getSourceId());
		assertEquals(0, result.getDecimals().size());
		assertEquals(0, result.getStrings().size());

	}

}

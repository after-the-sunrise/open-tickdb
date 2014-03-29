package com.after_sunrise.oss.otdb.je.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

/**
 * @author takanori.takase
 */
public class TickEntryTest {

	private TickEntry target;

	private TickKey key;

	private TickValue value;

	@Before
	public void setUp() {

		key = new TickKey(1, 2, 4);

		Map<Integer, BigDecimal> d = Maps.newHashMap();

		Map<Integer, String> s = Maps.newHashMap();

		value = new TickValue(8, d, s);

		target = new TickEntry(key, value);

	}

	@Test(expected = NullPointerException.class)
	public void testTickImpl_NullKey() {
		target = new TickEntry(null, value);
	}

	@Test(expected = NullPointerException.class)
	public void testTickImpl_NullValue() {
		target = new TickEntry(key, null);
	}

	@Test
	public void testHashCode() {
		assertEquals(key.hashCode(), target.hashCode());
	}

	@Test
	public void testToString() {
		assertNotNull(target.toString());
	}

	@Test
	public void testEqualsObject() {
		assertTrue(target.equals(target));
		assertTrue(target.equals(new TickEntry(key, value)));
		assertTrue(target.equals(new TickEntry(key, mock(TickValue.class))));
		assertFalse(target.equals(new TickEntry(mock(TickKey.class), value)));
		assertFalse(target.equals(new Object()));
		assertFalse(target.equals(null));
	}

	@Test
	public void testCompareTo() {

		assertEquals(0, target.compareTo(target));

		assertEquals(0, target.compareTo(new TickEntry(key, value)));

		assertEquals(0,
				target.compareTo(new TickEntry(key, mock(TickValue.class))));

		assertEquals(1,
				target.compareTo(new TickEntry(mock(TickKey.class), value)));

	}

	@Test
	public void testGetKey() {
		assertSame(key, target.getKey());
	}

	@Test
	public void testGetValue() {
		assertSame(value, target.getValue());
	}

}

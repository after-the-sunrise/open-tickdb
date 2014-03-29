package com.after_sunrise.oss.otdb.je.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

/**
 * @author takanori.takase
 */
public class TickImplTest {

	private TickImpl target;

	private String code;

	private TickEntry entry;

	private TickKey key;

	private TickValue value;

	@Before
	public void setUp() throws Exception {

		code = "foo";

		key = new TickKey(1, 2, 4);

		Map<Integer, BigDecimal> d = Maps.newHashMap();

		Map<Integer, String> s = Maps.newHashMap();

		value = new TickValue(8, d, s);

		entry = new TickEntry(key, value);

		target = new TickImpl(code, entry);

	}

	@Test
	public void testHashCode() {
		assertEquals(Objects.hash(code, entry), target.hashCode());
	}

	@Test
	public void testToString() {
		assertEquals(
				"TickImpl[code=foo,entry=TickEntry[key=TickKey[codeId=1,timestamp=2,sequence=4],"
						+ "value=TickValue[sourceId=8,decimals={},strings={}]]]",
				target.toString());
	}

	@Test
	public void testEqualsObject() {
		assertTrue(target.equals(target));
		assertTrue(target.equals(new TickImpl(code, entry)));
		assertFalse(target.equals(new TickImpl("bar", entry)));
		assertFalse(target.equals(new Object()));
		assertFalse(target.equals(null));
	}

	@Test
	public void testCompareTo() {
		assertTrue(0 == target.compareTo(target));
		assertTrue(0 == target.compareTo(new TickImpl(code, entry)));
		assertTrue(0 != target.compareTo(new TickImpl("bar", entry)));
	}

	@Test
	public void testGetCode() {
		assertEquals(code, target.getCode());
	}

	@Test
	public void testGetTimestamp() {
		assertEquals(key.getTimestamp(), target.getTimestamp());
	}

	@Test
	public void testGetSequence() {
		assertEquals(key.getSequence(), target.getSequence());
	}

	@Test
	public void testGetDecimals() {
		assertEquals(value.getDecimals(), target.getDecimals());
	}

	@Test
	public void testGetStrings() {
		assertEquals(value.getStrings(), target.getStrings());
	}

}

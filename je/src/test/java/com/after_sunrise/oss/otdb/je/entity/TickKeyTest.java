package com.after_sunrise.oss.otdb.je.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * @author takanori.takase
 */
public class TickKeyTest {

	private TickKey target;

	private long id;

	private long time;

	private long seq;

	@Before
	public void setUp() throws Exception {

		id = 1;
		time = 2;
		seq = 3;

		target = new TickKey(id, time, seq);

	}

	@Test
	public void testHashCode() {
		assertEquals(new HashCodeBuilder().append(id).append(time).append(seq)
				.toHashCode(), target.hashCode());
	}

	@Test
	public void testToString() {
		assertEquals("TickKey[codeId=1,timestamp=2,sequence=3]",
				target.toString());
	}

	@Test
	public void testEquals() {
		assertTrue(target.equals(target));
		assertTrue(target.equals(new TickKey(id, time, seq)));
		assertFalse(target.equals(new TickKey(0, time, seq)));
		assertFalse(target.equals(new TickKey(id, 0, seq)));
		assertFalse(target.equals(new TickKey(id, time, 0)));
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, target.compareTo(target));
		assertEquals(0, target.compareTo(new TickKey(id, time, seq)));
		assertEquals(1, target.compareTo(new TickKey(0, time, seq)));
		assertEquals(1, target.compareTo(new TickKey(id, 0, seq)));
		assertEquals(1, target.compareTo(new TickKey(id, time, 0)));
		assertEquals(-1, target.compareTo(new TickKey(8, time, seq)));
		assertEquals(-1, target.compareTo(new TickKey(id, 8, seq)));
		assertEquals(-1, target.compareTo(new TickKey(id, time, 8)));
	}

	@Test
	public void testGetCodeId() {
		assertEquals(id, target.getCodeId());
	}

	@Test
	public void testGetTimestamp() {
		assertEquals(time, target.getTimestamp());
	}

	@Test
	public void testGetSequence() {
		assertEquals(seq, target.getSequence());
	}

}

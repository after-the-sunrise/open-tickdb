package com.after_sunrise.oss.otdb.je.binding;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.after_sunrise.oss.otdb.je.entity.TickKey;
import com.sleepycat.je.DatabaseEntry;

/**
 * @author takanori.takase
 */
public class TickKeyTupleBindingTest {

	private TickKeyTupleBinding target;

	@Before
	public void setUp() {
		target = new TickKeyTupleBinding();
	}

	@Test
	public void testTarget() {

		TickKey object = new TickKey(1, 2, 3);

		DatabaseEntry entry = new DatabaseEntry();

		// Serialize
		target.objectToEntry(object, entry);

		// Deserialize
		TickKey result = target.entryToObject(entry);

		assertEquals(object, result);

	}

}

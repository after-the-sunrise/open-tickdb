package com.after_sunrise.oss.otdb.je.binding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.sleepycat.bind.tuple.PackedLongBinding;
import com.sleepycat.je.DatabaseEntry;

/**
 * @author takanori.takase
 */
public class InverseSecondaryKeyCreatorTest {

	private InverseSecondaryKeyCreator target;

	@Before
	public void setUp() throws Exception {
		target = new InverseSecondaryKeyCreator();
	}

	@Test
	public void testCreateSecondaryKey() {

		DatabaseEntry val = new DatabaseEntry();
		DatabaseEntry ret = new DatabaseEntry();

		PackedLongBinding valBinding = new PackedLongBinding();
		valBinding.objectToEntry(100L, val);

		assertTrue(target.createSecondaryKey(null, null, val, ret));

		assertEquals(100L, valBinding.entryToObject(ret).longValue());

	}

}

package com.after_sunrise.oss.otdb.lib.tick;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.math.BigDecimal;

import org.junit.Test;

/**
 * @author takanori.takase
 */
public class LibTickTypeTest {

	@Test
	public void testFind() {

		for (LibTickType type : LibTickType.values()) {
			assertSame(type, LibTickType.find(type.getId()));
		}

		assertNull(LibTickType.find(BigDecimal.TEN.negate()));

		assertNull(LibTickType.find(null));

	}

}

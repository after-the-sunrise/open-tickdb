package com.after_sunrise.oss.otdb.lib.tick;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.math.BigDecimal;

import org.junit.Test;

/**
 * @author takanori.takase
 */
public class LibTickTradeTypeTest {

	@Test
	public void testFind() {

		for (LibTickTradeType type : LibTickTradeType.values()) {
			assertSame(type, LibTickTradeType.find(type.getId()));
		}

		assertNull(LibTickTradeType.find(BigDecimal.TEN.negate()));

		assertNull(LibTickTradeType.find(null));

	}

}

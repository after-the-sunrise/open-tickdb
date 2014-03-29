package com.after_sunrise.oss.otdb.lib.tick;

import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_PRICE_01;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_PRICE_02;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_PRICE_03;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_PRICE_04;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_PRICE_05;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_PRICE_06;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_PRICE_07;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_PRICE_08;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_PRICE_09;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_PRICE_10;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_SIZE_01;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_SIZE_02;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_SIZE_03;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_SIZE_04;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_SIZE_05;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_SIZE_06;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_SIZE_07;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_SIZE_08;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_SIZE_09;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_SIZE_10;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_PRICE_01;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_PRICE_02;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_PRICE_03;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_PRICE_04;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_PRICE_05;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_PRICE_06;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_PRICE_07;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_PRICE_08;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_PRICE_09;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_PRICE_10;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_SIZE_01;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_SIZE_02;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_SIZE_03;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_SIZE_04;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_SIZE_05;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_SIZE_06;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_SIZE_07;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_SIZE_08;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_SIZE_09;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_SIZE_10;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * @author takanori.takase
 */
public class LibTickValueTypeTest {

	@Test
	public void testFind() {

		for (LibTickValueType type : LibTickValueType.values()) {
			assertSame(type, LibTickValueType.find(type.getId()));
		}

		assertNull(LibTickValueType.find(Integer.MIN_VALUE));

		assertNull(LibTickValueType.find(null));

	}

	@Test
	public void testGetAskPrice() {
		assertNull(LibTickValueType.getAskPrice(0));
		assertSame(ASK_PRICE_01, LibTickValueType.getAskPrice(1));
		assertSame(ASK_PRICE_02, LibTickValueType.getAskPrice(2));
		assertSame(ASK_PRICE_03, LibTickValueType.getAskPrice(3));
		assertSame(ASK_PRICE_04, LibTickValueType.getAskPrice(4));
		assertSame(ASK_PRICE_05, LibTickValueType.getAskPrice(5));
		assertSame(ASK_PRICE_06, LibTickValueType.getAskPrice(6));
		assertSame(ASK_PRICE_07, LibTickValueType.getAskPrice(7));
		assertSame(ASK_PRICE_08, LibTickValueType.getAskPrice(8));
		assertSame(ASK_PRICE_09, LibTickValueType.getAskPrice(9));
		assertSame(ASK_PRICE_10, LibTickValueType.getAskPrice(10));
		assertNull(LibTickValueType.getAskPrice(11));
	}

	@Test
	public void testGetAskPrices() {

		LibTickValueType[] types = LibTickValueType.getAskPrices();

		assertNotSame(types, LibTickValueType.getAskPrices());

		assertEquals(10, types.length);

		assertEquals(10, Sets.newHashSet(types).size());

		for (LibTickValueType type : types) {
			assertTrue(type.name().startsWith("ASK_PRICE_"));
		}

	}

	@Test
	public void testGetAskSize() {
		assertNull(LibTickValueType.getAskSize(0));
		assertSame(ASK_SIZE_01, LibTickValueType.getAskSize(1));
		assertSame(ASK_SIZE_02, LibTickValueType.getAskSize(2));
		assertSame(ASK_SIZE_03, LibTickValueType.getAskSize(3));
		assertSame(ASK_SIZE_04, LibTickValueType.getAskSize(4));
		assertSame(ASK_SIZE_05, LibTickValueType.getAskSize(5));
		assertSame(ASK_SIZE_06, LibTickValueType.getAskSize(6));
		assertSame(ASK_SIZE_07, LibTickValueType.getAskSize(7));
		assertSame(ASK_SIZE_08, LibTickValueType.getAskSize(8));
		assertSame(ASK_SIZE_09, LibTickValueType.getAskSize(9));
		assertSame(ASK_SIZE_10, LibTickValueType.getAskSize(10));
		assertNull(LibTickValueType.getAskSize(11));
	}

	@Test
	public void testGetAskSizes() {

		LibTickValueType[] types = LibTickValueType.getAskSizes();

		assertNotSame(types, LibTickValueType.getAskSizes());

		assertEquals(10, types.length);

		assertEquals(10, Sets.newHashSet(types).size());

		for (LibTickValueType type : types) {
			assertTrue(type.name().startsWith("ASK_SIZE_"));
		}

	}

	@Test
	public void testGetBidPrice() {
		assertNull(LibTickValueType.getBidPrice(0));
		assertSame(BID_PRICE_01, LibTickValueType.getBidPrice(1));
		assertSame(BID_PRICE_02, LibTickValueType.getBidPrice(2));
		assertSame(BID_PRICE_03, LibTickValueType.getBidPrice(3));
		assertSame(BID_PRICE_04, LibTickValueType.getBidPrice(4));
		assertSame(BID_PRICE_05, LibTickValueType.getBidPrice(5));
		assertSame(BID_PRICE_06, LibTickValueType.getBidPrice(6));
		assertSame(BID_PRICE_07, LibTickValueType.getBidPrice(7));
		assertSame(BID_PRICE_08, LibTickValueType.getBidPrice(8));
		assertSame(BID_PRICE_09, LibTickValueType.getBidPrice(9));
		assertSame(BID_PRICE_10, LibTickValueType.getBidPrice(10));
		assertNull(LibTickValueType.getBidPrice(11));
	}

	@Test
	public void testGetBidPrices() {

		LibTickValueType[] types = LibTickValueType.getBidPrices();

		assertNotSame(types, LibTickValueType.getBidPrices());

		assertEquals(10, types.length);

		assertEquals(10, Sets.newHashSet(types).size());

		for (LibTickValueType type : types) {
			assertTrue(type.name().startsWith("BID_PRICE_"));
		}

	}

	@Test
	public void testGetBidSize() {
		assertNull(LibTickValueType.getBidSize(0));
		assertSame(BID_SIZE_01, LibTickValueType.getBidSize(1));
		assertSame(BID_SIZE_02, LibTickValueType.getBidSize(2));
		assertSame(BID_SIZE_03, LibTickValueType.getBidSize(3));
		assertSame(BID_SIZE_04, LibTickValueType.getBidSize(4));
		assertSame(BID_SIZE_05, LibTickValueType.getBidSize(5));
		assertSame(BID_SIZE_06, LibTickValueType.getBidSize(6));
		assertSame(BID_SIZE_07, LibTickValueType.getBidSize(7));
		assertSame(BID_SIZE_08, LibTickValueType.getBidSize(8));
		assertSame(BID_SIZE_09, LibTickValueType.getBidSize(9));
		assertSame(BID_SIZE_10, LibTickValueType.getBidSize(10));
		assertNull(LibTickValueType.getBidSize(11));
	}

	@Test
	public void testGetBidSizes() {

		LibTickValueType[] types = LibTickValueType.getBidSizes();

		assertNotSame(types, LibTickValueType.getBidSizes());

		assertEquals(10, types.length);

		assertEquals(10, Sets.newHashSet(types).size());

		for (LibTickValueType type : types) {
			assertTrue(type.name().startsWith("BID_SIZE_"));
		}

	}

}

package com.after_sunrise.oss.otdb.lib.tick;

import static com.after_sunrise.commons.base.object.Conversions.map;
import static java.lang.String.format;

import java.util.Map;

import com.after_sunrise.commons.base.object.Conversions.Identifiable;

/**
 * @author takanori.takase
 */
public enum LibTickValueType implements Identifiable<Integer> {

	ASK_PRICE_01(10),

	ASK_PRICE_02(11),

	ASK_PRICE_03(12),

	ASK_PRICE_04(13),

	ASK_PRICE_05(14),

	ASK_PRICE_06(15),

	ASK_PRICE_07(16),

	ASK_PRICE_08(17),

	ASK_PRICE_09(18),

	ASK_PRICE_10(19),

	ASK_SIZE_01(20),

	ASK_SIZE_02(21),

	ASK_SIZE_03(22),

	ASK_SIZE_04(23),

	ASK_SIZE_05(24),

	ASK_SIZE_06(25),

	ASK_SIZE_07(26),

	ASK_SIZE_08(27),

	ASK_SIZE_09(28),

	ASK_SIZE_10(29),

	BID_PRICE_01(30),

	BID_PRICE_02(31),

	BID_PRICE_03(32),

	BID_PRICE_04(33),

	BID_PRICE_05(34),

	BID_PRICE_06(35),

	BID_PRICE_07(36),

	BID_PRICE_08(37),

	BID_PRICE_09(38),

	BID_PRICE_10(39),

	BID_SIZE_01(40),

	BID_SIZE_02(41),

	BID_SIZE_03(42),

	BID_SIZE_04(43),

	BID_SIZE_05(44),

	BID_SIZE_06(45),

	BID_SIZE_07(46),

	BID_SIZE_08(47),

	BID_SIZE_09(48),

	BID_SIZE_10(49),

	LAST_PRICE(50),

	LAST_SIZE(60),

	ACCUMULATED_VOLUME(70),

	VWAP(72),

	TICK_TYPE(74),

	TRADE_TYPE(76);

	private final Integer id;

	private LibTickValueType(int id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	private static final Map<Integer, LibTickValueType> VALUES = map(values());

	public static LibTickValueType find(Integer value) {
		return VALUES.get(value);
	}

	private static final int LEVELS = 10;

	private static final LibTickValueType[] ASK_PRICES = new LibTickValueType[LEVELS];

	private static final LibTickValueType[] BID_PRICES = new LibTickValueType[LEVELS];

	private static final LibTickValueType[] ASK_SIZES = new LibTickValueType[LEVELS];

	private static final LibTickValueType[] BID_SIZES = new LibTickValueType[LEVELS];

	static {

		for (int i = 0; i < LEVELS; i++) {

			String num = format("%02d", i + 1);

			ASK_PRICES[i] = valueOf("ASK_PRICE_" + num);

			ASK_SIZES[i] = valueOf("ASK_SIZE_" + num);

			BID_PRICES[i] = valueOf("BID_PRICE_" + num);

			BID_SIZES[i] = valueOf("BID_SIZE_" + num);

		}

	}

	private static <T> T findInArray(T[] types, int level) {
		return (level <= 0 || types.length < level) ? null : types[level - 1];
	}

	public static LibTickValueType getAskPrice(int level) {
		return findInArray(ASK_PRICES, level);
	}

	public static LibTickValueType[] getAskPrices() {
		return ASK_PRICES.clone();
	}

	public static LibTickValueType getAskSize(int level) {
		return findInArray(ASK_SIZES, level);
	}

	public static LibTickValueType[] getAskSizes() {
		return ASK_SIZES.clone();
	}

	public static LibTickValueType getBidPrice(int level) {
		return findInArray(BID_PRICES, level);
	}

	public static LibTickValueType[] getBidPrices() {
		return BID_PRICES.clone();
	}

	public static LibTickValueType getBidSize(int level) {
		return findInArray(BID_SIZES, level);
	}

	public static LibTickValueType[] getBidSizes() {
		return BID_SIZES.clone();
	}

}

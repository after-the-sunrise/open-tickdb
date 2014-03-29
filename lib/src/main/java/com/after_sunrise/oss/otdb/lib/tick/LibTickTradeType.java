package com.after_sunrise.oss.otdb.lib.tick;

import static com.after_sunrise.commons.base.object.Conversions.map;

import java.math.BigDecimal;
import java.util.Map;

import com.after_sunrise.commons.base.object.Conversions.Identifiable;

/**
 * @author takanori.takase
 */
public enum LibTickTradeType implements Identifiable<BigDecimal> {

	OFF_EXCHANGE(0),

	SESSION_DAY(1),

	SESSION_EVENING(2),

	SESSION_MORNING(3),

	SESSION_AFTERNOON(4);

	private final BigDecimal id;

	private LibTickTradeType(int id) {
		this.id = BigDecimal.valueOf(id);
	}

	public BigDecimal getId() {
		return id;
	}

	private static final Map<BigDecimal, LibTickTradeType> VALUES = map(values());

	public static LibTickTradeType find(BigDecimal value) {
		return VALUES.get(value);
	}

}

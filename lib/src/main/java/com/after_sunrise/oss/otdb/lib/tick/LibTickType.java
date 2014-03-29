package com.after_sunrise.oss.otdb.lib.tick;

import static com.after_sunrise.commons.base.object.Conversions.map;

import java.math.BigDecimal;
import java.util.Map;

import com.after_sunrise.commons.base.object.Conversions.Identifiable;

/**
 * @author takanori.takase
 */
public enum LibTickType implements Identifiable<BigDecimal> {

	TRADE(1),

	AUCTION(2),

	OPEN(3),

	CLOSE(4),

	HIGH(5),

	LOW(6),

	QUOTE(7),

	CORRECTION(8);

	private final BigDecimal id;

	private LibTickType(int id) {
		this.id = BigDecimal.valueOf(id);
	}

	public BigDecimal getId() {
		return id;
	}

	private static final Map<BigDecimal, LibTickType> VALUES = map(values());

	public static LibTickType find(BigDecimal value) {
		return VALUES.get(value);
	}

}

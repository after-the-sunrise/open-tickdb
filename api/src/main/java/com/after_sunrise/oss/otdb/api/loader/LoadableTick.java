package com.after_sunrise.oss.otdb.api.loader;

import java.math.BigDecimal;
import java.util.Map;

import com.after_sunrise.oss.otdb.api.tick.Tick;

/**
 * @author takanori.takase
 */
public interface LoadableTick {

	/**
	 * @return Instrument code
	 * @see Tick#getCode()
	 */
	String getCode();

	/**
	 * @return Time
	 * @see Tick#getTimestamp()
	 */
	Long getTimestamp();

	/**
	 * @return Property map
	 * @see Tick#getDecimals()
	 */
	Map<Integer, BigDecimal> getDecimals();

	/**
	 * @return Property map
	 * @see Tick#getStrings()
	 */
	Map<Integer, String> getStrings();

}

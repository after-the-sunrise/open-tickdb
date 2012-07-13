package jp.gr.java_conf.afterthesunrise.open.tickdb.loader;

import java.math.BigDecimal;
import java.util.Map;

import jp.gr.java_conf.afterthesunrise.open.tickdb.tick.Tick;

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
	 * @see Tick#getValues()
	 */
	Map<Integer, BigDecimal> getValues();

}

package jp.gr.java_conf.afterthesunrise.open.tickdb.tick;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author takanori.takase
 */
public interface Tick extends Comparable<Tick>, Serializable {

	/**
	 * <p>
	 * A code to uniquely identify the instrument of this tick. If the code
	 * consists of multiple properties, concatenating them with separator (the
	 * 'old fashion way') will construct a unique code. (example :
	 * "ISIN|MIC|CCY")
	 * </p>
	 * 
	 * @return Instrument code
	 */
	String getCode();

	/**
	 * <p>
	 * Time stamp of this tick.
	 * </p>
	 * 
	 * <p>
	 * Although Java uses milliseconds for default time unit, underlying
	 * implementation may store smaller granularity of time by accepting the
	 * maximum time limitation. (Unsigned long may be useful too.)
	 * </p>
	 * 
	 * <p>
	 * <table border="1">
	 * <tr>
	 * <th>Type</th>
	 * <th>Value</th>
	 * <th>Digits</th>
	 * </tr>
	 * <tr>
	 * <td align="right">Long.MAX_VALUE</td>
	 * <td>9223372036854775807</td>
	 * <td>19</td>
	 * </tr>
	 * <tr>
	 * <td align="right">294247-01-10 04:00:54:774 GMT</td>
	 * <td>9223372036854774</td>
	 * <td>16</td>
	 * </tr>
	 * <tr>
	 * <td align="right">2262-04-11 23:47:16:853 GMT</td>
	 * <td>9223372036853</td>
	 * <tD>13</td>
	 * </tr>
	 * </table>
	 * </p>
	 */
	long getTimestamp();

	/**
	 * <p>
	 * A sequence number used for uniquely identifying a tick.
	 * </p>
	 * <p>
	 * This sequence number is only guaranteed to be unique among the ticks with
	 * same code and time stamp, and is NOT guaranteed to unique in an
	 * environment. Therefore, this sequence cannot be used alone for
	 * identifying a single tick. Underlying implementations are allowed to
	 * reuse a same sequence id for ticks with different symbols and/or time
	 * stamps.
	 * </p>
	 * <p>
	 * Sorting must not depend on this sequence number as it will result its
	 * sorting order is undefined. It's preferred to use the
	 * {@code Tick#getCode()}, {@code Tick#getTimestamp()} and values in
	 * {@code Tick#getValues()} to have a meaningful sort order.
	 * </p>
	 * 
	 * @return Sequence number
	 */
	long getSequence();

	/**
	 * Retrieves map containing the properties of this tick.
	 * 
	 * @return Property map
	 */
	Map<Integer, BigDecimal> getValues();

}

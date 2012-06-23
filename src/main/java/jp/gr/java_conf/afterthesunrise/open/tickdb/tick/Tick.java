package jp.gr.java_conf.afterthesunrise.open.tickdb.tick;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author takanori.takase
 */
public interface Tick extends Comparable<Tick>, Serializable {

	String getCode();

	/**
	 * <p>
	 * Although Java uses milliseconds for default time scale, underlying
	 * implementation may store smaller granularity of time by accepting the
	 * maximum time limitation. (Unsigned long may be useful too.)
	 * </p>
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

	long getSequence();

	Map<Integer, BigDecimal> getValues();

}

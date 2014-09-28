package com.after_sunrise.oss.otdb.je.binding;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

/**
 * <p>
 * Utility to bind BigDecimal value to Tuple in/out with byte array size
 * optimization.
 * </p>
 * <p>
 * We assume that a typical tick value are <i>'relatively small'</i> positive
 * values with less than 15 digit of decimal scale, and its unscaled value is
 * less than {@code Long.MAX_VALUE} (=9223372036854775807). The default
 * implementation of the {@link TupleOutput} uses 3 bytes to store a
 * <i>'relatively small'</i> {@link BigDecimal} value at minimum, which is not
 * so size efficient for storing values such as 0, 1 or 1.1. This utility aims
 * to store these <i>'relatively small'</i> positive values in more size
 * efficient manner. If the value happens to fall outside of the <i>'relatively
 * small'</i> positive range, the implementation will be delegated to the
 * default {@link TupleOutput}'s implementation, with an additional cost of one
 * byte.
 * </p>
 * <p>
 * In this implementation, the first byte of the data defines how the following
 * data are written and should be read.
 * </p>
 * <ul>
 * <li>If the first byte is equal to -128 (b == -128), {@link TupleOutput}'s
 * default implementation is used.</li>
 * <li>If the first byte is greater than -8 and no greater than 128 (-8 &lt; b &lt;=
 * 128), the first byte itself represents the whole data.</li>
 * <li>Otherwise, the first byte indicates a combination of the scale and
 * unscaled value's byte length. Bytes following the first bytes represent the
 * unscaled value.</li>
 * </ul>
 * <table border="1">
 * <caption>First Byte Combination</caption>
 * <tr>
 * <th>Fist byte</th>
 * <th>Scale</th>
 * <th>Byte Length</th>
 * <th>Example Value</th>
 * </tr>
 * <tr>
 * <td>-8</td>
 * <td>0</td>
 * <td>1</td>
 * <td>-8, -100, -128</td>
 * </tr>
 * <tr>
 * <td>-9</td>
 * <td>1</td>
 * <td>1</td>
 * <td>0.1, -1.2</td>
 * </tr>
 * <tr>
 * <td>...</td>
 * <td>...</td>
 * <td>...</td>
 * <td>...</td>
 * </tr>
 * <tr>
 * <td>-23</td>
 * <td>0</td>
 * <td>2</td>
 * <td>128, 32767</td>
 * </tr>
 * <tr>
 * <td>-24</td>
 * <td>1</td>
 * <td>2</td>
 * <td>12.8, 3276.7</td>
 * </tr>
 * <tr>
 * <td>...</td>
 * <td>...</td>
 * <td>...</td>
 * <td>...</td>
 * </tr>
 * <tr>
 * <td>-126</td>
 * <td>13</td>
 * <td>8</td>
 * <td>922337.2036854775807</td>
 * </tr>
 * <tr>
 * <td>-127</td>
 * <td>14</td>
 * <td>8</td>
 * <td>92233.72036854775807</td>
 * </tr>
 * </table>
 * 
 * @author takanori.takase
 */
public final class TupleBindingUtils {

	private static final byte SCALE = 15;

	private static final byte LONG_BITS = 8;

	private static final byte MIN = Byte.MIN_VALUE + SCALE * LONG_BITS;

	private static final BigInteger MAX = BigInteger.valueOf(Long.MAX_VALUE);

	/**
	 * Write BigDecimal value to TupleOutput with size optimization.
	 * 
	 * @param out
	 *            Output to write value to.
	 * @param value
	 *            Value to write to output.
	 */
	public static void write(TupleOutput out, BigDecimal value) {

		int scale = value.scale();

		if (scale >= SCALE) {

			out.writeByte(Byte.MIN_VALUE);

			out.writeBigDecimal(value);

			return;

		}

		BigInteger bigInt = value.unscaledValue();

		if (bigInt.abs().compareTo(MAX) > 0) {

			out.writeByte(Byte.MIN_VALUE);

			out.writeBigDecimal(value);

			return;

		}

		long unscaled = bigInt.longValue();

		if (scale == 0 && MIN < unscaled && unscaled <= Byte.MAX_VALUE) {

			out.writeByte((byte) unscaled);

			return;

		}

		byte[] bytes = bigInt.toByteArray();

		int first = MIN - (bytes.length - 1) * SCALE - scale;

		out.writeByte(first);

		out.write(bytes);

	}

	/**
	 * Read BigDecimal value from TupleInput with size optimization.
	 * 
	 * @param in
	 *            Input to read value from.
	 * @return Value read from input.
	 */
	public static BigDecimal read(TupleInput in) {

		byte first = in.readByte();

		if (MIN < first) {
			return BigDecimal.valueOf(first);
		}

		if (first == Byte.MIN_VALUE) {
			return in.readBigDecimal();
		}

		int scale = (MIN - first) % SCALE;

		int length = (MIN - first) / SCALE + 1;

		byte[] bytes = new byte[length];

		int read = in.read(bytes);

		if (read != length) {
			throw new RuntimeException("Invalid length : " + read);
		}

		BigInteger unscaled = new BigInteger(bytes);

		return new BigDecimal(unscaled, scale);

	}

}

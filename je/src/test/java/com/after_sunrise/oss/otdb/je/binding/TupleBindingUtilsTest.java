package com.after_sunrise.oss.otdb.je.binding;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

/**
 * @author takanori.takase
 */
public class TupleBindingUtilsTest {

	@Test
	public void testLargeScale() {

		// scale 16
		BigDecimal value = new BigDecimal("+0.1234567890123456");

		// Write
		TupleOutput out = new TupleOutput();
		TupleBindingUtils.write(out, value);
		byte[] bytes = out.toByteArray();

		// Read
		TupleInput in = new TupleInput(bytes);
		BigDecimal result = TupleBindingUtils.read(in);

		assertEquals(value, result);
		assertEquals(10, bytes.length);

	}

	@Test
	public void testLargeValue() {

		// Long.MAX_VALUE (9223372036854775807) + 1L
		BigDecimal value = new BigDecimal("9223372036854775808");

		// Write
		TupleOutput out = new TupleOutput();
		TupleBindingUtils.write(out, value);
		byte[] bytes = out.toByteArray();

		// Read
		TupleInput in = new TupleInput(bytes);
		BigDecimal result = TupleBindingUtils.read(in);

		assertEquals(value, result);
		assertEquals(12, bytes.length);

	}

	@Test
	public void testLargeValue2() {

		// Long.MIN_VALUE == Long.MAX_VALUE + 1 * -1
		BigDecimal value = new BigDecimal(Long.MIN_VALUE);

		// Write
		TupleOutput out = new TupleOutput();
		TupleBindingUtils.write(out, value);
		byte[] bytes = out.toByteArray();

		// Read
		TupleInput in = new TupleInput(bytes);
		BigDecimal result = TupleBindingUtils.read(in);

		assertEquals(value, result);
		assertEquals(11, bytes.length);

	}

	@Test
	public void testLargeScaledValue() {

		// Long.MAX_VALUE (9223372036854775807) + 1L with scale 16
		BigDecimal value = new BigDecimal(
				"9223372036854775808.1234567890123456");

		// Write
		TupleOutput out = new TupleOutput();
		TupleBindingUtils.write(out, value);
		byte[] bytes = out.toByteArray();

		// Read
		TupleInput in = new TupleInput(bytes);
		BigDecimal result = TupleBindingUtils.read(in);

		assertEquals(value, result);
		assertEquals(18, bytes.length);

	}

	@Test
	public void testSingleByte() {

		List<BigDecimal> list = new ArrayList<>();
		list.add(new BigDecimal(Byte.MAX_VALUE));
		list.add(new BigDecimal("1"));
		list.add(new BigDecimal("0"));
		list.add(new BigDecimal("-1"));
		list.add(new BigDecimal("-7"));

		for (BigDecimal value : list) {

			// Write
			TupleOutput out = new TupleOutput();
			TupleBindingUtils.write(out, value);
			byte[] bytes = out.toByteArray();

			// Read
			TupleInput in = new TupleInput(bytes);
			BigDecimal result = TupleBindingUtils.read(in);

			assertEquals(value, result);
			assertEquals(value.toPlainString(), 1, bytes.length);

		}

	}

	@Test
	public void testDoubleByte() {

		List<BigDecimal> list = new ArrayList<>();
		list.add(new BigDecimal("-8"));
		list.add(new BigDecimal(Byte.MIN_VALUE));
		list.add(new BigDecimal("0.0"));
		list.add(new BigDecimal("+1.1"));
		list.add(new BigDecimal("-1.2"));
		list.add(new BigDecimal("+1.3"));
		list.add(new BigDecimal("-7.4"));
		list.add(new BigDecimal("1.27"));
		list.add(new BigDecimal("12.7"));
		list.add(new BigDecimal("0.0000127"));

		for (BigDecimal value : list) {

			// Write
			TupleOutput out = new TupleOutput();
			TupleBindingUtils.write(out, value);
			byte[] bytes = out.toByteArray();

			// Read
			TupleInput in = new TupleInput(bytes);
			BigDecimal result = TupleBindingUtils.read(in);

			assertEquals(value, result);
			assertEquals(value.toPlainString(), 2, bytes.length);

		}

	}

	@Test
	public void testTripleByte() {

		List<BigDecimal> list = new ArrayList<>();
		list.add(new BigDecimal("-129"));
		list.add(new BigDecimal("+128"));
		list.add(new BigDecimal(Short.MIN_VALUE));
		list.add(new BigDecimal(Short.MAX_VALUE));

		for (BigDecimal value : list) {

			// Write
			TupleOutput out = new TupleOutput();
			TupleBindingUtils.write(out, value);
			byte[] bytes = out.toByteArray();

			// Read
			TupleInput in = new TupleInput(bytes);
			BigDecimal result = TupleBindingUtils.read(in);

			assertEquals(value, result);
			assertEquals(value.toPlainString(), 3, bytes.length);

		}

	}

	@Test
	public void testNumbers() {

		List<BigDecimal> list = new ArrayList<>();

		BigDecimal ones = BigDecimal.ONE;

		for (int i = 1; i <= 20; i++) {

			ones = ones.movePointLeft(1).add(BigDecimal.ONE);

			// 1.111111...
			list.add(ones);

			// 0.00...01
			list.add(BigDecimal.ONE.movePointLeft(i));

			// 1.00...01
			list.add(BigDecimal.ONE.movePointLeft(i).add(BigDecimal.ONE));

		}

		for (BigDecimal value : list) {

			// Write
			TupleOutput out = new TupleOutput();
			TupleBindingUtils.write(out, value);
			byte[] bytes = out.toByteArray();

			// Read
			TupleInput in = new TupleInput(bytes);
			BigDecimal result = TupleBindingUtils.read(in);

			assertEquals(value, result);

			// System.out.println(value.toPlainString() + " : " + bytes.length);

		}

	}

}
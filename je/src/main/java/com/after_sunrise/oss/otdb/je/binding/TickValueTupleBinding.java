package com.after_sunrise.oss.otdb.je.binding;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.after_sunrise.oss.otdb.je.entity.TickValue;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

/**
 * @author takanori.takase
 */
@Component
public class TickValueTupleBinding extends TupleBinding<TickValue> implements
		Predicate<Entry<Integer, ?>> {

	@Override
	public TickValue entryToObject(TupleInput input) {

		long sourceId = input.readPackedLong();

		int d = input.readPackedInt();

		Map<Integer, BigDecimal> decimals = new HashMap<>();

		for (int i = 0; i < d; i++) {

			int key = input.readPackedInt();

			BigDecimal value = TupleBindingUtils.read(input);

			decimals.put(key, value);

		}

		int s = input.readPackedInt();

		Map<Integer, String> strings = new HashMap<>();

		for (int i = 0; i < s; i++) {

			int key = input.readPackedInt();

			String value = input.readString();

			strings.put(key, value);

		}

		return new TickValue(sourceId, decimals, strings);

	}

	@Override
	public void objectToEntry(TickValue object, TupleOutput output) {

		output.writePackedLong(object.getSourceId());

		Map<Integer, BigDecimal> decimals = object.getDecimals();

		decimals = Maps.filterEntries(decimals, this);

		output.writePackedInt(decimals.size());

		for (Entry<Integer, BigDecimal> entry : decimals.entrySet()) {

			output.writePackedInt(entry.getKey());

			TupleBindingUtils.write(output, entry.getValue());

		}

		Map<Integer, String> strings = object.getStrings();

		strings = Maps.filterEntries(strings, this);

		output.writePackedInt(strings.size());

		for (Entry<Integer, String> entry : strings.entrySet()) {

			output.writePackedInt(entry.getKey());

			output.writeString(entry.getValue());

		}

	}

	@Override
	public boolean apply(Entry<Integer, ?> input) {
		return input.getKey() != null && input.getValue() != null;
	}

}

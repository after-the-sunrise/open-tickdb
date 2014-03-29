package com.after_sunrise.oss.otdb.je.binding;

import com.after_sunrise.oss.otdb.je.entity.TickSource;
import com.sleepycat.bind.tuple.PackedLongBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

/**
 * @author takanori.takase
 */
public class TickSourceBinding extends TupleBinding<TickSource> implements
		SecondaryKeyCreator {

	private final PackedLongBinding idBinding = new PackedLongBinding();

	@Override
	public boolean createSecondaryKey(SecondaryDatabase secondary,
			DatabaseEntry key, DatabaseEntry data, DatabaseEntry result) {

		TickSource source = entryToObject(data);

		idBinding.objectToEntry(source.getId(), result);

		return true;

	}

	@Override
	public TickSource entryToObject(TupleInput input) {

		TickSource source = new TickSource(input.readPackedLong());

		if (input.readBoolean()) {
			source.setStartTime(input.readPackedLong());
		}

		if (input.readBoolean()) {
			source.setEndTime(input.readPackedLong());
		}

		if (input.readBoolean()) {
			source.setCount(input.readPackedLong());
		}

		if (input.readBoolean()) {
			source.setDeleted(input.readBoolean());
		}

		return source;

	}

	@Override
	public void objectToEntry(TickSource object, TupleOutput output) {

		output.writePackedLong(object.getId());

		write(object.getStartTime(), output);

		write(object.getEndTime(), output);

		write(object.getCount(), output);

		write(object.getDeleted(), output);

	}

	private void write(Long value, TupleOutput output) {

		if (value == null) {

			output.writeBoolean(false);

		} else {

			output.writeBoolean(true);

			output.writePackedLong(value);

		}

	}

	private void write(Boolean value, TupleOutput output) {

		if (value == null) {

			output.writeBoolean(false);

		} else {

			output.writeBoolean(true);

			output.writeBoolean(value);

		}

	}

}

package com.after_sunrise.oss.otdb.je.binding;

import org.springframework.stereotype.Component;

import com.after_sunrise.oss.otdb.je.entity.TickKey;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

/**
 * @author takanori.takase
 */
@Component
public class TickKeyTupleBinding extends TupleBinding<TickKey> {

	@Override
	public TickKey entryToObject(TupleInput input) {
		long codeId = input.readSortedPackedLong();
		long timestamp = input.readSortedPackedLong();
		long sequence = input.readSortedPackedLong();
		return new TickKey(codeId, timestamp, sequence);
	}

	@Override
	public void objectToEntry(TickKey object, TupleOutput output) {
		output.writeSortedPackedLong(object.getCodeId());
		output.writeSortedPackedLong(object.getTimestamp());
		output.writeSortedPackedLong(object.getSequence());
	}

}

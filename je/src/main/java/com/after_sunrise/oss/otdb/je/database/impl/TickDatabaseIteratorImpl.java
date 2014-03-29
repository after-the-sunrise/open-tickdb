package com.after_sunrise.oss.otdb.je.database.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.sleepycat.je.OperationStatus.SUCCESS;

import java.io.IOException;

import com.after_sunrise.oss.otdb.je.binding.TickKeyTupleBinding;
import com.after_sunrise.oss.otdb.je.binding.TickValueTupleBinding;
import com.after_sunrise.oss.otdb.je.database.TickDatabaseIterator;
import com.after_sunrise.oss.otdb.je.entity.TickEntry;
import com.after_sunrise.oss.otdb.je.entity.TickKey;
import com.after_sunrise.oss.otdb.je.entity.TickValue;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;

/**
 * @author takanori.takase
 */
public class TickDatabaseIteratorImpl implements TickDatabaseIterator {

	private final DatabaseEntry key = new DatabaseEntry();

	private final DatabaseEntry val = new DatabaseEntry();

	private final Cursor cursor;

	private final LockMode lockMode;

	private final TickKeyTupleBinding keyBinding;

	private final TickValueTupleBinding valBinding;

	private final TickKey start;

	private final TickKey end;

	public TickDatabaseIteratorImpl(Cursor cursor, LockMode lockMode,
			TickKeyTupleBinding keyBinding, TickValueTupleBinding valBinding,
			long id, long start, long end) {

		this.cursor = checkNotNull(cursor);
		this.lockMode = checkNotNull(lockMode);
		this.keyBinding = checkNotNull(keyBinding);
		this.valBinding = checkNotNull(valBinding);
		this.start = new TickKey(id, start, Long.MIN_VALUE);
		this.end = new TickKey(id, end, Long.MAX_VALUE);

	}

	@Override
	public void close() throws IOException {
		cursor.close();
	}

	@Override
	public TickEntry first() throws IOException {
		return next(start.getTimestamp());
	}

	@Override
	public TickEntry previous() throws IOException {

		if (cursor.getPrev(key, val, lockMode) != SUCCESS) {
			return null;
		}

		TickKey k = keyBinding.entryToObject(key);

		if (k.compareTo(start) < 0) {
			return null;
		}

		if (k.compareTo(end) > 0) {
			return previous();
		}

		TickValue v = valBinding.entryToObject(val);

		return new TickEntry(k, v);

	}

	@Override
	public TickEntry previous(long timestamp) throws IOException {

		long adjusted = Math.min(timestamp, end.getTimestamp());

		TickKey temp = new TickKey(end.getCodeId(), adjusted, Long.MAX_VALUE);

		keyBinding.objectToEntry(temp, key);

		// Returns equal or greater
		if (cursor.getSearchKeyRange(key, val, lockMode) != SUCCESS) {

			// If not found, cursor does not move, so force move here.
			cursor.getLast(key, val, lockMode);

		}

		TickKey k = keyBinding.entryToObject(key);

		if (k.compareTo(start) < 0) {
			return null;
		}

		if (k.compareTo(temp) > 0) {
			return previous();
		}

		TickValue v = valBinding.entryToObject(val);

		return new TickEntry(k, v);

	}

	@Override
	public TickEntry next() throws IOException {

		if (cursor.getNext(key, val, lockMode) != SUCCESS) {
			return null;
		}

		TickKey k = keyBinding.entryToObject(key);

		if (k.compareTo(start) < 0) {
			return next();
		}

		if (k.compareTo(end) > 0) {
			return null;
		}

		TickValue v = valBinding.entryToObject(val);

		return new TickEntry(k, v);

	}

	@Override
	public TickEntry next(long timestamp) throws IOException {

		long adjusted = Math.max(timestamp, start.getTimestamp());

		TickKey temp = new TickKey(start.getCodeId(), adjusted, Long.MIN_VALUE);

		keyBinding.objectToEntry(temp, key);

		// Returns equal or greater
		if (cursor.getSearchKeyRange(key, val, lockMode) != SUCCESS) {
			return null;
		}

		TickKey k = keyBinding.entryToObject(key);

		if (k.compareTo(temp) < 0) {
			return next();
		}

		if (k.compareTo(end) > 0) {
			return null;
		}

		TickValue v = valBinding.entryToObject(val);

		return new TickEntry(k, v);

	}

	@Override
	public TickEntry last() throws IOException {
		return previous(end.getTimestamp());
	}

}

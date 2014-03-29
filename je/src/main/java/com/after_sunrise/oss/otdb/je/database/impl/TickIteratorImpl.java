package com.after_sunrise.oss.otdb.je.database.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import com.after_sunrise.oss.otdb.api.service.TickIterator;
import com.after_sunrise.oss.otdb.api.tick.Tick;
import com.after_sunrise.oss.otdb.je.database.TickDatabaseIterator;
import com.after_sunrise.oss.otdb.je.entity.TickEntry;
import com.after_sunrise.oss.otdb.je.entity.TickImpl;

/**
 * @author takanori.takase
 */
public class TickIteratorImpl implements TickIterator {

	private final String code;

	private final TickDatabaseIterator delegate;

	public TickIteratorImpl(String code, TickDatabaseIterator delegate) {
		this.code = checkNotNull(code);
		this.delegate = checkNotNull(delegate);
	}

	private Tick convert(TickEntry entry) {
		return entry == null ? null : new TickImpl(code, entry);
	}

	@Override
	public Tick first() throws IOException {
		return convert(delegate.first());
	}

	@Override
	public Tick previous() throws IOException {
		return convert(delegate.previous());
	}

	@Override
	public Tick previous(long timestamp) throws IOException {
		return convert(delegate.previous(timestamp));
	}

	@Override
	public Tick next() throws IOException {
		return convert(delegate.next());
	}

	@Override
	public Tick next(long timestamp) throws IOException {
		return convert(delegate.next(timestamp));
	}

	@Override
	public Tick last() throws IOException {
		return convert(delegate.last());
	}

	@Override
	public void close() throws IOException {
		delegate.close();
	}

}

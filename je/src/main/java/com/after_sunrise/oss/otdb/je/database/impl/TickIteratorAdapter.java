package com.after_sunrise.oss.otdb.je.database.impl;

import java.io.IOException;

import com.after_sunrise.oss.otdb.api.service.TickIterator;
import com.after_sunrise.oss.otdb.api.tick.Tick;

/**
 * @author takanori.takase
 */
public class TickIteratorAdapter implements TickIterator {

	private static final TickIteratorAdapter INSTANCE = new TickIteratorAdapter();

	public static final TickIterator getInstance() {
		return INSTANCE;
	}

	protected TickIteratorAdapter() {
	}

	@Override
	public Tick first() throws IOException {
		return null;
	}

	@Override
	public Tick previous() throws IOException {
		return null;
	}

	@Override
	public Tick previous(long timestamp) throws IOException {
		return null;
	}

	@Override
	public Tick next() throws IOException {
		return null;
	}

	@Override
	public Tick next(long timestamp) throws IOException {
		return null;
	}

	@Override
	public Tick last() throws IOException {
		return null;
	}

	@Override
	public void close() throws IOException {
	}

}

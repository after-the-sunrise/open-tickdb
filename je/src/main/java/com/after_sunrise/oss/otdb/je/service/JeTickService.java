package com.after_sunrise.oss.otdb.je.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.after_sunrise.oss.otdb.api.service.TickIterator;
import com.after_sunrise.oss.otdb.api.service.TickService;
import com.after_sunrise.oss.otdb.api.tick.Tick;
import com.google.common.annotations.VisibleForTesting;

/**
 * @author takanori.takase
 */
public class JeTickService implements TickService {

	private static final String DEFAULT = "open-tickdb-context-read.xml";

	private final Lock lock = new ReentrantLock();

	private volatile String config = DEFAULT;

	private volatile ConfigurableApplicationContext context;

	private volatile TickService delegate;

	public void setConfig(String config) {
		try {

			lock.lock();

			_setConfig(config);

		} finally {
			lock.unlock();
		}
	}

	private void _setConfig(String config) {

		if (getClass().getClassLoader().getResource(config) == null) {
			throw new IllegalArgumentException("Invalid config : " + config);
		}

		if (delegate != null) {
			throw new IllegalStateException("Tick service already initialized.");
		}

		this.config = config;

	}

	@Override
	public void initialize() throws IOException {
		try {

			lock.lock();

			_initialize();

		} catch (IOException e) {

			IOUtils.closeQuietly(this);

			throw e;

		} finally {
			lock.unlock();
		}
	}

	private void _initialize() throws IOException {

		if (delegate != null) {
			throw new IOException("Tick service already initialized.");
		}

		context = new ClassPathXmlApplicationContext(config);

		delegate = context.getBean(JeTickServiceDelegate.class);

		delegate.initialize();

	}

	@Override
	public void close() throws IOException {
		try {

			lock.lock();

			_close();

		} finally {
			lock.unlock();
		}
	}

	private void _close() throws IOException {

		if (delegate == null) {
			throw new IOException("Tick service already closed.");
		}

		IOUtils.closeQuietly(delegate);

		delegate = null;

		context.close();

	}

	@VisibleForTesting
	TickService getDelegate() throws IOException {

		TickService reference;

		try {

			lock.lock();

			reference = delegate;

		} finally {
			lock.unlock();
		}

		if (reference == null) {
			throw new IOException("Service not initialized.");
		}

		return reference;

	}

	@Override
	public List<Tick> find(String code, Long start, Long end)
			throws IOException {
		return getDelegate().find(code, start, end);
	}

	@Override
	public List<Tick> find(String code, Long start, Long end,
			Set<Integer> fields) throws IOException {
		return getDelegate().find(code, start, end, fields);
	}

	@Override
	public TickIterator iterate(String code, Long start, Long end)
			throws IOException {
		return getDelegate().iterate(code, start, end);
	}

	@Override
	public TickIterator iterate(String code, Long start, Long end,
			Set<Integer> fields) throws IOException {
		return getDelegate().iterate(code, start, end, fields);
	}

}

package com.after_sunrise.oss.otdb.je.loader;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.after_sunrise.oss.otdb.api.loader.LoadableTickIterator;
import com.after_sunrise.oss.otdb.api.loader.LoadableTickLoader;
import com.google.common.annotations.VisibleForTesting;

/**
 * @author takanori.takase
 */
public class JeTickLoader implements LoadableTickLoader {

	private static final String DEFAULT = "open-tickdb-context-deferred.xml";

	private final Lock lock = new ReentrantLock();

	private volatile String config = DEFAULT;

	private volatile ConfigurableApplicationContext context;

	private volatile LoadableTickLoader delegate;

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

		delegate = context.getBean(JeTickLoaderDelegate.class);

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
	LoadableTickLoader getDelegate() throws IOException {

		LoadableTickLoader reference;

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
	public List<String> list() throws IOException {
		return getDelegate().list();
	}

	@Override
	public Long find(String source) throws IOException {
		return getDelegate().find(source);
	}

	@Override
	public Long delete(String source) throws IOException {
		return getDelegate().delete(source);
	}

	@Override
	public long load(String source, LoadableTickIterator iterator)
			throws IOException {
		return getDelegate().load(source, iterator);
	}

}

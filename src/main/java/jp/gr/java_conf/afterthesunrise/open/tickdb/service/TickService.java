package jp.gr.java_conf.afterthesunrise.open.tickdb.service;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import jp.gr.java_conf.afterthesunrise.open.tickdb.tick.Tick;

/**
 * @author takanori.takase
 */
public interface TickService extends Closeable {

	void initialize() throws IOException;

	List<Tick> find(String code, Long start, Long end) throws IOException;

	List<Tick> find(String code, Long start, Long end, Set<Integer> fields)
			throws IOException;

	TickIterator iterate(String code, Long start, Long end) throws IOException;

	TickIterator iterate(String code, Long start, Long end, Set<Integer> fields)
			throws IOException;

}

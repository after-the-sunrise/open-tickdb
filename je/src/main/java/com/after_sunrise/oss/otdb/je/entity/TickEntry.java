package com.after_sunrise.oss.otdb.je.entity;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author takanori.takase
 */
public class TickEntry implements Serializable, Comparable<TickEntry> {

	private static final long serialVersionUID = 5802199558820600683L;

	private final TickKey key;

	private final TickValue value;

	public TickEntry(TickKey key, TickValue value) {
		this.key = checkNotNull(key);
		this.value = checkNotNull(value);
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, SHORT_PREFIX_STYLE);
		builder.append("key", key);
		builder.append("value", value);
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof TickEntry && compareTo((TickEntry) o) == 0;
	}

	@Override
	public int compareTo(TickEntry o) {
		return key.compareTo(o.key);
	}

	public TickKey getKey() {
		return key;
	}

	public TickValue getValue() {
		return value;
	}

}

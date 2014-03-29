package com.after_sunrise.oss.otdb.je.entity;

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author takanori.takase
 */
public class TickKey implements Serializable, Comparable<TickKey> {

	private static final long serialVersionUID = -1158832424312702205L;

	private final long codeId;

	private final long timestamp;

	private final long sequence;

	public TickKey(long codeId, long timestamp, long sequence) {
		this.codeId = codeId;
		this.timestamp = timestamp;
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, SHORT_PREFIX_STYLE);
		builder.append("codeId", codeId);
		builder.append("timestamp", timestamp);
		builder.append("sequence", sequence);
		return builder.toString();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(codeId);
		builder.append(timestamp);
		builder.append(sequence);
		return builder.toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof TickKey && compareTo((TickKey) o) == 0;
	}

	@Override
	public int compareTo(TickKey o) {

		int comparison = Long.compare(codeId, o.getCodeId());
		if (comparison != 0) {
			return comparison;
		}

		comparison = Long.compare(timestamp, o.getTimestamp());
		if (comparison != 0) {
			return comparison;
		}

		return Long.compare(sequence, o.getSequence());

	}

	public long getCodeId() {
		return codeId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public long getSequence() {
		return sequence;
	}

}

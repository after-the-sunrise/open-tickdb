package com.after_sunrise.oss.otdb.je.entity;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.after_sunrise.oss.otdb.api.tick.Tick;

/**
 * @author takanori.takase
 */
public class TickImpl implements Tick {

	private static final long serialVersionUID = 3247800447871582698L;

	private final String code;

	private final TickEntry entry;

	public TickImpl(String code, TickEntry entry) {
		this.code = checkNotNull(code);
		this.entry = checkNotNull(entry);
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, SHORT_PREFIX_STYLE);
		builder.append("code", code);
		builder.append("entry", entry);
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, entry);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Tick && compareTo((Tick) o) == 0;
	}

	@Override
	public int compareTo(Tick o) {
		CompareToBuilder builder = new CompareToBuilder();
		builder.append(getCode(), o.getCode());
		builder.append(getTimestamp(), o.getTimestamp());
		builder.append(getSequence(), o.getSequence());
		return builder.toComparison();
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public long getTimestamp() {
		return entry.getKey().getTimestamp();
	}

	@Override
	public long getSequence() {
		return entry.getKey().getSequence();
	}

	@Override
	public Map<Integer, BigDecimal> getDecimals() {
		return entry.getValue().getDecimals();
	}

	@Override
	public Map<Integer, String> getStrings() {
		return entry.getValue().getStrings();
	}

}

package com.after_sunrise.oss.otdb.je.entity;

import static java.util.Collections.unmodifiableMap;
import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author takanori.takase
 */
public class TickValue implements Serializable {

	private static final long serialVersionUID = 327088322099954144L;

	private final long sourceId;

	private final Map<Integer, BigDecimal> decimals;

	private final Map<Integer, String> strings;

	public TickValue(long sourceId, Map<Integer, BigDecimal> decimals,
			Map<Integer, String> strings) {
		// No copy here on behalf of invoker's responsibility.
		this.sourceId = sourceId;
		this.decimals = unmodifiableMap(decimals);
		this.strings = unmodifiableMap(strings);
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, SHORT_PREFIX_STYLE);
		builder.append("sourceId", sourceId);
		builder.append("decimals", decimals);
		builder.append("strings", strings);
		return builder.toString();
	}

	public long getSourceId() {
		return sourceId;
	}

	public Map<Integer, BigDecimal> getDecimals() {
		return decimals;
	}

	public Map<Integer, String> getStrings() {
		return strings;
	}

}

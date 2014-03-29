package com.after_sunrise.oss.otdb.je.entity;

import static org.apache.commons.lang.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

/**
 * @author takanori.takase
 */
public class TickSource {

	private final long id;

	private Long startTime;

	private Long endTime;

	private Long count;

	private Boolean deleted;

	public TickSource(long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof TickSource) {
			return id == ((TickSource) o).id;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (int) (id % Integer.MAX_VALUE);
	}

	@Override
	public String toString() {
		return reflectionToString(this, SHORT_PREFIX_STYLE);
	}

	public long getId() {
		return id;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}

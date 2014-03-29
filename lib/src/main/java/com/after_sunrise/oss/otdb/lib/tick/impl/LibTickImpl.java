package com.after_sunrise.oss.otdb.lib.tick.impl;

import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ACCUMULATED_VOLUME;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_PRICE_01;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.ASK_SIZE_01;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_PRICE_01;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.BID_SIZE_01;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.LAST_PRICE;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.LAST_SIZE;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.TICK_TYPE;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.TRADE_TYPE;
import static com.after_sunrise.oss.otdb.lib.tick.LibTickValueType.VWAP;
import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.after_sunrise.oss.otdb.api.tick.Tick;
import com.after_sunrise.oss.otdb.lib.tick.LibTick;
import com.after_sunrise.oss.otdb.lib.tick.LibTickTradeType;
import com.after_sunrise.oss.otdb.lib.tick.LibTickType;
import com.after_sunrise.oss.otdb.lib.tick.LibTickValueType;
import com.google.common.base.Objects;

/**
 * @author takanori.takase
 */
public class LibTickImpl implements LibTick {

	private final Tick tick;

	public LibTickImpl(Tick tick) {
		this.tick = checkNotNull(tick);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof LibTickImpl) {
			return tick.equals(((LibTickImpl) o).tick);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return tick.hashCode();
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("tick", tick).toString();
	}

	private BigDecimal get(LibTickValueType type) {

		if (type == null) {
			return null;
		}

		return tick.getDecimals().get(type.getId());

	}

	private Map<Integer, BigDecimal> get(LibTickValueType[] types) {

		Map<Integer, BigDecimal> map = new HashMap<>();

		for (LibTickValueType type : types) {

			Integer id = type.getId();

			BigDecimal value = tick.getDecimals().get(id);

			map.put(id, value);

		}

		return Collections.unmodifiableMap(map);

	}

	@Override
	public String getCode() {
		return tick.getCode();
	}

	@Override
	public Long getTimestamp() {
		return tick.getTimestamp();
	}

	@Override
	public Long getSequence() {
		return tick.getSequence();
	}

	@Override
	public LibTickType getTickType() {
		return LibTickType.find(get(TICK_TYPE));
	}

	@Override
	public LibTickTradeType getTradeType() {
		return LibTickTradeType.find(get(TRADE_TYPE));
	}

	@Override
	public BigDecimal getAskPrice() {
		return get(ASK_PRICE_01);
	}

	@Override
	public BigDecimal getAskPrice(int level) {
		return get(LibTickValueType.getAskPrice(level));
	}

	@Override
	public Map<Integer, BigDecimal> getAskPrices() {
		return get(LibTickValueType.getAskPrices());
	}

	@Override
	public BigDecimal getAskSize() {
		return get(ASK_SIZE_01);
	}

	@Override
	public BigDecimal getAskSize(int level) {
		return get(LibTickValueType.getAskSize(level));
	}

	@Override
	public Map<Integer, BigDecimal> getAskSizes() {
		return get(LibTickValueType.getAskSizes());
	}

	@Override
	public BigDecimal getBidPrice() {
		return get(BID_PRICE_01);
	}

	@Override
	public BigDecimal getBidPrice(int level) {
		return get(LibTickValueType.getBidPrice(level));
	}

	@Override
	public Map<Integer, BigDecimal> getBidPrices() {
		return get(LibTickValueType.getBidPrices());
	}

	@Override
	public BigDecimal getBidSize() {
		return get(BID_SIZE_01);
	}

	@Override
	public BigDecimal getBidSize(int level) {
		return get(LibTickValueType.getBidSize(level));
	}

	@Override
	public Map<Integer, BigDecimal> getBidSizes() {
		return get(LibTickValueType.getBidSizes());
	}

	@Override
	public BigDecimal getLastPrice() {
		return get(LAST_PRICE);
	}

	@Override
	public BigDecimal getLastSize() {
		return get(LAST_SIZE);
	}

	@Override
	public BigDecimal getAccumulatedVolume() {
		return get(ACCUMULATED_VOLUME);
	}

	@Override
	public BigDecimal getVwap() {
		return get(VWAP);
	}

}

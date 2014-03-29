package com.after_sunrise.oss.otdb.lib.tick.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.util.Map;

import com.after_sunrise.oss.otdb.lib.tick.LibTick;
import com.after_sunrise.oss.otdb.lib.tick.LibTickTradeType;
import com.after_sunrise.oss.otdb.lib.tick.LibTickType;

/**
 * @author takanori.takase
 */
public class LibTickProxy implements LibTick {

	private final LibTick delegate;

	public LibTickProxy(LibTick delegate) {
		this.delegate = checkNotNull(delegate);
	}

	@Override
	public String getCode() {
		return delegate.getCode();
	}

	@Override
	public Long getTimestamp() {
		return delegate.getTimestamp();
	}

	@Override
	public Long getSequence() {
		return delegate.getSequence();
	}

	@Override
	public LibTickType getTickType() {
		return delegate.getTickType();
	}

	@Override
	public LibTickTradeType getTradeType() {
		return delegate.getTradeType();
	}

	@Override
	public BigDecimal getAskPrice() {
		return delegate.getAskPrice();
	}

	@Override
	public BigDecimal getAskSize() {
		return delegate.getAskSize();
	}

	@Override
	public BigDecimal getBidPrice() {
		return delegate.getBidPrice();
	}

	@Override
	public BigDecimal getBidSize() {
		return delegate.getBidSize();
	}

	@Override
	public BigDecimal getAskPrice(int level) {
		return delegate.getAskPrice(level);
	}

	@Override
	public BigDecimal getAskSize(int level) {
		return delegate.getAskSize(level);
	}

	@Override
	public BigDecimal getBidPrice(int level) {
		return delegate.getBidPrice(level);
	}

	@Override
	public BigDecimal getBidSize(int level) {
		return delegate.getBidSize(level);
	}

	@Override
	public Map<Integer, BigDecimal> getAskPrices() {
		return delegate.getAskPrices();
	}

	@Override
	public Map<Integer, BigDecimal> getAskSizes() {
		return delegate.getAskSizes();
	}

	@Override
	public Map<Integer, BigDecimal> getBidPrices() {
		return delegate.getBidPrices();
	}

	@Override
	public Map<Integer, BigDecimal> getBidSizes() {
		return delegate.getBidSizes();
	}

	@Override
	public BigDecimal getLastPrice() {
		return delegate.getLastPrice();
	}

	@Override
	public BigDecimal getLastSize() {
		return delegate.getLastSize();
	}

	@Override
	public BigDecimal getAccumulatedVolume() {
		return delegate.getAccumulatedVolume();
	}

	@Override
	public BigDecimal getVwap() {
		return delegate.getVwap();
	}

}

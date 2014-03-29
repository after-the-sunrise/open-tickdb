package com.after_sunrise.oss.otdb.lib.tick;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author takanori.takase
 */
public interface LibTick {

	String getCode();

	Long getTimestamp();

	Long getSequence();

	LibTickType getTickType();

	LibTickTradeType getTradeType();

	BigDecimal getAskPrice();

	BigDecimal getAskSize();

	BigDecimal getBidPrice();

	BigDecimal getBidSize();

	BigDecimal getAskPrice(int level);

	BigDecimal getAskSize(int level);

	BigDecimal getBidPrice(int level);

	BigDecimal getBidSize(int level);

	Map<Integer, BigDecimal> getAskPrices();

	Map<Integer, BigDecimal> getAskSizes();

	Map<Integer, BigDecimal> getBidPrices();

	Map<Integer, BigDecimal> getBidSizes();

	BigDecimal getLastPrice();

	BigDecimal getLastSize();

	BigDecimal getAccumulatedVolume();

	BigDecimal getVwap();

}

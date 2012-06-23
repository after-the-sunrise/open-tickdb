package jp.gr.java_conf.afterthesunrise.open.tickdb.loader;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author takanori.takase
 */
public interface LoadableTick {

	String getCode();

	Long getTimestamp();

	Map<Integer, BigDecimal> getValues();

}

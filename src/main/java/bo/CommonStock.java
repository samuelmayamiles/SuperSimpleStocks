package main.java.bo;

import main.java.bo.impl.AbstractStock;

/**
 * DTO for Common Stocks.
 * 
 * @author Samuel Maya Miles
 * @version 1.0
 *
 */
public class CommonStock extends AbstractStock {

	private final String	STOCK_TYPE	= "Common";

	public CommonStock() {
	}

	public CommonStock(String symbol, int lastDividend, int parValue) {
		setSymbol(symbol);
		setLastDividend(lastDividend);
		setParValue(parValue);
	}

	/**
	 * Get the Stock type.
	 */
	@Override
	public final String getStockType() {
		return STOCK_TYPE;
	}

	/**
	 * Get the Dividend Yield.
	 */
	@Override
	public final double getDividendYield() {
		double tickerPrice = getTickerPrice();
		int lastDividend = getLastDividend();
		if (tickerPrice == 0 || lastDividend == 0) {
			return 0;
		}
		return (lastDividend / tickerPrice);
	}
}

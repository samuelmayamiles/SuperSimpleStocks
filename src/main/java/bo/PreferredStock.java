package main.java.bo;

import main.java.bo.impl.AbstractStock;

/**
 * DTO for Preferred Stocks.
 * 
 * @author Samuel Maya Miles
 * @version 1.0
 *
 */
public class PreferredStock extends AbstractStock {

	private int				fixedDividend	= 0;
	private final String	STOCK_TYPE		= "Preferred";

	public PreferredStock() {
	}

	public PreferredStock(String symbol, int lastDividend, int fixedDividend,
			int parValue) {
		setSymbol(symbol);
		setLastDividend(lastDividend);
		setFixedDividend(fixedDividend);
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
		int dividend = (getFixedDividend() * getParValue());
		if (tickerPrice == 0 || dividend == 0) {
			return 0;
		}
		return (dividend / tickerPrice);
	}

	// Getters & Setters
	/**
	 * @return Integer The fixedDividend.
	 */
	public int getFixedDividend() {
		return fixedDividend;
	}

	/**
	 * @param fixedDividend
	 *            Integer The fixedDividend to set.
	 */
	public void setFixedDividend(int fixedDividend) {
		this.fixedDividend = fixedDividend;
	}
}

package main.java.bo.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import main.java.bo.Trade;

/**
 * Abstract class holding common values for Stock types.
 * 
 * @author Samuel Maya Miles
 * @version 1.0
 */
public abstract class AbstractStock {
	private String				symbol			= "";
	private int					lastDividend	= 0;
	private int					parValue		= 0;
	private LinkedList<Trade>	trades			= new LinkedList<Trade>();

	// Abstract Methods (Will be implemented by extending classes).
	/**
	 * Get the Stock type.
	 * 
	 * @return String The stock type value.
	 */
	public abstract String getStockType();

	/**
	 * Calculate the Dividend Yield.
	 * 
	 * @return Double The Divided Yield value.
	 */
	public abstract double getDividendYield();

	// Common methods for Stock dto's.
	/**
	 * Calculate the Ticker price.
	 * 
	 * @return Double The Ticker price.
	 */
	protected final double getTickerPrice() {
		int tickerPrice = 0;
		if (!trades.isEmpty()) {
			tickerPrice = getTrades().getLast().getPrice();
		}
		return tickerPrice;
	}

	/**
	 * Calculate the P/E Ratio price.
	 * 
	 * @return Double The P/E Ratio.
	 */
	public final double getPERatio() {
		double tickerPrice = getTickerPrice();
		if (tickerPrice == 0 || lastDividend == 0) {
			return 0;
		}
		return (tickerPrice / lastDividend);
	}

	/**
	 * Calculate the Geometric Mean.
	 * 
	 * @return Double The Geometric Mean.
	 */
	public final double getGeometricMean() {
		double total = 1.0;
		for (Trade trade : trades) {
			total = total * trade.getPrice();
		}
		return Math.pow(total, 1.0 / trades.size());
	}

	/**
	 * Calculate the Stock price within a given time lapse.
	 * 
	 * @param tickerTime
	 *            Long The maximum time in milliseconds allowed.
	 * @return Integer The Stock price.
	 */
	public final int getStockPrice(final long tickerTime) {
		int stockPrice = 0;
		long nowTime = new Date().getTime();
		double divisor = 0;
		double dividend = 0;

		Iterator<Trade> tradesIterator = trades.listIterator();
		while (tradesIterator.hasNext()) {
			Trade currentTrade = tradesIterator.next();
			long tradeTime = currentTrade.getTradeDate().getTime();
			if (nowTime - tradeTime <= tickerTime) {
				divisor += currentTrade.getShares();
				dividend += (currentTrade.getPrice() * currentTrade.getShares());
			}
		}

		if (divisor != 0) {
			stockPrice = (int) (dividend / divisor);
		}

		return stockPrice;
	}

	// Getters & Setters
	/**
	 * @return String The symbol.
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol
	 *            String The symbol to set.
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return Integer The lastDividend.
	 */
	public int getLastDividend() {
		return lastDividend;
	}

	/**
	 * @param lastDividend
	 *            Integer The lastDividend to set.
	 */
	public void setLastDividend(int lastDividend) {
		this.lastDividend = lastDividend;
	}

	/**
	 * @return Integer The parValue.
	 */
	public final int getParValue() {
		return parValue;
	}

	/**
	 * @param parValue
	 *            Integer The parValue to set.
	 */
	public void setParValue(int parValue) {
		this.parValue = parValue;
	}

	/**
	 * @return trades LinkedList<Trade> The Stock trades.
	 */
	public LinkedList<Trade> getTrades() {
		return trades;
	}

	/**
	 * @param trades
	 *            LinkedList <Trade> The trades to set.
	 */
	public void setTrades(LinkedList<Trade> trades) {
		this.trades = trades;
	}
}

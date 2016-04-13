package main.java.bo;

import java.util.Date;

/**
 * DTO for Trades.
 * 
 * @author Samuel Maya Miles
 * @version 1.0
 *
 */
public class Trade {
	private int		tradeId		= 0;
	private Date	tradeDate	= new Date();
	private int		shares		= 0;
	private boolean	sell		= false;
	private int		price		= 0;

	public Trade() {
		this.tradeDate = new Date();
	}

	public Trade(int id, int shares, boolean sell, int price) {
		setTradeId(id);
		setShares(shares);
		setSell(sell);
		setPrice(price);
	}

	// Getters & Setters
	/**
	 * @return Integer The tradeId.
	 */
	public int getTradeId() {
		return tradeId;
	}

	/**
	 * @param tradeId
	 *            Integer The tradeId to set.
	 */
	public void setTradeId(int tradeId) {
		this.tradeId = tradeId;
	}

	/**
	 * @return Date The tradeDate.
	 */
	public Date getTradeDate() {
		return tradeDate;
	}

	/**
	 * @param tradeDate
	 *            Date The tradeDate to set.
	 */
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	/**
	 * @return Integer The shares.
	 */
	public int getShares() {
		return shares;
	}

	/**
	 * @param shares
	 *            Integer The shares to set.
	 */
	public void setShares(int shares) {
		this.shares = shares;
	}

	/**
	 * @return boolean True if the Trade is for sell. False otherwise.
	 */
	public boolean isSell() {
		return sell;
	}

	/**
	 * @param sell
	 *            Boolean True if the Trade is to sell. False otherwise.
	 */
	public void setSell(boolean sell) {
		this.sell = sell;
	}

	/**
	 * @return Integer The price.
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            Integer The price to set.
	 */
	public void setPrice(int price) {
		this.price = price;
	}
}

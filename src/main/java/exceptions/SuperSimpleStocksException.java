package main.java.exceptions;

/**
 * Unifies the SuperSimpleStocks Exceptions.
 * 
 * @author Samuel Maya Miles
 * @version 1.0
 *
 */
public class SuperSimpleStocksException extends Exception {

	private static final long	serialVersionUID	= -8050849023364331619L;

	public SuperSimpleStocksException(String message) {
		super(message);
	}

	public SuperSimpleStocksException(String message, Throwable cause) {
		super(message, cause);
	}
}

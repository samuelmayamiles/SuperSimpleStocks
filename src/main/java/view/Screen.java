package main.java.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import main.java.exceptions.SuperSimpleStocksException;
import main.java.bo.impl.AbstractStock;

/**
 * Class to handle output to console.
 * 
 * @author Samuel Maya Miles
 * @version 1.0
 *
 */
public class Screen {
	private ResourceBundle	textBundle	= null;

	public Screen() {
		this.textBundle = ResourceBundle.getBundle("main.java.bundles.Text",
				new Locale("en", "US"));
	}

	/**
	 * Prints out the welcome menu.
	 */
	public void showWelcome(String lastError, String resultMessage) {
		System.out.println(textBundle.getString("welcome.select.title"));
		for (int i = 1; i <= 6; i++) {
			System.out.println(textBundle.getString("welcome.select.option."
					+ i));
		}
		if (!lastError.isEmpty()) {
			System.out.println(lastError + "\n");
		}
		if (!resultMessage.isEmpty()) {
			System.out.println(resultMessage + "\n");
		}
		System.out.println(textBundle.getString("welcome.select.option"));
	}

	/**
	 * Reads input entered at the console line.
	 * 
	 * @return String A string containing the console line input.
	 * @throws SuperSimpleStocksException
	 *             When an exception occurs.
	 */
	public String readConsoleLine() throws SuperSimpleStocksException {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			return bufferRead.readLine();
		} catch (IOException e) {
			throw new SuperSimpleStocksException(
					textBundle.getString("close.error"), e.getCause());
		}
	}

	/**
	 * When application is launched inside an executable jar an run in console
	 * line, this method will clean up the console screen.
	 * 
	 * @throws SuperSimpleStocksException
	 *             When an exception occurs.
	 */
	public void clearScreen() throws SuperSimpleStocksException {
		final String operatingSystem = System.getProperty("os.name");
		final File jarFile = new File(Screen.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath());
		boolean inJar = jarFile.isFile();
		if (inJar) {
			try {
				if (operatingSystem.contains("Windows")) {
					ProcessBuilder processBuilder = new ProcessBuilder("cmd",
							"/c", "cls");
					processBuilder.inheritIO().start().waitFor();
				} else {
					Runtime.getRuntime().exec("clear");
				}
			} catch (InterruptedException | IOException e) {
				throw new SuperSimpleStocksException(
						textBundle.getString("close.error"), e.getCause());
			}
		}
	}

	/**
	 * Prints out the Stock selection menu.
	 * 
	 * @param stockList
	 *            List<AbstractStock> The list containing all available Stocks.
	 * @param error
	 *            String If errors appear due to user input, these will we
	 *            printed.
	 */
	public void showStockSelectionMenu(List<AbstractStock> stockList,
			String error) {
		System.out.println(textBundle.getString("stock.select.title"));
		for (int i = 0; i < stockList.size(); i++) {
			System.out.println(i + ".- " + stockList.get(i).getSymbol());
		}
		if (!error.isEmpty()) {
			System.out.println("\n" + error);
		}
		System.out.println("\n" + stockList.size()
				+ textBundle.getString("stock.select.return"));
	}

	/**
	 * Prints out a message on screen.
	 * 
	 * @param message
	 *            String The message to print.
	 */
	public void printMessage(String message) {
		System.out.println(message);
	}

	/**
	 * Shows the different fields needed to record a Trade and catches the user
	 * input for these values.
	 * 
	 * @param stockSymbol
	 *            String The symbol for the selected Stock.
	 * @return String[] Contains the user input for the different Trade values.
	 * @throws SuperSimpleStocksException
	 *             When an exception occurs.
	 */
	public String[] getTradeInput(String stockSymbol)
			throws SuperSimpleStocksException {
		String[] enteredValues = new String[3];

		System.out.println(String.format(
				textBundle.getString("trade.insert.title"), stockSymbol));
		System.out.println(textBundle.getString("trade.insert.shares"));
		enteredValues[0] = readConsoleLine();
		System.out.println(textBundle.getString("trade.insert.price"));
		enteredValues[1] = readConsoleLine();
		System.out.println(textBundle.getString("trade.insert.buyorsell"));
		enteredValues[2] = readConsoleLine();

		return enteredValues;
	}
}

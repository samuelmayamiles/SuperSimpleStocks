package main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import main.java.dto.CommonStock;
import main.java.dto.PreferredStock;
import main.java.dto.Trade;
import main.java.impl.AbstractStock;

/**
 * GBCE All Share Index demo exercise.
 * 
 * @author Samuel Maya Miles
 * @version 1.0
 */
class SuperSimpleStocks {
	private static final long			TICKER_TIME		= 900000;
	private static List<AbstractStock>	stockList		= new LinkedList<>();
	private static String				lastError		= "";
	private static String				resultMessage	= "";
	private static ResourceBundle		textBundle		= null;

	public static void main(String[] args) {
		initializeTextBundle();
		loadValues();
		startMenu();
	}

	/**
	 * Initialises the TextBundle to use en_US.
	 */
	private static void initializeTextBundle() {
		textBundle = ResourceBundle.getBundle("main.java.bundles.Text",
				new Locale("en", "US"));
	}

	/**
	 * Loads a set of start values.
	 */
	private static void loadValues() {
		stockList.add(new CommonStock("TEA", 0, 100));
		stockList.add(new CommonStock("POP", 8, 100));
		stockList.add(new CommonStock("ALE", 23, 60));
		stockList.add(new PreferredStock("GIN", 8, 2, 100));
		stockList.add(new CommonStock("JOE", 13, 250));
	}

	/**
	 * Launches the Welcome menu and holds the logic to manage entered option.
	 */
	private static void startMenu() {
		boolean exit = false;
		try {
			clearScreen();
			while (!exit) {
				showWelcome();
				String input = readConsoleLine();
				lastError = checkOptionValid(input, 1, 6);
				clearScreen();
				if (lastError.isEmpty()) {
					int option = Integer.parseInt(input);
					if (option == 5) {
						resultMessage = showAllShareIndex();
					} else if (option == 6) {
						closeProgram(textBundle.getString("close.ok"));
					} else {
						AbstractStock selectedStock = selectStock();
						if (selectedStock != null) {
							switch (option) {
							case 1:
								resultMessage = String.format(
										textBundle.getString("dividend.yield"),
										selectedStock.getSymbol(),
										selectedStock.getDividendYield());
								break;
							case 2:
								resultMessage = String.format(
										textBundle.getString("pe.ratio"),
										selectedStock.getSymbol(),
										selectedStock.getPERatio());
								break;
							case 3:
								resultMessage = recordTrade(selectedStock);
								break;
							case 4:
								resultMessage = String.format(textBundle
										.getString("stock.price"),
										selectedStock.getSymbol(),
										selectedStock
												.getStockPrice(TICKER_TIME));
								break;
							default:
								break;
							}
							clearScreen();
						}
					}
				}
			}
		} catch (InterruptedException | IOException e) {
			closeProgram(textBundle.getString("close.error"));
		}
	}

	/**
	 * Prints out the GBCE All Share Index.
	 * 
	 * @return String A string containing the representation of the GBCE All
	 *         Share Index to print out.
	 */
	private static String showAllShareIndex() {
		String result = textBundle.getString("allshare.title");

		for (AbstractStock stock : stockList) {
			String valueToShow = textBundle.getString("allshare.notrades");
			double geometricMean = stock.getGeometricMean();
			if (!Double.isNaN(geometricMean)) {
				valueToShow = String.valueOf(geometricMean);
			}
			result = result + stock.getSymbol() + "\t" + valueToShow + "\n";
		}
		return result;
	}

	/**
	 * Shows a menu and manages the Trade creation for a given Stock.
	 * 
	 * @param selectedStock
	 *            AbstractStock The Stock selected by the user where the Trade
	 *            will be recorded.
	 * @return String A string containing the errors detected on insertion or
	 *         the successful message.
	 * @throws InterruptedException
	 *             When input can't be read of the console or screen can't be
	 *             cleared.
	 * @throws IOException
	 *             When input can't be read of the console or screen can't be
	 *             cleared.
	 */
	private static String recordTrade(final AbstractStock selectedStock)
			throws InterruptedException, IOException {
		String error = "";
		List<String> errors = new LinkedList<String>();
		String shares = "";
		String price = "";
		String buyOrSell = "";

		clearScreen();
		System.out.println(String.format(
				textBundle.getString("trade.insert.title"),
				selectedStock.getSymbol()));
		System.out.println(textBundle.getString("trade.insert.shares"));
		shares = readConsoleLine();
		System.out.println(textBundle.getString("trade.insert.price"));
		price = readConsoleLine();
		System.out.println(textBundle.getString("trade.insert.buyorsell"));
		buyOrSell = readConsoleLine();

		validateTradeInsert(shares, price, buyOrSell, errors);

		if (errors.isEmpty()) {
			Trade newTrade = new Trade();
			newTrade.setTradeId(selectedStock.getTrades().size() + 1);
			newTrade.setPrice(Integer.parseInt(price));
			newTrade.setShares(Integer.parseInt(shares));
			newTrade.setSell(Boolean.parseBoolean(buyOrSell));
			selectedStock.getTrades().add(newTrade);
			return String.format(textBundle.getString("trade.insert.ok"),
					selectedStock.getSymbol());
		}

		if (!errors.isEmpty()) {
			error = textBundle.getString("trade.insert.haserrors");
		}
		for (String detectedErrors : errors) {
			error += detectedErrors;
		}
		return error;
	}

	/**
	 * Validates the input fields for a Trade.
	 * 
	 * @param shares
	 *            String The shares inserted by the user.
	 * @param price
	 *            String The price inserted by the user.
	 * @param buyOrSell
	 *            String The buy or sell indicator inserted by the user.
	 * @param errors
	 *            List<String> List containing all the detected input errors.
	 */
	private static void validateTradeInsert(final String shares,
			final String price, String buyOrSell, List<String> errors) {
		int number = 0;

		try {
			number = Integer.parseInt(shares);
			if (number < 0) {
				errors.add(String.format(
						textBundle.getString("trade.insert.error.shares"),
						textBundle.getString("trade.insert.error.negative")));
			}
		} catch (NumberFormatException e) {
			errors.add(String.format(textBundle
					.getString("trade.insert.error.shares"), String.format(
					textBundle.getString("trade.insert.error.notvalidinput"),
					shares)));
		}

		try {
			number = Integer.parseInt(price);
			if (number < 0) {
				errors.add(String.format(
						textBundle.getString("trade.insert.error.price"),
						textBundle.getString("trade.insert.error.negative")));
			}
		} catch (NumberFormatException e) {
			errors.add(String.format(textBundle
					.getString("trade.insert.error.price"), String.format(
					textBundle.getString("trade.insert.error.notvalidinput"),
					price)));
		}

		if (buyOrSell.equalsIgnoreCase("B")) {
			buyOrSell = "true";
		} else if (buyOrSell.equalsIgnoreCase("S")) {
			buyOrSell = "false";
		} else {
			errors.add(String.format(textBundle
					.getString("trade.insert.error.buyorsell"), String.format(
					textBundle.getString("trade.insert.error.notvalidinput"),
					buyOrSell)));
		}
	}

	/**
	 * Reads input entered at the console line.
	 * 
	 * @return String A string containing the console line input.
	 * @throws IOException
	 *             When input can't be read of the console.
	 */
	private static String readConsoleLine() throws IOException {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));
		return bufferRead.readLine();
	}

	/**
	 * Prints a menu containing all the available Stocks for the user to choose
	 * one. Also, a return option is added to cancel.
	 * 
	 * @return AbstractStock The selected Stock or null if return option is
	 *         selected.
	 * @throws InterruptedException
	 *             When input can't be read of the console or screen can't be
	 *             cleared.
	 * @throws IOException
	 *             When input can't be read of the console or screen can't be
	 *             cleared.
	 */
	private static AbstractStock selectStock() throws InterruptedException,
			IOException {
		boolean validSelection = false;
		String error = "";
		while (!validSelection) {
			clearScreen();
			System.out.println(textBundle.getString("stock.select.title"));
			for (int i = 0; i < stockList.size(); i++) {
				System.out.println(i + ".- " + stockList.get(i).getSymbol());
			}
			if (!error.isEmpty()) {
				System.out.println("\n" + error);
			}
			System.out.println("\n" + stockList.size()
					+ textBundle.getString("stock.select.return"));
			String input = readConsoleLine();
			error = checkOptionValid(input, 0, stockList.size());
			if (error.isEmpty()) {
				int option = Integer.parseInt(input);
				if (option < stockList.size()) {
					return stockList.get(option);
				} else {
					validSelection = true;
				}
			}
		}
		return null;
	}

	/**
	 * When application is launched inside an executable jar an run in console
	 * line, this method will clean up the console screen.
	 * 
	 * @throws InterruptedException
	 *             When screen can't be cleared.
	 * @throws IOException
	 *             When screen can't be cleared.
	 */
	private static void clearScreen() throws InterruptedException, IOException {
		final String operatingSystem = System.getProperty("os.name");
		final File jarFile = new File(SuperSimpleStocks.class
				.getProtectionDomain().getCodeSource().getLocation().getPath());
		boolean inJar = jarFile.isFile();
		if (inJar) {
			if (operatingSystem.contains("Windows")) {
				ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c",
						"cls");
				processBuilder.inheritIO().start().waitFor();
			} else {
				Runtime.getRuntime().exec("clear");
			}
		}
	}

	/**
	 * Closes the running program.
	 * 
	 * @param message
	 *            String A message to show to user on closing.
	 */
	private static void closeProgram(final String message) {
		System.out.println(message);
		System.exit(0);
	}

	/**
	 * Prints out the welcome menu.
	 */
	private static void showWelcome() {
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
			resultMessage = "";
		}
		System.out.println(textBundle.getString("welcome.select.option"));
	}

	/**
	 * Check if an entered option is valid.
	 * 
	 * @param input
	 *            String Value entered in the console.
	 * @param minValue
	 *            Integer The minimum option value accepted.
	 * @param maxValue
	 *            Integer The maximum option value accepted.
	 * @return String A string containing detected errors or empty if input was
	 *         correct.
	 */
	private static String checkOptionValid(final String input,
			final int minValue, final int maxValue) {
		int optionValue = 0;
		try {
			optionValue = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return textBundle.getString("welcome.select.option.error.numbers");
		}
		if (optionValue < minValue || optionValue > maxValue) {
			return textBundle
					.getString("welcome.select.option.error.notavailable");
		}
		return "";
	}
}

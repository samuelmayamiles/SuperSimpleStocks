package main.java;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import main.java.bo.Trade;
import main.java.bo.impl.AbstractStock;
import main.java.exceptions.SuperSimpleStocksException;
import main.java.model.ValueLoader;
import main.java.view.Screen;

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
	private static final Screen			userScreen		= new Screen();
	private static final ValueLoader	loader			= new ValueLoader();

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
	 * Loads the demo start values.
	 */
	private static void loadValues() {
		stockList = loader.loadDummyValues();
	}

	/**
	 * Launches the Welcome menu and holds the logic to manage entered option.
	 */
	private static void startMenu() {
		boolean exit = false;
		try {
			userScreen.clearScreen();
			while (!exit) {
				userScreen.showWelcome(lastError, resultMessage);
				resultMessage = "";
				String input = userScreen.readConsoleLine();
				lastError = checkOptionValid(input, 1, 6);
				userScreen.clearScreen();
				if (lastError.isEmpty()) {
					processOption(input);
				}
			}
		} catch (SuperSimpleStocksException e) {
			closeProgram(e.getMessage());
		}
	}

	/**
	 * Processes valid user input.
	 * 
	 * @param input
	 *            String The option selected by the user.
	 * @throws SuperSimpleStocksException
	 *             When an exception occurs.
	 */
	private static void processOption(String input)
			throws SuperSimpleStocksException {
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
					resultMessage = String.format(
							textBundle.getString("stock.price"),
							selectedStock.getSymbol(),
							selectedStock.getStockPrice(TICKER_TIME));
					break;
				default:
					break;
				}
				userScreen.clearScreen();
			}
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
	 * @throws SuperSimpleStocksException
	 *             When an exception occurs.
	 */
	private static String recordTrade(final AbstractStock selectedStock)
			throws SuperSimpleStocksException {
		String error = "";
		List<String> errors = new LinkedList<String>();
		String[] enteredValues = new String[3];

		userScreen.clearScreen();
		enteredValues = userScreen.getTradeInput(selectedStock.getSymbol());
		validateTradeInsert(enteredValues, errors);

		if (errors.isEmpty()) {
			Trade newTrade = new Trade();
			newTrade.setTradeId(selectedStock.getTrades().size() + 1);
			newTrade.setShares(Integer.parseInt(enteredValues[0]));
			newTrade.setPrice(Integer.parseInt(enteredValues[1]));
			newTrade.setSell(Boolean.parseBoolean(enteredValues[2]));
			selectedStock.getTrades().add(newTrade);
			return String.format(textBundle.getString("trade.insert.ok"),
					selectedStock.getSymbol());
		}

		if (!errors.isEmpty()) {
			error = String.format(
					textBundle.getString("trade.insert.haserrors"),
					selectedStock.getSymbol());
		}
		for (String detectedErrors : errors) {
			error += detectedErrors;
		}
		return error;
	}

	/**
	 * Validates the input fields for a Trade.
	 * 
	 * @param enteredValues
	 *            String[] Holds the entered values for shares, price and buy or
	 *            sell type.
	 * @param errors
	 *            List<String> List containing all the detected input errors.
	 */
	private static void validateTradeInsert(String[] enteredValues,
			List<String> errors) {
		int number = 0;

		try {
			number = Integer.parseInt(enteredValues[0]);
			if (number < 0) {
				errors.add(String.format(
						textBundle.getString("trade.insert.error.shares"),
						textBundle.getString("trade.insert.error.negative")));
			}
		} catch (NumberFormatException e) {
			errors.add(String.format(textBundle
					.getString("trade.insert.error.shares"), String.format(
					textBundle.getString("trade.insert.error.notvalidinput"),
					enteredValues[0])));
		}

		try {
			number = Integer.parseInt(enteredValues[1]);
			if (number < 0) {
				errors.add(String.format(
						textBundle.getString("trade.insert.error.price"),
						textBundle.getString("trade.insert.error.negative")));
			}
		} catch (NumberFormatException e) {
			errors.add(String.format(textBundle
					.getString("trade.insert.error.price"), String.format(
					textBundle.getString("trade.insert.error.notvalidinput"),
					enteredValues[1])));
		}

		if (enteredValues[2].equalsIgnoreCase("B")) {
			enteredValues[2] = "true";
		} else if (enteredValues[2].equalsIgnoreCase("S")) {
			enteredValues[2] = "false";
		} else {
			errors.add(String.format(textBundle
					.getString("trade.insert.error.buyorsell"), String.format(
					textBundle.getString("trade.insert.error.notvalidinput"),
					enteredValues[2])));
		}
	}

	/**
	 * Prints a menu containing all the available Stocks for the user to choose
	 * one. Also, a return option is added to cancel.
	 * 
	 * @return AbstractStock The selected Stock or null if return option is
	 *         selected.
	 * @throws SuperSimpleStocksException
	 *             When an exception occurs.
	 */
	private static AbstractStock selectStock()
			throws SuperSimpleStocksException {
		boolean validSelection = false;
		String error = "";
		while (!validSelection) {
			userScreen.clearScreen();
			userScreen.showStockSelectionMenu(stockList, error);
			String input = userScreen.readConsoleLine();
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
	 * Closes the running program.
	 * 
	 * @param message
	 *            String A message to show to user on closing.
	 */
	private static void closeProgram(final String message) {
		userScreen.printMessage(message);
		System.exit(0);
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

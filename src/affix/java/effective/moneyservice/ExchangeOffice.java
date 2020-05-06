package affix.java.effective.moneyservice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This is an implementation of the interface MoneyService
 * 
 * @author group Center
 */
public class ExchangeOffice implements MoneyService{

	/**
	 * name - a String defining the name of the exchange office
	 */
	private String name;

	/**
	 * logger - a Logger object
	 */
	private final static Logger logger = Logger.getLogger("affix.java.effective.moneyservice");

	/**
	 * completedTransactions - a List for storing all the completeded transactions during a session
	 */
	private List<Transaction> completedTransactions
	= new ArrayList<>();

	/**
	 * inventory - a Map holding current inventory value for exchange office where
	 * key is currency code and value is the amount in specified currency
	 */
	private Map<String, Double> inventory
	= new HashMap<>();

	/**
	 * Constructor
	 * @param name - a String defining the name of the Exchange Office 
	 * @param inv - a Map holding data for the exchange office inventory key is a currency code
	 * and value is current inventory value in each currency
	 */
	public ExchangeOffice(String name, Map<String, Double> inv){
		this.name = name;
		this.inventory = inv;
	}

	@Override
	public boolean buyMoney(Order orderData) {

		logger.log(Level.INFO, "Entering buyMoney method -->");
		// Check if selected currency is one of the accepted currencies
		if(MoneyServiceApp.currencyMap.containsKey(orderData.getCurrencyCode())) {

			// Extract specific exchange rate for the currency the customer has requested
			Currency temp = MoneyServiceApp.currencyMap.get(orderData.getCurrencyCode());	
			// Alter the exchange rate with profit margin
			double alteredExchangeRate = temp.getExchangeRate() * ServiceConfig.BUY_RATE;
			// Amount to be returned to customer after bought currency
			double boughtInREF = orderData.getAmount() * alteredExchangeRate;

			if(validateOrderBuy(orderData, boughtInREF)) {
				// Calculate the new inventory of the reference currency
				Double newValueREF = inventory.get(MoneyServiceApp.referenceCurrencyCode) - boughtInREF;

				Optional<Double> maybeAvailableAmount = getAvailableAmount(orderData.getCurrencyCode());
				Double newBoughtCurrencyValue;

				//Check if currency exist in inventory
				if(maybeAvailableAmount.isEmpty()) {
					logger.log(Level.WARNING, "Currency: " + orderData.getCurrencyCode() + " does not exist");
					inventory.putIfAbsent(orderData.getCurrencyCode(), (double) 0);
					newBoughtCurrencyValue = (double) (0 + orderData.getAmount());
				}
				else {
					newBoughtCurrencyValue = maybeAvailableAmount.get() + orderData.getAmount();
				}
				// Update the inventory with the new values
				inventory.replace(MoneyServiceApp.referenceCurrencyCode, newValueREF);
				inventory.replace(orderData.getCurrencyCode(), newBoughtCurrencyValue);

				// Create new transaction and add to map with completed orders
				Transaction completedOrders = new Transaction(orderData.getCurrencyCode(), orderData.getAmount(), orderData.getMode());
				completedTransactions.add(completedOrders);

				System.out.println("Your requested order has been processesed and accepted!");
				System.out.format("Returning %.0f %s to customer\n\n",boughtInREF,MoneyServiceApp.referenceCurrencyCode);

				logger.log(Level.INFO, "Exiting buyMoney method <--");
				return true;
			}
			else {
				logger.log(Level.INFO, "Exiting buyMoney method <--");
				return false;
			}
		}
		else {
			logger.log(Level.WARNING, "missing amount in specified currency: " + orderData.getCurrencyCode());
			logger.log(Level.INFO, "Exiting buyMoney method <--");
			throw new IllegalArgumentException("The specified currency code is not accepted!");
		}
	}

	@Override
	public boolean sellMoney(Order orderData) {

		logger.log(Level.INFO, "Entering sellMoney method -->");
		// Check if selected currency is one of the accepted currencies
		if(MoneyServiceApp.currencyMap.containsKey(orderData.getCurrencyCode())) {

			// CurrencyCode is the sold currency
			// Extract specific exchange rate for the currency the customer has
			Currency temp = MoneyServiceApp.currencyMap.get(orderData.getCurrencyCode());

			// Alter the exchange rate with profit margin
			double alteredExchangeRate = temp.getExchangeRate() * ServiceConfig.SELL_RATE;

			// Calculation of what the customer has to pay for the transaction
			double soldAmountInREF = orderData.getAmount() / (1 / alteredExchangeRate);

			if(validateOrderSell(orderData)) {
				// Calculate the new inventory value of reference currency
				Double newValueREF = inventory.get(MoneyServiceApp.referenceCurrencyCode) + soldAmountInREF;
				// Calculate the new inventory value of sold currency
				Double newSoldCurrencyValue = inventory.get(orderData.getCurrencyCode()) - orderData.getAmount();

				// Update the inventory with the new values
				inventory.replace(MoneyServiceApp.referenceCurrencyCode, newValueREF);
				inventory.replace(orderData.getCurrencyCode(), newSoldCurrencyValue);

				// Create new transaction and add to map with completed orders
				Transaction completedOrders = new Transaction(orderData.getCurrencyCode(), orderData.getAmount(), orderData.getMode());
				completedTransactions.add(completedOrders);

				System.out.println("Your requested order has been processesed and accepted!");
				System.out.format("Amount to pay %.0f %s\n\n",soldAmountInREF,MoneyServiceApp.referenceCurrencyCode);
				System.out.println();
				logger.log(Level.INFO, "Exiting sellMoney method <--");
				return true;
			}
			else {
				logger.log(Level.INFO, "Exiting sellMoney method <--");
				return false;
			}
		}
		else {
			logger.log(Level.WARNING, "didn't pass validateOrderSell: ", validateOrderSell(orderData));
			logger.log(Level.INFO, "Exiting sellMoney method <--");
			throw new IllegalArgumentException("The specified currency code is not accepted!");
		}
	}

	@Override
	public void printSiteReport(String destination) {
		logger.log(Level.INFO, "Entering printSiteReport method -->");

		if(destination.equalsIgnoreCase("console")) {
			System.out.println("Current Inventory");
			inventory.forEach((key, value) -> System.out.format("%s: %.0f\n", key, value));
		}
		if(destination.equalsIgnoreCase("txt")) {
			try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("SiteReport.txt")))){
				//iterate map entries
				for(Map.Entry<String, Double> entry : inventory.entrySet()){

					//put key and value separated by a colon
					pw.write( entry.getKey() + ": " + entry.getValue() + "\n" );
				}
			}
			catch(IOException e) {
				logger.log(Level.SEVERE, "Print Report exception! " + e);
				e.printStackTrace();
			}
		}
		logger.log(Level.INFO, "Exiting printSiteReport method <--");

	}

	@Override
	public void shutDownService(String destination) {
		// Create Directories
		String path = String.format("Reports/%s", MoneyServiceApp.OFFICE_NAME);
		new File(path).mkdirs();

		// Path and Filename = fullPath
		String fullPath = String.format("%s/%s", path, destination);

		logger.log(Level.INFO, "Entering shutDownService method -->");
		// Serialize and store completed transactions.
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fullPath))){
			oos.writeObject(completedTransactions);
		}
		catch(IOException ioe) {
			logger.log(Level.SEVERE, "Save transaction to file exception! " + ioe);
			System.out.println("Sorry, could save transactions to file.");
		}
		logger.log(Level.INFO, "Exiting shutDownService method <--");
	}

	public Map<String, Currency> getCurrencyMap() {
		return MoneyServiceApp.currencyMap;
	}

	@Override
	public Optional<Double> getAvailableAmount(String currencyCode){
		// Get the value of the specified currency.
		Double AvailableAmount = inventory.get(currencyCode);
		// If the value is not null, return Optional value. Otherwise, return empty Optional.
		Optional<Double> opt = Optional.ofNullable(AvailableAmount);
		// Returning the amount of specified currency.
		return opt;
	}

	/**
	 * Helper method for validating a order of TransactionMode BUY
	 * @param orderData - a Order object holding information about requested transaction from customer
	 * @param boughtInRef - a double defining the ordered amount in reference currency
	 * @return boolean true if validation of the order passed else false
	 */
	private boolean validateOrderBuy(Order orderData, double boughtInRef) {
		// Check if amount is accepted configured via min amount
		if(orderData.getAmount() %MoneyServiceApp.orderAmountLimit == 0) {
			//Check if bought amount is available in inventory
			if(inventory.get(MoneyServiceApp.referenceCurrencyCode)>= boughtInRef) {
				return true;
			}
			else {
				System.out.format("Currently can not meet the requested amount of %d\n", orderData.getAmount());
			}
		}
		else {
			System.out.format("The amount does not meet the requirements (min/multiples) of %d\n", MoneyServiceApp.orderAmountLimit);
		}
		return false;
	}

	/**
	 * Helper method for validating a order of TransactionMode SELL
	 * @param orderData - a Order object holding information about requested transaction from customer
	 * @return boolean true if validation of the order passed else false
	 */
	private boolean validateOrderSell(Order orderData) {

		// Check if amount is accepted configured via min amount
		if(orderData.getAmount() %MoneyServiceApp.orderAmountLimit == 0) {
			//Check if bought currency is in inventory
			Optional<Double> maybeAvailableAmount = getAvailableAmount(orderData.getCurrencyCode());
			if(maybeAvailableAmount.isPresent()) {
				if(maybeAvailableAmount.get() >= orderData.getAmount()) {
					return true;					
				}
				else {
					System.out.format("Currently can not meet the requested amount of %d\n", orderData.getAmount());
				}
			}
		}
		else {
			System.out.format("The amount does not meet the requirements (min/multiples) of %d\n", MoneyServiceApp.orderAmountLimit);
		}
		return false;
	}
}


package affix.java.effective.moneyservice;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * This is an implementation of the generic interface MoneyService
 * plus some own implemented methods
 * @author group Center
 */
public class ExchangeOffice implements MoneyService{

	private String name;
	
	private final static Logger logger = Logger.getLogger("affix.java.effective.moneyservice");

	// CHANGED FROM List<Transaction> CHECK WITH OTHERS IN THE GROUP
	private List<Transaction> completedTransactions
	= new ArrayList<>();

	private Map<String, Double> inventory
	= new HashMap<>();

	public ExchangeOffice(String name, Map<String, Double> inv){
		this.name = name;
		this.inventory = inv;
	}

	public boolean buyMoney(Order orderData){
		logger.log(Level.INFO, "Entering buyMoney method..");
		// We do not allow buying orders with reference currency.
		if(orderData.getCurrencyCode() == MoneyServiceApp.referenceCurrencyCode 
				|| orderData.getAmount() %50 != 0) {
			logger.log(Level.WARNING, "orderData didn't meet requirements: ");
			return false;
		}

		// CurrencyCode is the bought currency
		// Extract specific exchange rate for the currency the customer has
		Currency temp = MoneyServiceApp.currencyMap.get(orderData.getCurrencyCode());		
		// If currency does not exist in currencyMap, then return false (Missing currencyRate).
		if(temp == null) {
			logger.log(Level.WARNING, "Currency has probably not been set: " + temp);
			return false;
		}
    
		// Alter the exchange rate with profit margin
		double alteredExchangeRate = temp.getExchangeRate() * ServiceConfig.BUY_RATE;

		// Amount to be returned to customer after bought currency
		double boughtInSEK = orderData.getAmount() * alteredExchangeRate;
    
		if(inventory.get(MoneyServiceApp.referenceCurrencyCode)>= boughtInSEK ) {
			Double newValueSEK = inventory.get(MoneyServiceApp.referenceCurrencyCode) - boughtInSEK;
			Double newBoughtCurrVal = inventory.get(orderData.getCurrencyCode());
			// Add new currency to list if it doens't exist.
			if(newBoughtCurrVal == null) {
				inventory.putIfAbsent(orderData.getCurrencyCode(), (double) 0);
				newBoughtCurrVal = (double) (0 + orderData.getAmount());
			}

			// Update the inventory with the new values
			inventory.replace(MoneyServiceApp.referenceCurrencyCode, newValueSEK);
			inventory.replace(orderData.getCurrencyCode(), newBoughtCurrVal);

			// Create new transaction and add to map with completed orders
			Transaction completedOrders = new Transaction(orderData.getCurrencyCode(), orderData.getAmount(), orderData.getMode());
			completedTransactions.add(completedOrders);
			return true;
		}
		else {
			logger.log(Level.WARNING, "missing amount in specified currency: ", inventory.get(MoneyServiceApp.referenceCurrencyCode));
			throw new IllegalArgumentException("Order could not be accepted! missing amount in specified currency!");
		}
	}

	public boolean sellMoney(Order orderData) {
		logger.log(Level.INFO, "Entering sellMoney method..");
		// We do not allow selling orders with reference currency.
		if(orderData.getCurrencyCode() == MoneyServiceApp.referenceCurrencyCode
				|| orderData.getAmount() %50 != 0) {
			logger.log(Level.WARNING, "orderData didn't meet requirements: ");
			return false;
		}

		// CurrencyCode is the sold currency
		// Extract specific exchange rate for the currency the customer has
		Currency temp = MoneyServiceApp.currencyMap.get(orderData.getCurrencyCode());
		
		// If currency does not exist in currencyMap, then return false (Missing currencyRate).
		if(temp == null) {
			logger.log(Level.WARNING, "Currency has probably not been set: " + temp);
			return false;
		}

		// Alter the exchange rate with profit margin
		double alteredExchangeRate = temp.getExchangeRate() * ServiceConfig.SELL_RATE;

		double soldAmount = orderData.getAmount() / (1 / alteredExchangeRate);

		if(validateOrder(orderData)) {
			Double newValueSEK = inventory.get(MoneyServiceApp.referenceCurrencyCode) + soldAmount;
			Double newSoldCurrVal = inventory.get(orderData.getCurrencyCode());
			
			// Return false if currency doesn't exist in office.
			if(newSoldCurrVal == null)
				return false;
			
			// Remove the amount from office.
			newSoldCurrVal -= orderData.getAmount();

			// Update the inventory with the new values
			inventory.replace(MoneyServiceApp.referenceCurrencyCode, newValueSEK);
			inventory.replace(orderData.getCurrencyCode(), newSoldCurrVal);

			// Create new transaction and add to map with completed orders
			Transaction completedOrders = new Transaction(orderData.getCurrencyCode(), orderData.getAmount(), orderData.getMode());
			completedTransactions.add(completedOrders);
			return true;
		}
		else {
			logger.log(Level.WARNING, "didn't pass validateOrder: ", validateOrder(orderData));
			throw new IllegalArgumentException("Order could not be accepted! missing amount in specified currency!");
		}

	}

	public void printSiteReport(String destination) {
		logger.log(Level.INFO, "Entering printSiteReport method..");

		if(destination.equalsIgnoreCase("console")) {
			inventory.forEach((key, value) -> System.out.println(key + ": " + value));
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

	}

	public void shutDownService(String destination) {
		logger.log(Level.INFO, "Entering shutDownService method..");
		// Serialize and store completed transactions.
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(destination))){
			oos.writeObject(completedTransactions);
		}
		catch(IOException ioe) {
			logger.log(Level.SEVERE, "Save transaction to file exception! " + ioe);
			System.out.println("Sorry, could save transactions to file.");
		}
	}

	public Map<String, Currency> getCurrencyMap() {
		return MoneyServiceApp.currencyMap;
	}

	public Optional<Double> getAvailableAmount(String currencyCode){
		// Get the value of the specified currency.
		Double AvailableAmount = inventory.get(currencyCode);
		// If the value is not null, return Optional value. Otherwise, return empty Optional.
		Optional<Double> opt = Optional.ofNullable(AvailableAmount);
		// Returning the amount of specified currency.
		return opt;
	}


	private boolean validateOrder(Order orderData) {
		// We do not allow order which are lower
		// then the limit. (read MoneyServiceConfig.txt - ORDER_AMOUNT_LIMIT)
		if(orderData.getAmount() < MoneyServiceApp.orderAmountLimit)
			return false;
		
		// Get the available amount at site.
		Optional<Double> maybeAvailableAmount = getAvailableAmount(orderData.getCurrencyCode());
		// Return true if amount requested is available. Otherwise, return false.
		if(maybeAvailableAmount.isPresent()) {
			if(maybeAvailableAmount.get() >= orderData.getAmount())
				return true;
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
}


package ya.thn.project.MoneyService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This is an implementation of the generic interface MoneyService
 * plus some own implemented methods
 * @author group Center
 */
public class ExchangeOffice implements MoneyService{

	private String name;

	
	// CHANGED FROM List<Transaction> CHECK WITH OTHERS IN THE GROUP
	private Map<LocalDateTime, Transaction> completedTransactions
	= new HashMap<>();

	private Map<String, Double> inventory
	= new HashMap<>();

	public ExchangeOffice(String name, Map<String, Double> inv){
		this.name = name;
		this.inventory = inv;
	}

	public boolean buyMoney(Order orderData){

		// CurrencyCode is the bought currency
		// Extract specific exchange rate for the currency the customer has
		Currency temp = MoneyServiceApp.currencyMap.get(orderData.getCurrencyCode());

		// Alter the exchange rate with profit margin
		double alteredExchangeRate = temp.getExchangeRate() * ServiceConfig.BUY_RATE;

		// Amount to be returned to customer after bought currency
		double boughtInSEK = orderData.getAmount() * alteredExchangeRate;

		if(inventory.get(MoneyServiceApp.referenceCurrencyCode)>= boughtInSEK) {
			double newValueSEK = inventory.get(MoneyServiceApp.referenceCurrencyCode) - boughtInSEK;
			double newBoughtCurrVal = inventory.get(orderData.getCurrencyCode()) + orderData.getAmount();

			// Update the inventory with the new values
			inventory.replace(MoneyServiceApp.referenceCurrencyCode, newValueSEK);
			inventory.replace(orderData.getCurrencyCode(), newBoughtCurrVal);

			// Create new transaction and add to map with completed orders
			Transaction completedOrders = new Transaction(orderData.getCurrencyCode(), orderData.getAmount(), orderData.getMode());
			completedTransactions.putIfAbsent(completedOrders.getCreatedAt(), completedOrders);
			return true;
		}
		else {
			throw new IllegalArgumentException("Order could not be accepted! missing amount in specified currency!");
		}
	}

	public boolean sellMoney(Order orderData) {

		// CurrencyCode is the sold currency
		// Extract specific exchange rate for the currency the customer has
		Currency temp = MoneyServiceApp.currencyMap.get(orderData.getCurrencyCode());

		// Alter the exchange rate with profit margin
		double alteredExchangeRate = temp.getExchangeRate() * ServiceConfig.SELL_RATE;

		double soldAmount = orderData.getAmount() / (1 / alteredExchangeRate);

		if(validateOrder(orderData)) {
			double newValueSEK = inventory.get(MoneyServiceApp.referenceCurrencyCode) + soldAmount;
			double newSoldCurrVal = inventory.get(orderData.getCurrencyCode()) - orderData.getAmount();

			// Update the inventory with the new values
			inventory.replace(MoneyServiceApp.referenceCurrencyCode, newValueSEK);
			inventory.replace(orderData.getCurrencyCode(), newSoldCurrVal);

			// Create new transaction and add to map with completed orders
			Transaction completedOrders = new Transaction(orderData.getCurrencyCode(), orderData.getAmount(), orderData.getMode());
			completedTransactions.putIfAbsent(completedOrders.getCreatedAt(), completedOrders);
			return true;
		}
		else {
			throw new IllegalArgumentException("Order could not be accepted! missing amount in specified currency!");
		}

	}

	public void printSiteReport(String destination) {

	}

	public void shutDownService(String destination) {

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

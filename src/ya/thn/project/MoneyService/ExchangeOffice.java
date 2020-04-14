package ya.thn.project.MoneyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This is an implementation of the generic interface MoneyService
 * plus some own implemented methods
 * @author group Center
 */
public class ExchangeOffice implements MoneyService{

	private static final double BUY_RATE = 0.995;
	private static final double SELL_RATE = 1.005;
	private static final String REFERENCE_CURRENCY = "SEK";

	private String name;
	//private Double amount; 
	private String currencyCode;

	private Map<DateTime, List<Transaction>> completedTransactions
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
		double alteredExchangeRate = temp.getExchangeRate() * BUY_RATE;

		// Amount to be returned to customer after bought currency
		double boughtInSEK = orderData.getAmount() * alteredExchangeRate;

		if(inventory.get(REFERENCE_CURRENCY)>= boughtInSEK) {
			double newValueSEK = inventory.get(REFERENCE_CURRENCY) - boughtInSEK;
			double newBoughtCurrVal = inventory.get(orderData.getCurrencyCode()) + orderData.getAmount();

			// Update the inventory with the new values
			inventory.replace(REFERENCE_CURRENCY, newValueSEK);
			inventory.replace(orderData.getCurrencyCode(), newBoughtCurrVal);

			// Create new transaction and add to map with completed orders
			Transaction completedOrders = new Transaction(orderData);
			completedTransactions.putIfAbsent(completedOrders.getCurrenDateTime(), completedOrders);
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
		double alteredExchangeRate = temp.getExchangeRate() * SELL_RATE;

		double soldAmount = orderData.getAmount() / (1 / alteredExchangeRate);

		if(validateOrder(orderData)) {
			double newValueSEK = inventory.get(REFERENCE_CURRENCY) + orderData.getAmount();
			double newSoldCurrVal = inventory.get(orderData.getCurrencyCode()) - soldAmount;

			// Update the inventory with the new values
			inventory.replace(REFERENCE_CURRENCY, newValueSEK);
			inventory.replace(orderData.getCurrencyCode(), newSoldCurrVal);

			// Create new transaction and add to map with completed orders
			Transaction completedOrders = new Transaction(orderData);
			completedTransactions.putIfAbsent(completedOrders.getCurrenDataTime(), completedOrders);
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


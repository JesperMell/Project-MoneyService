package affix.java.effective.moneyservice;

/**
 * Class for holding information with with currency and it's exchange rate
 * to reference currency
 * 
 * @author Group Center
 *
 */

public class Currency {
	
	/**
	 * currencyCode - a String defining a currency code
	 */
	private final String currencyCode;
	
	/**
	 * exchangeRate - a Double value defining the exchange rate to reference currency
	 */
	private final double exchangeRate;
	
	/**
	 * Constructor
	 * @param currencyCode - String holding the codename of a Currency
	 * @param rate - double holding the value of the exchange rate to reference currency
	 * @throws IllegalArgumentException if currencyCode is null or empty,
	 * or if the exchange rate is 0 or negative
	 */
	public Currency(String currencyCode, double rate) {
		
		if(currencyCode == null || currencyCode.isEmpty()) {
			throw new IllegalArgumentException("Currency Code missing!");
		}
		else {
			if(rate <= 0) {
				throw new IllegalArgumentException("Exchange rate cant be negative");
			}
		}
		
		this.currencyCode = currencyCode;
		this.exchangeRate = rate;
	}


	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}


	/**
	 * @return the rate
	 */
	public double getExchangeRate() {
		return exchangeRate;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		long temp;
		temp = Double.doubleToLongBits(exchangeRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Currency other = (Currency) obj;
		if (currencyCode == null) {
			if (other.currencyCode != null)
				return false;
		} else if (!currencyCode.equals(other.currencyCode))
			return false;
		if (Double.doubleToLongBits(exchangeRate) != Double.doubleToLongBits(other.exchangeRate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Currency [currencyCode=%s, exchange rate=%s]", currencyCode, exchangeRate);
	}
	
}

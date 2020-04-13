package ya.thn.project.MoneyService;

public class Currency {
	
	private final String currencyCode;
	private final double rate;
	
	
	/**
	 * @param currencyCode String holding the codename of a Currency
	 * @param rate double holding the value of the exchange rate to local currency
	 */
	public Currency(String currencyCode, double rate) {
		this.currencyCode = currencyCode;
		this.rate = rate;
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
	public double getRate() {
		return rate;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		long temp;
		temp = Double.doubleToLongBits(rate);
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
		if (Double.doubleToLongBits(rate) != Double.doubleToLongBits(other.rate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Currency [currencyCode=%s, exchange rate=%s]", currencyCode, rate);
	}
	
}

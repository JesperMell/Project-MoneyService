package affix.java.effective.moneyservice;

/**
 * Order is a value type defining customers requested order for the exchange
 * 
 * @author Group Center
 *
 */

public class Order {
		
	
	/**
	 * orderCounter - a int for generation of unique orderNr
	 */
	private static int orderCounter = 0;
	
	
	/**
	 * orderNr - a unique number for identification
	 */
	private final int orderNr;
	
	/**
	 * mode - a TransactionMode defining if its BUY or SELL order
	 */
	private final TransactionMode mode;
	
	/**
	 * currencyCode - String holding which currency the Order was done in
	 */
	private final String currencyCode;
	
	/**
	 * amount - a int holding information of the amount of the order
	 */
	private final int amount;
	
	/**
	 * Constructor
	 * @param mode - a TransactionMode defining if its BUY or SELL order
	 * @param amount - an int  what the order should BUY or SELL.
	 * @param currencyCode the currency the order should deal with.
	 * @throws IllegalArgumentException If the currencyCode is empty or null
	 * or if the amount is 0 or negative
	 */
	public Order(TransactionMode mode, int amount, String currencyCode) {
		
		if(currencyCode == null || currencyCode.isEmpty()) {
			throw new IllegalArgumentException("Currency code missing!");
		}
		else{
			if(amount <= 0) {
				throw new IllegalArgumentException("Amount cant be negative or zero!");
			}
		}
		this.orderNr = ++orderCounter;
		this.mode = mode;
		this.amount = amount;
		this.currencyCode = currencyCode;
	}


	/**
	 * @return the orderNr
	 */
	public int getOrderNr() {
		return orderNr;
	}

	
  
	/**
	 * @return the mode
	 */
	public TransactionMode getMode() {
		return mode;
	}
	
	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	
	
	@Override
	public String toString() {
		return "Order [orderNr=" + orderNr + ", mode=" + mode + ", currencyCode=" + currencyCode + ", amount=" + amount
				+ "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result + orderNr;
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
		Order other = (Order) obj;
		if (amount != other.amount)
			return false;
		if (currencyCode == null) {
			if (other.currencyCode != null)
				return false;
		} else if (!currencyCode.equals(other.currencyCode))
			return false;
		if (mode != other.mode)
			return false;
		if (orderNr != other.orderNr)
			return false;
		return true;
	}
}

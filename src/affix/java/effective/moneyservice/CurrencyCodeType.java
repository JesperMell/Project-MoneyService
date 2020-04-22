package affix.java.effective.moneyservice;

/**
 * CurrencyCodeType defines supported currencies.
 * @author Group Center
 */
enum CurrencyCodeType {
	
	SEK(1), GBP(2), USD(3), CANCEL(0);
	
	private final int currencyCodeChoice;
	
	CurrencyCodeType(int currencyCodeChoice) {
		this.currencyCodeChoice = currencyCodeChoice;
	}
	
	public int getCurrencyCodeChoice() {
		return currencyCodeChoice;
	}
	
	static CurrencyCodeType valueOf(int value) {
		
		for(CurrencyCodeType aCurrencyCodeType : CurrencyCodeType.values()) {
			if (aCurrencyCodeType.getCurrencyCodeChoice() == value)
				return aCurrencyCodeType;
		}
		
		return CANCEL;
	}

}

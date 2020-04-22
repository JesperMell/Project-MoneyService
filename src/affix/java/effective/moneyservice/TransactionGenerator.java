package affix.java.effective.moneyservice;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

class TransactionGenerator {
	@SuppressWarnings("unchecked")
	public static void run() {
		LocalDate start = LocalDate.parse("2020-04-01");
		LocalDate end = LocalDate.parse("2020-04-30");
		
		while(!start.isEqual(end)) {
			MoneyServiceApp.dummyDate = start;
			System.out.println(start);
			
			if(!MoneyServiceApp.configure()) {	
				start = start.plusDays(1);
				continue;		
			}
			MoneyService aExchangeOffice = new ExchangeOffice("THN", MoneyServiceApp.inventoryMap);
			System.out.println(MoneyServiceApp.currencyMap.get("AUD").getExchangeRate());
			
			for (Order o : OrderGenerator.generateOrders(25)) {
				try {
					if (o.getMode() == TransactionMode.BUY)
						aExchangeOffice.buyMoney(o);
					else
						aExchangeOffice.sellMoney(o);
				} catch (IllegalArgumentException | NullPointerException e) {
					System.out.println(o);
				}
			}
	
			aExchangeOffice.shutDownService("Reports/Report_CENTER_" + start + ".ser");
			start = start.plusDays(1);
		}

	}
	
	public static void read() {
		List<Transaction> l = null;

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Reports/Report_CENTER_2020-04-20.ser"))) {
			l = (List<Transaction>) ois.readObject();
		} catch (IOException | ClassNotFoundException ioe) {
			System.out.println("Sorry, could read from file.");
		}
		
		l.forEach(System.out::println);
	}
}

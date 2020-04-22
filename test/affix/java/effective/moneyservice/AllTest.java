package affix.java.effective.moneyservice;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses(
		{
			TestJUnitOrder.class,
			TestJUnitTransaction.class, 
			TestJUnitExchangeOffice.class,
			TestJUnitCurrency.class,
			

		}
		)
public class AllTest {;}

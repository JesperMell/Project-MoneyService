package ya.thn.project.MoneyService;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import affix.java.basic.movie.TestingJUnitJavaMovie;
import affix.java.basic.movie.TestingJUnitJavaMovieList;

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

package tutorial.workbench.table;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author mindbridge
 *
 */
public class VerbosityRating implements Serializable
{

	/**
	 * Method calculateVerbosity.
	 * Please note that this method is relatively slow
	 * It should not be used often, unless for fun :)
	 * @param objLocale
	 * @return int
	 */
	public static int calculateVerbosity(Locale objLocale)
	{
		int nWeekDayVerbosity = calculateWeekDayVerbosity(objLocale);
		int nMonthVerbosity = calculateMonthVerbosity(objLocale);
		return nWeekDayVerbosity + nMonthVerbosity;
	}

	public static int calculateWeekDayVerbosity(Locale objLocale)
	{
		SimpleDateFormat objWeekDay = new SimpleDateFormat("EEEE", objLocale);

		GregorianCalendar objCalendar = new GregorianCalendar();
		objCalendar.set(GregorianCalendar.YEAR, 2000);
		objCalendar.set(GregorianCalendar.MONTH, 0);
		objCalendar.set(GregorianCalendar.DATE, 1);

		int nCount = 0;
		for (int i = 0; i < 7; i++)
		{
			String strWeekDay = objWeekDay.format(objCalendar.getTime());
			nCount += strWeekDay.length();
			objCalendar.add(GregorianCalendar.DATE, 1);
		}

		return nCount;
	}

	public static int calculateMonthVerbosity(Locale objLocale)
	{
		SimpleDateFormat objMonth = new SimpleDateFormat("MMMM", objLocale);

		GregorianCalendar objCalendar = new GregorianCalendar();
		objCalendar.set(GregorianCalendar.YEAR, 2000);
		objCalendar.set(GregorianCalendar.MONTH, 0);
		objCalendar.set(GregorianCalendar.DATE, 1);

		int nCount = 0;
		for (int i = 0; i < 12; i++)
		{
			String strMonth = objMonth.format(objCalendar.getTime());
			nCount += strMonth.length();
			objCalendar.add(GregorianCalendar.MONTH, 1);
		}

		return nCount;
	}

	public static void main(String[] arrArgs)
	{
		int nMax = 0;
		int nMin = 1000;

		System.out.println("Starting");

		Locale[] arrLocales = Locale.getAvailableLocales();
		for (int i = 0; i < arrLocales.length; i++)
		{
			Locale objLocale = arrLocales[i];
			int nRating = calculateVerbosity(objLocale);
			if (nRating > nMax)
				nMax = nRating;
			if (nRating < nMin)
				nMin = nRating;
		}

		System.out.println("Min: " + nMin);
		System.out.println("Max: " + nMax);
	}

}

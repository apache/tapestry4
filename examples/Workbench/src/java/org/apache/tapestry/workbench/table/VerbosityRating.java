//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.workbench.table;

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

/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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

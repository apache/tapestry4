/*
 * Tapestry Web Application Framework
 * Copyright (c) 2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * mailto:hship@users.sf.net
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package net.sf.tapestry.junit;

import junit.framework.*;
import net.sf.tapestry.junit.prop.*;
import net.sf.tapestry.junit.parse.*;

public class TapestrySuite extends TestSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		
		suite.addTest(PropertyHelperTest.suite());
		suite.addTest(TemplateParserTest.suite());
		
		return suite;
	}
}


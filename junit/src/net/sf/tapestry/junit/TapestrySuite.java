//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.junit;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sf.tapestry.junit.parse.SpecificationParserTest;
import net.sf.tapestry.junit.parse.TemplateParserTest;
import net.sf.tapestry.junit.prop.PropertyHelperTest;
import net.sf.tapestry.junit.script.ScriptTest;
import net.sf.tapestry.junit.utils.TestDataSqueezer;
import net.sf.tapestry.junit.utils.TestEnum;
import net.sf.tapestry.junit.valid.ValidSuite;

/**
 *  Master suite of Tapestry tests, combining all other test suites.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class TapestrySuite extends TestSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite();

		suite.addTestSuite(ComponentStringsTest.class);
		suite.addTestSuite(PropertyHelperTest.class);
		suite.addTestSuite(TemplateParserTest.class);
		suite.addTestSuite(SpecificationParserTest.class);
		suite.addTest(ValidSuite.suite());
        suite.addTestSuite(TestEnum.class);
        suite.addTestSuite(TestDataSqueezer.class);
        suite.addTestSuite(ScriptTest.class);

		return suite;
	}

}
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

package tutorial.workbench.fields;

import java.math.BigDecimal;

import tutorial.workbench.WorkbenchValidationDelegate;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;

/**
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.7
 *
 **/

public class Fields extends BasePage
{

    public static final int INT_MIN = 5;
    public static final int INT_MAX = 20;
    public static final double DOUBLE_MIN = 3.14;
    public static final double DOUBLE_MAX = 27.5;
    public static final BigDecimal DECIMAL_MIN = new BigDecimal("2");

    public static final BigDecimal DECIMAL_MAX =
        new BigDecimal("100.123456234563456734563456356734567456784567456784567845675678456785678");

    public static final long LONG_MIN = 6;
    public static final long LONG_MAX = 21;

    public static final int STRING_MIN_LENGTH = 3;

    public void formSubmit(IRequestCycle cycle)
    {

        WorkbenchValidationDelegate delegate = (WorkbenchValidationDelegate) getBeans().getBean("delegate");

        // If no error message, advance to the Results page,

        // otherwise, stay here and show the error message.

        if (!delegate.getHasErrors())
            cycle.setPage("fields.Results");
    }

}
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

package net.sf.tapestry.pages;

import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.util.exception.ExceptionAnalyzer;
import net.sf.tapestry.util.exception.ExceptionDescription;

/**
 *  Default exception reporting page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Exception extends BasePage
{
    private ExceptionDescription[] _exceptions;

    public void detach()
    {
        _exceptions = null;

        super.detach();
    }

    public ExceptionDescription[] getExceptions()
    {
        return _exceptions;
    }

    public void setException(Throwable value)
    {
        ExceptionAnalyzer analyzer;

        analyzer = new ExceptionAnalyzer();

        _exceptions = analyzer.analyze(value);
    }

}
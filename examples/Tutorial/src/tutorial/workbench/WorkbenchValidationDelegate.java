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

package tutorial.workbench;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.form.IFormComponent;
import net.sf.tapestry.valid.ValidationDelegate;

/**
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.6
 *
 **/

public class WorkbenchValidationDelegate extends ValidationDelegate
{
    public void writeAttributes(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        if (isInError())
            writer.attribute("class", "field-error");
    }

    public void writeSuffix(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (isInError())
        {
            writer.print(" ");
            writer.beginEmpty("img");
            writer.attribute("src", "images/workbench/Warning-small.gif");
            writer.attribute("height", 20);
            writer.attribute("width", 20);
        }
    }

    public void writeLabelPrefix(IFormComponent component, IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (isInError(component))
        {
            writer.begin("span");
            writer.attribute("class", "label-error");
        }
    }

    public void writeLabelSuffix(IFormComponent component, IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (isInError(component))
            writer.end(); // <span>
    }
}
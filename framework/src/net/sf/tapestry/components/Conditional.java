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

package net.sf.tapestry.components;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  A conditional element on a page.  Will render its wrapped elements
 *  zero or one times.
 *
 * <table border=1>
 * <tr> 
 *    <th>Parameter</th>
 *    <th>Type</th>
 *	  <th>Direction</th>
 *    <th>Required</th> 
 *    <th>Default</th>
 *    <th>Description</th>
 * </tr>
 *
 * <tr>
 *  <td>condition</td>
 *  <td>boolean</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>false</td>
 *  <td>The condition to be met.  If this value is true, then the wrapped elements
 *  will be rendered.
 *  <p>The determination of true vs. false is made by the method
 *  {@link IBinding#getBoolean()}. </td>
 * </tr>
 *
 * <tr>
 *  <td>invert</td>
 *  <td>boolean</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>false</td>
 *  <td>If true, then the condition is inverted.  This is useful for simulating
 *  an else clause.</td>
 *  </tr>
 * </table>
 *
 * <p>Informal parameters are not allowed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Conditional extends AbstractComponent
{
	private boolean condition;
	private boolean invert;
	
    /**
     *  Renders its wrapped components only if the condition is true (technically,
     *  if condition matches invert).
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
      if (condition != invert)
            renderWrapped(writer, cycle);
    }
    
    public boolean getCondition()
    {
        return condition;
    }

    public void setCondition(boolean condition)
    {
        this.condition = condition;
    }

    public boolean getInvert()
    {
        return invert;
    }

    public void setInvert(boolean invert)
    {
        this.invert = invert;
    }

}
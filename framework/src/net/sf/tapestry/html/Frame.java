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

package net.sf.tapestry.html;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;

/**
 *  Implements a &lt;frame&gt; within a &lt;frameset&gt;.
 * 
 *  <table border=1>
 *  <tr> 
 *    <td>Parameter</td>
 *    <td>Type</td>
 *    <td>Read / Write </td>
 *    <td>Required</td> 
 *    <td>Default</td>
 *    <td>Description</td>
 *  </tr>
 *
 *   <tr>
 *    <td>page</td>
 *    <td>{@link String}</td>
 *    <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>The page to display in the frame.</td>
 *   </tr>
 *   </table>
 *
 *  <p>Informal parameters are allowed, but a body is not.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Frame extends AbstractComponent
{
	private String targetPage;
	    
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        if (cycle.isRewinding())
            return;
            
        IEngine engine = cycle.getEngine();
        IEngineService pageService = engine.getService(IEngineService.PAGE_SERVICE);
        
        Gesture g = pageService.buildGesture(cycle, this, new String[] { targetPage });
        
        writer.beginEmpty("frame");
        writer.attribute("src", g.getURL());
        
        generateAttributes(writer, cycle);
            
        writer.end();
    }


    public String getTargetPage()
    {
        return targetPage;
    }

    public void setTargetPage(String targetPage)
    {
        this.targetPage = targetPage;
    }

}

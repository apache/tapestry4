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

package net.sf.tapestry;

import net.sf.tapestry.link.DirectLink;

/**
 *  Interface that defines classes that may be messaged by the direct
 *  service.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public interface IDirect extends IComponent
{
	/**
	 *  Invoked by the direct service to have the component peform
	 *  the appropriate action.  The {@link DirectLink} component will
	 *  notify its listener.
	 *
	 **/

	public void trigger(IRequestCycle cycle)
		throws RequestCycleException;
        
     /**
      *  Invoked by the direct service to query the component as to
      *  whether it is stateful.  If stateful and no 
      *  {@link HttpSession} is active, then a 
      *  {@link net.sf.tapestry.StaleSessionException} is
      *  thrown by the service.
      * 
      *  @since 2.3
      * 
      **/
     
    public boolean isStateful();
     
    
}
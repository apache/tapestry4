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

import net.sf.tapestry.form.Form;
import net.sf.tapestry.form.Hidden;

/**
 * Defines a listener to an {@link IAction} component, which is way to
 * get behavior when the component's URL is triggered (or the form
 * containing the component is submitted).  Certain form elements ({@link Hidden})
 * also make use of this interface.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 *
 **/

public interface IActionListener
{

    /**
     *  Method invoked by the component (an {@link Action} or {@link
     *  Form}, when its URL is triggered.
     *
     *  @param action The component which was "triggered".
     *  @param cycle The request cycle in which the component was triggered.
     *
     **/

    public void actionTriggered(IComponent component, IRequestCycle cycle)
        throws RequestCycleException;
}
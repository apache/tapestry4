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

package net.sf.tapestry.spec;

import net.sf.tapestry.util.Enum;

/**
 *  An {@link Enum} of the different possible lifecycles for a JavaBean.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.4
 * 
 **/

public class BeanLifecycle extends Enum
{
	/**
	 *  No lifecycle; the bean is created fresh on each reference and not retained.
	 *
	 **/

	public static final BeanLifecycle NONE = new BeanLifecycle("NONE");

	/**
	 * The standard lifecycle; the bean is retained for the
	 * duration of the request cycle and is discarded at the end of the
	 * request cycle.
	 *
	 **/

	public static final BeanLifecycle REQUEST = new BeanLifecycle("REQUEST");

	/**
	 * The bean is created once and reused for the lifespan of the page
	 * containing the component.
	 *
	 **/

	public static final BeanLifecycle PAGE = new BeanLifecycle("PAGE");

    /**
     *  The bean is create and reused until the end of the current render,
     *  at which point it is discarded.
     * 
     *  @since 2.2
     * 
     **/
    
    public static final BeanLifecycle RENDER = new BeanLifecycle("RENDER");
    
	private BeanLifecycle(String name)
	{
		super(name);
	}

}
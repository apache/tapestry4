package com.primix.tapestry;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
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

/**
 * An element that may be asked to render itself to an
 * {@link IResponseWriter} using a {@link IRequestCycle}.
 *
 * <p>This primarily includes {@link IComponent} and {@link IPage},
 * but also extends to other things, such as objects responsible for
 * rendering static HTML text.
 *
 * @author Howard Ship
 * @version $Id$
 */


public interface IRender
{
    /**
     *  The principal rendering/rewinding method.  This will cause
     *  the receiving component to render its top level elements (HTML
     *  text and components).
     *
     *  <p>Renderring and rewinding are the exact same process.  The
     *  same code that renders must be able to restore state by going
     *  through the exact same operations (even though the output is
     *  discarded).
     *
     */
 
    public void render(IResponseWriter writer, IRequestCycle cycle)
        throws RequestCycleException;
}

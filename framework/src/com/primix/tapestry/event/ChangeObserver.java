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
 * May observe changes in an object's properties.  This is a "weak" variation
 * on JavaBean's style bound properties.  It is used when there will be at most
 * a single listener on property changes, and that the listener is not interested
 * in the old value.
 *
 * @author Howard Ship
 * @version $Id$
 */

package com.primix.tapestry.event;

public interface ChangeObserver
{
    /**
     *  Sent when the observed object changes a property.  The event identifies
     *  the object, the property and the new value.
     *
     */
 
    public void observeChange(ObservedChangeEvent event);
}



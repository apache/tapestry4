/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package net.sf.tapestry.util;

/**
 *  An interface implemented by objects that can be
 *  cleaned up, which is to say, can release unneeded
 *  object references.  This is useful for many classes which
 *  provide a pooling or caching function.  Over time, 
 *  some pooled or cached objects may no longer be useful
 *  to keep and can be released. 
 *  references to unneeded objects.
 *  This interface is the bridge between
 *  the {@link JanitorThread} class and an object that
 *  wishes to be periodically told to "clean up".
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.5
 *
 */

public interface ICleanable
{
	/**
	 *  Invoked periodically by the {@link JanitorThread}
	 *  to perform whatever memory cleanups are reasonable.
	 *
	 */

	public void executeCleanup();
}
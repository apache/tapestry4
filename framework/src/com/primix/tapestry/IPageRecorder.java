package com.primix.tapestry;

import java.util.*;
import com.primix.tapestry.event.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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
 *  Defines an object that can observe changes to properties of
 *  a page and its components, store the state of the page between request cycles,
 *  and restore a page's state on a subsequent request cycle.
 *
 *  <p>Concrete implementations of this can store the changes in memory,
 *  as client-side cookies, in a flat file, or in a database.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public interface IPageRecorder extends ChangeObserver
{
    /**
     *  Persists all changes that have been accumulated.  If the recorder
     *  saves change incrementally, this should ensure that all changes have been persisted.
     *
     */
 
    public void commit()
        throws PageRecorderCommitException;

    /**
     *  Returns an <code>Collection</code> of {@link IPageChange} objects that represent
     *  the persistant state of the page.
     *
     */
 
    public Collection getChanges();

	/**
	 *  Returns true if the recorder has any changes for the page.
	 *
	 */
	 
	public boolean getHasChanges();

    /**
     *  Indicates whether the recorder is active and recording
     *  changes.  An inactive recorder ignores changes until it is
     *  activated.
     *
     *  <p>The default is false.
     *
     */
 
    public boolean isActive();

    /**
     *  Returns true if the recorder has observed any changes that have not
     *  been committed to external storage.
     *
     */
 
    public boolean isDirty();

    /**
     *  Rolls back the page to the currently persisted state.
     *
     *  <p>A page recorder can only rollback changes to properties
     *  which have changed * at some point.  This can cause some minor
     *  problems, addressed by * {@link ILifecycle#reset()}.
     */
 
    public void rollback(IPage page);
 
    /**
     *  Activates or deactivates a recorder.  A deactivated recorder ignores
     *  changes to page properties.
     *
     */
 
    public void setActive(boolean value);
}

package com.primix.tapestry;

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
 *  Abstracts the process of loading pages from thier specifications as
 *  well as pooling of pages once loaded.  
 *
 *  <p>If the required page is not available, a page source may use an
 *  instance of {@link IPageLoader} to actually load the
 *  page (and all of its nested components).
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public interface IPageSource
{
    /**
     *  Gets a given page for the application.  This may involve using an previously
     *  loaded page from a pool of available pages, or the page may be loaded as needed.
     *
     */
 
    public IPage getPage(IApplication application, String pageName, IMonitor monitor)
        throws PageLoaderException;

    /**
     *  Invoked after the application is done with the page
     *  (typically, after the response to the client has been sent).
     *  The page is returned to the pool for later reuse.
     *
     */
 
    public void releasePage(IPage page);

    /**
     *  Invoked to have the source clear any internal cache.  This is most often
     *  used when debugging an application.
     *
     */

    public void reset();
}

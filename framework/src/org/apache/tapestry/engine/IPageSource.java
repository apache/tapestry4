/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.engine;

import org.apache.hivemind.ClassResolver;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;

/**
 *  Abstracts the process of loading pages from thier specifications as
 *  well as pooling of pages once loaded.  
 *
 *  <p>If the required page is not available, a page source may use an
 *  instance of {@link IPageLoader} to actually load the
 *  page (and all of its nested components).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface IPageSource
{
    /**
     *  Gets a given page for the engine.  This may involve using a previously
     *  loaded page from a pool of available pages, or the page may be loaded as needed.
     * 
     *  @param cycle the current request cycle
     *  @param pageName the name of the page.  May be qualified with a library id prefix, which
     *  may even be nested. Unqualified names are searched for extensively in the application
     *  namespace, and then in the framework namespace.
     *  @param monitor informed of any page loading activity
     *
     **/

    public IPage getPage(IRequestCycle cycle, String pageName, IMonitor monitor);

    /**
     *  Invoked after the engine is done with the page
     *  (typically, after the response to the client has been sent).
     *  The page is returned to the pool for later reuse.
     *
     **/

    public void releasePage(IPage page);

    /**
     *  Invoked to have the source clear any internal cache.  This is most often
     *  used when debugging an application.
     *
     **/

    public void reset();

    /**
     * 
     *  @since 3.0
     * 
     **/
    
    public ClassResolver getResourceResolver();
        
}
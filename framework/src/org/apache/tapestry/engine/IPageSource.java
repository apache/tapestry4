//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.engine;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceResolver;

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
    
    public IResourceResolver getResourceResolver();
        
}
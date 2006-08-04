// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.html;

import org.apache.hivemind.Resource;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.services.ComponentRenderWorker;
import org.apache.tapestry.util.PageRenderSupportImpl;
import org.apache.tapestry.web.WebResponse;

/**
 * The body of a Tapestry page. This is used since it allows components on the page access to an
 * initialization script (that is written the start, just inside the &lt;body&gt; tag). This is
 * currently used by {@link Rollover}and {@link Script}components. [ <a
 * href="../../../../../ComponentReference/Body.html">Component Reference </a>]
 * 
 * @author Howard Lewis Ship
 */

public abstract class Body extends AbstractComponent implements PageRenderSupport
{
    private PageRenderSupportImpl _pageRenderSupport;

    /**
     * Adds to the script an initialization for the named variable as an Image(), to the given URL.
     * <p>
     * Returns a reference, a string that can be used to represent the preloaded image in a
     * JavaScript function.
     * 
     * @since 1.0.2
     */

    public String getPreloadedImageReference(String URL)
    {
        return _pageRenderSupport.getPreloadedImageReference(URL);
    }

    /**
     * Adds other initialization, in the form of additional JavaScript code to execute from the
     * &lt;body&gt;'s <code>onLoad</code> event handler. The caller is responsible for adding a
     * semicolon (statement terminator). This method will add a newline after the script.
     */

    public void addInitializationScript(String script)
    {
        addInitializationScript(null, script);
    }

    /**
     * {@inheritDoc}
     */
    public void addInitializationScript(IComponent target, String script)
    {
        _pageRenderSupport.addInitializationScript(target, script);
    }
    
    /**
     * Adds additional scripting code to the page. This code will be added to a large block of
     * scripting code at the top of the page (i.e., the before the &lt;body&gt; tag).
     * <p>
     * This is typically used to add some form of JavaScript event handler to a page. For example,
     * the {@link Rollover}component makes use of this.
     * <p>
     * Another way this is invoked is by using the {@link Script}component.
     * <p>
     * The string will be added, as-is, within the &lt;script&gt; block generated by this
     * <code>Body</code> component. The script should <em>not</em> contain HTML comments, those
     * will be supplied by this Body component.
     * <p>
     * A frequent use is to add an initialization function using this method, then cause it to be
     * executed using {@link #addInitializationScript(String)}.
     */

    public void addBodyScript(String script)
    {
        addBodyScript(null, script);
    }

    /**
     * 
     * {@inheritDoc}
     */
    public void addBodyScript(IComponent target, String script)
    {
        _pageRenderSupport.addBodyScript(target, script);
    }
    
    /**
     * Used to include a script from an outside URL (the scriptLocation is a URL, probably obtained
     * from an asset. This adds an &lt;script src="..."&gt; tag before the main &lt;script&gt; tag.
     * The Body component ensures that each URL is included only once.
     * 
     * @since 1.0.5
     */

    public void addExternalScript(Resource scriptLocation)
    {
        addExternalScript(null, scriptLocation);
    }

    /**
     * 
     * {@inheritDoc}
     */
    public void addExternalScript(IComponent target, Resource scriptLocation)
    {
        _pageRenderSupport.addExternalScript(target, scriptLocation);
    }

    protected void prepareForRender(IRequestCycle cycle)
    {
        super.prepareForRender(cycle);

        _pageRenderSupport = new PageRenderSupportImpl(getAssetFactory(), getResponse()
                .getNamespace(), getLocation(), cycle.getResponseBuilder());
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        TapestryUtils.storePageRenderSupport(cycle, this);

        IMarkupWriter nested = writer.getNestedWriter();

        renderBody(nested, cycle);
        
        getEventWorker().renderBody(cycle, this);
        
        // Start the body tag.
        writer.println();
        writer.begin(getElement());
        renderInformalParameters(writer, cycle);

        writer.println();

        // Write the page's scripting. This is included scripts
        // and dynamic JavaScript.

        _pageRenderSupport.writeBodyScript(writer, cycle);

        // Close the nested writer, which dumps its buffered content
        // into its parent.

        nested.close();

        // Any initialization should go at the very end of the document
        // just before the close body tag. Older version of Tapestry
        // would create a window.onload event handler, but this is better
        // (it doesn't have to wait for external images to load).

        _pageRenderSupport.writeInitializationScript(writer);

        writer.end(); // <body>
    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        super.cleanupAfterRender(cycle);

        _pageRenderSupport = null;

        TapestryUtils.removePageRenderSupport(cycle);
    }

    /**
     * Parameter.
     */
    public abstract String getElement();

    /**
     * Injected.
     * 
     * @since 4.0
     */
    public abstract AssetFactory getAssetFactory();

    /**
     * Injected.
     * 
     * @since 4.0
     */

    public abstract WebResponse getResponse();

    /**
     * Injected.
     * @return
     * @since 4.1
     */
    public abstract ComponentRenderWorker getEventWorker();
    
    /** @since 3.0 */

    public String getUniqueString(String baseValue)
    {
        return _pageRenderSupport.getUniqueString(baseValue);
    }

}

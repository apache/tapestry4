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

package org.apache.tapestry.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IScriptProcessor;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.asset.PrivateAsset;
import org.apache.tapestry.resource.ClasspathResourceLocation;
import org.apache.tapestry.util.IdAllocator;

/**
 *  The body of a Tapestry page.  This is used since it allows components on the
 *  page access to an initialization script (that is written the start, just inside
 *  the &lt;body&gt; tag).  This is currently used by {@link Rollover} and {@link Script}
 *  components.
 * 
 *  [<a href="../../../../../ComponentReference/Body.html">Component Reference</a>]
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Body extends AbstractComponent implements IScriptProcessor
{
    // Lines that belong inside the onLoad event handler for the <body> tag.
    private StringBuffer _initializationScript;

    // The writer initially passed to render() ... wrapped elements render
    // into a nested response writer.

    private IMarkupWriter _outerWriter;

    // Any other scripting desired

    private StringBuffer _bodyScript;

    // Contains text lines related to image initializations

    private StringBuffer _imageInitializations;

    /**
     *  Map of URLs to Strings (preloaded image references).
     *
     **/

    private Map _imageMap;

    /**
     *  List of included scripts.  Values are Strings.
     *
     *  @since 1.0.5
     *
     **/

    private List _externalScripts;

    private IdAllocator _idAllocator;

    private static final String ATTRIBUTE_NAME = "org.apache.tapestry.active.Body";

    /**
     *  Tracks a particular preloaded image.
     *
     **/

    /**
     *  Adds to the script an initialization for the named variable as
     *  an Image(), to the given URL.
     *
     *  <p>Returns a reference, a string that can be used to represent
     *  the preloaded image in a JavaScript function.
     *
     *  @since 1.0.2
     **/

    public String getPreloadedImageReference(String URL)
    {
        if (_imageMap == null)
            _imageMap = new HashMap();

        String reference = (String) _imageMap.get(URL);

        if (reference == null)
        {
            int count = _imageMap.size();
            String varName = "tapestry_preload[" + count + "]";
            reference = varName + ".src";

            if (_imageInitializations == null)
                _imageInitializations = new StringBuffer();

            _imageInitializations.append("  ");
            _imageInitializations.append(varName);
            _imageInitializations.append(" = new Image();\n");
            _imageInitializations.append("  ");
            _imageInitializations.append(reference);
            _imageInitializations.append(" = \"");
            _imageInitializations.append(URL);
            _imageInitializations.append("\";\n");

            _imageMap.put(URL, reference);
        }

        return reference;
    }

    /**
     *  Adds other initialization, in the form of additional JavaScript
     *  code to execute from the &lt;body&gt;'s <code>onLoad</code> event
     *  handler.  The caller is responsible for adding a semicolon (statement
     *  terminator).  This method will add a newline after the script.
     *
     **/

    public void addInitializationScript(String script)
    {
        if (_initializationScript == null)
            _initializationScript = new StringBuffer(script.length() + 1);

        _initializationScript.append(script);
        _initializationScript.append('\n');

    }

    /**
     *  Adds additional scripting code to the page.  This code
     *  will be added to a large block of scripting code at the
     *  top of the page (i.e., the before the &lt;body&gt; tag).
     *
     *  <p>This is typically used to add some form of JavaScript
     *  event handler to a page.  For example, the
     *  {@link Rollover} component makes use of this.
     *
     *  <p>Another way this is invoked is by using the
     *  {@link Script} component.
     *
     *  <p>The string will be added, as-is, within
     *  the &lt;script&gt; block generated by this <code>Body</code> component.
     *  The script should <em>not</em> contain HTML comments, those will
     *  be supplied by this Body component.
     *
     *  <p>A frequent use is to add an initialization function using
     *  this method, then cause it to be executed using
     *  {@link #addInitializationScript(String)}.
     *
     **/

    public void addBodyScript(String script)
    {
        if (_bodyScript == null)
            _bodyScript = new StringBuffer(script.length());

        _bodyScript.append(script);
    }

    /**
     *  Used to include a script from an outside URL (the scriptLocation
     *  is a URL, probably obtained from an asset.  This adds
     *  an &lt;script src="..."&gt; tag before the main
     *  &lt;script&gt; tag.  The Body component ensures
     *  that each URL is included only once.
     *
     *  @since 1.0.5
     *
     **/

    public void addExternalScript(IResourceLocation scriptLocation)
    {
        if (_externalScripts == null)
            _externalScripts = new ArrayList();

        if (_externalScripts.contains(scriptLocation))
            return;

        // Alas, this won't give a good ILocation for the actual problem.

        if (!(scriptLocation instanceof ClasspathResourceLocation))
            throw new ApplicationRuntimeException(
                Tapestry.format("Body.include-classpath-script-only", scriptLocation),
                this,
                null,
                null);

        // Record the URL so we don't include it twice.

        _externalScripts.add(scriptLocation);
    }

    /**
     * Writes &lt;script&gt; elements for all the external scripts.
     */
    private void writeExternalScripts(IMarkupWriter writer)
    {
        int count = Tapestry.size(_externalScripts);
        for (int i = 0; i < count; i++)
        {
            ClasspathResourceLocation scriptLocation =
                (ClasspathResourceLocation) _externalScripts.get(i);

            // This is still very awkward!  Should move the code inside PrivateAsset somewhere
            // else, so that an asset does not have to be created to to build the URL.
            PrivateAsset asset = new PrivateAsset(scriptLocation, null);
            String url = asset.buildURL(getPage().getRequestCycle());

            // Note: important to use begin(), not beginEmpty(), because browser don't
            // interpret <script .../> properly.

            writer.begin("script");
            writer.attribute("language", "JavaScript");
            writer.attribute("type", "text/javascript");
            writer.attribute("src", url);
            writer.end();
            writer.println();
        }

    }

    /**
     *  Retrieves the <code>Body</code> that was stored into the
     *  request cycle.  This allows components wrapped by the
     *  <code>Body</code> to locate it and access the services it
     *  provides.
     *
     **/

    public static Body get(IRequestCycle cycle)
    {
        return (Body) cycle.getAttribute(ATTRIBUTE_NAME);
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("Body.may-not-nest"),
                this,
                null,
                null);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        _outerWriter = writer;

        IMarkupWriter nested = writer.getNestedWriter();

        renderBody(nested, cycle);

        // Start the body tag.
        writer.println();
        writer.begin(getElement());
        renderInformalParameters(writer, cycle);

        writer.println();

        // Write the page's scripting.  This is included scripts
        // and dynamic JavaScript, including initialization.

        writeScript(_outerWriter);

        // Close the nested writer, which dumps its buffered content
        // into its parent.

        nested.close();

        writer.end(); // <body>

    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        super.cleanupAfterRender(cycle);

        if (_idAllocator != null)
            _idAllocator.clear();

        if (_imageMap != null)
            _imageMap.clear();

        if (_externalScripts != null)
            _externalScripts.clear();

        if (_initializationScript != null)
            _initializationScript.setLength(0);

        if (_imageInitializations != null)
            _imageInitializations.setLength(0);

        if (_bodyScript != null)
            _bodyScript.setLength(0);

        _outerWriter = null;
    }

    /**
     *  Writes a single large JavaScript block containing:
     *  <ul>
     *  <li>Any image initializations
     *  <li>Any scripting
     *  <li>Any initializations
     *  </ul>
     *
     *  <p>The script is written into a nested markup writer.
     *
     *  <p>If there are any other initializations 
     *  (see {@link #addInitializationScript(String)}),
     *  then a function to execute them is created.
     **/

    protected void writeScript(IMarkupWriter writer)
    {
        if (!Tapestry.isEmpty(_externalScripts))
            writeExternalScripts(writer);

        if (!(any(_initializationScript) || any(_bodyScript) || any(_imageInitializations)))
            return;

        writer.begin("script");
        writer.attribute("language", "JavaScript");
        writer.attribute("type", "text/javascript");
        writer.printRaw("<!--");

        if (any(_imageInitializations))
        {
            writer.printRaw("\n\nvar tapestry_preload = new Array();\n");
            writer.printRaw("if (document.images)\n");
            writer.printRaw("{\n");
            writer.printRaw(_imageInitializations.toString());
            writer.printRaw("}\n");
        }

        if (any(_bodyScript))
        {
            writer.printRaw("\n\n");
            writer.printRaw(_bodyScript.toString());
        }

        if (any(_initializationScript))
        {

            writer.printRaw("\n\n" + "window.onload = function ()\n" + "{\n");

            writer.printRaw(_initializationScript.toString());

            writer.printRaw("}");
        }

        writer.printRaw("\n\n// -->");
        writer.end();
    }

    private boolean any(StringBuffer buffer)
    {
        if (buffer == null)
            return false;

        return buffer.length() > 0;
    }

    public abstract String getElement();

    public abstract void setElement(String element);

    /**
     * Sets the element parameter property to its default, "body".
     * 
     * @since 3.0
     */
    protected void finishLoad()
    {
        setElement("body");
    }

    /** @since 3.0 */

    public String getUniqueString(String baseValue)
    {
        if (_idAllocator == null)
            _idAllocator = new IdAllocator();

        return _idAllocator.allocateId(baseValue);
    }

}
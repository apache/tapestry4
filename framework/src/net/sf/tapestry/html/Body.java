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

package net.sf.tapestry.html;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IPageSource;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.Tapestry;

/**
 *  The body of a Tapestry page.  This is used since it allows components on the
 *  page access to an initialization script (that is written the start, just before
 *  the &lt;body&gt; tag).  This is currently used by {@link Rollover} and {@link Script}
 *  components.
 * 
 *  [<a href="../../../../../ComponentReference/Body.html">Component Reference</a>]
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Body extends AbstractComponent
{
    // Unique id number, used for naming DOM items in the HTML.

    private int _uniqueId;

    // Lines that belong inside the onLoad event handler for the <body> tag.
    private StringBuffer _otherInitialization;

    // The writer initially passed to render() ... wrapped elements render
    // into a nested response writer.

    private IMarkupWriter _outerWriter;

    // Any other scripting desired

    private StringBuffer _otherScript;

    // Contains text lines related to image initializations

    private StringBuffer _imageInitializations;

    /**
     *  Map of URLs to Strings (preloaded image references).
     *
     **/

    private Map _imageMap;

    /**
     *  Set of included scripts.
     *
     *  @since 1.0.5
     *
     **/

    private Set _includedScripts;
    
    /** 
     *  Element name to use.
     * 
     *  @since 2.2
     * 
     **/
    
    private String _element = "body";

    private static final String ATTRIBUTE_NAME = "net.sf.tapestry.active.Body";

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

    public void addOtherInitialization(String script)
    {
        if (_otherInitialization == null)
            _otherInitialization = new StringBuffer(script.length() + 1);

        _otherInitialization.append(script);
        _otherInitialization.append('\n');

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
     *  {@link #addOtherInitialization(String)}.
     *
     **/

    public void addOtherScript(String script)
    {
        if (_otherScript == null)
            _otherScript = new StringBuffer(script.length());

        _otherScript.append(script);
    }

    /**
     *  Used to include a script from an outside URL.  This adds
     *  an &lt;script src="..."&gt; tag before the main
     *  &lt;script&gt; tag.  The Body component ensures
     *  that each URL is included only once.
     *
     *  @since 1.0.5
     *
     **/

    public void includeScript(String URL)
    {
        if (_includedScripts == null)
            _includedScripts = new HashSet();

        if (_includedScripts.contains(URL))
            return;

        _outerWriter.begin("script");
        _outerWriter.attribute("language", "JavaScript");
        _outerWriter.attribute("type", "text/javascript");
        _outerWriter.attribute("src", URL);
        _outerWriter.end();
        _outerWriter.println();

        // Record the URL so we don't include it twice.

        _includedScripts.add(URL);
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

    /**
     *  Returns a String that is unique for the current rendering
     *  of this Body component.  This unique id is often appended to
     *  names to form unique ids for elements and JavaScript functions.
     *
     **/

    public String getUniqueId()
    {
        return Integer.toString(_uniqueId++);
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        IMarkupWriter nested;
        String onLoadName;

        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new RequestCycleException(Tapestry.getString("Body.may-not-nest"), this);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        _uniqueId = 0;
        _outerWriter = writer;

        try
        {
            nested = writer.getNestedWriter();

            renderWrapped(nested, cycle);

            // Write the script (i.e., just before the <body> tag).
            // If an onLoad event handler was needed, its name is
            // returned.

            onLoadName = writeScript();

            // Start the body tag.
            writer.println();
            writer.begin("body");
            generateAttributes(writer, cycle);

            if (onLoadName != null)
                writer.attribute("onLoad", "javascript:" + onLoadName + "();");

            // Close the nested writer, which dumps its buffered content
            // into its parent.

            nested.close();

            writer.end(); // <body>
        }
        finally
        {
            if (_imageMap != null)
                _imageMap.clear();

            if (_includedScripts != null)
                _includedScripts.clear();

            if (_otherInitialization != null)
                _otherInitialization.setLength(0);

            if (_imageInitializations != null)
                _imageInitializations.setLength(0);

            if (_otherScript != null)
                _otherScript.setLength(0);

            _outerWriter = null;
        }

    }

    /**
     *  Writes a single large JavaScript block containing:
     *  <ul>
     *  <li>Any image initializations
     *  <li>Any scripting
     *  <li>Any initializations
     *  </ul>
     *
     *  <p>The script is written just before the &lt;body&gt; tag.
     *
     *  <p>If there are any other initializations 
     *  (see {@link #addOtherInitialization(String)}),
     *  then a function to execute them is created, and its name
     *  is returned.
     **/

    private String writeScript()
    {
        if (!(any(_otherInitialization)
            || any(_otherScript)
            || any(_imageInitializations)))
            return null;

        _outerWriter.begin("script");
        _outerWriter.attribute("language", "JavaScript");
        _outerWriter.printRaw("<!--");

        if (any(_imageInitializations))
        {
            _outerWriter.printRaw("\n\nvar tapestry_preload = new Array();\n");
            _outerWriter.printRaw("if (document.images)\n");
            _outerWriter.printRaw("{\n");
            _outerWriter.printRaw(_imageInitializations.toString());
            _outerWriter.printRaw("}\n");
        }

        if (any(_otherScript))
        {
            _outerWriter.printRaw("\n\n");
            _outerWriter.printRaw(_otherScript.toString());
        }

        String result = null;

        if (any(_otherInitialization))
        {
            result = "tapestry_onLoad";

            _outerWriter.printRaw("\n\n" + "function " + result + "()\n" + "{\n");

            _outerWriter.printRaw(_otherInitialization.toString());

            _outerWriter.printRaw("}");
        }

        _outerWriter.printRaw("\n\n// -->");
        _outerWriter.end();

        return result;
    }

    private boolean any(StringBuffer buffer)
    {
        if (buffer == null)
            return false;

        return buffer.length() > 0;
    }

    /**
     *  Invoked to process the contents of a {@link ScriptSession}.
     *
     *  @since 1.0.5
     *
     **/

    public void process(ScriptSession session)
    {
        String body = session.getBody();

        if (!Tapestry.isNull(body))
            addOtherScript(body);

        String initialization = session.getInitialization();

        if (!Tapestry.isNull(initialization))
            addOtherInitialization(initialization);

        List includes = session.getIncludedScripts();

        if (includes == null || includes.size() == 0)
            return;

        IRequestCycle cycle = getPage().getRequestCycle();
        IEngine engine = getPage().getEngine();
        IPageSource source = engine.getPageSource();

        Iterator i = includes.iterator();
        while (i.hasNext())
        {
            String path = (String) i.next();
            IAsset asset = source.getPrivateAsset(path);
            String URL = asset.buildURL(cycle);

            includeScript(URL);
        }
    }
    
    /** @since 2.2 **/
    
    public String getElement()
    {
        return _element;
    }

    /** @since 2.2 **/
    
    public void setElement(String element)
    {
        _element = element;
    }

}
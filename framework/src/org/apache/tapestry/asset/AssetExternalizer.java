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

package org.apache.tapestry.asset;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.util.StringSplitter;

/**
 *  Responsible for copying assets from the classpath to an external directory that
 *  is visible to the web server. The externalizer is stored inside
 *  the {@link ServletContext} as a named attribute.
 *
 *  <p>The externalizer uses the name <code>org.apache.tapestry.AssetExternalizer.<i>application name</i>
 *  </code>.  It configures itself using two additional 
 *  properties (searching in 
 *  {@link org.apache.tapestry.IEngine#getPropertySource()}.
 *
 *  <table border=1>
 *  <tr> <th>Parameter</th> <th>Description</th> </tr>
 *  <tr valign=top> 
 *		<td><code>org.apache.tapestry.asset.dir</code> </td>
 * 		 <td>The directory to which assets will be copied.</td> </tr>
 *  <tr valign=top>
 *		<td><code>org.apache.tapestry.asset.URL</code> </td>
 *		  <td>The corresponding URL for the asset directory.</td> </tr>
 *	</table>
 *
 * <p>If either of these parameters is null, then no externalization occurs.
 * Private assets will still be available, just less efficiently, as the application
 * will be invoked via its servlet and, ultimately, the {@link AssetService} will need
 * to retrieve the asset.
 *
 * <p>Assets maintain thier directory structure when copied.  For example,
 * an asset with a resource path of <code>/com/skunkworx/Banner.gif</code> would
 * be copied to the file system as <code><i>dir</i>/com/skunkworx/Banner.gif</code> and
 * would have a URL of <code><i>URL</i>/com/skunkworx/Banner.gif</code>.
 *
 * <p>The externalizer will create any directories as needed.
 *
 * <p>The externalizer will not overwrite existing files.  When a new version of the application
 * is deployed with changed assets, there are two deployment stategies:
 * <ul>
 * <li>Delete the existing asset directory and allow the externalizer to recreate and
 * repopulate it.
 * <li>Change the asset directory and URL, allowing the old and new assets to exist
 *  side-by-side.
 * </ul>
 *
 * <p>When using the second approach, it is best to use a directory that has
 * a version number in it, for example, <code>D:/inetpub/assets/0</code> mapped to the URL
 * <code>/assets/0</code>.  When a new version of the application is deployed, the trailing
 * version number is incremented from 0 to 1.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class AssetExternalizer
{
    private static final Log LOG = LogFactory.getLog(AssetExternalizer.class);

    private IResourceResolver _resolver;
    private File _assetDir;
    private String _URL;

    /**
     *  A map from resource path (as a String) to final URL (as a String).
     *
     **/

    private Map _resources = new HashMap();

    private static final int BUFFER_SIZE = 2048;

    protected AssetExternalizer(IRequestCycle cycle)
    {
        _resolver = cycle.getEngine().getResourceResolver();
    
        IPropertySource properties = cycle.getEngine().getPropertySource();


        String directory = properties.getPropertyValue("org.apache.tapestry.asset.dir");

        if (directory == null)
            return;

        _URL = properties.getPropertyValue("org.apache.tapestry.asset.URL");

        if (_URL == null)
            return;

        _assetDir = new File(directory);

        LOG.debug("Initialized with directory " + _assetDir + " mapped to " + _URL);
    }

    protected void externalize(String resourcePath) throws IOException
    {
        String[] path;
        int i;
        File file;
        StringSplitter splitter;
        InputStream in;
        OutputStream out;
        int bytesRead;
        URL inputURL;
        byte[] buffer;

        if (LOG.isDebugEnabled())
            LOG.debug("Externalizing " + resourcePath);

        file = _assetDir;

        // Resources are always split by the unix seperator, even on Win32.

        splitter = new StringSplitter('/');

        path = splitter.splitToArray(resourcePath);

        // The path is expected to start with a leading slash, but the StringSplitter
        // will ignore that leading slash.

        for (i = 0; i < path.length - 1; i++)
        {
            // Doing it this way makes sure the path seperators are right.

            file = new File(file, path[i]);
        }

        // Make sure the directories exist.

        file.mkdirs();

        file = new File(file, path[path.length - 1]);

        // If the file exists, then assume all is well.  This is OK for development,
        // but there may be multithreading (or even multiprocess) race conditions
        // around the creation of the file.

        if (file.exists())
            return;

        // Get the resource and copy it to the file.

        inputURL = _resolver.getResource(resourcePath);
        if (inputURL == null)
            throw new IOException(Tapestry.format("missing-resource", resourcePath));

        in = inputURL.openStream();

        out = new FileOutputStream(file);

        buffer = new byte[BUFFER_SIZE];

        while (true)
        {
            bytesRead = in.read(buffer, 0, BUFFER_SIZE);
            if (bytesRead < 0)
                break;

            out.write(buffer, 0, bytesRead);
        }

        in.close();
        out.close();

        // The file is copied!
    }

    /**
     *  Gets the externalizer singleton for the application.  If it does not already
     *  exist, it is created and stored into the {@link ServletContext}.
     *
     *  <p>Each Tapestry application within a single {@link ServletContext}
     *  will have its own externalizer; they are differentiated by the
     *  application name.
     *
     *  @see org.apache.tapestry.spec.ApplicationSpecification#getName()
     *
     **/

    public static AssetExternalizer get(IRequestCycle cycle)
    {
        HttpServlet servlet = cycle.getRequestContext().getServlet();
        ServletContext context = servlet.getServletContext();

        String servletName = servlet.getServletName();
        
        String attributeName = "org.apache.tapestry.AssetExternalizer:" + servletName;

        AssetExternalizer result = (AssetExternalizer) context.getAttribute(attributeName);

        if (result == null)
        {
            result = new AssetExternalizer(cycle);
            context.setAttribute(attributeName, result);
        }

        return result;
    }

    /**
     *  Gets the URL to a private resource.  If the resource was
     *  previously copied out of the classpath, the previously
     *  generated URL is returned.
     * 
     *  <p>If the asset directory and URL are not configured, then
     *  returns null.
     *
     *  <p>Otherwise, the asset is copied out to the asset directory,
     *  the URL is constructed (and recorded for later) and the URL is
     *  returned.
     *
     *  <p>This method is not explicitly synchronized but should work
     *  multi-threaded.  It synchronizes on the internal
     *  <code>Map</code> used to map resource paths to URLs.
     *
     *  @param resourcePath The full path of the resource within the
     *  classpath.  This is expected to include a leading slash.  For
     *  example: <code>/com/skunkworx/Banner.gif</code>.
     *
     **/

    public String getURL(String resourcePath)
    {
        String result;

        if (_assetDir == null)
            return null;

        synchronized (_resources)
        {
            result = (String) _resources.get(resourcePath);

            if (result != null)
                return result;

            try
            {
                externalize(resourcePath);
            }
            catch (IOException ex)
            {
                throw new ApplicationRuntimeException(
                    Tapestry.format("AssetExternalizer.externalize-failure", resourcePath, _assetDir),
                    ex);
            }

            result = _URL + resourcePath;

            _resources.put(resourcePath, result);

            return result;
        }
    }
}
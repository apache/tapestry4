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

package org.apache.tapestry.asset;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.util.StringSplitter;

/**
 * Implementation of the {@link org.apache.tapestry.asset.AssetExternalizer}service interface.
 * Responsible for copying assets from the classpath to an external directory that is visible to the
 * web server. The externalizer is stored inside the {@link ServletContext}as a named attribute.
 * <p>
 * The externalizer uses the name
 * <code>org.apache.tapestry.AssetExternalizer.<i>application name</i>
 *  </code>. It configures
 * itself using two additional properties (searching in
 * {@link org.apache.tapestry.IEngine#getPropertySource()}. <table border=1>
 * <tr>
 * <th>Parameter</th>
 * <th>Description</th>
 * </tr>
 * <tr valign=top>
 * <td><code>org.apache.tapestry.asset.dir</code></td>
 * <td>The directory to which assets will be copied.</td>
 * </tr>
 * <tr valign=top>
 * <td><code>org.apache.tapestry.asset.URL</code></td>
 * <td>The corresponding URL for the asset directory.</td>
 * </tr>
 * </table>
 * <p>
 * If either of these parameters is null, then no externalization occurs. Private assets will still
 * be available, just less efficiently, as the application will be invoked via its servlet and,
 * ultimately, the {@link AssetService}will need to retrieve the asset.
 * <p>
 * Assets maintain thier directory structure when copied. For example, an asset with a resource path
 * of <code>/com/skunkworx/Banner.gif</code> would be copied to the file system as
 * <code><i>dir</i>/com/skunkworx/Banner.gif</code> and would have a URL of
 * <code><i>URL</i>/com/skunkworx/Banner.gif</code>.
 * <p>
 * The externalizer will create any directories as needed.
 * <p>
 * The externalizer will not overwrite existing files. When a new version of the application is
 * deployed with changed assets, there are two deployment stategies:
 * <ul>
 * <li>Delete the existing asset directory and allow the externalizer to recreate and repopulate
 * it.
 * <li>Change the asset directory and URL, allowing the old and new assets to exist side-by-side.
 * </ul>
 * <p>
 * When using the second approach, it is best to use a directory that has a version number in it,
 * for example, <code>D:/inetpub/assets/0</code> mapped to the URL <code>/assets/0</code>. When
 * a new version of the application is deployed, the trailing version number is incremented from 0
 * to 1.
 * 
 * @author Howard Lewis Ship
 */

public class AssetExternalizerImpl implements AssetExternalizer
{
    /** @since 4.0 */
    private Log _log;

    private ClassResolver _resolver;

    /** @since 4.0 */
    private IPropertySource _propertySource;

    private File _assetDir;

    private String _URL;

    /**
     * A map from resource path (as a String) to final URL (as a String).
     */

    private Map _resources = new HashMap();

    private static final int BUFFER_SIZE = 2048;

    public void initializeService()
    {
        String directory = _propertySource.getPropertyValue("org.apache.tapestry.asset.dir");

        if (directory == null)
            return;

        _URL = _propertySource.getPropertyValue("org.apache.tapestry.asset.URL");

        if (_URL == null)
            return;

        _assetDir = new File(directory);

        _log.debug("Initialized with directory " + _assetDir + " mapped to " + _URL);
    }

    protected void externalize(String resourcePath) throws IOException
    {
        if (_log.isDebugEnabled())
            _log.debug("Externalizing " + resourcePath);

        File file = _assetDir;

        // Resources are always split by the unix seperator, even on Win32.

        StringSplitter splitter = new StringSplitter('/');

        String[] path = splitter.splitToArray(resourcePath);

        // The path is expected to start with a leading slash, but the StringSplitter
        // will ignore that leading slash.

        for (int i = 0; i < path.length - 1; i++)
        {
            // Doing it this way makes sure the path seperators are right.

            file = new File(file, path[i]);
        }

        // Make sure the directories exist.

        file.mkdirs();

        file = new File(file, path[path.length - 1]);

        // If the file exists, then assume all is well. This is OK for development,
        // but there may be multithreading (or even multiprocess) race conditions
        // around the creation of the file.

        if (file.exists())
            return;

        // Get the resource and copy it to the file.

        URL inputURL = _resolver.getResource(resourcePath);
        if (inputURL == null)
            throw new IOException(Tapestry.format("missing-resource", resourcePath));

        InputStream in = null;
        OutputStream out = null;

        try
        {
            in = new BufferedInputStream(inputURL.openStream());

            out = new BufferedOutputStream(new FileOutputStream(file));

            byte[] buffer = new byte[BUFFER_SIZE];

            while (true)
            {
                int bytesRead = in.read(buffer, 0, BUFFER_SIZE);
                if (bytesRead < 0)
                    break;

                out.write(buffer, 0, bytesRead);
            }
        }
        finally
        {
            close(in);
            close(out);
        }

        // The file is copied!
    }

    private void close(InputStream in)
    {
        if (in != null)
            try
            {
                in.close();
            }
            catch (IOException ex)
            {
                // Ignore.
            }
    }

    private void close(OutputStream out)
    {
        if (out != null)

            try
            {
                out.close();
            }
            catch (IOException ex)
            {
                // Ignore
            }
    }

    /**
     * Gets the URL to a private resource. If the resource was previously copied out of the
     * classpath, the previously generated URL is returned.
     * <p>
     * If the asset directory and URL are not configured, then returns null.
     * <p>
     * Otherwise, the asset is copied out to the asset directory, the URL is constructed (and
     * recorded for later) and the URL is returned.
     * <p>
     * This method is not explicitly synchronized but should work multi-threaded. It synchronizes on
     * the internal <code>Map</code> used to map resource paths to URLs.
     * 
     * @param resourcePath
     *            The full path of the resource within the classpath. This is expected to include a
     *            leading slash. For example: <code>/com/skunkworx/Banner.gif</code>.
     */

    public String getURL(String resourcePath)
    {
        if (_assetDir == null)
            return null;

        synchronized (_resources)
        {
            String result = (String) _resources.get(resourcePath);

            if (result != null)
                return result;

            try
            {
                externalize(resourcePath);
            }
            catch (IOException ex)
            {
                throw new ApplicationRuntimeException(Tapestry.format(
                        "AssetExternalizer.externalize-failure",
                        resourcePath,
                        _assetDir), ex);
            }

            result = _URL + resourcePath;

            _resources.put(resourcePath, result);

            return result;
        }
    }

    /** @since 4.0 */
    public void setLog(Log log)
    {
        _log = log;
    }

    /** @since 4.0 */
    public void setClassResolver(ClassResolver resolver)
    {
        _resolver = resolver;
    }

    /** since 4.0 */
    public void setPropertySource(IPropertySource propertySource)
    {
        _propertySource = propertySource;
    }
}
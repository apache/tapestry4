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

package org.apache.tapestry.asset;

import java.io.InputStream;
import java.net.URL;

import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 *  An implementation of {@link IAsset} for localizable assets within
 *  the JVM's classpath.
 *
 *  <p>The localization code here is largely cut-and-paste from
 *  {@link ContextAsset}.
 *
 *  @author Howard Ship
 *  @version $Id$
 * 
 **/

public class PrivateAsset extends AbstractAsset
{

    private AssetExternalizer _externalizer;

    public PrivateAsset(ClasspathResource resourceLocation, Location location)
    {
        super(resourceLocation, location);
    }

    /**
     *  Gets the localized version of the resource.  Build
     *  the URL for the resource.  If possible, the application's
     *  {@link AssetExternalizer} is located, to copy the resource to
     *  a directory visible to the web server.
     *
     **/

    public String buildURL(IRequestCycle cycle)
    {
        if (_externalizer == null)
            _externalizer = AssetExternalizer.get(cycle);

        String path = getResourceLocation().getPath();

        String externalURL = _externalizer.getURL(path);

        if (externalURL != null)
            return externalURL;

        // Otherwise, the service is responsible for dynamically retrieving the
        // resource.

        String[] parameters = new String[] { path };

        IEngineService service = cycle.getEngine().getService(Tapestry.ASSET_SERVICE);

        ILink link = service.getLink(cycle, null, parameters);

        return link.getURL();
    }

    public InputStream getResourceAsStream(IRequestCycle cycle)
    {
        Resource location = getResourceLocation();

        try
        {
            URL url = location.getResourceURL();

            return url.openStream();
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("PrivateAsset.resource-missing", location),
                ex);
        }
    }

}
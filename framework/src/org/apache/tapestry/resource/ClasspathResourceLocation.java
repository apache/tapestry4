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

package org.apache.tapestry.resource;

import java.net.URL;
import java.util.Locale;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.util.LocalizedResource;
import org.apache.tapestry.util.LocalizedResourceFinder;

/**
 *  Implementation of {@link org.apache.tapestry.IResourceLocation}
 *  for resources found within the classpath.
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class ClasspathResourceLocation extends AbstractResourceLocation
{
    private IResourceResolver _resolver;

    public ClasspathResourceLocation(IResourceResolver resolver, String path)
    {
        this(resolver, path, null);
    }

    public ClasspathResourceLocation(IResourceResolver resolver, String path, Locale locale)
    {
        super(path, locale);

        _resolver = resolver;
    }

    /**
     *  Locates the localization of the
     *  resource using {@link org.apache.tapestry.util.LocalizedResourceFinder}
     * 
     **/

    public IResourceLocation getLocalization(Locale locale)
    {
        String path = getPath();
        LocalizedResourceFinder finder = new LocalizedResourceFinder(_resolver);

        LocalizedResource localizedResource = finder.resolve(path, locale);

        if (localizedResource == null)
            return null;

        String localizedPath = localizedResource.getResourcePath();
        Locale pathLocale = localizedResource.getResourceLocale();

        if (localizedPath == null)
            return null;

        if (path.equals(localizedPath))
            return this;

        return new ClasspathResourceLocation(_resolver, localizedPath, pathLocale);
    }

    /**
     *  Invokes {@link IResourceResolver#getResource(String)}
     * 
     **/

    public URL getResourceURL()
    {
        return _resolver.getResource(getPath());
    }

    public String toString()
    {
        return "classpath:" + getPath();
    }

    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder(4783, 23);

        builder.append(getPath());

        return builder.toHashCode();
    }

    protected IResourceLocation buildNewResourceLocation(String path)
    {
        return new ClasspathResourceLocation(_resolver, path);
    }

}

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

package org.apache.tapestry;

import java.io.InputStream;

/**
 *  Representation of a asset (GIF, JPEG, etc.) that may be owned by a
 *  {@link IComponent}.
 *
 *  <p>Assets may be completely external (i.e., on some other web site), 
 *  contained by the {@link javax.servlet.ServletContext},  
 *  or stored somewhere in the classpath.
 *
 *  <p>In the latter two cases, the resource may be localized.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public interface IAsset extends ILocatable
{
    /**
     *  Returns a URL for the asset, ready to be inserted into the output HTML.
     *  If the asset can be localized, the localized version (matching the
     *  {@link java.util.Locale} of the current {@link IPage page}) is returned.
     * 
     *  @throws ApplicationRuntimeException if the asset does not exist.
     *
     **/

    public String buildURL(IRequestCycle cycle);

    /**
     *  Accesses the localized version of the resource (if possible) and returns it as
     *  an input stream.  A version of the resource localized to the
     *  current {@link IPage page} is returned.
     * 
     *  @throws ApplicationRuntimeException if the asset does not exist, or
     *  can't be read.
     *
     **/

    public InputStream getResourceAsStream(IRequestCycle cycle);
    
    /**
     *  Returns the underlying location of the asset.
     * 
     **/
    
    public IResourceLocation getResourceLocation();
}
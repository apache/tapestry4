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

import java.net.URL;
import java.util.Locale;

/**
 *  Describes the location of a resource, such as a specification
 *  or template.  Resources may be located within the classpath,
 *  or within the Web context root or somewhere else entirely.
 * 
 *  <p>
 *  Resources may be either base or localized.  A localized
 *  version of a base resource may be obtained
 *  via {@link #getLocalization(Locale)}.
 * 
 *  <p>
 *  Resource locations are used as Map keys, they must 
 *  implement {@link java.lang.Object#hashCode()} and
 *  {@link java.lang.Object#equals(java.lang.Object)}
 *  properly.
 * 
 *  <p>
 *  Resource locations are valid even if the corresponding
 *  resource <i>doesn't exist</i>.  To verify if a localization
 *  actually exists, use {@link #getResourceURL()}, which returns
 *  null if the resource doesn't exist.  {@link #getLocalization(Locale)}
 *  returns only real resource locations, where the resource exists.
 * 
 *  <p>
 *  Folders must be represented with a trailing slash.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public interface IResourceLocation
{
    /**
     *  Returns a URL for the resource.
     * 
     *  @return the URL for the resource if it exists, or null if it does not
     * 
     **/
    
    public URL getResourceURL();
    
    /**
     *  Returns the file name portion of the resource location.
     * 
     **/
    
    public String getName();
    
    /**
     *  Returns a localized version of this resource (or this resource, if no
     *  appropriate localization is found).  Should only be invoked
     *  on a base resource.
     * 
     *  @param locale to localize for, or null for no localization.
     *  @return a localized version of this resource, of null if the resource
     *  itself does not exist.
     * 
     **/
    
    public IResourceLocation getLocalization(Locale locale);
    
    /**
     *  Returns at a relative location to this resource.  
     *  The new resource may or may not exist; this can be determined
     *  via {@link #getResourceURL()}.
     * 
     *  @param name name of new resource, possibly as a relative path, or
     *  as an absolute path (starting with a slash).
     * 
     **/
    
    public IResourceLocation getRelativeLocation(String name);
    
    /**
     *  Returns the path that represents the resource.  This should 
     *  only be used when the type of resource is known.
     * 
     **/
    
    public String getPath();

    /**
     *  Returns the locale for which this resource has been localized 
     *  or null if the resource has not been localized. This should 
     *  only be used when the type of resource is known.
     * 
     *  This locale is the same or more general than the locale for which localization 
     *  was requested. For example, if the requested locale was en_US, but only the file 
     *  Home_en was found, this locale returned would be en. 
     **/
    
    public Locale getLocale();
}

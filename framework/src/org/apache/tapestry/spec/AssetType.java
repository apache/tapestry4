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

package org.apache.tapestry.spec;

import org.apache.commons.lang.enum.Enum;

/**
 *  Defines the types of assets.
 *
 *  @see IAssetSpecification
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public final class AssetType extends Enum
{
    /**
     *  An external resource.
     *
     **/

    public static final AssetType EXTERNAL = new AssetType("EXTERNAL");

    /**
     *  A resource visible to the {@link javax.servlet.ServletContext}.
     *
     **/

    public static final AssetType CONTEXT = new AssetType("CONTEXT");

    /**
     *  An internal resource visible only on the classpath.  Typically,
     *  a resource package in a WAR or JAR file alongside the classes.
     *
     **/

    public static final AssetType PRIVATE = new AssetType("PRIVATE");

    private AssetType(String name)
    {
        super(name);
    }

}
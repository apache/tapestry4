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

import org.apache.tapestry.ILocatable;
import org.apache.tapestry.ILocationHolder;
import org.apache.tapestry.util.IPropertyHolder;

/**
 *  Defines an internal, external or private asset.
 * 
 * @author glongman@intelligentworks.com
 * @version $Id$
 */
public interface IAssetSpecification extends IPropertyHolder, ILocationHolder, ILocatable
{
    /**
     *  Returns the base path for the asset.  This may be interpreted as a URL, relative URL
     *  or the path to a resource, depending on the type of asset.
     *
     **/
    public abstract String getPath();
    public abstract AssetType getType();
    /** @since 3.0 **/
    public abstract void setPath(String path);
    /** @since 3.0 **/
    public abstract void setType(AssetType type);
}
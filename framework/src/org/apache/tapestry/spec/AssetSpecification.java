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

/**
 *  Defines an internal, external or private asset.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class AssetSpecification extends LocatablePropertyHolder implements IAssetSpecification
{
    private AssetType type;
    protected String path;

    /**
     *  Returns the base path for the asset.  This may be interpreted as a URL, relative URL
     *  or the path to a resource, depending on the type of asset.
     *
     **/

    public String getPath()
    {
        return path;
    }

    public AssetType getType()
    {
        return type;
    }

    /** @since 3.0 **/

    public void setPath(String path)
    {
        this.path = path;
    }

    /** @since 3.0 **/

    public void setType(AssetType type)
    {
        this.type = type;
    }

}
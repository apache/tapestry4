// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.describe;

import org.apache.tapestry.IRender;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class RenderableAdapterFactoryImpl implements RenderableAdapterFactory
{
    private RenderableAdapter _adapter;

    /**
     * Returns a new instance of {@link RenderBridge}.
     */
    public IRender getRenderableAdaptor(Object object)
    {
        return new RenderBridge(object, _adapter);
    }

    public void setAdapter(RenderableAdapter adapter)
    {
        _adapter = adapter;
    }
}
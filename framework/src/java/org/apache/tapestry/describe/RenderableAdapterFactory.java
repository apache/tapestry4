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
 * A source of light-weight objects that can render a particular object. This is a more pure
 * implementation of the GoF Adapter pattern.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface RenderableAdapterFactory
{
    /**
     * Returns an object that can render the input object. Simple implementations will just just
     * output a string; complex implementations may output complex markup.
     */
    public IRender getRenderableAdaptor(Object object);
}
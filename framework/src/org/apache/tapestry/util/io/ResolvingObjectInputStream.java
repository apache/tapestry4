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

package org.apache.tapestry.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import org.apache.tapestry.IResourceResolver;

/**
 *  Specialized subclass of {@link java.io.ObjectInputStream}
 *  that knows how to resolve classes with a non-default
 *  class loader (represented by an instance of
 *  {@link org.apache.tapestry.IResourceResolver}).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class ResolvingObjectInputStream extends ObjectInputStream
{
    private IResourceResolver _resolver;

    public ResolvingObjectInputStream(IResourceResolver resolver, InputStream input) throws IOException
    {
        super(input);

        _resolver = resolver;
    }

    /**
     *  Overrides the default implementation to
     *  have the resource resolver find the class.
     * 
     **/

    protected Class resolveClass(ObjectStreamClass v) throws IOException, ClassNotFoundException
    {
        return _resolver.findClass(v.getName());
    }
}
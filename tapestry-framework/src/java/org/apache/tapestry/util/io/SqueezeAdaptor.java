// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.tapestry.services.DataSqueezer;

/**
 * Interface which defines a class used to convert data for a specific Java type into a String
 * format (squeeze it), or convert from a String back into a Java type (unsqueeze).
 * <p>
 * This interface is somewhat misnamed; this is more of the GoF Strategy pattern than GoF Adaptor
 * pattern.
 * 
 * @author Howard Lewis Ship
 */

public interface SqueezeAdaptor
{
    /**
     * Returns one or more characters, each of which will be a prefix for this adaptor.
     */

    String getPrefix();

    /**
     * Returns the class (or interface) which can be encoded by this adaptor.
     */

    Class getDataClass();

    /**
     * Converts the data object into a String.
     * 
     */

    String squeeze(DataSqueezer squeezer, Object data);

    /**
     * Converts a String back into an appropriate object.
     */

    Object unsqueeze(DataSqueezer squeezer, String string);
}

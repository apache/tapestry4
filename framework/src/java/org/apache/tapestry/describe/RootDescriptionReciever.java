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

/**
 * Extended version of {@link org.apache.tapestry.describe.DescriptionReceiver} that adds the
 * ability to describe the root object, and to finish the description of an object (allowing the
 * same receiver to be used to describe several objects).
 * 
 * @author Howard M. Lewis Ship
 */
public interface RootDescriptionReciever extends DescriptionReceiver
{

    /**
     * Describes the object, using a {@link DescribableStrategy}, or just the object's toString()
     * if there is no strategy for the object. Automatically invokes {@link #finishUp()} when done.
     * 
     * @param object
     *            to be described, which may be null
     */
    void describe(Object object);

    /**
     * Invoked after one object has been fully described. Ends a &lt;table&gt;, if one has been
     * started, and resets the receiver to begin a new object.
     */
    void finishUp();

}
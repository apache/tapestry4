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

import java.util.Collection;

/**
 * An object that is provided with a description of another object. The receiver will format this
 * information.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface DescriptionReceiver
{
    /**
     * Invoke to describe another object instead of the current object.
     */

    public void describeAlternate(Object alternate);

    /**
     * Provides a title for the object; usually the object's class name.
     * 
     * @throws IllegalStateException
     *             if called more than once (for the same object)
     */
    public void title(String title);

    /**
     * Starts a new sub-section within the description. A description may have any number of
     * sections (but sections do not nest). A second title is only emitted when the firstproperty
     * within the section is emitted.
     * 
     * @throws IllegalStateException
     *             if called before invoking {@link #title(String)}.
     */
    public void section(String section);

    /**
     * Emits a key/value pair, describing a property of the object. The value will itself be
     * described. This method is overridden for scalar property types.
     * 
     * @throws IllegalStateException
     *             if called before invoking {@link #title(String)}
     */
    public void property(String key, Object value);

    public void property(String key, boolean value);

    public void property(String key, byte value);

    public void property(String key, short value);

    public void property(String key, int value);

    public void property(String key, long value);

    public void property(String key, float value);

    public void property(String key, double value);

    public void property(String key, char value);

    /**
     * Emits a list of values for the key. Each value will be described. Emits nothing if the array
     * is null.
     */
    public void array(String key, Object[] values);

    /**
     * As with {@link #array(String, Object[])}, but the values are in a collection (which may be
     * null, to emit nothing).
     */

    public void collection(String key, Collection values);
}
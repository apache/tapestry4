// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.components;

/**
 * An interface for converting an objects to their primary keys and back. Typically used to
 * determine how to store a given object as a hidden value when rendering a form.
 * <p>
 * This interface is used by the {@link org.apache.tapestry.components.ForBean For component}. When
 * a primary key converter is available, it is used during the render, and as part of the rewind
 * phase that processes the form submission.
 * <p>
 * During rendering, {@link #getPrimaryKey(Object)} is invoked for each value. This method is
 * invoked just before the For's body is rendered. The resulting primary key is written into the
 * client as a hidden form field.
 * <p>
 * Likewise, during rewind, {@link #getValue(Object)} is invoked for each key, to get back the same
 * (or equivalent) object. Again, the method is invoked just before the For's body is rendered.
 * <p>
 * The {@link org.apache.tapestry.util.DefaultPrimaryKeyConverter} uses this relationship between a
 * For component and its primary key converter to track what the current value being rendered or
 * rewound is.
 * 
 * @author mb
 * @since 4.0
 */
public interface IPrimaryKeyConverter
{
    /**
     * Returns the primary key of the given value
     * 
     * @param objValue
     *            the value for which a primary key needs to be extracted
     * @return the primary key of the value
     */
    Object getPrimaryKey(Object value);

    /**
     * Returns the value corresponding the given primary key
     * 
     * @param objPrimaryKey
     *            the primary key for which a value needs to be generated
     * @return the generated value corresponding to the given primary key
     */
    Object getValue(Object primaryKey);

}

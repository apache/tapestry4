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

package org.apache.tapestry.coerce;

/**
 * Converts a value (possibly null) to an alternate data type; typically from String to boolean or a
 * number type.
 * <p>
 * Typically, a ValueConverter will select a particular
 * {@link org.apache.tapestry.coerce.TypeConverter}to perform the conversion. The
 * {@link org.apache.tapestry.coerce.ValueConverterImpl}implementation also makes use of built-in
 * {@link java.beans.PropertyEditor}s.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface ValueConverter
{
    /**
     * Performs a conversion of a value to a particular type.
     * 
     * @param value
     *            The value to be converted (may be null)
     * @param desiredType
     *            the type that will be converted to
     * @returns the value converted to the indicated type. May return the input value if it is
     *          already assignable to the desiredType.
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the value can not be converted
     */
    public Object coerceValue(Object value, Class desiredType);
}
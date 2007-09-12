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

package org.apache.tapestry.coerce;

import org.apache.tapestry.TapestryUtils;

/**
 * Converts a string to a {@link org.apache.tapestry.form.IPropertySelectionModel}. The string is
 * broken apart at commas into terms. Each term consists of a label (displayed to the user) and a
 * value (used as a client-side value and as the server-side property) seperated by an equals sign;
 * if the equals sign is omitted, then the value is the same as the label.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public final class StringToPropertySelectionModelConverter implements TypeConverter
{
    public Object convertValue(Object value)
    {
        String input = (String) value;

        String[] terms = TapestryUtils.split(input);

        return new StringConvertedPropertySelectionModel(terms);
    }

}

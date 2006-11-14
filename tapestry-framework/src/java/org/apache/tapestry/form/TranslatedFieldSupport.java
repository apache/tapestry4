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

package org.apache.tapestry.form;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.ValidatorException;

/**
 * 
 */
public interface TranslatedFieldSupport
{

    /**
     * Formats the field translation.
     * @param field
     * @param object
     */
    String format(TranslatedField field, Object object);

    /**
     * Parses the field value.
     * @param field
     * @param text
     * 
     * @throws ValidatorException
     */
    Object parse(TranslatedField field, String text)
        throws ValidatorException;

    /**
     * Renders any contributions.
     * @param field
     * @param writer
     * @param cycle
     */
    void renderContributions(TranslatedField field,
            IMarkupWriter writer, IRequestCycle cycle);
}

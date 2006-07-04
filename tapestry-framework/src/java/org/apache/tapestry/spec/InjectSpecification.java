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

package org.apache.tapestry.spec;

import org.apache.hivemind.LocationHolder;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface InjectSpecification extends LocationHolder
{

    /**
     * Returns the name of the property to be created.
     */

    String getProperty();

    void setProperty(String property);

    /**
     * Returns the type of injection. Different injection types interpret the
     * {@link #getObject() object property} differently. Ultimately, the type is
     * used to select the correct
     * {@link org.apache.tapestry.enhance.InjectEnhancementWorker}.
     */

    String getType();

    void setType(String type);

    /**
     * Returns the string used to indentify a particular object.
     */

    String getObject();

    void setObject(String object);
}

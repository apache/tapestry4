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

import java.util.Map;

import org.apache.hivemind.LocationHolder;
import org.apache.tapestry.util.IPropertyHolder;

/**
 * Defines an "extension", which is much like a helper bean, but is part of a
 * library or application specification (and has the same lifecycle as the
 * application).
 * 
 * @author glongman@intelligentworks.com
 */
public interface IExtensionSpecification extends IPropertyHolder,
        LocationHolder
{

    String getClassName();

    void setClassName(String className);

    void addConfiguration(String propertyName, String value);

    /**
     * Returns an immutable Map of the configuration; keyed on property name,
     * with values as properties to assign.
     */
    Map getConfiguration();

    /**
     * Invoked to instantiate an instance of the extension and return it. It
     * also configures properties of the extension.
     */
    Object instantiateExtension();

    /**
     * Returns true if the extensions should be instantiated immediately after
     * the containing {@link org.apache.tapestry.spec.LibrarySpecification}if
     * parsed. Non-immediate extensions are instantiated only as needed.
     */
    boolean isImmediate();

    void setImmediate(boolean immediate);
}

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

package org.apache.tapestry.services.impl;

import org.apache.hivemind.Location;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.schema.Translator;

/**
 * An encapsulation of an invocation of
 * {@link org.apache.hivemind.schema.Translator#translate(org.apache.hivemind.internal.Module, java.lang.Class, java.lang.String, org.apache.hivemind.Location)},
 * allowing the actual invocation (and all the object creation, etc., that entails) to be deferred,
 * or even avoided all together.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class DeferredObjectImpl implements DeferredObject
{
    private final Module _module;

    private final Translator _objectTranslator;

    private final String _objectReference;

    private final Location _location;

    private Object _object;

    public DeferredObjectImpl(final Translator objectTranslator, final Module module,
            final String objectReference, final Location location)
    {
        _objectTranslator = objectTranslator;
        _module = module;
        _objectReference = objectReference;
        _location = location;
    }

    public synchronized Object getObject()
    {
        if (_object == null)
            _object = _objectTranslator.translate(
                    _module,
                    Object.class,
                    _objectReference,
                    _location);

        return _object;
    }

    public Location getLocation()
    {
        return _location;
    }
}
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

package org.apache.tapestry.binding;

import org.apache.hivemind.Defense;
import org.apache.hivemind.Location;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * Base class for {@link IBinding}implementations.
 * 
 * @author Howard Lewis Ship
 */

public abstract class AbstractBinding implements IBinding
{
    /** @since 3.1 */

    private final String _description;

    /** @since 3.1 */

    private final ValueConverter _valueConverter;

    /** @since 3.0 */

    private final Location _location;

    /** @since 3.0 */

    protected AbstractBinding(String description, ValueConverter valueConverter, Location location)
    {
        Defense.notNull(description, "description");
        Defense.notNull(valueConverter, "valueConverter");

        _description = description;
        _valueConverter = valueConverter;
        _location = location;
    }

    public Location getLocation()
    {
        return _location;
    }

    /**
     * Overridden in subclasses that are not invariant.
     * 
     * @throws ReadOnlyBindingException
     *             always.
     */

    public void setObject(Object value)
    {
        throw createReadOnlyBindingException(this);
    }

    /**
     * Default implementation: returns true.
     * 
     * @since 2.0.3
     */

    public boolean isInvariant()
    {
        return true;
    }

    public Object getObject(Class type)
    {
        Defense.notNull(type, "type");

        Object raw = getObject();

        try
        {
            return _valueConverter.coerceValue(raw, type);
        }
        catch (Exception ex)
        {
            String message = BindingMessages.convertObjectError(this, ex);

            throw new BindingException(message, getComponent(), _location, this, ex);
        }
    }

    /**
     * Returns the component to which this binding is connected; this is currently only used when
     * building certain exceptions. This implementation returns null.
     * 
     * @since 3.1
     */

    public Object getComponent()
    {
        return null;
    }

    /** @since 3.0 */

    protected BindingException createReadOnlyBindingException(IBinding binding)
    {
        return new BindingException(BindingMessages.readOnlyBinding(binding), binding);
    }

    /** @since 3.1 */

    public String getDescription()
    {
        return _description;
    }
}
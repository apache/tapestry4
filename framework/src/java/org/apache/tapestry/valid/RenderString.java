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

package org.apache.tapestry.valid;

import java.io.Serializable;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;

/**
 * A wrapper around {@link String}&nbsp;that allows the String to be renderred. This is primarily
 * used to present error messages.
 * 
 * @author Howard Lewis Ship
 */

public class RenderString implements IRender, Serializable
{
    private static final long serialVersionUID = 6215074338439140780L;

    private String _string;

    private boolean _raw = false;

    public RenderString(String string)
    {
        _string = string;
    }

    /**
     * @param string
     *            the string to render
     * @param raw
     *            if true, the String is rendered as-is, with no filtering. If false (the default),
     *            the String is filtered.
     */

    public RenderString(String string, boolean raw)
    {
        _string = string;
        _raw = raw;
    }

    /**
     * Renders the String to the writer. Does nothing if the string is null. If raw is true, uses
     * {@link IMarkupWriter#printRaw(String)}, otherwise {@link IMarkupWriter#print(String)}.
     */

    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (_string == null)
            return;

        writer.print(_string, _raw);
    }

    public String getString()
    {
        return _string;
    }

    public boolean isRaw()
    {
        return _raw;
    }

    /**
     * Returns the string that would be rendered. This is part of the contract for error renderers
     * used with validation ... must provide a user-presentable toString() that does not include any
     * markup.
     */

    public String toString()
    {
        return _string;
    }
}
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

package org.apache.tapestry.util.xml;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.LocationImpl;
import org.xml.sax.SAXParseException;

/**
 * Exception thrown if there is any kind of error parsing the an XML document.
 * 
 * @see org.apache.tapestry.parse.SpecificationParser
 * @author Howard Lewis Ship
 * @since 0.2.10
 */

public class DocumentParseException extends ApplicationRuntimeException
{
    public DocumentParseException(String message, Throwable rootCause)
    {
        super(message, null, rootCause);
    }

    public DocumentParseException(String message, Location location, Throwable rootCause)
    {
        super(message, location, rootCause);
    }

    public DocumentParseException(String message, Resource resource, SAXParseException rootCause)
    {
        this(message, resource == null ? null : new LocationImpl(resource, rootCause
                .getLineNumber(), rootCause.getColumnNumber()), rootCause);
    }

    public DocumentParseException(String message, Resource resource, Throwable rootCause)
    {
        this(message, resource == null ? null : new LocationImpl(resource), rootCause);
    }
}
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
 *  Exception thrown if there is any kind of error parsing the
 *  an XML document. 
 *
 *  @see org.apache.tapestry.parse.SpecificationParser
 *
 *  @author Howard Lewis Ship
 *  @since 0.2.10
 *
 **/

public class DocumentParseException extends ApplicationRuntimeException
{
    private Resource _documentLocation;

    public DocumentParseException(String message, Throwable rootCause)
    {
        this(message, null, null, rootCause);
    }

    public DocumentParseException(String message, Resource documentLocation)
    {
        this(message, documentLocation, null);
    }

    public DocumentParseException(String message, Resource documentLocation, Throwable rootCause)
    {
        this(message, documentLocation, null, rootCause);
    }

    public DocumentParseException(String message, Location location, Throwable rootCause)
    {
        this(message, location == null ? null : location.getResource(), location, rootCause);
    }

    private DocumentParseException(
        String message,
        Resource documentLocation,
        Location location,
        Throwable rootCause)
    {
        super(message, null, location, rootCause);

        _documentLocation = documentLocation;
    }

    public DocumentParseException(
        String message,
        Resource documentLocation,
        SAXParseException rootCause)
    {
        this(
            message,
            documentLocation,
            rootCause == null
                || documentLocation == null
                    ? null
                    : new LocationImpl(
                        documentLocation,
                        rootCause.getLineNumber(),
                        rootCause.getColumnNumber()),
            rootCause);
    }

    public DocumentParseException(String message)
    {
        this(message, null, null, null);
    }

    public DocumentParseException(Throwable rootCause)
    {
        this(rootCause.getMessage(), rootCause);
    }

    public DocumentParseException(SAXParseException rootCause)
    {
        this(rootCause.getMessage(), (Throwable) rootCause);
    }

    public Resource getDocumentLocation()
    {
        return _documentLocation;
    }
}
//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.parse;

import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.xml.DocumentParseException;
import org.xml.sax.Attributes;

/**
 *  Rule used to validate the public id of the document, ensuring that
 *  it is not null, and that it matches an expected value.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class ValidatePublicIdRule extends BaseDocumentRule
{
    private String[] _publicIds;
    private String _rootElement;

    public ValidatePublicIdRule(String[] publicIds, String rootElement)
    {
        _publicIds = publicIds;
        _rootElement = rootElement;
    }

    public void startDocument(String namespace, String name, Attributes attributes)
        throws Exception
    {
        SpecificationDigester digester = getDigester();
        IResourceLocation location = digester.getResourceLocation();

        String publicId = digester.getPublicId();

		// publicId will never be null because we use a validating parser.

        for (int i = 0; i < _publicIds.length; i++)
        {
            if (_publicIds[i].equals(publicId))
            {

                if (!name.equals(_rootElement))
                    throw new DocumentParseException(
                        Tapestry.format(
                            "AbstractDocumentParser.incorrect-document-type",
                            _rootElement,
                            name),
                        location);

                return;
            }

        }

        throw new DocumentParseException(
            Tapestry.format("AbstractDocumentParser.unknown-public-id", location, publicId),
            location);
    }

}

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

import org.apache.hivemind.Locatable;
import org.apache.hivemind.Location;

/**
 * Exception thrown if there is any kind of error validating a string during document parsing
 * 
 * @author Geoffrey Longman
 * @since 2.2
 */

public class InvalidStringException extends DocumentParseException implements Locatable
{
    private static final long serialVersionUID = 8199998733585358584L;
    
	private String _invalidString;

    public InvalidStringException(String message, String invalidString, Location location)
    {
        super(message, location, null);

        _invalidString = invalidString;
    }

    public String getInvalidString()
    {
        return _invalidString;
    }
}
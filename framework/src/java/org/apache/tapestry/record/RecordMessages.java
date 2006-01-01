// Copyright 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.record;

import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IComponent;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
class RecordMessages
{
    private final static MessageFormatter _formatter = new MessageFormatter(RecordMessages.class);

    static String unknownPersistenceStrategy(String name)
    {
        return _formatter.format("unknown-persistence-strategy", name);
    }

    static String missingPropertySpecification(String propertyName, IComponent component)
    {
        return _formatter.format("missing-property-specification", propertyName, component
                .getExtendedId(), component.getSpecification().getSpecificationLocation());
    }

    static String recorderLocked(String propertyName, IComponent component)
    {
        return _formatter.format("recorder-locked", propertyName, component.getExtendedId());
    }

    static String decodeFailure(Throwable cause)
    {
        return _formatter.format("decode-failure", cause);
    }

    static String encodeFailure(Throwable cause)
    {
        return _formatter.format("encode-failure", cause);
    }

    static String unknownPrefix(String prefix)
    {
        return _formatter.format("unknown-prefix", prefix);
    }
}
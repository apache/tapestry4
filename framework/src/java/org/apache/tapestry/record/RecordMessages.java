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

import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IComponent;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
final class RecordMessages
{

    private final static Messages MESSAGES = new MessageFormatter(RecordMessages.class);

    /** @since 4.1 */
    private RecordMessages()
    {
    }

    static String unknownPersistenceStrategy(String name)
    {
        return MESSAGES.format("unknown-persistence-strategy", name);
    }

    static String missingPropertySpecification(String propertyName, IComponent component)
    {
        return MESSAGES.format("missing-property-specification", propertyName, component.getExtendedId(), component
                .getSpecification().getSpecificationLocation());
    }

    static String recorderLocked(String propertyName, IComponent component)
    {
        return MESSAGES.format("recorder-locked", propertyName, component.getExtendedId());
    }

    static String decodeFailure(Throwable cause)
    {
        return MESSAGES.format("decode-failure", cause);
    }

    static String encodeFailure(Throwable cause)
    {
        return MESSAGES.format("encode-failure", cause);
    }

    static String unknownPrefix(String prefix)
    {
        return MESSAGES.format("unknown-prefix", prefix);
    }
}

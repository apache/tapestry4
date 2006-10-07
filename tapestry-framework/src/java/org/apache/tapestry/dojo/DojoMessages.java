// Copyright May 16, 2006 The Apache Software Foundation
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

package org.apache.tapestry.dojo;

import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author andyhot
 * @since 4.1
 */
public final class DojoMessages
{
    protected static final MessageFormatter _formatter = new MessageFormatter(DojoMessages.class);
    
    /* defeat instantiation */
    private DojoMessages() { }
    
    public static String mustUseValidJsonInParameter(String parameterName)
    {
        return _formatter.format("must-use-valid-json-in-parameter", parameterName);
    }

}

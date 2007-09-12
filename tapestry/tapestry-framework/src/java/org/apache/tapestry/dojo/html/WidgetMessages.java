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
package org.apache.tapestry.dojo.html;

import org.apache.hivemind.impl.MessageFormatter;


/**
 * Provides error message formatting functions for the dojo html 
 * widget component package.
 */
public final class WidgetMessages
{
    protected static final MessageFormatter _formatter = new MessageFormatter(WidgetMessages.class);
    
    /* defeat instantiation */
    private WidgetMessages() { }
    
    public static String invalidTextMode(String value)
    {
        return _formatter.format("invalid-text-mode", value);
    }
}

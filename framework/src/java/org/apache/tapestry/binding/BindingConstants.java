// Copyright 2005 The Apache Software Foundation
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

/**
 * Constant values related to bindings.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class BindingConstants
{
    /**
     * Prefix used when the value should be interpreted as an OGNL expression.
     */

    public static final String OGNL_PREFIX = "ognl";

    /**
     * Prefix used when the value should be interpeted as a key for a localized message (of the
     * component's message catalog).
     */
    public static final String MESSAGE_PREFIX = "message";
    
    /**
     * Prefix used to ensure that the value is interpreted as a literal
     * string.
     * 
     */
    
    public static final String LITERAL_PREFIX = "literal";
}
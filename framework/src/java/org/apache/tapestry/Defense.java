// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry;

/**
 * Simple utilities for defensive programming.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public final class Defense
{
    private Defense()
    {
        // Prevent instantiation
    }

    /**
     * Check for null parameter when not allowed.
     * 
     * @throws NullPointerException
     *             if parameter is null (the message indicates the name of the parameter).
     */
    public static void notNull(Object parameter, String parameterName)
    {
        if (parameter == null)
            throw new NullPointerException(TapestryMessages.paramNotNull(parameterName));
    }
}
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

package org.apache.tapestry.describe;

/**
 * An object that can describe its properties to a
 * {@link org.apache.tapestry.describe.DescriptionReceiver}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface Describable
{
    /**
     * Invoked to have the receiver describe itself (largely in terms of properties and values). If
     * the caller does not invoke any methods on the receiver, then the a default description of the
     * caller will be used (based on <code>toString()</code>.
     */
    public void describeTo(DescriptionReceiver receiver);
}
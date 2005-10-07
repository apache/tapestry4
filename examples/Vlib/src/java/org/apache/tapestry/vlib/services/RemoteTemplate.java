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

package org.apache.tapestry.vlib.services;

/**
 * Used to execute a {@link org.apache.tapestry.vlib.services.RemoteCallback}.
 * 
 * @author Howard M. Lewis Ship
 */
public interface RemoteTemplate
{
    /**
     * Executes the callback within a retry loop; consumes any RemoteExceptions.
     * 
     * @param callback
     *            the callback to execute
     * @param errorMessage
     *            used when the callback fails
     * @return the return value of the callback
     */
    Object doRemote(RemoteCallback callback, String errorMessage);
}

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
 * Threaded (i.e. per-request) service that stores lifecycle operations and actions; used to control
 * logout behavior.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface ApplicationLifecycle
{
    /**
     * Logs the user out of the systems; sets a flag that causes the session to be discarded at the
     * end of the request.
     */
    void logout();

    /**
     * If true, then the session (if it exists) should be discarded at the end of the request.
     */
    boolean getDiscardSession();
}

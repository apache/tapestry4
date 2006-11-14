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

package org.apache.tapestry.web;

import org.apache.tapestry.describe.Describable;

/**
 * Primarily concerned with maintaining server-side state as attributes.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface WebSession extends AttributeHolder, Describable
{
    /**
     * Returns a unique string identifier used to identify the session. This value is provided by
     * the container, and is typically incorporated into URLs, or stored as a HTTP cookie.
     * 
     * @see org.apache.tapestry.web.WebResponse#encodeURL(String) .
     */
    String getId();

    /**
     * Returns true if the client does not yet know about the session or if the client chooses not
     * to join the session.
     */
    boolean isNew();

    /**
     * Invalidates this session then unbinds any objects bound to it.
     * 
     * @throws IllegalStateException
     *             if the session is already invalidated.
     */

    void invalidate();
    
    /**
     * Returns the time when this session was created, measured in milliseconds 
     * since midnight January 1, 1970 GMT.
     * 
     * @return a long specifying when this session was created, 
     *          expressed in milliseconds since 1/1/1970 GMT
     */
    
    long getCreationTime();
    
    /**
     * Returns the last time the client sent a request associated with this session, as 
     * the number of milliseconds since midnight January 1, 1970 GMT, and marked by the 
     * time the container recieved the request.
     *
     * <p> Actions that your application takes, such as getting or setting a value associated 
     *  with the session, do not affect the access time.</p>
     * 
     * @return a long  representing the last time the client sent a request associated with 
     *          this session, expressed in milliseconds since 1/1/1970 GMT
     */
    
    long getLastAccessedTime();
    
    /**
     * Returns the maximum time interval, in seconds, that the servlet container will 
     * keep this session open between client accesses. After this interval, the servlet 
     * container will invalidate the session. The maximum time interval can be set with 
     * the setMaxInactiveInterval method. A negative time indicates the session should 
     * never timeout.
     * 
     * @return an integer specifying the number of seconds this session 
     *         remains open between client requests
     */
    
    int getMaxInactiveInterval();
    
    /**
     * Specifies the time, in seconds, between client requests before the servlet container 
     * will invalidate this session. A negative time indicates the session should never timeout.
     * 
     * @param interval - An integer specifying the number of seconds
     */
    
    void setMaxInactiveInterval(int interval);
}

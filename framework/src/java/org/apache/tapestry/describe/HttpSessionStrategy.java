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

import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.apache.tapestry.web.WebUtils;

/**
 * Describes an {@link javax.servlet.http.HttpSession}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class HttpSessionStrategy implements DescribableStrategy
{

    public void describeObject(Object object, DescriptionReceiver receiver)
    {
        HttpSession session = (HttpSession) object;

        receiver.title("HttpSession");

        receiver.property("creationTime", new Date(session.getCreationTime()));
        receiver.property("id", session.getId());
        receiver.property("lastAccessedTime", new Date(session.getLastAccessedTime()));
        receiver.property("maxInactiveInterval", session.getMaxInactiveInterval());
        receiver.property("new", session.isNew());

        receiver.section("Attributes");
        Iterator i = WebUtils.toSortedList(session.getAttributeNames()).iterator();
        while (i.hasNext())
        {
            String key = (String) i.next();
            receiver.property(key, session.getAttribute(key));
        }
    }

}
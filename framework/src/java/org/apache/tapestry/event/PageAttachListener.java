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

package org.apache.tapestry.event;

import java.util.EventListener;

/**
 * Listener interface notified when a page is attached to the current request. Notifications occur
 * after persistent page properties have been restored, but before the page is activated (if it is
 * activated). This allows the properties of the page to be updated to reflect the current user
 * session.
 * 
 * @see org.apache.tapestry.event.PageDetachListener
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface PageAttachListener extends EventListener
{
    public void pageAttached(PageEvent event);
}
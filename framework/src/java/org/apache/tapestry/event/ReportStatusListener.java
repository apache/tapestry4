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
 * Implemented by (typically) a HiveMind service implementation, and registerred with the
 * {@link org.apache.tapestry.describe.ReportStatusHub} so that the service can report its status
 * (in terms of properties and values).
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface ReportStatusListener extends EventListener
{
    void reportStatus(ReportStatusEvent event);
}

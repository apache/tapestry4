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

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.event.ReportStatusListener;

/**
 * Service for collecting together status information across the framework; service implementations
 * implement the {@link org.apache.tapestry.event.ReportStatusListener} interface and register
 * themselves as listeners here. When desired, the {@link #fireReportStatus(IMarkupWriter)} event
 * will invoke the listener method on each registered object.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface ReportStatusHub
{
    /**
     * Adds the listener; listeners will be invoked in the order in which they are added.
     * <strong>Note: only service implementation that are singletons should be report status
     * listeners. Threaded or pooled implementations should not be added; or should be careful to
     * add and remove themselves from the hub directly.</strong>
     * 
     * @param listener
     */
    public void addReportStatusListener(ReportStatusListener listener);

    public void removeReportStatusListener(ReportStatusListener listener);

    /**
     * Generates an HTML status report by invoking
     * {@link ReportStatusListener#reportStatus(ReportStatusEvent)} on each registered listener.
     * 
     * @param writer
     *            a markup writer to send the report to.
     */
    public void fireReportStatus(IMarkupWriter writer);
}

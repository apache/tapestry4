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

package org.apache.tapestry.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Filter interface used for the commands in the
 * <code>tapestry.portlet.RenderRequestServicerPipeline</code> configuration.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 * @see org.apache.tapestry.portlet.RenderRequestServicer
 */
public interface RenderRequestServicerFilter
{
    public void service(RenderRequest request, RenderResponse response,
            RenderRequestServicer servicer) throws IOException, PortletException;
}
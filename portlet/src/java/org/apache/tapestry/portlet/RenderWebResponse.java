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
import java.io.PrintWriter;

import javax.portlet.RenderResponse;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.util.ContentType;

/**
 * Wrapper around {@link javax.portlet.RenderResponse}&nbsp;to adapt it as
 * {@link org.apache.tapestry.portlet.PortletWebResponse}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class RenderWebResponse extends PortletWebResponse
{
    private final RenderResponse _renderResponse;

    public RenderWebResponse(RenderResponse renderResponse)
    {
        super(renderResponse);

        _renderResponse = renderResponse;
    }

    public void reset()
    {
        _renderResponse.reset();
    }

    public PrintWriter getPrintWriter(ContentType contentType) throws IOException
    {
        Defense.notNull(contentType, "contentType");

        _renderResponse.setContentType(contentType.toString());

        return _renderResponse.getWriter();
    }

    public String getNamespace()
    {
        return _renderResponse.getNamespace();
    }
}
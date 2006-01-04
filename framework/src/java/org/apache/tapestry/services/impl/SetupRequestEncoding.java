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

package org.apache.tapestry.services.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.services.ServletRequestServicer;
import org.apache.tapestry.services.ServletRequestServicerFilter;

/**
 * Analyzes the incoming request to set the correct output encoding.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class SetupRequestEncoding implements ServletRequestServicerFilter
{
    private boolean _skipSet;

    private String _outputEncoding;

    public void service(HttpServletRequest request, HttpServletResponse response,
            ServletRequestServicer servicer) throws IOException, ServletException
    {
        if (!_skipSet)
        {
            String encoding = request.getCharacterEncoding();

            if (encoding == null)
                setRequestEncodingToOutputEncoding(request);
        }

        // Next in chain

        servicer.service(request, response);
    }

    private void setRequestEncodingToOutputEncoding(HttpServletRequest request)
    {
        try
        {
            // We compile against the 2.3 API but allow
            // the code to execute against the 2.2 In the
            // later case, we can get some interesting errors.

            request.setCharacterEncoding(_outputEncoding);
        }
        catch (UnsupportedEncodingException ex)
        {
            throw new ApplicationRuntimeException(
                    ImplMessages.invalidEncoding(_outputEncoding, ex), ex);
        }
        catch (NoSuchMethodError ex)
        {
            _skipSet = true;
        }
        catch (AbstractMethodError ex)
        {
            _skipSet = true;
        }
    }

    public void setOutputEncoding(String outputEncoding)
    {
        _outputEncoding = outputEncoding;
    }
}
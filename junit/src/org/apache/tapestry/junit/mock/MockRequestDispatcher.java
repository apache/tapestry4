//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.junit.mock;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *  Used to enable mock testing of internal request forwarding.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/
public class MockRequestDispatcher implements RequestDispatcher
{	
	private String _resourcePath;
	
    public MockRequestDispatcher(String resourcePath)
    {
    	_resourcePath = resourcePath;
    }


    public void forward(ServletRequest request, ServletResponse response)
        throws ServletException, IOException
    {
    	if (_resourcePath.endsWith("/FAIL_SERVLET"))
    		throw new ServletException("Test-directed ServletException from RequestDispatcher forward().");
    	
    	// For testing purposes, assume we only forward to static HTML files.
    	
     	
    	InputStream in = new FileInputStream(_resourcePath);

   		response.setContentType("test/html");
    	
    	OutputStream out =	response.getOutputStream();

    	
    	byte[] buffer = new byte[1000];
    	
    	while (true)
    	{
    		int length = in.read(buffer);
    		
    		if (length < 0)
    			break;
    			
    		out.write(buffer, 0, length);
    	}
    	
    	in.close();
    	out.close();
    }

    public void include(ServletRequest request, ServletResponse response)
        throws ServletException, IOException
    {
    	throw new ServletException("MockRequestDispatcher.include() not supported.");
    }

}

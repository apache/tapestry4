// Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.services;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter interface for {@link org.apache.tapestry.services.RequestServicer}.
 * Allows objects and services to "plug into" the pipeline.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface RequestServicerFilter
{
    public void service(
        HttpServletRequest request,
        HttpServletResponse response,
        RequestServicer servicer)
        throws IOException, ServletException;
}

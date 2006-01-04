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

package org.apache.tapestry.vlib;

import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.services.BookQuerySource;
import org.apache.tapestry.vlib.services.ErrorPresenter;
import org.apache.tapestry.vlib.services.RemoteTemplate;

/**
 * Interface for pages and components that need to use the
 * {@link org.apache.tapestry.vlib.ejb.IOperations} EJB. Acts as a mixin, using annotations to
 * inject supporting properties into the page or component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface OperationsUser
{
    @InjectObject("service:vlib.Operations")
    public abstract IOperations getOperations();

    @InjectObject("service:vlib.RemoteTemplate")
    public abstract RemoteTemplate getRemoteTemplate();

    @InjectObject("service:vlib.ErrorPresenter")
    public abstract ErrorPresenter getErrorPresenter();

    @InjectObject("service:vlib.BookQuerySource")
    public abstract BookQuerySource getBookQuerySource();
}

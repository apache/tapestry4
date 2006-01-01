// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.vlib.pages;

import org.apache.tapestry.pages.Exception;
import org.apache.tapestry.vlib.IErrorProperty;

/**
 * A customized exception page; in non-debug mode, it omits displays the main exception display.
 * 
 * @author Howard Lewis Ship
 * @since 2.2
 */

public abstract class ApplicationUnavailable extends Exception implements IErrorProperty
{
    @Override
    public void setException(Throwable value)
    {
        super.setException(value);

        String message = value.getMessage();

        if (message == null)
            message = value.getClass().getName();

        setError(message);
    }
}

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

package org.apache.tapestry.wml;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.apache.tapestry.IMarkupWriter;

/**
 *  Subclass of {@link org.apache.tapestry.wml.WMLWriter} that is nested.  A nested writer
 *  buffers its output, then inserts it into its parent writer when it is
 *  closed.
 *
 *  @version $Id$
 *  @author David Solis
 *  @since 0.2.9
 * 
 **/

public class NestedWMLWriter extends WMLWriter
{
    private IMarkupWriter _parent;
    private CharArrayWriter _internalBuffer;

    public NestedWMLWriter(IMarkupWriter parent)
    {
        super(parent.getContentType());

        _parent = parent;

        _internalBuffer = new CharArrayWriter();

       setWriter(new PrintWriter(_internalBuffer));
    }

    /**
     *  Invokes the {@link WMLWriter#close() super-class
     *  implementation}, then gets the data accumulated in the
     *  internal buffer and provides it to the containing writer using
     *  {@link IMarkupWriter#printRaw(char[], int, int)}.
     *
     **/

    public void close()
    {
        super.close();

        char[] data = _internalBuffer.toCharArray();

        _parent.printRaw(data, 0, data.length);

        _internalBuffer = null;
        _parent = null;
    }
}
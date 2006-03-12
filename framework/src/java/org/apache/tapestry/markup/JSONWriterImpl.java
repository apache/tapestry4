// Copyright 2006 The Apache Software Foundation
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
package org.apache.tapestry.markup;

import java.io.PrintWriter;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IJSONWriter;

/**
 * Implementation of {@link IJSONWriter}.
 * 
 * @author jkuhnert
 */
public class JSONWriterImpl implements IJSONWriter
{   
    /** Outputstream writer. */
    protected PrintWriter _writer;
    
    /**
     * For every {@link #start()} call there must be a corresponding
     * end to close out the block, this counter is used to keep
     * track of these calls.
     */
    private int _stackCount = 0;
    
    /**
     * Tracks items written to a block, all items must be delimited by
     * "," characters, so this keeps track of started lists.
     */
    private boolean _listOpen = false;
    
    /**
     * Creates a new instance that will write all content to 
     * the specified {@link PrintWriter}.
     * 
     * @param writer The outputstream to write to.
     */
    public JSONWriterImpl(PrintWriter writer)
    {
        Defense.notNull(writer, "writer");
        
        _writer = writer;
        begin(); // all JSON responses must be contained by { } 
    }
    
    /**
     * {@inheritDoc}
     */
    public void write(Object key, Object value)
    {
        if (_listOpen)
            _writer.write(",");
        
        _writer.write("\"" + key + "\"" + ":" + "\"" + value + "\"");
        _listOpen = true;
    }
    
    /** 
     * {@inheritDoc}
     */
    public void begin()
    {
        _writer.print(BEGIN);
        
        _stackCount++;
    }
    
    /** 
     * {@inheritDoc}
     */
    public void end()
    {
        _writer.print(END);
        
        _stackCount--;
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public void close()
    {
        while (_stackCount != 0)
            end();
        
        _writer.flush();
        _writer.close();
    }
    
    /**
     * The outputstream being used to write this 
     * instance's content.
     * @return 
     */
    protected PrintWriter getWriter()
    {
        return _writer;
    }
}

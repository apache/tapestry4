package net.sf.tapestry.wml;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import net.sf.tapestry.IMarkupWriter;

/**
 *  Subclass of {@link net.sf.tapestry.wml.WMLWriter} that is nested.  A nested writer
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
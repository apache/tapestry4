package net.sf.tapestry.inspector;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.util.io.BinaryDumpOutputStream;

/**
 *  Component of the {@link Inspector} page used to display
 *  the properties of the {@link IEngine} as well as a serialized view of it.
 *  Also, the {@link net.sf.tapestry.RequestContext} is dumped out.
 *
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class ShowEngine extends BaseComponent implements PageDetachListener
{
    private byte[] serializedEngine;

    /**
     *  Registers with the page as a {@link PageDetachListener}.
     *
     *  @since 1.0.5
     *
     **/

    protected void finishLoad()
    {
        getPage().addPageDetachListener(this);
    }

    public void pageDetached(PageEvent event)
    {
        serializedEngine = null;
    }


    /**
     *  Workaround for OGNL limitation --- OGNL can't dereference
     *  past class instances.
     * 
     *  @since 2.2
     * 
     **/
    
    public String getEngineClassName()
    {
        return getPage().getEngine().getClass().getName();
    }
        
    private byte[] getSerializedEngine()
    {
        if (serializedEngine == null)
            buildSerializedEngine();

        return serializedEngine;
    }

    private void buildSerializedEngine()
    {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;

        try
        {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);

            // Write the application object to the stream.

            oos.writeObject(getPage().getEngine());

            // Extract the application as an array of bytes.

            serializedEngine = bos.toByteArray();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("ShowEngine.could-not-serialize"),
                ex);
        }
        finally
        {
            close(oos);
            close(bos);
        }

        // It would be nice to deserialize the application object now, but in
        // practice, that fails due to class loader problems.
    }

    private void close(OutputStream stream)
    {
        if (stream == null)
            return;

        try
        {
            stream.close();
        }
        catch (IOException ex)
        {
            // Ignore.
        }
    }

    public int getEngineByteCount()
    {
        return getSerializedEngine().length;
    }

    public IRender getEngineDumpDelegate()
    {
        return new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle)
                throws RequestCycleException
            {
                dumpSerializedEngine(writer);
            }
        };
    }

    private void dumpSerializedEngine(IMarkupWriter responseWriter)
    {
        CharArrayWriter writer = null;
        BinaryDumpOutputStream bos = null;

        try
        {
            // Because IReponseWriter doesn't implement the
            // java.io.Writer interface, we have to buffer this
            // stuff then pack it in all at once.  Kind of a waste!

            writer = new CharArrayWriter();

            bos = new BinaryDumpOutputStream(writer);
            bos.setBytesPerLine(32);

            bos.write(getSerializedEngine());
            bos.close();

            responseWriter.print(writer.toString());
        }
        catch (IOException ex)
        {
            // Ignore.
        }
        finally
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                }
                catch (IOException ex)
                {
                    // Ignore.
                }
            }

            if (writer != null)
            {
                writer.reset();
                writer.close();
            }
        }
    }

}
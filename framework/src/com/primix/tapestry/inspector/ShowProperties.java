package com.primix.tapestry.inspector;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.components.*;
import com.primix.foundation.io.*;
import java.io.*;
import java.util.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */


/**
 *  Component of the {@link Inspector} page used to display
 *  the persisent properties of the page, and the serialized view
 *  of the application object.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class ShowProperties extends BaseComponent
implements ILifecycle
{
	private List properties;
	private IPageChange change;
	private IPage inspectedPage;
    private byte[] serializedApplication;

	public void cleanupAfterRender(IRequestCycle cycle)
	{
		properties = null;
		change = null;
		inspectedPage = null;
        serializedApplication = null;
	}

	private void buildProperties()
	{
		Inspector inspector;
		IPageRecorder recorder;
		IApplication application;
		
		inspector = (Inspector)page;
		
		inspectedPage = inspector.getInspectedPage();

		application = inspectedPage.getApplication();
		recorder = application.getPageRecorder(inspectedPage.getName());
		
		if (recorder.getHasChanges())
			properties = new ArrayList(recorder.getChanges());
	}

	/**
	 *  Returns a {@link List} of {@link IPageChange} objects.
     *
     * <p>Sort order is not defined.
	 *
	 */
	 
	public List getProperties()
	{
		if (properties == null)
			buildProperties();
			
		return properties;
	}
	
	public void setChange(IPageChange value)
	{
		change = value;
	}
	
	public IPageChange getChange()
	{
		return change;
	}
	
	/**
	 *  Returns true if the current change has a non-null component path.
	 *
	 */
	 
	public boolean getEnableComponentLink()
	{
		return change.getComponentPath() != null;
	}
	
	/**
	 *  Returns the name of the value's class, if the value is non-null.
	 *
	 */
	 
	public String getValueClassName()
	{
		Object value;
		
		value = change.getNewValue();
		
		if (value == null)
			return "<null>";
		
		return value.getClass().getName();
	}	

    private byte[] getSerializedApplication()
    {
        if (serializedApplication == null)
            buildSerializedApplication();

        return serializedApplication;

    }

    private void buildSerializedApplication()
    {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;

        try
        {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);

            // Write the application object to the stream.

            oos.writeObject(page.getApplication());

            // Extract the application as an array of bytes.

            serializedApplication = bos.toByteArray();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException("Could not serialize the application object.", ex);
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

    public int getApplicationByteCount()
    {
        return getSerializedApplication().length;
    }

    public IRender getApplicationDumpDelegate()
    {
        return new IRender()
        {
            public void render(IResponseWriter writer, IRequestCycle cycle)
            throws RequestCycleException
            {
                dumpSerializedApplication(writer);
            }
        };
    }

    private void dumpSerializedApplication(IResponseWriter responseWriter)
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

            bos.write(getSerializedApplication());
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

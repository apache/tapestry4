/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package net.sf.tapestry.inspector;

import com.primix.tapestry.event.*;
import com.primix.tapestry.*;
import com.primix.tapestry.util.io.*;

import net.sf.tapestry.*;
import net.sf.tapestry.event.*;
import net.sf.tapestry.util.io.*;

import java.io.*;
import java.util.*;

/**
 *  Component of the {@link Inspector} page used to display
 *  the properties of the {@link IEngine} as well as a serialized view of it.
 *  Also, the {@link RequestContext} is dumped out.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class ShowEngine extends BaseComponent implements PageDetachListener
{
	private byte[] serializedEngine;

	/**
	 *  Registers with the page as a {@link PageDetachListener}.
	 *
	 *  @since 1.0.5
	 *
	 */

	protected void finishLoad()
	{
		page.addPageDetachListener(this);
	}

	public void pageDetached(PageEvent event)
	{
		serializedEngine = null;
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

			oos.writeObject(page.getEngine());

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

	/**
	 *  Invokes {@link IEngine#isResetServiceEnabled()} and inverts the result.
	 *
	 */

	public boolean isResetServiceDisabled()
	{
		IEngine engine = page.getEngine();

		return !engine.isResetServiceEnabled();
	}

}
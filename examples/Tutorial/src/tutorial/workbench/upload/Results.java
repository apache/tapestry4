//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package tutorial.workbench.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IUploadFile;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.util.io.BinaryDumpOutputStream;

/**
 *  Displays the uploaded file as a hexadecimal dump.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Results extends BasePage
{
	private IUploadFile file;
	private String fileDump;
	
	public void detach()
	{
		file = null;
		fileDump = null;
		
		super.detach();
	}
	
	public IUploadFile getFile()
	{
		return file;
	}
	
	public String getFileDump()
	{
		return fileDump;
	}
	
	public void activate(IUploadFile file, boolean showAscii, int bytesPerLine, IRequestCycle cycle)
	throws RequestCycleException
	{
		this.file = file;
		
		StringWriter writer = null;
		BinaryDumpOutputStream out = null;
		InputStream in = null;
		
		try
		{
			in = file.getStream();
			
			writer = new StringWriter();
			out = new BinaryDumpOutputStream(writer);
			
			out.setShowAscii(showAscii);
			out.setBytesPerLine(bytesPerLine)			;
			
			byte[] buffer = new byte[1000];
			
			while (true)
			{
				int length = in.read(buffer);
				
				if (length < 0)
					break;
					
				out.write(buffer, 0, length);				
			}
			
			
			in.close();
			in = null;
			
			out.close();
			out = null;
			
			fileDump = writer.getBuffer().toString();
			
			writer.close();
			writer = null;		
		}
		catch (IOException ex)
		{
			throw new RequestCycleException("Unable to display file.", this, ex);
		}
		finally
		{
			close(in);
			close(out);
			close(writer);
		}
	
		cycle.setPage(this);
	}

	private void close(InputStream stream)
	{
		if (stream != null)
		{
			try 
			{
				stream.close();
			}
			catch (IOException ex) 
			{
			}
		}
	}

	private void close(OutputStream stream)
	{
		if (stream != null)
		{
			try 
			{
				stream.close();
			}
			catch (IOException ex) 
			{
			}
		}
	}	
	
	private void close(Writer writer)
	{
		if (writer != null)
		{
			try 
			{
				writer.close();
			}
			catch (IOException ex) 
			{
			}
		}
	}			
}

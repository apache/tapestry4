package tutorial.workbench.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import java.io.Writer;

import net.sf.tapestry.*;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IUploadFile;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.html.*;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.util.io.*;
import net.sf.tapestry.util.io.BinaryDumpOutputStream;

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

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
 
 package com.primix.tapestry;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 *  Basic implementation of {@link IUploadFile} used by {@link RequestContext}.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class UploadFile implements IUploadFile
{
	private String fileName;
	private byte[] data;
	
	UploadFile(String fileName, byte[] data)
	{
		this.fileName = fileName;
		this.data = data;
	}
	
	public String getFileName()
	{
		return fileName;
	}

	/**
	 *  At time of writing, this always returns false, but this may change
	 *  in the future, when more sophisticated handling of the form/multipart-data
	 *  encoding is implemented.
	 * 
	 **/
	
	public boolean isTruncated()
	{
		return false;
	}

	public InputStream getStream()
	{
		return new ByteArrayInputStream(data);
	}

}

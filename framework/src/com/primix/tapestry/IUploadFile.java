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

import java.io.InputStream;

/**
 *  Represents a file uploaded from a client side form.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public interface IUploadFile
{
	/**
	 *  Returns the name of the file that was uploaded.
	 * 
	 **/

	public String getFileName();

	/**
	 *  Returns true if the uploaded file was truncated.  In the current
	 *  implementation, truncation does not occur (which can result in uploaded
	 *  files eating a lot of memory).  A future enhancement may limit the
	 *  size of any single file uploaded, or various other measures.  Struts
	 *  (for example) has
	 *  a whole host of options targetted at defanging denial of service attacks.
	 * 
	 **/

	public boolean isTruncated();

	/**
	 *  Returns an input stream of the content of the file.  There is no guarantee
	 *  that this stream will be valid after the end of the current request cycle,
	 *  so it should be processed immediately.
	 * 
	 *  <p>As of release 1.0.8, this will be a a {@link ByteArrayInputStream},
	 *  but that, too, may change (a future implementation may upload the stream
	 *  to a temporary file and return an input stream from that).
	 * 
	 **/

	public InputStream getStream();
}
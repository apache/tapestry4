/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.workbench.upload;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RequestCycleException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationConstraint;

/**
 *  Contains a form, including an {@link org.apache.tapestry.form.Upload}
 *  component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Upload extends BasePage
{
	private static final String[] bytesPerLineOptions =
		new String[] { "8", "16", "24", "32", "40", "48" };

	private static final String DEFAULT_BPL = "16";

	private String bytesPerLine = DEFAULT_BPL;
	private boolean showAscii;
	private IUploadFile file;
	private IPropertySelectionModel bplModel;

	public void detach()
	{
		bytesPerLine = DEFAULT_BPL;
		showAscii = false;
		file = null;

		super.detach();
	}

	public void formSubmit(IRequestCycle cycle) throws RequestCycleException
	{
		if (Tapestry.isNull(file.getFileName()))
		{
			IValidationDelegate delegate =
				(IValidationDelegate) getBeans().getBean("delegate");

			delegate.setFormComponent((IFormComponent) getComponent("inputFile"));
			delegate.record(
				"You must specify a file to upload.",
				ValidationConstraint.REQUIRED);
			return;
		}

		UploadResults results = (UploadResults) cycle.getPage("UploadResults");

		results.activate(file, showAscii, Integer.parseInt(bytesPerLine), cycle);
	}

	public String getBytesPerLine()
	{
		return bytesPerLine;
	}

	public void setBytesPerLine(String bytesPerLine)
	{
		this.bytesPerLine = bytesPerLine;

		fireObservedChange("bytesPerLine", bytesPerLine);
	}

	public boolean getShowAscii()
	{
		return showAscii;
	}

	public void setShowAscii(boolean showAscii)
	{
		this.showAscii = showAscii;

		fireObservedChange("showAscii", showAscii);
	}

	public IPropertySelectionModel getBytesPerLineModel()
	{
		if (bplModel == null)
			bplModel = new StringPropertySelectionModel(bytesPerLineOptions);

		return bplModel;
	}

	public IUploadFile getFile()
	{
		return file;
	}

	public void setFile(IUploadFile file)
	{
		this.file = file;
	}

	public void setMessage(String message)
	{

	}

}
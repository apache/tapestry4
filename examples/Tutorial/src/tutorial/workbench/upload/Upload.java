/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2002 by Howard Lewis Ship
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
package tutorial.workbench.upload;

import com.primix.tapestry.BasePage;
import com.primix.tapestry.IRequestCycle;
import com.primix.tapestry.IUploadFile;
import com.primix.tapestry.RequestCycleException;
import com.primix.tapestry.Tapestry;
import com.primix.tapestry.form.IFormComponent;
import com.primix.tapestry.form.IPropertySelectionModel;
import com.primix.tapestry.form.StringPropertySelectionModel;
import com.primix.tapestry.valid.IValidationDelegate;
import com.primix.tapestry.valid.ValidationConstraint;

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
				ValidationConstraint.REQUIRED,
				null);
			return;
		}

		Results results = (Results) cycle.getPage("upload.Results");

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
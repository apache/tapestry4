package tutorial.workbench.upload;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IUploadFile;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.form.IFormComponent;
import net.sf.tapestry.form.IPropertySelectionModel;
import net.sf.tapestry.form.StringPropertySelectionModel;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.valid.IValidationDelegate;
import net.sf.tapestry.valid.ValidationConstraint;

/**
 *  Contains a form, including an {@link net.sf.tapestry.form.Upload}
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
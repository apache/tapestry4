package net.sf.tapestry.form;

import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IUploadFile;
import net.sf.tapestry.RequestCycleException;

/**
 *  Form element used to upload files.  For the momement, it is necessary to
 *  explicitly set the form's enctype to "multipart/form-data".
 * 
 *  [<a href="../../../../../ComponentReference/Upload.html">Component Reference</a>]
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 * 
 **/

public class Upload extends AbstractFormComponent
{
    /** @since 2.2 **/

    private IUploadFile _file;
    private boolean _disabled;
    private String _name;

    public String getName()
    {
        return _name;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        IForm form = getForm(cycle);

        _name = form.getElementId(this);

        if (form.isRewinding())
        {
            _file = cycle.getRequestContext().getUploadFile(_name);

            return;
        }

        writer.beginEmpty("input");
        writer.attribute("type", "file");
        writer.attribute("name", _name);

        if (_disabled)
            writer.attribute("disabled");

        // Size, width, etc. can be specified as informal parameters
        // (Not making the same mistake here I made with Text and friends).

        generateAttributes(writer, cycle);
    }

    public boolean isDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    public IUploadFile getFile()
    {
        return _file;
    }

    public void setFile(IUploadFile file)
    {
        _file = file;
    }

}
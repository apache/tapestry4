/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package tutorial.workbench.palette;

import java.util.List;
import java.util.ResourceBundle;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.contrib.palette.SortMode;
import net.sf.tapestry.form.EnumPropertySelectionModel;
import net.sf.tapestry.form.IPropertySelectionModel;
import net.sf.tapestry.form.StringPropertySelectionModel;
import net.sf.tapestry.html.BasePage;

import org.apache.commons.lang.enum.Enum;

/**
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Palette extends BasePage
{
    private List _selectedColors;

    private SortMode _sort = SortMode.USER;

    private IPropertySelectionModel _sortModel;

    public void detach()
    {
        _sort = SortMode.USER;
        _selectedColors = null;

        super.detach();
    }

    public void formSubmit(IRequestCycle cycle)
    {
        // Does nothing ... may be invoked because
        // the user changed the sort
    }

    /**
     *  Invoked before {@link #formSubmit(IRequestCycle)} if the
     *  user clicks the "advance" button.
     * 
     **/

    public void advance(IRequestCycle cycle)
    {
        // Since Palette and palette.Results come from
        // a library now, we need to make sure
        // the namespace id is part of the name.

        Results results = (Results) cycle.getPage(getNamespace().constructQualifiedName("palette.Results"));

        results.setSelectedColors(_selectedColors);

        cycle.setPage(results);
    }

    private IPropertySelectionModel colorModel;

    private String[] colors = { "Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet" };

    public IPropertySelectionModel getColorModel()
    {
        if (colorModel == null)
            colorModel = new StringPropertySelectionModel(colors);

        return colorModel;
    }

    public void setSort(SortMode value)
    {
        _sort = value;

        fireObservedChange("sort", value);
    }

    public SortMode getSort()
    {
        return _sort;
    }

    public IPropertySelectionModel getSortModel()
    {
        if (_sortModel == null)
        {
            ResourceBundle bundle = ResourceBundle.getBundle("tutorial.workbench.palette.SortModeStrings", getLocale());

            Enum[] options = new Enum[] { SortMode.NONE, SortMode.LABEL, SortMode.VALUE, SortMode.USER };

            _sortModel = new EnumPropertySelectionModel(options, bundle);
        }

        return _sortModel;
    }

    public List getSelectedColors()
    {
        return _selectedColors;
    }

    public void setSelectedColors(List selectedColors)
    {
        _selectedColors = selectedColors;
    }

}
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
package net.sf.tapestry.valid;

import java.util.List;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.form.IFormComponent;

/**
 *  Interface used to track validation errors in forms and
 *  {@link IFormComponent}s (including {@link IField}).
 * 
 *  <p>In addition,
 *  controls how fields that are in error are presented (they can be
 *  marked in various ways by the delegate; the default implementation
 *  adds two red asterisks to the right of the field).
 *
 *  <p>The interface is designed so that a single instance is be shared
 *  with many instances of {@link IFormComponent}.
 *
 *  <p>Starting with release 1.0.8, this interface was extensively revised
 *  (in a non-backwards compatible way) to move the tracking of errors and
 *  invalid values (during a request cycle) to the delegate.  It has evolved from
 *  a largely stateless conduit for error messages into a very stateful tracker
 *  of field state.
 * 
 *  <p>Starting with release 1.0.9, this interface was <em>again</em>
 *  reworked, to allow tracking of errors in {@link IFormComponent form components}
 *  (not just {@link IField}s}, and to allow unassociated (with any field) errors
 *  to be tracked.
 *  
 *  <p><b>Fields vs. {@link IField} vs. Form Components</b><br>
 *  For most simple forms, these terms are pretty much synonymous.
 *  Your form will render normally, and each form component will render
 *  only once.  Some of your form components will by {@link ValidField}
 *  components and handle most of
 *  their validation internally (with the help of {@link IValidator} objects).
 *  In addition, your form listener may do additional validation and notify
 *  the validation delegate of additional errors, some of which
 *  are associated with a particular field, some of which are unassociated
 *  with any particular field.
 * 
 *  <p>
 *  But what happens if you use a {@link net.sf.tapestry.components.Foreach} or 
 *  {@link net.sf.tapestry.form.ListEdit} inside your form?
 *  Some of your components will render multiple times.  In this case you will have
 *  multiple <em>fields</em>.  Each field will have a unique field name (you can see this
 *  in the generated HTML).  It is this field name that the delegate keys off of, which
 *  means that some fields generated by a component may have errors and some may not, it
 *  all works fine (with one exception).
 * 
 *  <p><b>The Exception</b><br>
 *  The problem is that a component doesn't know its field name until its
 *  <code>render()</code> method is invoked (at which point, it allocates a unique field
 *  name from the {@link net.sf.tapestry.IForm#getElementId(IComponent)}. 
 *  This is not a problem for the field or its
 *  {@link IValidator}, but screws things up for the {@link FieldLabel}.
 * 
 *  <p>Typically, the label is rendered <em>before</em> the corresponding form component.
 *  Form components leave their last assigned field name in their
 *  {@link IFormComponent#getName() name property}.  So if the form component is in any kind of
 *  loop, the {@link FieldLabel} will key its name, 
 *  {@link IFormComponent#getDisplayName() display name} and error status off of
 *  its last renderred value.  So the moral of the story is don't use
 *  {@link FieldLabel} in this situation.
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public interface IValidationDelegate
{
    /**
     *  Invoked before other methods to configure the delegate for the given 
     *  form component.  Sets the current field based on
     *  the {@link IFormComponent#getName() name} of the form component
     *  (which is almost always a {@link ValidField}).
     * 
     *  <p>The caller should invoke this with a parameter of null to record
     *  unassociated global errors (errors not associated with any particular field).
     * 
     *  @since 1.0.8
     *
     **/

    public void setFormComponent(IFormComponent component);

    /**
     *  Returns true if the current component is in error (that is, had bad input
     *  submitted by the end user).
     * 
     *  @since 1.0.8
     * 
     **/

    public boolean isInError();

    /**
     *  Returns the string submitted by the client as the value for
     *  the current field.
     * 
     *  @since 1.0.8
     * 
     **/

    public String getFieldInputValue();

    /**
     *  Returns a {@link List} of {@link IFieldTracking}, in default order
     *  (the order in which fields are renderred). A caller should
     *  not change the values (the List is immutable).  
     *  May return null if no fields are in error.
     * 
     *  @since 1.0.8
     **/

    public List getFieldTracking();

    /**
     *  Resets any tracking information for the current field.  This will
     *  clear the field's inError flag, and set its error message and invalid input value
     *  to null.
     * 
     *  @since 1.0.8
     * 
     **/

    public void reset();

    /**
     *  Clears all tracking information.
     * 
     *  @since 1.0.10
     * 
     **/

    public void clear();

    /**
     *  Records the user's input for the current form component.  Input should
     *  be recorded even if there isn't an explicit error, since later form-wide
     *  validations may discover an error in the field.
     * 
     *  @since 2.4
     * 
     **/

    public void recordFieldInputValue(String input);

    /**
     *  The error notification method, invoked during the rewind phase
     *  (that is, while HTTP parameters are being extracted from the request
     *  and assigned to various object properties).  
     *
     *  <p>Typically, the delegate simply invokes
     *  {@link #record(String, ValidationConstraint)} or
     *  {@link #record(IRender, ValidationConstraint)}, but special
     *  delegates may override this behavior to provide (in some cases)
     *  different error messages or more complicated error renderers.
     * 
     **/

    public void record(ValidatorException ex);

    /**
     *  Records an error in the current field, or an unassociated error
     *  if there is no current field.
     *  
     *  @param message message to display (@see RenderString}
     *  @param constraint the constraint that was violated, or null if not known
     * 
     *  @since 1.0.9
     **/

    public void record(String message, ValidationConstraint constraint);

    /**
     *  Records an error in the current component, or an unassociated error.
     *  The maximum flexibility recorder.
     *  
     *  @param errorRenderer object that will render the error message (@see RenderString}
     *  @param constraint the constraint that was violated, or null if not known
     * 
     **/

    public void record(IRender errorRenderer, ValidationConstraint constraint);

    /**
     *  Invoked before the field is rendered.  If the field is in error,
     *  the delegate may decorate the field in some way (to highlight its
     *  error state).
     *
     **/

    public void writePrefix(
        IMarkupWriter writer,
        IRequestCycle cycle,
        IFormComponent component,
        IValidator validator)
        throws RequestCycleException;

    /**
     *  Invoked just before the &lt;input&gt; element is closed.
     *  The delegate can write additional attributes.  This is often used
     *  to set the CSS class of the field so that it can be displayed 
     *  differently, if in error (or required).
     *
     *  @since 1.0.5
     **/

    public void writeAttributes(
        IMarkupWriter writer,
        IRequestCycle cycle,
        IFormComponent component,
        IValidator validator)
        throws RequestCycleException;

    /**
     *  Invoked after the form component is rendered, so that the
     *  delegate may decorate the form component (if it is in error).
     *
     **/

    public void writeSuffix(
        IMarkupWriter writer,
        IRequestCycle cycle,
        IFormComponent component,
        IValidator validator)
        throws RequestCycleException;

    /**
     *  Invoked by a {@link FieldLabel} just before writing the name
     *  of the form component.
     *
     **/

    public void writeLabelPrefix(
        IFormComponent component,
        IMarkupWriter writer,
        IRequestCycle cycle)
        throws RequestCycleException;

    /**
     *  Invoked by a {@link FieldLabel} just after writing the name
     *  of the form component.
     *
     **/

    public void writeLabelSuffix(
        IFormComponent component,
        IMarkupWriter writer,
        IRequestCycle cycle)
        throws RequestCycleException;

    /**
     *   Returns true if any form component has errors.
     * 
     **/

    public boolean getHasErrors();
}
// Copyright Jun 2, 2006 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.apache.tapestry.internal.event;


/**
 * Provides a mapping for listener methods that are bound to events, used
 * internally by {@link ComponentEventProperty}.
 * 
 * @author jkuhnert
 */
public class EventBoundListener
{
    // the method name to invoke
    private String _methodName;
    // if not null the form to submit before invoking listener
    private String _formId;
    // if _formId set whether or not to validate form when submitted
    private boolean _validateForm;
    // The targeted component to listen to events on
    private String _componentId;
    // If targeting a form, whether or not to submit it asynchronously
    private boolean _async;
    
    /**
     * Creates a new listener binding. 
     * @param methodName
     *          The method to invoke.
     */
    public EventBoundListener(String methodName, String componentId)
    {
        _methodName = methodName;
        _componentId = componentId;
    }
    
    /**
     * Creates a new listener binding. 
     * @param methodName
     *          The method to invoke.
     * @param formId
     *          If not null the form to submit before invoking listener
     * @param validateForm
     *          If formId is set, whether or not to validate form when submitting.
     */
    public EventBoundListener(String methodName, String formId, 
            boolean validateForm, String componentId, boolean async)
    {
        _methodName = methodName;
        _formId = formId;
        _validateForm = validateForm;
        _componentId = componentId;
        _async = async;
    }
    
    /**
     * @return the formId
     */
    public String getFormId()
    {
        return _formId;
    }
    
    /**
     * @return the methodName
     */
    public String getMethodName()
    {
        return _methodName;
    }
    
    /**
     * @return the componentId
     */
    public String getComponentId()
    {
        return _componentId;
    }

    /**
     * @return the validateForm
     */
    public boolean isValidateForm()
    {
        return _validateForm;
    }
    
    /**
     * Whether or not listener should submit form
     * asynchronously.
     * @return
     */
    public boolean isAsync()
    {
        return _async;
    }
    
    /** 
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((_componentId == null) ? 0 : _componentId.hashCode());
        result = prime * result + ((_methodName == null) ? 0 : _methodName.hashCode());
        return result;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final EventBoundListener other = (EventBoundListener) obj;
        if (_componentId == null) {
            if (other._componentId != null) return false;
        } else if (!_componentId.equals(other._componentId)) return false;
        if (_methodName == null) {
            if (other._methodName != null) return false;
        } else if (!_methodName.equals(other._methodName)) return false;
        return true;
    }
}

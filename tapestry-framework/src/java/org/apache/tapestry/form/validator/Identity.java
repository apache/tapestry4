// Copyright 2007 The Apache Software Foundation
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

package org.apache.tapestry.form.validator;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.json.JSONLiteral;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.valid.ValidationConstants;
import org.apache.tapestry.valid.ValidatorException;
import org.apache.tapestry.valid.ValidationConstraint;

/**
 * Validates that the input value is the same as the value of another field.
 * This validator can also work in 'differ' mode. 
 * <p/>
 * Apply this validator to the second field in question and define the name
 * of the component against which to compare the current value.
 *
 * @since 4.1.2
 */
public class Identity extends BaseValidator {
    private String _fieldName;
    private int _matchType;
    private String _identityMessage;

    private static final int DIFFER = 0;
    private static final int MATCH = 1;


    public Identity() {
        super();
    }


    public Identity(String initializer) {
        super(initializer);
    }

    public String toString(IFormComponent field, Object value) {
        if (value == null)
            return null;

        return value.toString();
    }

    public void validate(IFormComponent field, ValidationMessages messages, Object object)
            throws ValidatorException {
        IFormComponent referent = (IFormComponent) field.getContainer().getComponent(_fieldName);
        Object referentValue = referent.getBinding("value").getObject();

        //TODO: if component is null treat _fieldName as an ognl expression
        boolean notEq = notEqual(referentValue, object);

        if (_matchType == MATCH ? notEq : !notEq)
            throw new ValidatorException(buildIdentityMessage(messages, field, referent),
                    ValidationConstraint.CONSISTENCY);
    }
    
    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
        if(field.isDisabled())
            return;
        
        IFormComponent referent = (IFormComponent) field.getContainer().getComponent(_fieldName);
        
        JSONObject profile = context.getProfile();
        
        if (!profile.has(ValidationConstants.CONSTRAINTS)) {
            profile.put(ValidationConstants.CONSTRAINTS, new JSONObject());
        }
        JSONObject cons = profile.getJSONObject(ValidationConstants.CONSTRAINTS);
        
        String func = (_matchType == MATCH) ? 
            "tapestry.form.validation.isEqual" :
            "tapestry.form.validation.isNotEqual";
        
        accumulateProperty(cons, field.getClientId(), 
                new JSONLiteral("[" + func + ",\""
                        + referent.getClientId() + "\"]"));                
        
        accumulateProfileProperty(field, profile, 
                ValidationConstants.CONSTRAINTS, buildIdentityMessage(context, field, referent));        
    }

    public String getMatch() {
        return _fieldName;
    }

    public void setMatch(String field) {
        _fieldName = field;
        _matchType = MATCH;

    }

    public String getDiffer() {
        return _fieldName;
    }

    public void setDiffer(String field) {
        _fieldName = field;
        _matchType = DIFFER;
    }


    /** @since 3.0 */
    public String getIdentityMessage() {
        return _identityMessage;
    }

    /**
     * Overrides the <code>field-too-short</code> bundle key. Parameter {0} is the minimum length.
     * Parameter {1} is the display name of the field.
     *
     * @since 3.0
     */

    public void setIdentityMessage(String string) {
        _identityMessage = string;
    }

    /** @since 3.0 */

    protected String buildIdentityMessage(ValidationMessages messages, IFormComponent field, IFormComponent referent) {
        Object[] parameters = new Object[]{
                field.getDisplayName(), new Integer(_matchType), referent.getDisplayName()
        };
        return messages.formatValidationMessage(_identityMessage,
                "invalid-field-equality", parameters);

    }

    private boolean notEqual(Object o1, Object o2) {
        if (o1 == null && o2 == null)
            return false;
        if (o1 == null || o2 == null)
            return true;
        return !o1.equals(o2);
    }

}
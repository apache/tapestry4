// Copyright 2005 The Apache Software Foundation
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

import java.util.List;

import org.apache.tapestry.IComponent;

/**
 * Constructs {@link org.apache.tapestry.form.validator.Validator} instances from a specification. A
 * specification is a comma-seperated list of entries. Each entry is in one of the following forms:
 * <ul>
 * <li><em>name</em>
 * <li><em>name</em>=<em>value</em>
 * <li><em>name[<em>message</em>]</em>
 * <li><em>name</em>=<em>value</em>[<em>message</em>]
 * </ul>
 * <p>
 * Most validator classes are <em>configurable</em>: they have a property that matches their
 * name. For example, {@link org.apache.tapestry.form.validator.MinDate} (which is named "minDate"
 * has a <code>minDate</code> property. A few validators are not configurable ("required" =>
 * {@link org.apache.tapestry.form.validator.Required}, for example).
 * <p>
 * Validators are expected to have a public no-args constructor. They are also expected to have a
 * <code>message</code> property which is set from the value in brackets.
 * <p>
 * A full validator specification might be:
 * <code>required,email,minLength=20[Email addresses must be at least 20 characters long.]
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public interface ValidatorFactory
{
    /**
     * Constructs a new (immutable) List of {@link Validator}, or returns a previously constructed
     * List.
     * 
     * @param specification
     *            a string identifying which validators and their configuration
     * @return List of {@link Validator} (possibly empty)
     */
    public List constructValidatorList(String specification);
}

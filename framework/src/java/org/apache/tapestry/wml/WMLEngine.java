// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.wml;

import org.apache.tapestry.engine.BaseEngine;

/**
 * Subclass of {@link BaseEngine} used for WML applications to change the Exception, StaleLink and
 * StaleSession pages.
 * 
 * @author David Solis
 * @since 3.0
 * @deprecated To be removed in 4.1. No longer necessary; the differences between WML applications
 *             and HTML applications are now handled via a startup mode. See
 *             {@link org.apache.tapestry.services.impl.SetupServletApplicationGlobals}.
 */

public class WMLEngine extends BaseEngine
{
}
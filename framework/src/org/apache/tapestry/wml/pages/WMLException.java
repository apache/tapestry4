//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.wml.pages;

import org.apache.tapestry.util.exception.ExceptionAnalyzer;
import org.apache.tapestry.util.exception.ExceptionDescription;
import org.apache.tapestry.wml.Deck;

/**
 *  Default exception reporting page for WML applications.
 *
 *  @author David Solis
 *  @version $Id$
 *  @since 3.0
 * 
 **/
public class WMLException extends Deck 
{
	private ExceptionDescription[] _exceptions;

	public void initialize()
	{
		_exceptions = null;
	}

	public ExceptionDescription[] getExceptions()
	{
		return _exceptions;
	}

	public void setException(Throwable value)
	{
		ExceptionAnalyzer analyzer;

		analyzer = new ExceptionAnalyzer();

		_exceptions = analyzer.analyze(value);
	}
}

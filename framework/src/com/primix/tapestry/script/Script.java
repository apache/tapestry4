/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry.script;

import com.primix.tapestry.*;
import com.primix.tapestry.html.*;
import com.primix.tapestry.util.xml.*;
import java.util.*;
import java.io.*;
import java.net.*;
import org.apache.log4j.*;

/**
 *  Works with the {@link Body} component to add a script (and perhaps some initialization) 
 *  to the HTML response.
 *
 * <table border=1>
 * <tr> 
 *    <th>Property</th>
 *    <th>Type</th>
 *	  <th>Read / Write </th>
 *    <th>Required</th> 
 *    <th>Default</th>
 *    <th>Description</th>
 * </tr>
 *
 * <tr>
 *  <td>script</td>
 *  <td>{@link String}</td>
 *  <td>R</td>
 *  <td>yes</td>
 *  <td>&nbsp;</td>
 *  <td>The path of a resource (on the classpath) containing the script.</td>
 * </tr>
 *
 * <tr>
 *  <td>cautious</td>
 *  <td>boolean</td>
 *  <td>R</td>
 *  <td>no</td>
 *  <td>false</td>
 *  <td>If false (the default), then the {@link ParsedScript} is maintained
 *  across request cycles without question.  If true, then a check is made
 *  that the script parameter and {@link ParsedScript} are in agreement
 *  (and a new {@link ParsedScript} is obtained if needed).</td>
 *  </tr>
 *
 * <tr>
 *  <td>symbols</td>
 *  <td>{@link Map}</td>
 *  <td>R</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td>The base set of symbols to be provided to the {@link ParsedScript}.
 *  To this is added (in a copy of the {@link Map}) any informal parameters.
 *  </tr>
 *
 * </table>
 *
 *  <p>Allows informal parameters (which become symbols visible to the script), but
 *  does not allow a body.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class Script
extends AbstractComponent
{
	private static final Category CAT = 
		Category.getInstance(Script.class);

	private IBinding scriptBinding;

	private IBinding cautiousBinding;
	private boolean staticCautious;
	private boolean cautiousValue;

	private IBinding symbolsBinding;

	private ParsedScript parsedScript;
	private ScriptParser scriptParser;

	public void setScriptBinding(IBinding value)
	{
		scriptBinding = value;
	}

	public IBinding getScriptBinding()
	{
		return scriptBinding;
	}

	public void setCautiousBinding(IBinding value)
	{
		cautiousBinding = value;
		staticCautious = value.isStatic();
		if (staticCautious)
			cautiousValue = value.getBoolean();
	}

	public IBinding getCautiousBinding()
	{
		return cautiousBinding;
	}

	public void setSymbolsBinding(IBinding value)
	{
		symbolsBinding = value;
	}

	public IBinding getSymbolsBinding()
	{
		return symbolsBinding;
	}

	/**
	*  Returns true if the script is configured cautious (may load
	*  a different script on a subsequent render) or normal
	*  (once a script is loaded, it is "locked in" for all subsequent
	*  request cycles).  This is determined by the cautious parameter, which
	*  defaults off.
	*
	*/

	public boolean isCautious()
	{
		if (staticCautious)
			return cautiousValue;

		if (cautiousBinding == null)
			return false;

		return cautiousBinding.getBoolean();
	}

	/**
	*  Constructs the symbols {@link Map}.  This starts with the
	*  contents of the symbols parameter (if specified) to which is added
	*  any informal parameters.  If both a symbols parameter and informal
	*  parameters are bound, then a copy of the symbols parameter's value is made
	*  (that is, the {@link Map} provided by the symbols parameter is read, but not modified).
	*
	*/

	public Map getSymbols()
	{
		Map result = null;
		boolean copy = false;
		Iterator i;
		String bindingName;
		IBinding binding;
		String value;

		if (symbolsBinding != null)
		{
			result = (Map)symbolsBinding.getObject("symbols", Map.class);

			// Make a writable copy if there are any informal parameters
			copy = true;
		}

		// Now, iterator through all the binding names (which includes both
		// formal and informal parmeters).  Skip the formal ones and
		// access the informal ones.

		i = getBindingNames().iterator();
		while (i.hasNext())
		{
			bindingName = (String)i.next();

			// Skip formal parameters

			if (specification.getParameter(bindingName) != null)
				continue;

			binding = getBinding(bindingName);

			value = (String)binding.getObject(bindingName, String.class);

			if (value == null)
				continue;

			if (result == null)
				result = new HashMap();
			else
				if (copy)
			{
				result = new HashMap(result);
				copy = false;
			}

			result.put(bindingName, value);
		}

		return result;
	}

	/**
	*  Gets the {@link ParsedScript} for the correct script.
	*
	*  <p>The ParsedScript is cached between invocations; once constructed, it will
	*  be re-used for all future request cycles, unless this Script component
	*  is configured to be cautious.
	*
	*  <p>When cautious, the script parameter is checked and, if it doesn't match
	*  the script last parsed (i.e., the script that matches the generator), then
	*  the current ParsedScript is discarded and a new one built, using
	*  the new value of the script parameter.
	*
	*/

	public ParsedScript getParsedScript()
	{
		String scriptPath;

		if (parsedScript != null && ! isCautious())
			return parsedScript;

		scriptPath = (String)scriptBinding.getObject("script", String.class);

		if (scriptPath == null)
			throw new NullValueForBindingException(scriptBinding);

		// If this script is different than the last script parsed by this
		// component, then throw it away and parse again (using the new script).


		if (parsedScript != null &&
			!parsedScript.getScriptPath().equals(scriptPath))
			parsedScript = null;

		if (parsedScript == null)
			parsedScript = buildParsedScript(scriptPath);
			
		return parsedScript;
	}

	private ParsedScript buildParsedScript(String scriptPath)
	{
		IResourceResolver resolver = page.getEngine().getResourceResolver();
		URL scriptURL;
		InputStream stream = null;

		if (CAT.isDebugEnabled())
			CAT.debug("Reading script from resource " + scriptPath);
			
		scriptURL = resolver.getResource(scriptPath);
		
		if (scriptURL == null)
			throw new ApplicationRuntimeException
				("Unable to locate script resource " + scriptPath + " in classpath.");

		if (scriptParser == null)
			scriptParser = new ScriptParser();
			
		try
		{
			stream = scriptURL.openStream();

			return scriptParser.parse(stream, scriptPath);
		}
		catch (DocumentParseException ex)
		{
			throw new ApplicationRuntimeException(ex);
		}
		catch (IOException ex)
		{
			throw new ApplicationRuntimeException
				("Unable to open stream for " + scriptURL + ".", ex);
		}
		finally
		{
			close(stream);
		}

	}

	private void close(InputStream stream)
	{
		try
		{
			if (stream != null)
				stream.close();
		}
		catch (IOException ex)
		{
			// Ignore.
		}
	}

	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		ScriptSession session;
		
		if (cycle.isRewinding())
			return;

		Body body = Body.get(cycle);

		if (body == null)
			throw new RequestCycleException(
				"A Script component must be wrapped by a Body component.",
				this);

		try
		{
			session = getParsedScript().execute(getSymbols());
		}
		catch (ScriptException ex)
		{
			throw new RequestCycleException(this, ex);
		}

		String value = session.getBody();
		if (value != null)
			body.addOtherScript(value);
		
		value = session.getInitialization()	;
		if (value != null)
			body.addOtherInitialization(value);

		// This component is not allowed to have a body.
	}
}

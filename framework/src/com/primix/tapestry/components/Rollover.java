package com.primix.tapestry.components;

import com.primix.tapestry.*;
import com.primix.tapestry.script.*;
import java.io.*;
import java.util.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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

/**
 *  Combines an {@link Action} with an &lt;img&gt; and JavaScript code
 *  to create a rollover effect that work with both Netscape Navigator and 
 *  Internet Explorer.
 *
 *  A <code>Rollover</code> must be contained within a {@link Body} and within
 *  an {@link IActionListener} (the JavaScript handlers for dealing with mouse
 *  movement are applied to the &lt;a&gt; created by the {@link IActionListener}).
 *
 *  <p>Informal parameters are applied only to the &lt;img&gt;
 *  element, there isn't a way to apply them to the &lt;a&gt; element.
 *
 * <table border=1>
 * <tr> 
 *    <td>Parameter</td>
 *    <td>Type</td>
 *	  <td>Read / Write </td>
 *    <td>Required</td> 
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 *  <tr>
 *    <td>image</td>
 *    <td>{@link IAsset}</td>
 *    <td>R</td>
 *   	<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The (initial) image to show.</td>
 *	</tr>
 *
 * <tr>
 *		<td>focus</td>
 *		<td>{@link IAsset}</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>If provided (and the component is not disabled), then a JavaScript <code>onMouseOver</code> event handler
 * is added to change the image.  An <code>onMouseOut</code> event handler is also
 * provided, to change the button to either it's base image, or its blur image. </td> </tr>
 *
 * <tr>
 *		<td>blur</td>
 *		<td>{@link IAsset}</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>If provided (and the component is not disabled), then a JavaScript <code>onMouseOut</code> event handler
 *  is added to change the image when the mouse is moved off of the image. </td> </tr>
 *
 * <tr>
 *
 * <tr>
 *		<td>disabled</td>
 *		<td>{@link IAsset}</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>Image used when the <code>Rollover</code> is disabled.  A <code>Rollover</code>
 * is disabled when the {@link IServiceLink} containing it is disabled.  If no disabled
 * asset is specified, then image asset is used instead.  
 * A disabled <code>Rollover</code> does not respond to the mouse.
    </td> </tr>
 *
 * <tr>
 *
 * </table>
 *
 * <p>Informal parameters are allowed.
 *
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Rollover extends AbstractComponent
{
	// Shared by all instances of Rollover

	private static ScriptGenerator generator;

    // Symbols used when generating the script.

	private Map symbols;

    private static final int MAP_SIZE = 7;    

	private IBinding image;
	private IBinding focus;
	private IBinding blur;
	private IBinding disabled;

	/**
	*  Note that the onMouseXXX attributes are NOT reserved, 
	*  but should be used with care.
	*
	*/

	private final static String[] reservedNames = { "name", "src", "border" };

	/**
	*  Converts an {@link IAsset} binding into a usable URL.  Returns null
	*  if the binding does not exist or the binding's value is null.
	*
	*/

	protected String getAssetURL(String name, IBinding binding, IRequestCycle cycle)
	throws RequestCycleException
	{
		IAsset asset;

		if (binding == null)
			return null;

		try
		{
			asset = (IAsset)binding.getValue();
		}
		catch (ClassCastException e)
		{
			throw new RequestCycleException("Binding " + name + " is not of type IAsset.",
				this, cycle, e);
		}

		if (asset == null)
			return null;

		return asset.buildURL(cycle);
	}

	public IBinding getBlurBinding()
	{
		return blur;
	}

	public IBinding getDisabledBinding()
	{
		return disabled;
	}

	public IBinding getFocusBinding()
	{
		return focus;
	}

	public IBinding getImageBinding()
	{
		return image;
	}

	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		String imageURL = null;
		String focusURL = null;
		String blurURL = null;
		String uniqueId = null;
		String onMouseOverName = null;
		boolean dynamic = false;
		String onMouseOutName = null;
		int borderValue = 0;
		boolean compressed;
		Body body;
		String focusImageName = null;
		String blurImageName = null;
		IServiceLink serviceLink;
		boolean enabled;
		String imageName = null;
		String script;

		body = Body.get(cycle);
		if (body == null)
			throw new RequestCycleException(
				"Rollover components must be contained within a Body component.",
				this, cycle);

		serviceLink = (IServiceLink)cycle.getAttribute(IServiceLink.ATTRIBUTE_NAME);
		if (serviceLink == null)
			throw new RequestCycleException(
				"Rollover components must be contained within an IServiceLink component.",
				this, cycle);

		// No body, so we skip it all if not rewinding (assumes no side effects on
		// accessors).

		if (cycle.isRewinding())
			return;

		enabled = serviceLink.isEnabled();

		if (!enabled)
		{
			imageURL = getAssetURL("disabled", disabled, cycle);

			if (imageURL == null)
				imageURL = getAssetURL("image", image, cycle);
		}
		else
		{
			imageURL = getAssetURL("image", image, cycle);
			focusURL = getAssetURL("focus", focus, cycle);
			blurURL = getAssetURL("blur", blur, cycle);

			dynamic = (focusURL != null) || (blurURL != null);
		}

		if (imageURL == null)
			throw new RequiredParameterException(this, "image", null, cycle);			

		compressed = writer.compress(true);


		writer.beginOrphan("img");

		writer.attribute("src", imageURL);

		writer.attribute("border", 0);
	
		if (dynamic)
        {
            try
            {
                if (focusURL == null)
                    focusURL = imageURL;

                if (blurURL == null)
                    blurURL = imageURL;

                imageName = writeScript(body, serviceLink, focusURL, blurURL);
            }
            catch (IOException ex)
            {
                throw new RequestCycleException("Unable to write Rollover script.",
                    this, cycle, ex);
            }

		    writer.attribute("name", imageName);
        }

		generateAttributes(cycle, writer, reservedNames);

		writer.closeTag();

		writer.setCompressed(compressed);
	}

    private ScriptGenerator getScriptGenerator()
    throws IOException
    {
        if (generator == null)
        {
            synchronized (Rollover.class)
            {
                if (generator == null)
                {
                    InputStream stream = null;

                    try
                    {
                        stream = getClass().getResourceAsStream("Rollover.script");

                        generator = new ScriptGenerator(stream);
                    }
                    finally
                    {
                        close(stream);
                    }
                }
            }
        }

        return generator;
    }

    private void close(InputStream stream)
    {
        if (stream != null)
        {
            try
            {
                stream.close();
            }
            catch (IOException ex)
            {
                // ignore.
            }
        }
    }

    private String writeScript(Body body, IServiceLink link,
        String focusURL, String blurURL)
        throws IOException
    {
        String uniqueId = body.getUniqueId();
        String imageName = "rollover_" + uniqueId;
        String onMouseOverName = "onMouseOver_" + uniqueId;
        String onMouseOutName = "onMouseOut_" + uniqueId;
        String blurImageName = "blur_" + uniqueId;
        String focusImageName = "focus_" + uniqueId;
        ScriptGenerator generator = getScriptGenerator();

        body.addImageInitialization(focusImageName, focusURL);
        body.addImageInitialization(blurImageName, blurURL);

        if (symbols == null)
            symbols = new HashMap(MAP_SIZE);
            
        symbols.put("imageName",       imageName);
        symbols.put("onMouseOverName", onMouseOverName);
        symbols.put("onMouseOutName",  onMouseOutName);
        symbols.put("focusImageURL",    body.getInitializedImage(focusImageName));
        symbols.put("blurImageURL",   body.getInitializedImage(blurImageName));

    	// Add this to the scripting block at the top of the page.
    	
    	body.addOtherScript(generator.generateScript(symbols));
    	
    	// Add attributes to the link to control mouse over/out.
    	// Because the script is written before the <body> tag,
    	// there won't be any timing issues (such as cause
    	// bug #113893).
    	
    	link.setAttribute("onMouseOver",
    			"javascript:" + onMouseOverName + "();");
    	link.setAttribute("onMouseOut",
    			"javascript:" + onMouseOutName + "();");

        symbols.clear();

        // Return the value that must be assigned to the 'name' attribute
        // of the <img> tag.

        return imageName;

    }

	public void setBlurBinding(IBinding newBlur)
	{
		blur = newBlur;
	}

	public void setDisabledBinding(IBinding newDisabled)
	{
		disabled = newDisabled;
	}

	public void setFocusBinding(IBinding newFocus)
	{
		focus = newFocus;
	}

	public void setImageBinding(IBinding value)
	{
		image = value;
	}
}


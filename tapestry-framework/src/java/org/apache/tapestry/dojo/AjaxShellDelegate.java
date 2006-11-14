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
package org.apache.tapestry.dojo;

import java.util.Locale;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.html.Shell;
import org.apache.tapestry.json.JSONObject;

/**
 * The default rendering delegate responseible for include the 
 * dojo sources into the {@link Shell} component.
 *
 * @author jkuhnert
 */
public class AjaxShellDelegate implements IRender
{
    
    /** Client side debug log level. */
    public static final String BROWSER_LOG_DEBUG="DEBUG";
    /** Client side info log level. */
    public static final String BROWSER_LOG_INFO="INFO";
    /** Client side warning log level. */
    public static final String BROWSER_LOG_WARNING="WARNING";
    /** Client side error log level. */
    public static final String BROWSER_LOG_ERROR="ERROR";
    /** Client side critical log level. */
    public static final String BROWSER_LOG_CRITICAL="CRITICAL";
    
    private IAsset _dojoSource;
    
    private IAsset _dojoPath;
    
    private IAsset _tapestrySource;
    
    private IEngineService _assetService;
    
    private boolean _parseWidgets;
    
    private String _browserLogLevel = BROWSER_LOG_WARNING;
    
    private boolean _debug;
    
    private String _debugContainerId;
    
    private boolean _consoleEnabled;
    
    private boolean _preventBackButtonFix;
    
    private boolean _debugAtAllCosts;
    
    /**
     * {@inheritDoc}
     */
    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        // first configure dojo, has to happen before package include
        
        JSONObject dojoConfig = new JSONObject();
        
        //
        // Debugging configuration , debugAtAlCosts causes the individual 
        // .js files to included in the document head so that javascript errors
        // are able to resolve to the context of the file instead of just "dojo.js"
        
        dojoConfig.put("isDebug", _debug);
        
        if (_debugAtAllCosts)
            dojoConfig.put("debugAtAllCosts", _debugAtAllCosts);
        if (_debugContainerId != null)
            dojoConfig.put("debugContainerId", _debugContainerId);
        
        // The key to resolving everything out of the asset service
        
        dojoConfig.put("baseRelativePath", 
                _assetService.getLink(true, _dojoPath.getResourceLocation().getPath()).getURL());
        
        dojoConfig.put("preventBackButtonFix", _preventBackButtonFix);
        dojoConfig.put("parseWidgets", _parseWidgets);
        
        //
        // Supports setting up locale in dojo environment to match the requested page locale.
        // (for things that use these settings, like DropdownDatePicker / date parsing / etc..
        
        Locale locale = cycle.getPage().getLocale();
        
        dojoConfig.put("locale", locale.getLanguage().toLowerCase()
                + ((locale.getCountry() != null && locale.getCountry().trim().length() > 0)
                ? "-" + locale.getCountry().toLowerCase()
                        : ""));
        
        //
        // Write the required script includes and dojo.requires
        
        StringBuffer str = new StringBuffer("<script type=\"text/javascript\">");
        str.append("djConfig = ").append(dojoConfig.toString())
        .append(" </script>\n\n ");
        
        // include the core dojo.js package
        
        str.append("<script type=\"text/javascript\" src=\"")
        .append(_assetService.getLink(true,
                _dojoSource.getResourceLocation()
                .getPath()).getURL()).append("\"></script>");
        
        // include core tapestry.js package
        
        str.append("<script type=\"text/javascript\" src=\"")
        .append(_assetService.getLink(true,
                _tapestrySource.getResourceLocation()
                .getPath()).getURL()).append("\"></script>");
        
        String logRequire = _consoleEnabled ? "dojo.require(\"dojo.debug.console\");\n"
                : "dojo.require(\"dojo.logging.Logger\");\n";
        
        // logging configuration
        
        str.append("\n<script type=\"text/javascript\">\n");
        
        if (_debug) {
            str.append(logRequire)
            .append("dojo.log.setLevel(dojo.log.getLevel(\"").append(_browserLogLevel)
            .append("\"));\n");
        }
        
        str.append("dojo.require(\"tapestry.namespace\");\n").append("</script>");
        
        writer.printRaw(str.toString());
        writer.println();
    }
    
    /**
     * Sets the dojo logging level. Similar to log4j style
     * log levels. 
     * @param level The string constant for the level, valid values
     *              are:
     *              <p>
     *              <ul>
     *              <li>{@link #BROWSER_LOG_DEBUG}</li>
     *              <li>{@link #BROWSER_LOG_INFO}</li>
     *              <li>{@link #BROWSER_LOG_WARNING}</li>
     *              <li>{@link #BROWSER_LOG_ERROR}</li>
     *              <li>{@link #BROWSER_LOG_CRITICAL}</li>
     *              </ul>
     *              </p>
     */
    public void setLogLevel(String level)
    {
        Defense.notNull("level", level);
        
        _browserLogLevel = level;
    }
    
    /**
     * Allows for turning browser debugging on/off.
     * 
     * @param debug If false, no logging output will be written.
     */
    public void setDebug(boolean debug)
    {
        _debug = debug;
    }
    
    /**
     * Turns off deep context level javascript debugging mode for dojo. This means
     * that exceptions/debug statements will show you line numbers from the actual 
     * javascript file that generated them instead of the normal default which is 
     * usually bootstrap.js .
     * 
     * <p>The default value is false if not set.</p>
     * 
     * <p>
     *  People should be wary of turning this on as it may cause problems
     *  under certain conditions, and you definitely don't ever want this 
     *  on in production. 
     * </p>
     * 
     * @param value If true deep debugging will be turned on.
     */
    public void setDebugAtAllCosts(boolean value)
    {
        _debugAtAllCosts = value;
    }
    
    /**
     * Sets the html element node id of the element you would like all browser
     * debug content to go to.
     * 
     * @param debugContainerId the debugContainerId to set
     */
    public void setDebugContainerId(String debugContainerId)
    {
        _debugContainerId = debugContainerId;
    }
    
    /**
     * Enables/disables the dojo.debug.console functionality which should redirect
     * most logging messages to your browsers javascript console. (if it supports 
     * one).
     * 
     * <p>
     *  The debug console is disabled by default. Currently known supported 
     *  browsers are FireFox(having FireBug extension helps a great deal)/Opera/Safari.
     * </p>
     * 
     * @param enabled Whether or not the enable debug console.
     */
    public void setConsoleEnabled(boolean enabled)
    {
        _consoleEnabled = enabled;
    }
    
    /**
     * Sets the dojo preventBackButtonFix djConfig configuration. This should
     * typically be avoided but is provided for flexibility.
     * 
     * @param prevent
     */
    public void setPreventBackButtonFix(boolean prevent)
    {
        _preventBackButtonFix = prevent;
    }
    
    /**
     * Tells dojo whether or not to parse widgets by traversing the entire 
     * dom node of your document. It is highly reccomended that you keep this
     * at its default value of false.
     * 
     * @param parseWidgets the parseWidgets to set
     */
    public void setParseWidgets(boolean parseWidgets)
    {
        _parseWidgets = parseWidgets;
    }

    /**
     * Sets a valid path to the base dojo javascript installation
     * directory.
     * @param dojoSource
     */
    public void setDojoSource(IAsset dojoSource)
    {
        _dojoSource = dojoSource;
    }
    
    /**
     * Sets the dojo baseRelativePath value.
     * @param dojoPath
     */
    public void setDojoPath(IAsset dojoPath)
    {
        _dojoPath = dojoPath;
    }
    
    /**
     * Sets a valid base path to resolve tapestry.js.
     * @param tapestrySource
     */
    public void setTapestrySource(IAsset tapestrySource)
    {
        _tapestrySource = tapestrySource;
    }
    
    /**
     * Injected asset service.
     * @param service
     */
    public void setAssetService(IEngineService service)
    {
        _assetService = service;
    }
}

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

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.*;
import org.apache.tapestry.html.Shell;
import org.apache.tapestry.json.JSONObject;

import java.util.Locale;

/**
 * The default rendering delegate responseible for include the dojo sources in
 * to the {@link Shell} component.
 */
public class AjaxShellDelegate implements IRender {

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

    private static final String SYSTEM_NEWLINE= (String)java.security.AccessController.doPrivileged(
      new sun.security.action.GetPropertyAction("line.separator"));

    private IAsset _dojoSource;

    private IAsset _dojoFormSource;

    private IAsset _dojoWidgetSource;

    private IAsset _dojoPath;

    private IAsset _tapestrySource;

    private IAsset _tapestryPath;

    private boolean _parseWidgets;

    private String _browserLogLevel = BROWSER_LOG_WARNING;

    private boolean _debug;

    private String _debugContainerId;

    private boolean _consoleEnabled;

    private boolean _preventBackButtonFix;

    private boolean _debugAtAllCosts;

    /** Default list of pre-bundled dojo supported locales */
    protected String[] SUPPORTED_LOCALES = { "en-us", "en", "de-de", "de", "en-gb",
                                             "es-es", "es", "fr-fr", "fr", "zh-cn",
                                             "zh-tw", "zh" , "it-it", "it", "ja-jp",
                                             "ja", "ko-kr", "ko", "pt-br", "pt", "xx"};

    /**
     * {@inheritDoc}
     */
    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        // first configure dojo, has to happen before package include

        JSONObject dojoConfig = new JSONObject();

        // Debugging configuration , debugAtAlCosts causes the individual 
        // .js files to included in the document head so that javascript errors
        // are able to resolve to the context of the file instead of just "dojo.js"

        if (_debug)
        {
            dojoConfig.put("isDebug", _debug);
        }

        if (_debugAtAllCosts)
            dojoConfig.put("debugAtAllCosts", _debugAtAllCosts);
        if (_debugContainerId != null)
            dojoConfig.put("debugContainerId", _debugContainerId);

        IPage page = cycle.getPage();

        // The key to resolving everything out of the asset service

        dojoConfig.put("baseRelativePath", _dojoPath.buildURL());

        if (page.hasFormComponents())
        {
            dojoConfig.put("preventBackButtonFix", _preventBackButtonFix);
        }
        dojoConfig.put("parseWidgets", _parseWidgets);

        // Supports setting up locale in dojo environment to match the requested page locale.
        // (for things that use these settings, like DropdownDatePicker / date parsing / etc..

        Locale locale = cycle.getPage().getLocale();

        String localeStr = locale.getLanguage().toLowerCase()
                           + ((locale.getCountry() != null && locale.getCountry().trim().length() > 0)
                              ? "-" + locale.getCountry().toLowerCase()
                              : "");

        if (isLocaleSupported(localeStr))
        {
            dojoConfig.put("locale", localeStr);
        }

        // Write the required script includes and dojo.requires

        StringBuffer str = new StringBuffer("<script type=\"text/javascript\">");
        str.append("djConfig = ").append(dojoConfig.toString())
          .append(" </script>")
          .append(SYSTEM_NEWLINE).append(SYSTEM_NEWLINE);

        // include the core dojo.js package

        str.append("<script type=\"text/javascript\" src=\"")
          .append(_dojoSource.buildURL()).append("\"></script>");

        if (page.hasFormComponents())
        {
            str.append("<script type=\"text/javascript\" src=\"")
              .append(_dojoFormSource.buildURL()).append("\"></script>");
        }

        if (page.hasWidgets())
        {
            str.append("<script type=\"text/javascript\" src=\"")
              .append(_dojoWidgetSource.buildURL()).append("\"></script>");
        }

        // configure basic dojo properties , logging includes

        if (_debug)
        {
            String logRequire = _consoleEnabled ? "dojo.require(\"dojo.debug.console\");" + SYSTEM_NEWLINE
                                : "dojo.require(\"dojo.logging.Logger\");" + SYSTEM_NEWLINE;

            str.append(SYSTEM_NEWLINE).append("<script type=\"text/javascript\">").append(SYSTEM_NEWLINE);
            str.append(logRequire)
              .append("dojo.log.setLevel(dojo.log.getLevel(\"").append(_browserLogLevel)
              .append("\"));").append(SYSTEM_NEWLINE)
              .append("</script>");
        }

        // module path registration to tapestry javascript sources

        String tapestryUrl = _tapestryPath.buildURL();
        if (tapestryUrl.endsWith("/"))
        {
            tapestryUrl = tapestryUrl.substring(0, tapestryUrl.length() - 1);
        }

        str.append(SYSTEM_NEWLINE).append("<script type=\"text/javascript\">").append(SYSTEM_NEWLINE)
          .append("dojo.registerModulePath(\"tapestry\", \"")
          .append(tapestryUrl).append("\");").append(SYSTEM_NEWLINE);
        str.append("</script>").append(SYSTEM_NEWLINE);

        // include core tapestry.js package

        str.append("<script type=\"text/javascript\" src=\"")
          .append(_tapestrySource.buildURL()).append("\"></script>");

        // namespace registration

        str.append(SYSTEM_NEWLINE).append("<script type=\"text/javascript\">").append(SYSTEM_NEWLINE);
        str.append("dojo.require(\"tapestry.namespace\");").append(SYSTEM_NEWLINE)
          .append("tapestry.requestEncoding='")
          .append(cycle.getEngine().getOutputEncoding()).append("';")
          .append(SYSTEM_NEWLINE).append("</script>");

        writer.printRaw(str.toString());
        writer.println();
    }

    /**
     * Checks if the provided locale string matches one of the predefined {@link #SUPPORTED_LOCALES}
     * in the dojo javascript library.
     *
     * @param locale
     *          The Dojo formatted locale string to check.
     *
     * @return True if locale is supported and ok to define in dojoConfig - false otherwise.
     */
    protected boolean isLocaleSupported(String locale)
    {
        if (locale == null)
            return false;

        for (int i=0; i < SUPPORTED_LOCALES.length; i++)
        {
            if (locale.equals(SUPPORTED_LOCALES[i]))
                return true;
        }

        return false;
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
     *          Whether or not to prevent back button fix.
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
     *
     * @param dojoSource
     *          Path to dojo source directory core "dojo.js" file.
     */
    public void setDojoSource(IAsset dojoSource)
    {
        _dojoSource = dojoSource;
    }

    public void setDojoFormSource(IAsset formSource)
    {
        _dojoFormSource = formSource;
    }

    public void setDojoWidgetSource(IAsset widgetSource)
    {
        _dojoWidgetSource = widgetSource;
    }

    /**
     * Sets the dojo baseRelativePath value.
     *
     * @param dojoPath
     *          The base path to dojo directory.
     */
    public void setDojoPath(IAsset dojoPath)
    {
        _dojoPath = dojoPath;
    }

    /**
     * Sets a valid base path to resolve tapestry core.js.
     *
     * @param tapestrySource
     *          Main tapestry core.js file.
     */
    public void setTapestrySource(IAsset tapestrySource)
    {
        _tapestrySource = tapestrySource;
    }

    /**
     * Sets the path to the tapestry javascript modules. (Needed for dojo to resolve the 
     * path to tapestry javascript, esp when overriding the default bundled dojo.)
     *
     * @param tapestryPath The path to tapestry.
     */
    public void setTapestryPath(IAsset tapestryPath)
    {
        _tapestryPath = tapestryPath;
    }
}

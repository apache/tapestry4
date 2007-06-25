<?xml version="1.0" encoding="UTF-8"?>
<!-- 
   Copyright 2004, 2005 The Apache Software Foundation

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" 
  xmlns:redirect="http://xml.apache.org/xalan/redirect" extension-element-prefixes="redirect">
  <xsl:param name="base.dir" select="string('.')"/>
  
  <xsl:template match="/registry">
    <!-- The master index is one level up from most of the content, so a few things
         are handled idiosyncratically here. -->
    <xsl:message>Writing master index to <xsl:value-of select="$base.dir"/></xsl:message>
    <html>
      <head>
        <title>HiveMind Registry</title>
        <link rel="STYLESHEET" type="text/css" href="hivemind.css"/>
      </head>
      <body>
        <div class="title">HiveMind Module Registry</div>
        <table class="summary" cellspacing="0">
          <tr>
            <th>Module</th>
            <th>Version</th>
            <th>Description</th>
          </tr>
          <xsl:for-each select="module">
            <xsl:sort select="@id"/>
            <tr>
              <td>
                <a href="module/{@id}.html">
                  <xsl:value-of select="@id"/>
                </a>
              </td>
              <td>
                <xsl:value-of select="@version"/>
              </td>
              <td>
                <xsl:value-of select="./text()"/>
              </td>
            </tr>
          </xsl:for-each>
        </table>
        <table class="layout" cellspacing="0">
          <tr>
            <td>
              <xsl:call-template name="master-index-configurations"/>
            </td>
            <td>
              <xsl:call-template name="master-index-services"/>
            </td>
            <td>
              <xsl:call-template name="master-index-schemas"/>
            </td>
          </tr>
        </table>
      </body>
    </html>
    <xsl:call-template name="output-detail-files"/>
  </xsl:template>
  <!--
    == master-index-configurations
    -->
  <xsl:template name="master-index-configurations">
    <table class="summary" cellspacing="0">
      <tr>
        <th>Configuration Points</th>
      </tr>
      <xsl:for-each select="module/configuration-point">
        <xsl:sort select="@id"/>
        <tr>
          <td>
            <a href="config/{@id}.html">
              <xsl:value-of select="@id"/>
            </a>
          </td>
        </tr>
      </xsl:for-each>
    </table>
    <div class="info">
      <xsl:value-of select="count(module/configuration-point)"/> configuration points
    </div>
  </xsl:template>
  <!--
    == master-index-services
    -->
  <xsl:template name="master-index-services">
    <table class="summary" cellspacing="0">
      <tr>
        <th>Service Points</th>
      </tr>
      <xsl:for-each select="module/service-point">
        <xsl:sort select="@id"/>
        <tr>
          <td>
            <a href="service/{@id}.html">
              <xsl:value-of select="@id"/>
            </a>
          </td>
        </tr>
      </xsl:for-each>
    </table>
    <div class="info">
      <xsl:value-of select="count(module/service-point)"/> service points
    </div>    
  </xsl:template>
  <!--
    == master-index-schemas
    -->
  <xsl:template name="master-index-schemas">
    <table class="summary" cellspacing="0">
      <tr>
        <th>Schemas</th>
      </tr>
      <xsl:for-each select="module/schema">
        <xsl:sort select="@id"/>
        <tr>
          <td>
            <a href="schema/{@id}.html">
              <xsl:value-of select="@id"/>
            </a>
          </td>
        </tr>
      </xsl:for-each>
    </table>
    <div class="info">
      <xsl:value-of select="count(module/schema)"/> schemas
    </div>     
  </xsl:template>
  <!--
    == link-to-configuration
    -->
  <xsl:template name="link-to-configuration">
    <!-- 'config' must be a configuration-point element -->
    <xsl:param name="config" select="."/>
    <a href="../config/{$config/@id}.html">
      <xsl:value-of select="$config/@id"/>
    </a>
  </xsl:template>
  <!--
    == link-to-service
    -->
  <xsl:template name="link-to-service">
    <!-- 'service' must be a service-point element -->
    <xsl:param name="service" select="."/>
    <a href="../service/{$service/@id}.html">
      <xsl:value-of select="$service/@id"/>
    </a>
  </xsl:template>
  <!--
    == link-to-schema
    -->
  <xsl:template name="link-to-schema">
    <!-- 'schema' must be a schema element -->
    <xsl:param name="schema" select="."/>
    <a href="../schema/{$schema/@id}.html">
      <xsl:value-of select="$schema/@id"/>
    </a>
  </xsl:template>
  <!--
    == link-to-module
    -->
  <xsl:template name="link-to-module">
    <!-- 'module' must be a module element. A little different than the other
         link templates; expects the module to be the immediate parent of the current
         node. -->
    <xsl:param name="module" select=".."/>
    <a href="../module/{$module/@id}.html">
      <xsl:value-of select="$module/@id"/>
    </a>
  </xsl:template>
  <!--
    == output-detail-files
    == Outputs each individual file with details about a particular module, configuration,
    == schema, or service.
    -->
  <xsl:template name="output-detail-files">
    <xsl:apply-templates select="/registry/module"/>
    <xsl:apply-templates select="/registry/module/schema" mode="detail-file"/>
    <xsl:apply-templates select="/registry/module/configuration-point"/>
    <xsl:apply-templates select="/registry/module/service-point"/>
  </xsl:template>
  <!-- 
    == module
    == Outputs a module index (listing stuff within a single module).
    -->
  <xsl:template match="module">
    <xsl:message>Writing module <xsl:value-of select="@id"/></xsl:message>
    <redirect:write file="{$base.dir}/module/{@id}.html">
      <html>
        <xsl:call-template name="head">
          <xsl:with-param name="title"> Module <xsl:value-of select="@id"/> </xsl:with-param>
        </xsl:call-template>
        <body>
          <div class="title">Module <xsl:value-of select="@id"/></div>
          <div class="center-nav">
            <a href="../index.html">Back to master index</a>
          </div>
          <table class="top-level-object" cellspacing="0">
            <tr>
              <th class="object-id">Module <xsl:value-of select="@id"/></th>
              <th>Version:</th>
              <td>
                <xsl:value-of select="@version"/>
              </td>
            </tr>
            <tr>
              <th class="object-id" colspan="2">Package:</th>
              <td><xsl:value-of select="@package"/></td>
            </tr>
            <xsl:call-template name="output-description"/>
            <!-- TODO dependencies -->
            <!-- TODO submodules -->
            <xsl:if test="configuration-point|service-point|schema">
              <tr>
                <td colspan="3" class="container">
                  <table class="layout" cellspacing="0">
                    <tr>
                      <td>
                        <xsl:call-template name="output-configurations-for-module"/>
                      </td>
                      <td>
                        <xsl:call-template name="output-services-for-module"/>
                      </td>
                      <td>
                        <xsl:call-template name="output-schemas-for-module"/>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
            </xsl:if>
            <xsl:apply-templates select="contribution" mode="module-listing">
              <xsl:sort select="@configuration-id"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="implementation" mode="module-listing">
              <xsl:sort select="@service-id"/>
            </xsl:apply-templates>
          </table>
          <hr/>
          <div class="center-nav">
            <a href="../index.html">Back to master index</a>
          </div>
        </body>
      </html>
    </redirect:write>
  </xsl:template>
  <!-- head
    == Writes out the head block, with a title and a stylesheet link.
    -->
  <xsl:template name="head">
    <xsl:param name="title"/>
    <head>
      <link rel="stylesheet" type="text/css" href="../hivemind.css"/>
      <title>HiveMind Registry - <xsl:value-of select="$title"/></title>
    </head>
  </xsl:template>
  <!-- output-description
    == Outputs a row for a description, if one is present.  A description
    == is character data inside certain elements.
    -->
  <xsl:template name="output-description">
    <xsl:if test="normalize-space(./text())">
      <tr>
        <td class="description" colspan="3">
          <xsl:value-of select="./text()"/>
        </td>
      </tr>
    </xsl:if>
  </xsl:template>
  <!-- output-configurations-for-module
    == Writes an index of configuration-points for the current module.
    -->
  <xsl:template name="output-configurations-for-module">
    <xsl:if test="configuration-point">
      <table class="summary" cellspacing="0">
        <tr>
          <th>Configuration Points</th>
        </tr>
        <xsl:for-each select="configuration-point">
          <xsl:sort select="@id"/>
          <tr>
            <td>
              <xsl:call-template name="link-to-configuration"/>
            </td>
          </tr>
        </xsl:for-each>
      </table>
      <div class="info">
        <xsl:value-of select="count(configuration-point)"/> configuration points
      </div>      
    </xsl:if>    
  </xsl:template>
  <!--
    == output-services-for-module
    -->
  <xsl:template name="output-services-for-module">
    <xsl:if test="service-point">
      <table class="summary" cellspacing="0">
        <tr>
          <th>Service Points</th>
        </tr>
        <xsl:for-each select="service-point">
          <xsl:sort select="@id"/>
          <tr>
            <td>
              <xsl:call-template name="link-to-service"/>
            </td>
          </tr>
        </xsl:for-each>
      </table>
      <div class="info">
        <xsl:value-of select="count(service-point)"/> service points
      </div>       
    </xsl:if>
  </xsl:template>
  <xsl:template name="output-schemas-for-module">
    <xsl:if test="schema">
      <table class="summary" cellspacing="0">
        <tr>
          <th>Schemas</th>
        </tr>
        <xsl:for-each select="schema">
          <xsl:sort select="@id"/>
          <tr>
            <td>
              <xsl:call-template name="link-to-schema"/>
            </td>
          </tr>
        </xsl:for-each>
      </table>
      <div class="info">
        <xsl:value-of select="count(schema)"/> schemas
      </div>       
    </xsl:if>
  </xsl:template>
  <!--
    == contribution
    -->
  <xsl:template match="contribution">
    <tr>
      <th class="object-id"> <!-- Columns 2 and 3 are for the if attribute. If it doesn't exist,
           "expand" this column to eat those columns. --> <xsl:if test="not(@if)"> <xsl:attribute name="colspan">3</xsl:attribute> </xsl:if> Contribution to <xsl:call-template name="link-to-configuration"> <xsl:with-param name="config" select="/registry/module/configuration-point[@id = current()/@configuration-id]"/> </xsl:call-template> </th>
      <xsl:if test="@if">
        <th>If:</th>
        <td>
          <xsl:value-of select="@if"/>
        </td>
      </xsl:if>
    </tr>
    <tr>
      <td colspan="3" class="container">
        <xsl:call-template name="output-content-raw"/>
      </td>
    </tr>
  </xsl:template>
  <!--
    == implementation
    -->
  <xsl:template match="implementation" mode="module-listing">
    <tr>
      <th class="object-id"> <!-- Columns 2 and 3 are for the if attribute. If it doesn't exist,
           "expand" this column to eat those columns. --> <xsl:if test="not(@if)"> <xsl:attribute name="colspan">3</xsl:attribute> </xsl:if> Implementation for service <xsl:call-template name="link-to-service"> <xsl:with-param name="service" select="/registry/module/service-point[@id = current()/@service-id]"/> </xsl:call-template> </th>
      <xsl:if test="@if">
        <th>If:</th>
        <td>
          <xsl:value-of select="@if"/>
        </td>
      </xsl:if>
    </tr>
    <xsl:call-template name="output-service-implementation"/>
  </xsl:template>
  <!--
    == implementation
    -->
  <xsl:template match="implementation">
    <tr>
      <th class="section"> <!-- Columns 2 and 3 are for the if attribute. If it doesn't exist,
           "expand" this column to eat those columns. --> <xsl:if test="not(@if)"> <xsl:attribute name="colspan">3</xsl:attribute> </xsl:if> Implementation from module <xsl:call-template name="link-to-module"/> </th>
      <xsl:if test="@if">
        <th>If:</th>
        <td>
          <xsl:value-of select="@if"/>
        </td>
      </xsl:if>
    </tr>
    <xsl:call-template name="output-service-implementation"/>
  </xsl:template>  
  <!--
    == output-service-implementation
    -->
  <xsl:template name="output-service-implementation">
    <xsl:if test="create-instance|invoke-factory|interceptor">
      <tr>
        <td colspan="3" class="container">
          <div class="xml">
            <ul>
              <xsl:apply-templates select="create-instance"/>
              <xsl:apply-templates select="invoke-factory"/>
              <xsl:apply-templates select="interceptor"/>
            </ul>
          </div>
        </td>
      </tr>
    </xsl:if>
  </xsl:template>
  <!--
    == output-content-raw
    == Invoked to output the content of an element as raw XML.  We "cook" the elements
    == by wrapping them is special elements to control output formatting.
    -->
  <xsl:template name="output-content-raw">
    <xsl:if test="*">
      <div class="xml">
        <xsl:for-each select="*">
          <xsl:apply-templates select="." mode="raw"/>
          <xsl:if test="position() != last()">
            <br/>
          </xsl:if>
        </xsl:for-each>
      </div>
    </xsl:if>
  </xsl:template>
  <!--
    == * (mode=raw)
    -->
  <xsl:template match="*" mode="raw">
     &lt;<xsl:value-of select="name()"/>   
      <xsl:for-each select="@*" xml:space="preserve">
        <xsl:value-of select="' '"/>
        <span class="attribute"><xsl:value-of select="name()"/></span>="<span class="attribute-value"><xsl:value-of select="."/></span>"
      </xsl:for-each> <xsl:call-template name="raw-content"/> </xsl:template>
  <!--
    == raw-content
    == Closes the current tag and outputs its content (if any) in the form of nested elements or character data.
    -->
  <xsl:template name="raw-content">
    <xsl:choose>
      <xsl:when test="*"> &gt; <ul> <xsl:apply-templates mode="nested-raw"/> </ul> &lt;/<xsl:value-of select="name()"/>&gt; </xsl:when>
      <xsl:when test="normalize-space()"> &gt; <ul> <li> <xsl:value-of select="."/> </li> </ul> &lt;/<xsl:value-of select="name()"/>&gt; </xsl:when>
      <xsl:otherwise> /&gt; </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <!--
    == * (mode=nested-raw)
    -->
  <xsl:template match="*" mode="nested-raw">
    <li>
      <xsl:apply-templates select="." mode="raw"/>
    </li>
  </xsl:template>
  <!--
    == create-instance
    -->
  <xsl:template match="create-instance">
    <li>
      <xsl:apply-templates select="." mode="raw"/>
    </li>
  </xsl:template>
  <!--
    == interceptor
    -->
  <xsl:template match="interceptor">
    <li> &lt;interceptor <span class="attribute">service-id</span>="<span class="attribute-value"><xsl:call-template name="link-to-service"> <xsl:with-param name="service" select="/registry/module/service-point[@id = current()/@service-id]"/> </xsl:call-template></span>" <xsl:for-each select="@*[name() != 'service-id']" xml:space="preserve">
        <span class="attribute"><xsl:value-of select="name()"/></span>="<span class="attribute-value"><xsl:value-of select="."/></span>"
      </xsl:for-each> <xsl:call-template name="raw-content"/> </li>
  </xsl:template>
  <!-- 
    == invoke-factory
    -->
  <xsl:template match="invoke-factory">
    <li> &lt;invoke-factory <span class="attribute">service-id</span>="<span class="attribute-value"><xsl:choose> <xsl:when test="@service-id"> <xsl:call-template name="link-to-service"> <xsl:with-param name="service" select="/registry/module/service-point[@id = current()/@service-id]"/> </xsl:call-template> </xsl:when> <xsl:otherwise> <a href="../services/hivemind.BuilderFactory.html">hivemind.BuilderFactory</a> </xsl:otherwise> </xsl:choose></span>" <span class="attribute">model</span>="<span 
      class="attribute-value"><a href="../config/hivemind.ServiceModels.html"><xsl:choose> <xsl:when test="@model"> <xsl:value-of select="@model"/> </xsl:when> <xsl:otherwise>singleton</xsl:otherwise></xsl:choose></a></span>" <xsl:call-template name="raw-content"/> </li>
  </xsl:template>
  <!--
    == schema (detail-file)
    == Outputs a detail file for the schema.
    -->
  <xsl:template match="schema" mode="detail-file">
    <xsl:message>Writing schema <xsl:value-of select="@id"/></xsl:message>
    <redirect:write file="{$base.dir}/schema/{@id}.html">
      <html>
        <xsl:call-template name="head">
          <xsl:with-param name="title"> Schema <xsl:value-of select="@id"/> </xsl:with-param>
        </xsl:call-template>
        <body>
          <xsl:call-template name="artifact-links"/>
          <hr/>
          <table class="top-level-object" cellspacing="0">
            <tr>
              <th class="object-id" colspan="3"> <xsl:call-template name="output-visibility"/> Schema <xsl:value-of select="@id"/> </th>
            </tr>
            <xsl:call-template name="output-description"/>
            <tr>
              <td class="container" colspan="3">
                <table class="nested-object" cellspacing="0">
                  <xsl:apply-templates select="*"/>
                  <xsl:if test="/registry/module/configuration-point[@schema-id = current()/@id] | /registry/module/service-point[@parameters-schema-id = current()/@id]">
                    <tr>
                      <th colspan="3" class="section">References</th>
                    </tr>
                    <tr>
                      <td colspan="3" class="container">
                        <table cellspacing="0" class="layout">
                          <tr>
                            <xsl:if test="/registry/module/configuration-point[@schema-id = current()/@id]">
                              <td>
                                <table class="summary" cellspacing="0">
                                  <tr>
                                    <th> Configurations </th>
                                  </tr>
                                  <xsl:for-each select="/registry/module/configuration-point[@schema-id = current()/@id]">
                                    <xsl:sort select="@id"/>
                                    <tr>
                                      <td>
                                        <xsl:call-template name="link-to-configuration"/>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </xsl:if>
                            <xsl:if test="/registry/module/service-point[@parameters-schema-id = current()/@id]">
                              <td>
                                <table class="summary" cellspacing="0">
                                  <tr>
                                    <th> Services </th>
                                  </tr>
                                  <xsl:for-each select="/registry/module/service-point[@parameters-schema-id = current()/@id]">
                                    <xsl:sort select="@id"/>
                                    <tr>
                                      <td>
                                        <xsl:call-template name="link-to-service"/>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </xsl:if>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                </table>
              </td>
            </tr>
          </table>
          <hr/>
          <xsl:call-template name="artifact-links"/>
        </body>
      </html>
    </redirect:write>
  </xsl:template>
  <!--
    == element
    == element within a schema
    == contains attributes and conversion rules
    == See schema-content template
    -->
  <xsl:template match="element">
    <tr>
      <th class="object-id"> <xsl:if test="not(@content-translator)"> <xsl:attribute name="colspan">3</xsl:attribute> </xsl:if> Element &lt;<xsl:value-of select="@name"/>&gt; </th>
      <xsl:if test="@content-translator">
        <th> Content translator: </th>
        <td>
          <xsl:call-template name="link-to-translator">
            <xsl:with-param name="translator" select="@content-translator"/>
          </xsl:call-template>
        </td>
      </xsl:if>
    </tr>
    <xsl:call-template name="output-description"/>
    <xsl:apply-templates select="attribute">
      <xsl:sort select="@name"/>
    </xsl:apply-templates>
    <xsl:if test="element">
      <tr>
        <th colspan="3" class="section">Nested Elements</th>
      </tr>
      <tr>
        <td colspan="3" class="container">
          <table class="nested-object" cellspacing="0">
            <xsl:apply-templates/>
          </table>
        </td>
      </tr>
    </xsl:if>
    <xsl:if test="rules">
      <tr>
        <th colspan="3" class="section">Conversion Rules</th>
      </tr>
      <tr>
        <td colspan="3" class="container">
          <xsl:apply-templates select="rules"/>
        </td>
      </tr>
    </xsl:if>
  </xsl:template>
  <!--
    == rules
    == For rules  elements, we don't care about the element itself, but we do want
    == to output its content formatted as XML.
    -->
  <xsl:template match="rules">
    <xsl:call-template name="output-content-raw"/>
  </xsl:template>
  <!--
    == attribute
    -->
  <xsl:template match="attribute">
    <tr>
      <th class="sub-id">Attribute <xsl:value-of select="@name"/> <xsl:if test="@required = 'true'"> (required) </xsl:if> <xsl:if test="@unique = 'true'"> (unique) </xsl:if> <!-- TODO: mark if key attribute --> </th>
      <th> Translator: </th>
      <td>
        <xsl:choose>
          <xsl:when test="@translator">
            <xsl:call-template name="link-to-translator">
              <xsl:with-param name="translator" select="@translator"/>
            </xsl:call-template>
          </xsl:when>
          <xsl:otherwise>
            <xsl:call-template name="link-to-translator">
              <xsl:with-param name="translator" select="'string'"/>
            </xsl:call-template>
          </xsl:otherwise>
        </xsl:choose>
      </td>
    </tr>
    <xsl:call-template name="output-description"/>
  </xsl:template>
  <!--
    == link-to-translator
    -->
  <xsl:template name="link-to-translator">
    <xsl:param name="translator"/>
    <a href="../config/hivemind.Translators.html">
      <xsl:value-of select="$translator"/>
    </a>
  </xsl:template>
  <!--
    == artifact-links
    == Writes links back up the the module and master index.
    -->
  <xsl:template name="artifact-links">
    <div class="title"> Module <xsl:call-template name="link-to-module"/> </div>
    <div class="center-nav">
      <a href="../index.html">Back to master index</a>
    </div>
  </xsl:template>
  <!--
    == configuration-point
    -->
  <xsl:template match="configuration-point">
    <xsl:message>Writing configuration-point <xsl:value-of select="@id"/></xsl:message>
    <redirect:write file="{$base.dir}/config/{@id}.html">
      <html>
        <xsl:call-template name="head">
          <xsl:with-param name="title"> Configuration Point <xsl:value-of select="@id"/> </xsl:with-param>
        </xsl:call-template>
        <body>
          <xsl:call-template name="artifact-links"/>
          <hr/>
          <table class="top-level-object" cellspacing="0">
            <tr>
              <th class="object-id"> <xsl:call-template name="output-visibility"/> Configuration Point <xsl:value-of select="@id"/> </th>
              <th>Occurs:</th>
              <td>
                <xsl:value-of select="@occurs"/>
                <xsl:if test="not(@occurs)"> unbounded </xsl:if>
              </td>
            </tr>
            <xsl:if test="@schema-id">
              <tr>
                <th colspan="2">Schema:</th>
                <td>
                  <xsl:call-template name="link-to-schema">
                    <xsl:with-param name="schema" select="/registry/module/schema[@id = current()/@schema-id]"/>
                  </xsl:call-template>
                </td>
              </tr>
            </xsl:if>
            <xsl:call-template name="output-description"/>
            <xsl:if test="schema">
              <tr>
                <th colspan="3" class="section">Schema</th>
              </tr>
              <tr>
                <td colspan="3" class="container">
                  <!-- TODO: usage -->
                  <table class="nested-object" cellspacing="0">
                    <xsl:apply-templates select="schema/element"/>
                  </table>
                </td>
              </tr>
            </xsl:if>
            <xsl:apply-templates select="/registry/module/contribution[@configuration-id = current()/@id]">
              <!-- Order by module id -->
              <xsl:sort select="../@id"/>
            </xsl:apply-templates>
          </table>
          <hr/>
          <xsl:call-template name="artifact-links"/>
        </body>
      </html>
    </redirect:write>
  </xsl:template>
  <!--
    == contribution
    -->
  <xsl:template match="contribution">
    <tr>
      <th class="section"> <xsl:if test="not(@if)"> <xsl:attribute name="colspan">3</xsl:attribute> </xsl:if> Contribution from module <xsl:call-template name="link-to-module"/> </th>
      <xsl:if test="@if">
        <th>If:</th>
        <td>
          <xsl:value-of select="@if"/>
        </td>
      </xsl:if>
    </tr>
    <tr>
      <td colspan="3" class="container">
        <xsl:call-template name="output-content-raw"/>
      </td>
    </tr>
  </xsl:template>
  <!--
    == contribution
    -->
  <xsl:template match="contribution" mode="module-listing">
    <tr>
      <th class="object-id"> <xsl:if test="not(@if)"> <xsl:attribute name="colspan">3</xsl:attribute> </xsl:if> Contribution to <xsl:call-template name="link-to-configuration"> <xsl:with-param name="config" select="/registry/module/configuration-point[@id = current()/@configuration-id]"/> </xsl:call-template> </th>
      <xsl:if test="@if">
        <th>If:</th>
        <td>
          <xsl:value-of select="@if"/>
        </td>
      </xsl:if>
    </tr>
    <tr>
      <td colspan="3" class="container">
        <xsl:call-template name="output-content-raw"/>
      </td>
    </tr>
  </xsl:template>
  <!-- 
    == output-visibility
    -->
  <xsl:template name="output-visibility">
    <xsl:choose>
      <xsl:when test="@visibility = 'private'">
        <img src="../private.png" width="20" height="20" alt="[private]"/>
      </xsl:when>
      <xsl:otherwise>
        <img src="../public.png" width="20" height="20" alt="[public]"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="service-point">
    <xsl:message>Writing service-point <xsl:value-of select="@id"/></xsl:message>
    <redirect:write file="{$base.dir}/service/{@id}.html">
      <html>
        <xsl:call-template name="head">
          <xsl:with-param name="title"> Service Point <xsl:value-of select="@id"/> </xsl:with-param>
        </xsl:call-template>
        <body>
          <xsl:call-template name="artifact-links"/>
          <hr/>
          <table class="top-level-object" cellspacing="0">
            <tr>
              <th class="object-id"> <xsl:call-template name="output-visibility"/> Service Point <xsl:value-of select="@id"/> </th>
              <th> Interface: </th>
              <td>
                <a ><xsl:attribute name="href">http://tapestry.apache.org/tapestry4.1/apidocs/<xsl:value-of select="translate(@interface, '.', '/')"/>.html</xsl:attribute> 
                <xsl:value-of select="@interface"/>
                </a>
              </td>
            </tr>
            <xsl:call-template name="output-description"/>
            <xsl:if test="@parameters-schema-id">
              <tr>
                <th colspan="2"> Parameters Schema: </th>
                <td>
                  <xsl:call-template name="link-to-schema">
                    <xsl:with-param name="schema" select="/registry/module/schema[@id = current()/@parameters-schema-id]"/>
                  </xsl:call-template>
                </td>
              </tr>
            </xsl:if>
            <xsl:if test="parameters-schema or @parameters-schema-id">
              <tr>
                <th colspan="2"> Parameters occurs: </th>
                <td>
                  <xsl:value-of select="@parameters-occurs"/>
                  <xsl:if test="not(@parameters-occurs)">1</xsl:if>
                </td>
              </tr>
            </xsl:if>
            <xsl:if test="parameters-schema">
              <tr>
                <th colspan="3" class="section"> Parameters Schema </th>
              </tr>
              <tr>
                <td colspan="3" class="container">
                  <!-- TODO: usage -->
                  <table class="nested-object" cellspacing="0">
                    <xsl:apply-templates select="parameters-schema/element"/>
                  </table>
                </td>
              </tr>
            </xsl:if>
            <xsl:if test="interceptor|create-instance|invoke-factory">
              <tr>
                <th colspan="3" class="section">Implementation</th>
              </tr>
              <xsl:call-template name="output-service-implementation"/>
            </xsl:if>
            <xsl:apply-templates select="/registry/module/implementation[@service-id = current()/@id]">
              <xsl:sort select="../@id"/>
            </xsl:apply-templates>
          </table>
          <hr/>
          <xsl:call-template name="artifact-links"/>
        </body>
      </html>
    </redirect:write>
  </xsl:template>
</xsl:stylesheet>
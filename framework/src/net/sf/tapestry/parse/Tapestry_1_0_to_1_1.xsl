<?xml version="1.0" encoding="UTF-8"?>
<!-- $Id$ -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output doctype-public="-//Howard Ship//Tapestry Specification 1.1//EN" doctype-system="http://tapestry.sf.net/dtd/Tapestry_1_1.dtd" encoding="UTF-8" method="xml" indent="yes"/>

<!--

This transforms a Tapestry 1.0 application or component specification to 1.1.  This is mostly
a matter of folding contained elements into attributes, and otherwise re-arrainging things.

This strips out all comments.  It also is not very good at handling the allow-body, allow-informal-parameters
and required elements from 1.0 (since the value inside is unconstrained).

-->

  <xsl:template match="/">
    <xsl:comment> &#036;Id&#036; </xsl:comment>
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="application">
    <application name="{name}" engine-class="{engine-class}">
      <xsl:apply-templates select="properties"/>
      <xsl:apply-templates select="page"/>
      <xsl:apply-templates select="component-alias"/>
    </application>
  </xsl:template>

  <xsl:template match="page">
    <page name="{name}" specification-path="{specification-path}"/>
  </xsl:template>

  <xsl:template match="component-alias">
    <component-alias type="{alias}" specification-path="{specification-path}"/>
  </xsl:template>

  <xsl:template match="properties">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="property">
    <property name="{name}">
      <xsl:value-of select="value"/>
    </property>
  </xsl:template>

  <xsl:template match="specification">
    <specification class="{class}">
	   <xsl:if test="allow-body='no' or allow-body='false'">
			<xsl:attribute name="allow-body">no</xsl:attribute>
		</xsl:if>
		<xsl:if test="parameters/allow-informal-parameters='no' or parameters/allow-informal-parameters='false'">
		  <xsl:attribute name="allow-informal-parameters">no</xsl:attribute>
		</xsl:if>
      <xsl:apply-templates select="properties"/>
      <xsl:apply-templates select="parameters"/>
      <xsl:apply-templates select="components"/>
      <xsl:apply-templates select="assets"/>
    </specification>
  </xsl:template>

  <xsl:template match="parameters">
    <xsl:apply-templates select="parameter"/>
  </xsl:template>

  <xsl:template match="parameter">
    <parameter name="{name}">
		<xsl:if test="java-type">
		<xsl:attribute name="java-type">
			<xsl:value-of select="java-type"/>
		</xsl:attribute>
		</xsl:if>
      <xsl:if test="required='true' or required='yes'">
        <xsl:attribute name="required">yes</xsl:attribute>
      </xsl:if>
    </parameter>
  </xsl:template>

<xsl:template match="components">
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="component">
  <component id="{id}" type="{type}">
    <xsl:apply-templates select="bindings"/>
  </component>
</xsl:template>

<xsl:template match="bindings">
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="binding">
  <binding name="{name}" property-path="{property-path}"/>
</xsl:template>

<xsl:template match="field-binding">
  <field-binding name="{name}" field-name="{field-name}"/>
</xsl:template>

<xsl:template match="inherited-binding">
  <inherited-binding name="{name}" parameter-name="{parameter-name}"/>
</xsl:template>

<xsl:template match="static-binding">
  <static-binding name="{name}">
	  <xsl:value-of select="value"/>
	</static-binding>
</xsl:template>

<xsl:template match="assets">
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="external-asset">
  <external-asset name="{name}" URL="{URL}"/>
</xsl:template>

<xsl:template match="private-asset">
  <private-asset name="{name}" resource-path="{resource-path}"/>
</xsl:template>


<xsl:template match="internal-asset">
  <context-asset name="{name}" path="{path}"/>
</xsl:template>


<xsl:template match="context-asset">
  <context-asset name="{name}" path="{path}"/>
</xsl:template>

</xsl:stylesheet>

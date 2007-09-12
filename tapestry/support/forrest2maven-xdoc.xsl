<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output indent="yes" method="xml"/>
    <xsl:template match="//document">
        <document>
            <properties>
                <title>
                    <xsl:value-of select="header/title"/>
                </title>
            </properties>
            <!-- Ignore the rest of the header and all of the footer. -->
            <xsl:apply-templates select="body"/>
        </document>
    </xsl:template>
    <xsl:template match="body/section">
        <section name="{title}">
            <xsl:apply-templates/>
        </section>
    </xsl:template>
    <xsl:template match="section[ancestor:: section]">
        <subsection name="{title}">
            <xsl:apply-templates/>
        </subsection>
    </xsl:template>
    <!-- Just edit these out. -->
    <xsl:template match="title"/>
    <!-- Forrest <link> (and <jump> and <fork> is just <a> in Maven/Anakia -->
    <xsl:template match="link|jump|fork">
        <xsl:element name="a">
            <!-- Skip the 'role' attribute, which is in Forrest but not in Anakia or HTML. -->
            <xsl:apply-templates select="@*[name() != 'role']|node()"/>
        </xsl:element>
    </xsl:template>
    <!-- Anakia doesn't have note, warning or fixme. -->
    <xsl:template match="note">
        <p>
            <strong>Note:</strong>
            <br/>
            <xsl:apply-templates/>
        </p>
    </xsl:template>
    <xsl:template match="warning">
        <p>
            <strong>Warning:</strong>
            <br/>
            <xsl:apply-templates/>
        </p>
    </xsl:template>
    <xsl:template match="fixme">
        <p>
            <strong>Fixme:</strong>
            <br/>
            <xsl:apply-templates/>
        </p>
    </xsl:template>
    <!-- Forrest has <icon> and <figure>, which we translate to <img>. -->
    <xsl:template match="icon|figure">
        <img>
            <xsl:apply-templates select="@*"/>
        </img>
    </xsl:template>
    <xsl:template match="anchor">
        <a name="{id}"/>
    </xsl:template>
    <!-- Forrest XML defines default values for colspan and rowspan that force them to
         be output, so we edit those out. -->
    <xsl:template match="td|th">
        <xsl:copy>
            <!-- Probably an easier way to do this, but it works. -->
            <xsl:if test="@colspan != 1">
                <xsl:attribute name="colspan">
                    <xsl:value-of select="@colspan"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="@rowspan != 1">
                <xsl:attribute name="rowspan">
                    <xsl:value-of select="@rowspan"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="@*[name() != 'rowspan'][name() != 'colspan']"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!-- General copy element/attribute rule. -->
    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>

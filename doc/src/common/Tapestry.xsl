<?xml version='1.0'?>
<!-- $Id$ -->
<!--

  A stylesheet used commonly for Tapestry documentation.
  
  -->
  
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version='1.0'
                xmlns="http://www.w3.org/TR/xhtml1/transitional"
                exclude-result-prefixes="#default">

<xsl:import href="##DOCBOOK_XSL_DIR##/html/chunk.xsl"/>

<xsl:template match="programlisting|screen|synopsis">
  <xsl:param name="suppress-numbers" select="'0'"/>
  <xsl:variable name="vendor" select="system-property('xsl:vendor')"/>
  <xsl:variable name="id"><xsl:call-template name="object.id"/></xsl:variable>

  <xsl:if test="@id">
    <a href="{$id}"/>
  </xsl:if>

  <xsl:choose>
    <xsl:when test="$suppress-numbers = '0'
                    and @linenumbering = 'numbered'
                    and $use.extensions != '0'
                    and $linenumbering.extension != '0'">
      <xsl:variable name="rtf">
        <xsl:apply-templates/>
      </xsl:variable>
      <table border="0" bgcolor="#E0E0E0" width="90%">
      <tr><td>
      <pre class="{name(.)}">
        <xsl:call-template name="number.rtf.lines">
          <xsl:with-param name="rtf" select="$rtf"/>
        </xsl:call-template>
      </pre>
      </td></tr></table>
    </xsl:when>
    <xsl:otherwise>
      <table border="0" bgcolor="#E0E0E0" width="90%">
      <tr><td>
      <pre class="{name(.)}">
        <xsl:apply-templates/>
      </pre>
      </td></tr></table>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

</xsl:stylesheet>


<?xml version='1.0'?>
<!-- $Id$ -->
<!--

  A stylesheet used commonly for Tapestry documentation.
  
  -->
  
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version='1.0'
                xmlns="http://www.w3.org/TR/xhtml1/transitional"
                exclude-result-prefixes="#default">

<!-- The common buildfiles ensure that the XSL files are unpacked
     to this relative directory. -->
     
<xsl:import href="../common/docbook-xsl-1.45/html/chunk.xsl"/>

<!-- Enable shading of verbatim blocks, such as programlisting. -->
 
<xsl:param name="shade.verbatim" select="1"/>

<!-- Each individual DocBook project is expected to copy the necessary admonition
     and callout graphics -->
     
<xsl:param name="admon.graphics" select="1"/>
<xsl:param name="admon.graphics.path">standard-images/</xsl:param>

<xsl:param name="admom.callout.graphics" select="1"/>
<xsl:param name="callout.graphics.path">standard-images/callouts/</xsl:param>

<!-- The Tapestry documentation is inside a frameset and we want to keep the
     frameset even when the use clicks a link "outside" the documentation.  -->
     
<xsl:param name="ulink.target">_self</xsl:param>

<xsl:param name="use.id.as.filename" select="1"/>

</xsl:stylesheet>


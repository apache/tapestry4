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

<!-- Override html\param.xsl to enable shading of verbatim blocks, such as programlisting. -->
 
<xsl:param name="shade.verbatim" select="1"/>

</xsl:stylesheet>


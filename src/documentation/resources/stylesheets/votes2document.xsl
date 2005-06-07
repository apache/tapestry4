<?xml version="1.0"?>
<!-- 
   Copyright 2005 The Apache Software Foundation

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

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <document>
      <header>
        <title>Project Voting History</title>
      </header>
      <body>
        <table>
          <tr>
            <th>Date</th>
            <th>Title</th>
            <th>Developer</th>
          </tr>
          <xsl:apply-templates select="//vote"/>
        </table>
      </body>
    </document>
  </xsl:template>
  <xsl:template match="vote">
    <tr>
      <td>
        <xsl:value-of select="@date"/>
      </td>
      <td>
        <xsl:value-of select="@title"/>
      </td>
      <td>
        <xsl:value-of select="@dev"/>
      </td>
    </tr>
    <tr>
      <td/>
      <td colspan="2">
        <xsl:value-of select="motion"/>
        <ul>
          <xsl:for-each select="response">
            <li>
              <xsl:value-of select="text()"/>
            </li>
          </xsl:for-each>
        </ul>
      </td>
    </tr>
  </xsl:template>
</xsl:stylesheet>
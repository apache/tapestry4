/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.vlib.components;

import org.apache.tapestry.BaseComponent;

/**
 *  Creates a link to the {@link org.apache.tapestry.vlib.pages.ViewPerson} 
 *  page using the external service.
 *
 *
 *  <table border=1>
 *  <tr> 
 *  <th>Parameter</th> 
 *  <th>Type</th> 
 *  <th>Direction</th> 
 *  <th>Required</th> 
 *  <th>Default</th>
 *  <th>Description</th>
 * </tr>
 * 
 * <tr>
 *  <td>primaryKey</td> 
 *  <td>{@link Integer}</td>
 *  <td>in</td>
 *  <td>yes</td> 
 *  <td>&nbsp;</td>
 *  <td>The primary key of the {@link org.apache.tapestry.vlib.ejb.IPerson} to create a link to.</td>
 * </tr>
 *
 * <tr>
 *      <td>name</td>
 *      <td>{@link String}</td>
 *      <td>in</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>The name of the person to create a link to.
 *      </td>
 *  </tr>
 *
 *  <tr>
 *		<td>omit</td>
 *		<td>boolean</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>If true, then the link is omitted and replaced with an &amp;nbsp;.
 *		</td>
 *	</tr>
 *
 *  <tr>
 *      <td>class</td>
 *      <td>String</td>
 *      <td>in</td>
 *      <td>no</td>
 *      <td>&nbsp;</td>
 *      <td>The CSS class to use with the generated link.
 *      </td>
 *  </tr>
 *  </table>
 *
 *  <p>Informal parameters are not allowed.  A body is not allowed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class PersonLink extends BaseComponent
{
    private Integer _primaryKey;
    private String _name;
    private boolean _omit;

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public boolean getOmit()
    {
        return _omit;
    }

    public void setOmit(boolean omit)
    {
        _omit = omit;
    }

    public Integer getPrimaryKey()
    {
        return _primaryKey;
    }

    public void setPrimaryKey(Integer primaryKey)
    {
        _primaryKey = primaryKey;
    }

}
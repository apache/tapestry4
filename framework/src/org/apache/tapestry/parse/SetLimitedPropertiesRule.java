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

package org.apache.tapestry.parse;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.tapestry.Tapestry;
import org.xml.sax.Attributes;

/**
 *  Much like {@link org.apache.commons.digester.SetPropertiesRule}, but
 *  only properties that are declared will be copied; other properties
 *  will be ignored.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/
public class SetLimitedPropertiesRule extends AbstractSpecificationRule
{
	private String[] _attributeNames;
	private String[] _propertyNames;
	
	private Map _populateMap = new HashMap();
	
	public SetLimitedPropertiesRule(String attributeName, String propertyName)
	{
		this(new String[] { attributeName }, new String[] { propertyName });
	}
	
	public SetLimitedPropertiesRule(String[] attributeNames, String[] propertyNames)
	{
		_attributeNames = attributeNames;
		_propertyNames = propertyNames;
	}
	
    public void begin(String namespace, String name, Attributes attributes) throws Exception
    {
  		_populateMap.clear();
  		
  		int count = attributes.getLength();
  		
  		for (int i = 0; i < count; i++)
  		{
  			String attributeName = attributes.getLocalName(i);
  			
  			if (Tapestry.isNull(attributeName))
  				attributeName = attributes.getQName(i);
  				
  			for (int x = 0; x < _attributeNames.length; x++)
  			{
  				if (_attributeNames[x].equals(attributeName))
  				{
  					String value = attributes.getValue(i);
  					String propertyName = _propertyNames[x];
  					
  					_populateMap.put(propertyName, value);
  					
  					break;
  				}
  			}
  		}
  		
  		if (_populateMap.isEmpty())
  			return;
  		
  		Object top = digester.peek();
  			
  		BeanUtils.populate(top, _populateMap);
  		
  		_populateMap.clear();
  					
    }

}

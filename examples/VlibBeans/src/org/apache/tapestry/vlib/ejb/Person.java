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

package org.apache.tapestry.vlib.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *  A light-weight version of the {@link IPerson} bean.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Person implements Serializable
{
    private static final long serialVersionUID = 4191736656532597561L;
    
	private Object[] columns;

	public static final int PRIMARY_KEY_COLUMN = 0;
	public static final int FIRST_NAME_COLUMN = 1;
	public static final int LAST_NAME_COLUMN = 2;
	public static final int EMAIL_COLUMN = 3;
	public static final int VERIFIED_COLUMN = 4;
	public static final int LOCKED_OUT_COLUMN = 5;
	public static final int ADMIN_COLUMN = 6;
	public static final int AUTHORIZATION_CODE_COLUMN = 7;
	public static final int LAST_ACCESS_COLUMN = 8;

	public static final int N_COLUMNS = 9;

	public Person(Object[] columns)
	{
		if (columns == null)
			throw new IllegalArgumentException("Must provide a non-null columns.");

		if (columns.length != N_COLUMNS)
			throw new IllegalArgumentException("Wrong number of columns for a Person.");

		this.columns = new Object[N_COLUMNS];
		System.arraycopy(columns, 0, this.columns, 0, N_COLUMNS);
	}

	public Integer getPrimaryKey()
	{
		return (Integer) columns[PRIMARY_KEY_COLUMN];
	}

	public String getFirstName()
	{
		return (String) columns[FIRST_NAME_COLUMN];
	}

	public String getLastName()
	{
		return (String) columns[LAST_NAME_COLUMN];
	}

	public String getEmail()
	{
		return (String) columns[EMAIL_COLUMN];
	}

	public String getNaturalName()
	{
		if (columns[FIRST_NAME_COLUMN] == null)
			return (String) columns[LAST_NAME_COLUMN];

		return (String) columns[FIRST_NAME_COLUMN]
			+ " "
			+ (String) columns[LAST_NAME_COLUMN];
	}

	public Timestamp getLastAccess()
	{
		return (Timestamp) columns[LAST_ACCESS_COLUMN];
	}

	public String toString()
	{
		StringBuffer buffer;

		buffer = new StringBuffer("Person[");

		if (columns[FIRST_NAME_COLUMN] != null)
		{
			buffer.append((String) columns[FIRST_NAME_COLUMN]);
			buffer.append(' ');
		}

		buffer.append((String) columns[LAST_NAME_COLUMN]);
		buffer.append(']');

		return buffer.toString();
	}

	public boolean isAdmin()
	{
		return getBit(ADMIN_COLUMN);
	}

	public void setAdmin(boolean value)
	{
		setBit(ADMIN_COLUMN, value);
	}

	public boolean isLockedOut()
	{
		return getBit(LOCKED_OUT_COLUMN);
	}

	public void setLockedOut(boolean value)
	{
		setBit(LOCKED_OUT_COLUMN, value);
	}

	public boolean isVerified()
	{
		return getBit(VERIFIED_COLUMN);
	}

	public void setVerified(boolean value)
	{
		setBit(VERIFIED_COLUMN, value);
	}

	private void setBit(int column, boolean value)
	{
		columns[column] = value ? Boolean.TRUE : Boolean.FALSE;
	}

	private boolean getBit(int column)
	{
		Boolean b = (Boolean) columns[column];

		return b.booleanValue();
	}

	public String getAuthorizationCode()
	{
		return (String) columns[AUTHORIZATION_CODE_COLUMN];
	}
}
package net.sf.tapestry.vlib.components;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;

/**
 *  Creates a link to the {@link net.sf.tapestry.vlib.pages.ViewPerson} 
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
 *  <td>The primary key of the {@link net.sf.tapestry.vlib.ejb.IPerson} to create a link to.</td>
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
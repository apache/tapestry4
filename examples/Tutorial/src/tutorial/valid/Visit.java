package tutorial.valid;

import java.io.*;
import java.math.*;

public class Visit implements Serializable
{
	private String stringData;
	private int intData;
	private double doubleData;
	private BigDecimal decimalData;
	private BigInteger bigIntData;
	
	private static final BigDecimal DECIMAL_ZERO = new BigDecimal(0.0);
	private static final BigInteger BIGINT_ZERO = new BigInteger("0");
	
	public String getStringData()
	{
		return stringData;
	}
	
	public void setStringData(String value)
	{
		stringData = value;
	}
	
	public int getIntData()
	{
		return intData;
	}
	
	public void setIntData(int value)
	{
		intData = value;
	}	
	
	public double getDoubleData()
	{
		return doubleData;
	}
	
	public void setDoubleData(double value)
	{
		doubleData = value;
	}
	
	public BigDecimal getDecimalData()
	{
		if (decimalData == null)
			return DECIMAL_ZERO;
		
		return decimalData;
	}
	
	public void setDecimalData(BigDecimal value)
	{
		decimalData = value;
	}
	
	public BigInteger getBigIntData()
	{
		if (bigIntData == null)
			return BIGINT_ZERO;
		
		return bigIntData;
	}
	
	public void setBigIntData(BigInteger value)
	{
		bigIntData = value;
	}
}

package org.apache.tapestry.util.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.hivemind.ApplicationRuntimeException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class TestExceptionAnalyzer extends Assert {
	
	public void test_report_exception() {
		ApplicationRuntimeException ex = new ApplicationRuntimeException("TAPerrorSTRY");		
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		new ExceptionAnalyzer().reportException(ex, new PrintStream(stream));
		
		String output = stream.toString();
			
		assertTrue(output.contains("org.apache.hivemind.ApplicationRuntimeException"));
		assertTrue(output.contains("TAPerrorSTRY"));
	}
	
	// test for TAPESTRY-2570
	public void test_with_null_tostring_exception() {
		Exception dummyException = new Exception("TAPerrorSTRY"){
			public Object getDummy() {
				return new Object(){
					@Override
					public String toString() {
						return null;
					}
				};
			}
		};
		ApplicationRuntimeException ex = new ApplicationRuntimeException(dummyException);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		new ExceptionAnalyzer().reportException(ex, new PrintStream(stream));
		
		String output = stream.toString();
		
		assertTrue(output.contains("org.apache.hivemind.ApplicationRuntimeException"));
		assertTrue(output.contains("TAPerrorSTRY"));
		
	}
	
	
}

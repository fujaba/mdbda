package org.hahnpro.mdbda.codegen.helper;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.hahnpro.mdbda.runtime.annotations.Matcher;
import org.junit.Test;

public class TestPatternConfiguratorHelper {

	public class A{
		public boolean foo(){
			return true;
		}
		
		@Matcher
		 public boolean bar(){
			return true;
		}
	}
	
	
	@Test
	public void testGetMatcherMethod(){

		try{
			String matcherMethod = PatternConfiguratorHelper.getMatcherMethod(A.class.getName());
		
			assertTrue("bar".equals(matcherMethod));
		}catch(ClassNotFoundException e){
			fail();
		}
	}
}

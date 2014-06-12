package org.mdbda.codegen.helper;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestBeautifyJava {

	@Test
	public void testEmpty() {
		String in = "\n";
		String out = "";
		
		assertEquals(out, CodeGenHelper.beautifyJava(in, 0));
	}
	
	@Test
	public void testCode() {
		String in =   "	if(42 == 23){\n"
					+ "		word.stop();\n"
					+ "	}";
		String out =  "if(42 == 23){\n"
					+ "	word.stop();\n"
					+ "}\n";
		
		assertEquals(out, CodeGenHelper.beautifyJava(in, 0));
	}
	
	@Test
	public void testCode2() {
		String in =   "	if(42 == 23){\n"
					+ "		word.stop();\n"
					+ "	}\n";
		String out =  "		if(42 == 23){\n"
					+ "			word.stop();\n"
					+ "		}\n";
		
		assertEquals(out, CodeGenHelper.beautifyJava(in, 2));
	}
}

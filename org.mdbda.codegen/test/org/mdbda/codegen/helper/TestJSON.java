package org.mdbda.codegen.helper;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class TestJSON {

	@Test
	public void test() throws ParseException, IOException, URISyntaxException {
		org.json.simple.parser.JSONParser sp = new JSONParser();
		byte[] allBytes = Files.readAllBytes(Paths.get(URI.create("file:/C:/Users/hahn/git/mdbda/org.mdbda.codegen/test/org/mdbda/codegen/helper/BinningConfig.json")));
		String s = new String(allBytes);
		
		Object o = JSONValue.parse(s);
		System.out.println("");
	}

}

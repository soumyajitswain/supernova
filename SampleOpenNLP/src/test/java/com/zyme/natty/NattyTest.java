package com.zyme.natty;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;



import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class NattyTest {
	@SuppressWarnings({ "unused", "rawtypes" })
	@Test
    public void helloTest() {
		final String input = "I want to go on 19901001";
		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse(input);
		for(DateGroup group:groups) {
		  List dates = group.getDates();
		  int line = group.getLine();
		  int column = group.getPosition();
		  String matchingValue = group.getText();
		  String syntaxTree = group.getSyntaxTree().toStringTree();
		  Map parseMap = group.getParseLocations();
		  boolean isRecurreing = group.isRecurring();
		  Date recursUntil = group.getRecursUntil();
		  System.out.print(dates.get(0));
		}
    }
}
